package com.bookstudio.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.bookstudio.dao.ProfileDao;
import com.bookstudio.models.User;
import com.bookstudio.utils.DbConnection;

public class ProfileDaoImpl implements ProfileDao {
	@Override
	public User updateProfile(User user) {
	    boolean updatePassword = user.getPassword() != null;

	    String sql = updatePassword
	        ? "UPDATE Users SET FirstName = ?, LastName = ?, Password = ? WHERE UserID = ?"
	        : "UPDATE Users SET FirstName = ?, LastName = ? WHERE UserID = ?";

		try (Connection cn = DbConnection.getConexion(); PreparedStatement ps = cn.prepareStatement(sql)) {

			ps.setString(1, user.getFirstName());
			ps.setString(2, user.getLastName());
			
	        if (updatePassword) {
	            ps.setString(3, user.getPassword());
	            ps.setString(4, user.getUserId());
	        } else {
	            ps.setString(3, user.getUserId());
	        }

			int result = ps.executeUpdate();
			if (result == 0) {
				System.out.println("No se encontró el usuario con el ID proporcionado.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return user;
	}

	@Override
	public User updateProfilePhoto(User user) {
		String sql = "UPDATE Users SET ProfilePhoto = ? WHERE UserID = ?";

		try (Connection cn = DbConnection.getConexion(); PreparedStatement ps = cn.prepareStatement(sql)) {

			ps.setBytes(1, user.getProfilePhoto());
			ps.setString(2, user.getUserId());

			int result = ps.executeUpdate();
			if (result == 0) {
				System.out.println("No se encontró el usuario con el ID proporcionado.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return user;
	}
	
	@Override
	public String getPasswordByUserId(String userId) {
	    String sql = "SELECT Password FROM Users WHERE UserID = ?";
	    String hashedPassword = null;

	    try (Connection cn = DbConnection.getConexion(); PreparedStatement ps = cn.prepareStatement(sql)) {
	        ps.setString(1, userId);
	        var rs = ps.executeQuery();

	        if (rs.next()) {
	            hashedPassword = rs.getString("Password");
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return hashedPassword;
	}
}
