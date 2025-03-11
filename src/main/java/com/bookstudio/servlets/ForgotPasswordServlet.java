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
			response.getWriter().write("{\"success\": false, \"message\": \"El correo ingresado no está registrado.\"}");
			return;
		}

		String token = UUID.randomUUID().toString();
		long expiryTime = System.currentTimeMillis() + EXPIRATION_TIME;

		boolean tokenSaved = tokenDao.savePasswordResetToken(email, token, expiryTime);
		if (!tokenSaved) {
			response.getWriter()
					.write("{\"success\": false, \"message\": \"No se pudo generar el enlace.\"}");
			return;
		}

		boolean emailSent = sendResetEmail(email, token, request);
		if (emailSent) {
			response.getWriter().write(
					"{\"success\": true, \"message\": \"Se ha enviado un correo para restablecer la contraseña.\"}");
		} else {
			response.getWriter().write("{\"success\": false, \"message\": \"Ocurrió un error al enviar el correo.\"}");
		}
	}

	private boolean sendResetEmail(String email, String token, HttpServletRequest request) {
		Properties configProps = new Properties();
		try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
			if (input == null) {
				System.err.println("No se pudo encontrar config.properties");
				return false;
			}
			configProps.load(input);
		} catch (IOException ex) {
			ex.printStackTrace();
			return false;
		}

		final String username = configProps.getProperty("bookstudio.email");
		final String password = configProps.getProperty("bookstudio.password");

		if (username == null || password == null) {
			System.err.println("Credenciales de email no encontradas en config.properties");
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

			String resetLink = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
					+ request.getContextPath() + "/reset-password.jsp?token=" + token;

			String msg = "<html><body>"
		             + "<p>Hola,</p>"
		             + "<p>Para restablecer tu contraseña, por favor haz clic en el siguiente enlace:</p>"
		             + "<p><a href=\"" + resetLink + "\">Restablecer contraseña</a></p>"
		             + "<p>Este enlace es válido por 30 minutos. Si no solicitaste este cambio, por favor ignora este correo.</p>"
		             + "<p>Saludos,<br/>El equipo de BookStudio</p>"
		             + "</body></html>";

			message.setContent(msg, "text/html; charset=UTF-8");

			Transport.send(message);
			return true;
		} catch (MessagingException e) {
			e.printStackTrace();
			return false;
		}
	}
}
