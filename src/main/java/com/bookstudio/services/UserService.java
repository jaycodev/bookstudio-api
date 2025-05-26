package com.bookstudio.services;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.Base64;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

import com.bookstudio.dao.UserDao;
import com.bookstudio.dao.impl.UserDaoImpl;
import com.bookstudio.models.User;
import com.bookstudio.utils.LoginConstants;
import com.bookstudio.utils.PasswordUtils;

public class UserService {
	private UserDao userDao = new UserDaoImpl();

	public List<User> listUsers(HttpServletRequest request) throws SQLException {
		Object userIdObj = request.getSession().getAttribute(LoginConstants.ID);

		if (userIdObj == null) {
			throw new IllegalStateException("User ID not present in session. Ensure the user is logged in.");
		}

		int loggedUserId;
		try {
			loggedUserId = Integer.parseInt(userIdObj.toString());
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("Invalid user ID: " + userIdObj);
		}

		List<User> userData = userDao.listAll(loggedUserId);

		for (User user : userData) {
			encodeUserPhoto(user);
		}

		return userData;
	}

	public User getUser(String userId) throws SQLException {
		User user = userDao.getById(userId);
		encodeUserPhoto(user);
		return user;
	}

	public User createUser(HttpServletRequest request) throws Exception {
		String username = getUtf8Parameter(request, "addUserUsername");
		String email = getUtf8Parameter(request, "addUserEmail");
		String firstName = getUtf8Parameter(request, "addUserFirstName");
		String lastName = getUtf8Parameter(request, "addUserLastName");
		String password = getUtf8Parameter(request, "addUserPassword");
		String role = getUtf8Parameter(request, "addUserRole");

		byte[] profilePhoto = readPhoto(request, "addUserProfilePhoto");

		User user = new User();
		user.setUsername(username);
		user.setEmail(email);
		user.setFirstName(firstName);
		user.setLastName(lastName);
		
	    if (password != null && !password.isEmpty()) {
	        user.setPassword(PasswordUtils.hashPassword(password));
	    } else {
	        user.setPassword(null);
	    }
	    
		user.setRole(role);
		user.setProfilePhoto(profilePhoto);

		User createdUser = userDao.create(user);
		encodeUserPhoto(createdUser);

		return createdUser;
	}

	public User updateUser(HttpServletRequest request) throws Exception {
		String userId = request.getParameter("userId");
		String firstName = getUtf8Parameter(request, "editUserFirstName");
		String lastName = getUtf8Parameter(request, "editUserLastName");
		String role = getUtf8Parameter(request, "editUserRole");
		String deletePhoto = request.getParameter("deleteProfilePhoto");

		byte[] profilePhoto = null;
		if ("true".equals(deletePhoto)) {
			profilePhoto = null;
		} else {
			profilePhoto = readPhoto(request, "editUserProfilePhoto");
			if (profilePhoto == null) {
				User currentUser = userDao.getById(userId);
				profilePhoto = currentUser.getProfilePhoto();
			}
		}

		User user = new User();
		user.setUserId(userId);
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setRole(role);
		user.setProfilePhoto(profilePhoto);

		User updatedUser = userDao.update(user);
		encodeUserPhoto(updatedUser);

		return updatedUser;
	}

	public boolean deleteUser(String userId) throws SQLException {
		return userDao.deleteUser(userId);
	}

	private String getUtf8Parameter(HttpServletRequest request, String fieldName) throws IOException, ServletException {
		Part part = request.getPart(fieldName);
		if (part != null) {
			try (InputStream is = part.getInputStream()) {
				return new String(is.readAllBytes(), StandardCharsets.UTF_8);
			}
		}
		
		return "";
	}

	private byte[] readPhoto(HttpServletRequest request, String partName) {
		try {
			Part part = request.getPart(partName);
			if (part != null) {
				InputStream inputStream = part.getInputStream();
				if (inputStream.available() > 0) {
					return inputStream.readAllBytes();
				}
			}
		} catch (Exception e) {
			System.err.println("Error reading photo from part '" + partName + "': " + e.getMessage());
		}
		
		return null;
	}

	private void encodeUserPhoto(User user) {
		byte[] photo = user.getProfilePhoto();
		if (photo != null) {
			String photoBase64 = Base64.getEncoder().encodeToString(photo);
			user.setProfilePhotoBase64("data:image/jpeg;base64," + photoBase64);
		}
	}
}
