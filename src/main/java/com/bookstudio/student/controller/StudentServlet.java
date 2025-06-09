package com.bookstudio.student.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bookstudio.shared.util.LocalDateAdapter;
import com.bookstudio.shared.util.SelectOptions;
import com.bookstudio.student.model.Student;
import com.bookstudio.student.service.StudentService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@WebServlet("/StudentServlet")
public class StudentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private StudentService studentService = new StudentService();
	private Gson gson;

	@Override
	public void init() throws ServletException {
		gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateAdapter()).create();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");
		
		String type = request.getParameter("type");
		switch (type) {
		case "list":
			listStudents(request, response);
			break;
		case "details":
			getStudent(request, response);
			break;
		case "populateSelects":
			populateSelects(request, response);
			break;
		default:
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action or GET method not allowed for this action");
			break;
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");
		
		String type = request.getParameter("type");
		switch (type) {
		case "create":
			createStudent(request, response);
			break;
		case "update":
			updateStudent(request, response);
			break;
		default:
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action");
			break;
		}
	}

	private void listStudents(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			List<Student> studentData = studentService.listStudents();

			if (studentData != null && !studentData.isEmpty()) {
				String json = gson.toJson(studentData);
				response.setStatus(HttpServletResponse.SC_OK);
				response.getWriter().write(json);
			} else {
				response.setStatus(HttpServletResponse.SC_NO_CONTENT);
				response.getWriter().write("{\"success\": false, \"message\": \"No students found.\", \"errorType\": \"no_content\", \"statusCode\": 204}");
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().write("{\"success\": false, \"message\": \"Error listing students.\", \"errorType\": \"server_error\", \"statusCode\": 500, \"details\": \"" + e.getMessage() + "\"}");
		}
	}

	private void getStudent(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			String studentId = request.getParameter("studentId");

			if (studentId == null || studentId.trim().isEmpty()) {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				response.getWriter().write("{\"success\": false, \"message\": \"Student ID is required.\", \"errorType\": \"invalid_request\", \"statusCode\": 400}");
				return;
			}

			Student student = studentService.getStudent(studentId);

			if (student != null) {
				String json = gson.toJson(student);
				response.setStatus(HttpServletResponse.SC_OK);
				response.getWriter().write(json);
			} else {
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
				response.getWriter().write("{\"success\": false, \"message\": \"Student not found.\", \"errorType\": \"not_found\", \"statusCode\": 404}");
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().write("{\"success\": false, \"message\": \"Error retrieving student data.\", \"errorType\": \"server_error\", \"statusCode\": 500, \"details\": \"" + e.getMessage() + "\"}");
		}
	}

	private void createStudent(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
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
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().write("{\"success\": false, \"message\": \"Server error while creating student.\", \"errorType\": \"server_error\", \"statusCode\": 500, \"details\": \"" + e.getMessage() + "\"}");
		}
	}

	private void updateStudent(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			try {
				Student updatedStudent = studentService.updateStudent(request);
				if (updatedStudent != null) {
					String json = gson.toJson(updatedStudent);
					response.setStatus(HttpServletResponse.SC_OK);
					response.getWriter().write("{\"success\": true, \"data\": " + json + "}");
				} else {
					response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
					response.getWriter().write("{\"success\": false, \"message\": \"Update failed: Student data is null.\", \"errorType\": \"update_failed\", \"statusCode\": 400}");
				}
			} catch (SQLException e) {
				String errorMessage = e.getMessage();
				if (errorMessage.contains("correo electrónico")) {
					sendErrorResponse(response, errorMessage, "editStudentEmail");
				} else {
					sendErrorResponse(response, "Error al actualizar el estudiante.", "");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().write("{\"success\": false, \"message\": \"Server error while updating student.\", \"errorType\": \"server_error\", \"statusCode\": 500, \"details\": \"" + e.getMessage() + "\"}");
		}
	}

	private void populateSelects(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			SelectOptions selectOptions = studentService.populateSelects();

			if (selectOptions != null && selectOptions.getFaculties() != null) {
				String json = gson.toJson(selectOptions);
				response.setStatus(HttpServletResponse.SC_OK);
				response.getWriter().write(json);
			} else {
				response.setStatus(HttpServletResponse.SC_NO_CONTENT);
				response.getWriter().write("{\"success\": false, \"message\": \"No select options found.\", \"errorType\": \"no_content\", \"statusCode\": 204}");
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().write("{\"success\": false, \"message\": \"Error populating select options.\", \"errorType\": \"server_error\", \"statusCode\": 500, \"details\": \"" + e.getMessage() + "\"}");
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
