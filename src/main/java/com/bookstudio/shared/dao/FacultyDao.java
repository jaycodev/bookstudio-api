package com.bookstudio.shared.dao;

import java.util.List;

import com.bookstudio.shared.model.Faculty;

public interface FacultyDao {
	public List<Faculty> populateFacultySelect();
}
