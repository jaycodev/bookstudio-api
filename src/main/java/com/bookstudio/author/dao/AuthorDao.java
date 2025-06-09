package com.bookstudio.author.dao;

import java.util.List;

import com.bookstudio.author.model.Author;
import com.bookstudio.shared.dao.CrudDao;

public interface AuthorDao extends CrudDao<Author, String> {
    List<Author> populateAuthorSelect();
}
