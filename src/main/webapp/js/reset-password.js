$(document).ready(function() {
	var isFirstSubmit = false;

	function getTokenFromURL() {
		const urlParams = new URLSearchParams(window.location.search);
		return urlParams.get("token");
	}

	function redirectToForgotPassword() {
		window.location.href = "forgot-password.jsp?linkInvalid=true";
	}

	var token = getTokenFromURL();
	if (!token) {
		redirectToForgotPassword();
		return;
	}
	
	$.ajax({
		type: "POST",
		url: "ValidateTokenServlet",
		data: { token: token },
		dataType: "json",
		success: function(response) {
			if (!response.valid) {
				redirectToForgotPassword();
			}
		},
		error: function() {
			redirectToForgotPassword();
		}
	});

	function validatePassword() {
		var password = $("#newPassword").val().trim();
		var passwordRegex = /^(?=.*[A-Z])(?=.*\d)(?=.*[\W_]).{8,}$/;

		if (!passwordRegex.test(password)) {
			$("#newPassword").addClass("is-invalid");
			$("#newPassword").siblings(".invalid-feedback").html("La contraseña debe tener 8 caracteres, una mayúscula, un número y un símbolo.");
			return false;
		} else {
			$("#newPassword").removeClass("is-invalid");
			$("#newPassword").siblings(".invalid-feedback").html("");
			return true;
		}
	}

	function validateConfirmPassword() {
		var password = $("#newPassword").val().trim();
		var confirmPassword = $("#confirmNewPassword").val().trim();

		if (confirmPassword !== password) {
			$("#confirmNewPassword").addClass("is-invalid");
			$("#confirmNewPassword").siblings(".invalid-feedback").html("Las contraseñas no coinciden.");
			return false;
		} else {
			$("#confirmNewPassword").removeClass("is-invalid");
			$("#confirmNewPassword").siblings(".invalid-feedback").html("");
			return true;
		}
	}

	$("#newPassword, #confirmNewPassword").on("input", function() {
		if (isFirstSubmit) {
			validatePassword();
			validateConfirmPassword();
		}
	});

	$("#resetPasswordForm").on("submit", function(e) {
		e.preventDefault();

		isFirstSubmit = true;

		if (!validatePassword() || !validateConfirmPassword()) {
			return;
		}

		var newPassword = $("#newPassword").val().trim();

		$("#resetBtn").prop("disabled", true);
		$("#spinner").removeClass("d-none");
		$("#resetText").addClass("d-none");

		$.ajax({
			type: "POST",
			url: "ResetPasswordServlet",
			data: { token: token, newPassword: newPassword },
			dataType: "json",
			success: function(response) {
				if (response && response.success) {
					showToast(response.message, 'success');
					setTimeout(function() {
						window.location.href = "login.jsp";
					}, 3000);
				} else {
					showToast(response.message, 'error');
				}
			},
			error: function() {
				showToast("Ocurrió un error al procesar la solicitud.", "error");
			},
			complete: function() {
				$("#resetBtn").prop("disabled", false);
				$("#spinner").addClass("d-none");
				$("#resetText").removeClass("d-none");
			}
		});
	});
});