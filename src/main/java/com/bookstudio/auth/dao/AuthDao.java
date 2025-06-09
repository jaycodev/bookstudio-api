package com.bookstudio.auth.dao;

import com.bookstudio.user.model.User;

public interface AuthDao {
	public User verifyLogin(String username, String password);
}
