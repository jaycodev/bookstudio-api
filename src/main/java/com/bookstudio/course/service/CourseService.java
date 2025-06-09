package com.bookstudio.course.service;

import java.sql.SQLException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

import com.bookstudio.course.dao.CourseDao;
import com.bookstudio.course.dao.CourseDaoImpl;
import com.bookstudio.course.model.Course;

public class CourseService {
	private CourseDao courseDao = new CourseDaoImpl();

	public List<Course> listCourses() throws SQLException {
		return courseDao.listAll();
	}

	public Course getCourse(String courseId) throws SQLException {
		return courseDao.getById(courseId);
	}

	public Course createCourse(HttpServletRequest request) throws Exception {
		String name = request.getParameter("addCourseName");
		String level = request.getParameter("addCourseLevel");
		String description = request.getParameter("addCourseDescription");
		String status = request.getParameter("addCourseStatus");

		Course course = new Course();
		course.setName(name);
		course.setLevel(level);
		course.setDescription(description);
		course.setStatus(status);

		return courseDao.create(course);
	}

	public Course updateCourse(HttpServletRequest request) throws Exception {
		String courseId = request.getParameter("courseId");
		String name = request.getParameter("editCourseName");
		String level = request.getParameter("editCourseLevel");
		String description = request.getParameter("editCourseDescription");
		String status = request.getParameter("editCourseStatus");

		Course course = new Course();
		course.setCourseId(courseId);
		course.setName(name);
		course.setLevel(level);
		course.setDescription(description);
		course.setStatus(status);

		return courseDao.update(course);
	}
}
