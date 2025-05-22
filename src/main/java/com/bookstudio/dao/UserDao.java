package com.bookstudio.dao;

import java.util.List;

import com.bookstudio.models.User;

public interface UserDao extends CrudDao<User, String> {
	List<User> listAll(int loggedUserId);
	boolean deleteUser(String userId);
}
