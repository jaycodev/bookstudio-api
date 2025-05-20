package com.bookstudio.servlets;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.bookstudio.dao.PasswordResetTokenDao;
import com.bookstudio.dao.impl.PasswordResetTokenDaoImpl;

@WebServlet("/ForgotPasswordServlet")
public class ForgotPasswordServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static final long EXPIRATION_TIME = 30 * 60 * 1000;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");

		response.setContentType("application/json");

		String email = request.getParameter("email");

		PasswordResetTokenDao tokenDao = new PasswordResetTokenDaoImpl();
		if (!tokenDao.emailExists(email)) {
			response.getWriter().write(
					"{\"success\": false, \"message\": \"La dirección de correo electrónico ingresada no está asociada a ninguna cuenta.\", \"target\": \"field-error\"}");
			return;
		}

		String token = UUID.randomUUID().toString();
		long expiryTime = System.currentTimeMillis() + EXPIRATION_TIME;

		boolean tokenSaved = tokenDao.savePasswordResetToken(email, token, expiryTime);
		if (!tokenSaved) {
			response.getWriter().write("{\"success\": false, \"message\": \"No se pudo generar el enlace.\"}");
			return;
		}

		boolean emailSent = sendResetEmail(email, token, request);
		if (emailSent) {
			response.getWriter().write("{\"success\": true}");
		} else {
			response.getWriter().write("{\"success\": false, \"message\": \"Ocurrió un error al enviar el correo.\"}");
		}
	}

	private boolean sendResetEmail(String email, String token, HttpServletRequest request) {
		final String username = System.getenv("BOOKSTUDIO_EMAIL");
		final String password = System.getenv("BOOKSTUDIO_PASSWORD");

		if (username == null || password == null) {
		    System.err.println("Credenciales de email no encontradas en variables de entorno");
		    return false;
		}

		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
		props.put("mail.smtp.ssl.trust", "smtp.gmail.com");

		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});

		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(username));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
			message.setSubject("Restablecer tu contraseña");

			String resetLink = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/reset-password.jsp?token=" + token;

			String msg = "<!DOCTYPE html>" + "<html>" + "<head>" + "<meta charset='UTF-8'>" + "<style>"
					+ "body { font-family: Arial, sans-serif; margin: 0; padding: 20px; background-color: #f4f4f4; }"
					+ ".container { max-width: 600px; background: #fff; padding: 20px; border-radius: 10px; box-shadow: 0 0 10px rgba(0,0,0,0.1); text-align: center; }"
					+ ".title { font-size: 24px; font-weight: bold; color: #333; }"
					+ ".content { font-size: 16px; color: #555; line-height: 1.5; }"
					+ ".button { display: inline-block; background-color: #000; color: #fff !important; padding: 12px 20px; border-radius: 8px; text-decoration: none; font-size: 16px; margin-top: 20px; }"
					+ ".footer { font-size: 12px; color: #777; margin-top: 20px; }"
					+ ".link { display: block; margin-top: 10px; color: #000; text-decoration: none; font-size: 14px; word-break: break-word; }"
					+ "</style>" + "</head>" + "<body>" + "<div class='container'>"
					+ "    <p class='title'>¿Tienes problemas para acceder a tu cuenta de BookStudio?</p>"
					+ "    <p class='content'>No te preocupes. Estamos aquí para ayudarte.</p>"
					+ "    <p class='content'>Selecciona el botón a continuación para restablecer tu contraseña.</p>"
					+ "    <p class='content'>Este enlace es válido por 30 minutos. Si no solicitaste restablecer tu contraseña, puedes ignorar este email.</p>"
					+ "    <a href='" + resetLink + "' class='button'>Restablecer contraseña</a>" + "    <a href='"
					+ resetLink + "' class='link'>" + resetLink + "</a>"
					+ "    <p class='footer'>Saludos,<br/>El equipo de BookStudio</p>" + "</div>" + "</body>"
					+ "</html>";

			message.setContent(msg, "text/html; charset=UTF-8");

			Transport.send(message);
			return true;
		} catch (MessagingException e) {
			e.printStackTrace();
			return false;
		}
	}
}
