package com.bookstudio.user.dao;

import java.util.List;

import com.bookstudio.shared.dao.CrudDao;
import com.bookstudio.user.model.User;

public interface UserDao extends CrudDao<User, String> {
	List<User> listAll(int loggedUserId);
	boolean deleteUser(String userId);
}
