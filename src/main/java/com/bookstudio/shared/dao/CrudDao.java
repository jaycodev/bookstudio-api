package com.bookstudio.shared.dao;

import java.util.List;

public interface CrudDao<T, ID> {
    List<T> listAll();
    T getById(ID id);
    T create(T entity) throws Exception;
    T update(T entity) throws Exception;
}
