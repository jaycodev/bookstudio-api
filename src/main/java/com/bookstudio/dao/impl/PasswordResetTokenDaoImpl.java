package com.bookstudio.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import com.bookstudio.dao.PasswordResetTokenDao;
import com.bookstudio.utils.DbConnection;
import com.bookstudio.utils.PasswordUtils;

public class PasswordResetTokenDaoImpl implements PasswordResetTokenDao {
	@Override
	public boolean emailExists(String email) {
		String sql = "SELECT COUNT(*) FROM Users WHERE Email = ?";
		try (Connection cn = DbConnection.getConexion(); PreparedStatement ps = cn.prepareStatement(sql)) {
			ps.setString(1, email);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return rs.getInt(1) > 0;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	@Override
	public boolean savePasswordResetToken(String email, String token, long expiryTime) {
		String sql = "INSERT INTO PasswordResetTokens (Email, Token, ExpiryTime) VALUES (?, ?, ?)";
		try (Connection cn = DbConnection.getConexion(); PreparedStatement ps = cn.prepareStatement(sql)) {
			ps.setString(1, email);
			ps.setString(2, token);
			ps.setTimestamp(3, new java.sql.Timestamp(expiryTime));
			int rowsAffected = ps.executeUpdate();
			return rowsAffected > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	@Override
	public boolean isTokenValid(String token) {
		String sql = "SELECT ExpiryTime FROM PasswordResetTokens WHERE Token = ?";
		try (Connection cn = DbConnection.getConexion(); PreparedStatement ps = cn.prepareStatement(sql)) {
			ps.setString(1, token);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					Timestamp expiryTimestamp = rs.getTimestamp("ExpiryTime");
					return new java.util.Date().before(expiryTimestamp);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	@Override
	public boolean updatePassword(String token, String newPassword) {
		String sqlSelect = "SELECT Email FROM PasswordResetTokens WHERE Token = ? AND ExpiryTime >= ?";
		String email = null;
		try (Connection cn = DbConnection.getConexion(); PreparedStatement ps = cn.prepareStatement(sqlSelect)) {
			ps.setString(1, token);
			ps.setTimestamp(2, new java.sql.Timestamp(System.currentTimeMillis()));
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					email = rs.getString("Email");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}

		if (email == null) {
			return false;
		}
		
		String hashedPassword = PasswordUtils.hashPassword(newPassword);

		String sqlUpdate = "UPDATE Users SET Password = ? WHERE Email = ?";
		try (Connection cn = DbConnection.getConexion(); PreparedStatement ps = cn.prepareStatement(sqlUpdate)) {
			ps.setString(1, hashedPassword);
			ps.setString(2, email);
			int rowsAffected = ps.executeUpdate();
			if (rowsAffected > 0) {
				String sqlDelete = "DELETE FROM PasswordResetTokens WHERE Token = ?";
				try (PreparedStatement psDelete = cn.prepareStatement(sqlDelete)) {
					psDelete.setString(1, token);
					psDelete.executeUpdate();
				}
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	@Override
	public String getEmailByToken(String token) {
		String sql = "SELECT Email FROM PasswordResetTokens WHERE Token = ?";
		try (Connection cn = DbConnection.getConexion(); PreparedStatement ps = cn.prepareStatement(sql)) {
			ps.setString(1, token);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return rs.getString("Email");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}
}
