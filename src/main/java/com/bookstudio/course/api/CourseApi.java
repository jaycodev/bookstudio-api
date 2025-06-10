package com.bookstudio.course.api;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bookstudio.course.model.Course;
import com.bookstudio.course.service.CourseService;
import com.bookstudio.shared.api.BaseApiServlet;
import com.bookstudio.shared.util.LocalDateAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@MultipartConfig
@WebServlet(name = "CourseApi", urlPatterns = { "/api/courses", "/api/courses/*" })
public class CourseApi extends BaseApiServlet {
	private static final long serialVersionUID = 1L;

	private CourseService courseService = new CourseService();
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
			listCourses(response);
		} else if (pathInfo.startsWith("/")) {
			String courseId = pathInfo.substring(1);
			getCourse(courseId, response);
		} else {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid GET request");
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
	        throws ServletException, IOException {
	    String overrideMethod = request.getParameter("_method");

	    if ("PUT".equalsIgnoreCase(overrideMethod)) {
	        String pathInfo = request.getPathInfo();
	        if (pathInfo != null && pathInfo.startsWith("/")) {
	            String courseId = pathInfo.substring(1);
	            updateCourse(courseId, request, response);
	        } else {
	            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Course ID is required for update.");
	        }
	    } else {
	        createCourse(request, response);
	    }
	}

	private void listCourses(HttpServletResponse response) throws IOException {
		try {
			List<Course> courses = courseService.listCourses();
			if (courses != null && !courses.isEmpty()) {
				String json = gson.toJson(courses);
				response.setStatus(HttpServletResponse.SC_OK);
				response.getWriter().write(json);
			} else {
				response.setStatus(HttpServletResponse.SC_NO_CONTENT);
				response.getWriter().write(
						"{\"success\": false, \"message\": \"No courses found.\", \"errorType\": \"no_content\", \"statusCode\": 204}");
			}
		} catch (Exception e) {
			handleException(response, "Error listing courses.", e);
		}
	}

	private void getCourse(String courseId, HttpServletResponse response) throws IOException {
		try {
			if (courseId == null || courseId.trim().isEmpty()) {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				response.getWriter().write(
						"{\"success\": false, \"message\": \"Course ID is required.\", \"errorType\": \"invalid_request\", \"statusCode\": 400}");
				return;
			}

			Course course = courseService.getCourse(courseId);
			if (course != null) {
				String json = gson.toJson(course);
				response.setStatus(HttpServletResponse.SC_OK);
				response.getWriter().write(json);
			} else {
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
				response.getWriter().write(
						"{\"success\": false, \"message\": \"Course not found.\", \"errorType\": \"not_found\", \"statusCode\": 404}");
			}
		} catch (Exception e) {
			handleException(response, "Error retrieving course.", e);
		}
	}

	private void createCourse(HttpServletRequest request, HttpServletResponse response) throws IOException {
		if (!isAuthorized(request, response))
			return;

		try {
			Course createdCourse = courseService.createCourse(request);
			if (createdCourse != null) {
				String json = gson.toJson(createdCourse);
				response.setStatus(HttpServletResponse.SC_CREATED);
				response.getWriter().write("{\"success\": true, \"data\": " + json + "}");
			} else {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				response.getWriter().write(
						"{\"success\": false, \"message\": \"Creation failed: Course data is null.\", \"errorType\": \"creation_failed\", \"statusCode\": 400}");
			}
		} catch (Exception e) {
			handleException(response, "Server error while creating course.", e);
		}
	}

	private void updateCourse(String courseId, HttpServletRequest request, HttpServletResponse response) throws IOException {
		if (!isAuthorized(request, response))
			return;

		try {
	        if (courseId == null || courseId.trim().isEmpty()) {
	            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
	            response.getWriter().write("{\"success\": false, \"message\": \"Course ID is required.\", \"statusCode\": 400}");
	            return;
	        }
			
			Course updatedCourse = courseService.updateCourse(courseId, request);
			if (updatedCourse != null) {
				String json = gson.toJson(updatedCourse);
				response.setStatus(HttpServletResponse.SC_OK);
				response.getWriter().write("{\"success\": true, \"data\": " + json + "}");
			} else {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				response.getWriter().write(
						"{\"success\": false, \"message\": \"Update failed: Course data is null.\", \"errorType\": \"update_failed\", \"statusCode\": 400}");
			}
		} catch (Exception e) {
			handleException(response, "Server error while updating course.", e);
		}
	}
}
