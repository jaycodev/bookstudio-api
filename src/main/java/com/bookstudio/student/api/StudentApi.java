package com.bookstudio.student.api;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bookstudio.shared.api.BaseApiServlet;
import com.bookstudio.shared.util.LocalDateAdapter;
import com.bookstudio.student.model.Student;
import com.bookstudio.student.service.StudentService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@WebServlet(name = "StudentApi", urlPatterns = { "/api/students", "/api/students/*" })
public class StudentApi extends BaseApiServlet {
	private static final long serialVersionUID = 1L;

	private StudentService studentService = new StudentService();
	private Gson gson;

	@Override
	public void init() throws ServletException {
		gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateAdapter()).create();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String pathInfo = request.getPathInfo();
		if (pathInfo == null || pathInfo.equals("/")) {
			listStudents(response);
		} else if (pathInfo.startsWith("/")) {
			String studentId = pathInfo.substring(1);
			getStudent(studentId, response);
		} else {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid GET request.");
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String overrideMethod = request.getParameter("_method");
		if ("PUT".equalsIgnoreCase(overrideMethod)) {
	        String pathInfo = request.getPathInfo();
	        if (pathInfo != null && pathInfo.startsWith("/")) {
	            String studentId = pathInfo.substring(1);
	            updateStudent(studentId, request, response);
	        } else {
	            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Student ID is required for update.");
	        }
		} else {
			createStudent(request, response);
		}
	}

	private void listStudents(HttpServletResponse response) throws ServletException, IOException {
		try {
			List<Student> students = studentService.listStudents();

			if (students != null && !students.isEmpty()) {
				String json = gson.toJson(students);
				response.setStatus(HttpServletResponse.SC_OK);
				response.getWriter().write(json);
			} else {
				response.setStatus(HttpServletResponse.SC_NO_CONTENT);
				response.getWriter().write(
						"{\"success\": false, \"message\": \"No students found.\", \"errorType\": \"no_content\", \"statusCode\": 204}");
			}
		} catch (Exception e) {
			handleException(response, "Error listing students.", e);
		}
	}

	private void getStudent(String studentId, HttpServletResponse response) throws ServletException, IOException {
		try {
			if (studentId == null || studentId.trim().isEmpty()) {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				response.getWriter().write(
						"{\"success\": false, \"message\": \"Student ID is required.\", \"errorType\": \"invalid_request\", \"statusCode\": 400}");
				return;
			}

			Student student = studentService.getStudent(studentId);

			if (student != null) {
				String json = gson.toJson(student);
				response.setStatus(HttpServletResponse.SC_OK);
				response.getWriter().write(json);
			} else {
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
				response.getWriter().write(
						"{\"success\": false, \"message\": \"Student not found.\", \"errorType\": \"not_found\", \"statusCode\": 404}");
			}
		} catch (Exception e) {
			handleException(response, "Error retrieving student data.", e);
		}
	}

	private void createStudent(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			Student createdStudent = studentService.createStudent(request);

			if (createdStudent != null) {
				String json = gson.toJson(createdStudent);
				response.setStatus(HttpServletResponse.SC_OK);
				response.getWriter().write("{\"success\": true, \"data\": " + json + "}");
			} else {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				response.getWriter().write(
						"{\"success\": false, \"message\": \"Creation failed: Student data is null.\", \"errorType\": \"creation_failed\", \"statusCode\": 400}");
			}
		} catch (SQLException e) {
			String errorMessage = e.getMessage();
			if (errorMessage.contains("DNI")) {
				sendErrorResponse(response, errorMessage, "addStudentDNI");
			} else if (errorMessage.contains("correo electrónico")) {
				sendErrorResponse(response, errorMessage, "addStudentEmail");
			} else {
				sendErrorResponse(response, "Error al crear el estudiante.", "");
			}
		} catch (Exception e) {
			handleException(response, "Server error while creating student.", e);
		}
	}

	private void updateStudent(String studentId, HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
	        if (studentId == null || studentId.trim().isEmpty()) {
	            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
	            response.getWriter().write("{\"success\": false, \"message\": \"Student ID is required.\", \"statusCode\": 400}");
	            return;
	        }
			
			Student updatedStudent = studentService.updateStudent(studentId, request);
			if (updatedStudent != null) {
				String json = gson.toJson(updatedStudent);
				response.setStatus(HttpServletResponse.SC_OK);
				response.getWriter().write("{\"success\": true, \"data\": " + json + "}");
			} else {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				response.getWriter().write(
						"{\"success\": false, \"message\": \"Update failed: Student data is null.\", \"errorType\": \"update_failed\", \"statusCode\": 400}");
			}
		} catch (SQLException e) {
			String errorMessage = e.getMessage();
			if (errorMessage.contains("correo electrónico")) {
				sendErrorResponse(response, errorMessage, "editStudentEmail");
			} else {
				sendErrorResponse(response, "Error al actualizar el estudiante.", "");
			}
		} catch (Exception e) {
			handleException(response, "Server error while updating student.", e);
		}
	}

	private void sendErrorResponse(HttpServletResponse response, String message, String field) throws IOException {
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();

		if (field == null || field.isEmpty()) {
			out.write("{\"success\": false, \"message\": \"" + message + "\"}");
		} else {
			out.write("{\"success\": false, \"message\": \"" + message + "\", \"field\": \"" + field + "\"}");
		}
		out.flush();
	}
}
