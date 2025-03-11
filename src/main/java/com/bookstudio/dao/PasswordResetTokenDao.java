package com.bookstudio.dao;

public interface PasswordResetTokenDao {
	boolean emailExists(String email);
	boolean savePasswordResetToken(String email, String token, long expiryTime);
	boolean isTokenValid(String token);
	boolean updatePassword(String token, String newPassword);
	String getEmailByToken(String token);
}
