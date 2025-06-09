package com.bookstudio.shared.dao;

import java.util.List;

import com.bookstudio.shared.model.Genre;

public interface GenreDao {
	public List<Genre> populateGenreSelect();
}
