package com.bookstudio.auth.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.bookstudio.auth.util.PasswordUtils;
import com.bookstudio.shared.util.DbConnection;
import com.bookstudio.user.model.User;

public class AuthDaoImpl implements AuthDao {
	@Override
	public User verifyLogin(String username, String password) {
	    User user = null;

	    String sql = "SELECT UserID, Username, Email, FirstName, LastName, Password, Role, ProfilePhoto FROM Users WHERE Username = ?";

	    try (Connection cn = DbConnection.getConexion(); PreparedStatement ps = cn.prepareStatement(sql)) {
	        ps.setString(1, username);

	        try (ResultSet rs = ps.executeQuery()) {
	            if (rs.next()) {
	                String hashedPassword = rs.getString("Password");

	                if (PasswordUtils.checkPassword(password, hashedPassword)) {
	                    user = new User();
	                    user.setUserId(rs.getString("UserID"));
	                    user.setUsername(rs.getString("Username"));
	                    user.setEmail(rs.getString("Email"));
	                    user.setFirstName(rs.getString("FirstName"));
	                    user.setLastName(rs.getString("LastName"));
	                    user.setRole(rs.getString("Role"));
	                    user.setProfilePhoto(rs.getBytes("ProfilePhoto"));
	                }
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return user;
	}
}
