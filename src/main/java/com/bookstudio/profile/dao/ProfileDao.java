package com.bookstudio.profile.dao;

import com.bookstudio.user.model.User;

public interface ProfileDao {
	User updateProfile(User user);
	User updateProfilePhoto(User user);
	String getPasswordByUserId(String userId);
}
