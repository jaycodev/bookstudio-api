package com.bookstudio.book.dao;

import java.util.List;

import com.bookstudio.book.model.Book;
import com.bookstudio.shared.dao.CrudDao;

public interface BookDao extends CrudDao<Book, String> {
	public List<Book> populateBookSelect();
}
