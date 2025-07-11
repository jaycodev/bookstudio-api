/**
 * login.js
 *
 * Handles login form validation, submission, and dynamic background updates based on the current theme.
 * Implements real-time validation for username and password fields, and performs authentication
 * via REST API using the Fetch API.
 *
 * Also initializes and updates particle animations depending on the selected theme.
 *
 * @author Jason
 */

import { showToast } from '../../shared/utils/ui/index.js'

$(document).ready(function () {
	let formSubmitted = false

	// Validation functions
	function validateUsername() {
		const username = $('#txtUsername').val().trim()
		if (username === '') {
			if (formSubmitted) $('#txtUsername').addClass('is-invalid')
			return false
		} else {
			$('#txtUsername').removeClass('is-invalid')
			return true
		}
	}

	function validatePassword() {
		const password = $('#txtPassword').val().trim()
		if (password === '') {
			if (formSubmitted) $('#txtPassword').addClass('is-invalid')
			return false
		} else {
			$('#txtPassword').removeClass('is-invalid')
			return true
		}
	}

	// Prevent spaces in password field in real-time
	$('#txtPassword').on('input', function () {
		const inputElement = this
		const cursorPosition = inputElement.selectionStart
		const originalValue = $(this).val()
		const newValue = originalValue.replace(/\s/g, '')

		if (originalValue !== newValue) {
			$(this).val(newValue)
			const spacesRemoved = (
				originalValue.slice(0, cursorPosition).match(/\s/g) || []
			).length
			inputElement.setSelectionRange(
				cursorPosition - spacesRemoved,
				cursorPosition - spacesRemoved,
			)
		}
	})

	// Event listeners
	$('#txtUsername, #txtPassword').on('input', function () {
		if (formSubmitted) {
			validateUsername()
			validatePassword()
		}
	})

	// Form submission handling
	$('#loginForm').on('submit', async function (event) {
		event.preventDefault()
		formSubmitted = true

		const isUsernameValid = validateUsername()
		const isPasswordValid = validatePassword()

		if (!isUsernameValid || !isPasswordValid) return

		$('#loginBtn').prop('disabled', true)
		$('#spinner').removeClass('d-none')
		$('#loginText').addClass('d-none')

		const formData = new URLSearchParams()
		formData.append('txtUsername', $('#txtUsername').val().trim())
		formData.append('txtPassword', $('#txtPassword').val().trim())

		try {
			const res = await fetch('/api/auth/login', {
				method: 'POST',
				headers: {
					'Content-Type': 'application/x-www-form-urlencoded',
				},
				body: formData.toString(),
				credentials: 'include',
			})

			const data = await res.json()

			if (data.success) {
				window.location.href = './'
			} else {
				showToast(data.message || 'Error de autenticación.', 'error')
				$('#txtUsername').removeClass('is-invalid')
				$('#txtPassword').removeClass('is-invalid')
			}
		} catch (err) {
			console.error(err)
			showToast('Se produjo un error inesperado.', 'error')
			$('#txtUsername').removeClass('is-invalid')
			$('#txtPassword').removeClass('is-invalid')
		} finally {
			$('#loginBtn').prop('disabled', false)
			$('#spinner').addClass('d-none')
			$('#loginText').removeClass('d-none')
		}
	})
})
