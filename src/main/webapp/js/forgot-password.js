$(document).ready(function() {
	var isFirstSubmit = false;

	function validateEmail() {
		var email = $("#email").val().trim();
		var emailRegex = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,6}$/;
		if (!emailRegex.test(email)) {
			$("#email").addClass("is-invalid");
			$("#email").siblings(".invalid-feedback").html("Por favor ingrese un correo electrónico válido.");
			return false;
		} else {
			$("#email").removeClass("is-invalid");
			$("#email").siblings(".invalid-feedback").html("");
			return true;
		}
	}

	$("#email").on("input", function() {
		if (isFirstSubmit) {
			validateEmail();
		}
	});

	$("#forgotPasswordForm").on("submit", function(e) {
		e.preventDefault();

		isFirstSubmit = true;

		if (!validateEmail()) {
			return;
		}

		var email = $("#email").val().trim();

		$("#sendBtn").prop("disabled", true);
		$("#spinner").removeClass("d-none");
		$("#sendText").addClass("d-none");

		$.ajax({
			type: "POST",
			url: "ForgotPasswordServlet",
			data: { email: email },
			dataType: "json",
			success: function(response) {
				if (response && response.success) {
					showToast(response.message, 'success');
					setTimeout(function() {
						window.location.href = "login.jsp";
					}, 3000);
				} else {
					showToast(response.message, 'error');
					$("#email").val("");
					$("#email").addClass("is-invalid");
				}
			},
			error: function() {
				showToast("Ocurrió un error al procesar la solicitud.", "error");
			},
			complete: function() {
				$("#sendBtn").prop("disabled", false);
				$("#spinner").addClass("d-none");
				$("#sendText").removeClass("d-none");
			}
		});
	});

	const urlParams = new URLSearchParams(window.location.search);
	if (urlParams.get("linkInvalid") === "true") {
		var alertHTML = `
			<div class="alert alert-danger alert-dismissible fade show" role="alert">
			    <i class="bi bi-x-circle me-1"></i>
			    <small>El enlace de restablecimiento ha expirado o es inválido.</small>
			</div>
        `;
		$("#alertContainer").html(alertHTML);
	}
});
