package com.bookstudio.dao;

import java.util.List;

import com.bookstudio.models.Author;

public interface AuthorDao extends CrudDao<Author, String> {
    List<Author> populateAuthorSelect();
}
