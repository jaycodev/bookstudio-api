package com.bookstudio.dao;

import java.util.List;

import com.bookstudio.models.Book;

public interface BookDao extends CrudDao<Book, String> {
	public List<Book> populateBookSelect();
}
