package com.bookstudio.course.dao;

import java.util.List;

import com.bookstudio.course.model.Course;
import com.bookstudio.shared.dao.CrudDao;

public interface CourseDao extends CrudDao<Course, String> {
	public List<Course> populateCourseSelect();
}
