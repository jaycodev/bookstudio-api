package com.bookstudio.dao;

import java.util.List;

import com.bookstudio.models.Course;

public interface CourseDao extends CrudDao<Course, String> {
	public List<Course> populateCourseSelect();
}
