package com.bookstudio.student.dao;

import java.util.List;

import com.bookstudio.shared.dao.CrudDao;
import com.bookstudio.student.model.Student;

public interface StudentDao extends CrudDao<Student, String> {
	List<Student> populateStudentSelect();
}
