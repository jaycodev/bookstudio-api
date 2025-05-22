package com.bookstudio.dao;

import java.util.List;

import com.bookstudio.models.Student;

public interface StudentDao extends CrudDao<Student, String> {
	List<Student> populateStudentSelect();
}
