/**
 * users.js
 *
 * Handles the initialization and behavior of the users table,
 * including loading data, configuring modals for creating, viewing,
 * editing, and deleting user records.
 *
 * Uses the Fetch API to communicate with RESTful endpoints for all user-related
 * CRUD operations. Manages UI components such as placeholders, enhanced dropdowns,
 * validation feedback, loading states, image cropping, and tooltips.
 *
 * Also includes features for generating PDF reports and exporting table data to Excel.
 *
 * @author Jason
 */

import { loadTableData } from '../../shared/utils/tables/index.js'

import {
	isValidEmail,
	isValidUsername,
	isValidText,
	isValidPassword,
	doPasswordsMatch,
	isValidImageFile,
	validateImageFileUI,
} from '../../shared/utils/forms/index.js'

import {
	showToast,
	toggleButtonLoading,
	toggleModalLoading,
	placeholderColorSelect,
	placeholderColorEditSelect,
	initializeCropper,
	setupBootstrapSelectDropdownStyles,
	initializeTooltips,
	togglePasswordVisibility,
} from '../../shared/utils/ui/index.js'

/*****************************************
 * GLOBAL VARIABLES AND HELPER FUNCTIONS
 *****************************************/

// Global variable to handle profile photo deletion in edit modal
let deletePhotoFlag = false

/*****************************************
 * TABLE HANDLING
 *****************************************/

function generateRow(user) {
	return `
		<tr>
			<td class="align-middle text-start">
				<span class="badge bg-body-tertiary text-body-emphasis border">${user.formattedUserId}</span>
			</td>
			<td class="align-middle text-start">${user.username}</td>
			<td class="align-middle text-start">${user.email}</td>
			<td class="align-middle text-start">${user.firstName}</td>
			<td class="align-middle text-start">${user.lastName}</td>
			<td class="align-middle text-start">
			  <span class="badge bg-body-secondary text-body-emphasis border">
			    ${
						user.role === 'administrador'
							? '<i class="bi bi-shield-lock me-1"></i> Administrador'
							: '<i class="bi bi-person-workspace me-1"></i> Bibliotecario'
					}
			  </span>
			</td>
			<td class="align-middle text-center">
				${
					user.profilePhotoUrl
						? `<img src="${user.profilePhotoUrl}" alt="Foto del Usuario" class="img-fluid rounded-circle" style="width: 23px; height: 23px;">`
						: `<svg xmlns="http://www.w3.org/2000/svg" width="23" height="23" fill="currentColor" class="bi-person-circle" viewBox="0 0 16 16">
						<path d="M11 6a3 3 0 1 1-6 0 3 3 0 0 1 6 0"></path>
						<path fill-rule="evenodd" d="M0 8a8 8 0 1 1 16 0A8 8 0 0 1 0 8m8-7a7 7 0 0 0-5.468 11.37C3.242 11.226 4.805 10 8 10s4.757 1.225 5.468 2.37A7 7 0 0 0 8 1"></path>
					</svg>`
				}
			</td>
			<td class="align-middle text-center">
				<div class="d-inline-flex gap-2">
					<button class="btn btn-sm btn-icon-hover" data-tooltip="tooltip" data-bs-placement="top" title="Detalles"
						data-bs-toggle="modal" data-bs-target="#detailsUserModal" data-id="${user.userId}" data-formatted-id="${user.formattedUserId}">
						<i class="bi bi-info-circle"></i>
					</button>
					<button class="btn btn-sm btn-icon-hover" data-tooltip="tooltip" data-bs-placement="top" title="Editar"
						data-bs-toggle="modal" data-bs-target="#editUserModal" data-id="${user.userId}" data-formatted-id="${user.formattedUserId}">
						<i class="bi bi-pencil"></i>
					</button>
					<button class="btn btn-sm btn-icon-hover" data-tooltip="tooltip" data-bs-placement="top" title="Eliminar"
						data-bs-toggle="modal" data-bs-target="#deleteUserModal" data-id="${user.userId}" data-formatted-id="${user.formattedUserId}">
						<i class="bi bi-trash"></i>
					</button>
				</div>
			</td>
		</tr>
	`
}

function addRowToTable(user) {
	const table = $('#table').DataTable()
	const rowHtml = generateRow(user)
	const $row = $(rowHtml)

	table.row.add($row).draw(false)

	initializeTooltips($row)
}

function updateRowInTable(user) {
	const table = $('#table').DataTable()

	const row = table
		.rows()
		.nodes()
		.to$()
		.filter(function () {
			return (
				$(this).find('td').eq(0).text().trim() ===
				user.formattedUserId.toString()
			)
		})

	if (row.length > 0) {
		row.find('td').eq(3).text(user.firstName)
		row.find('td').eq(4).text(user.lastName)
		row
			.find('td')
			.eq(5)
			.find('span')
			.html(
				user.role === 'administrador'
					? '<i class="bi bi-shield-lock me-1"></i> Administrador'
					: '<i class="bi bi-person-workspace me-1"></i> Bibliotecario',
			)

		if (user.profilePhotoUrl && user.profilePhotoUrl.trim() !== '') {
			row
				.find('td')
				.eq(6)
				.html(
					`<img src="${user.profilePhotoUrl}" alt="Foto del Usuario" class="img-fluid rounded-circle" style="width: 23px; height: 23px;">`,
				)
		} else {
			row.find('td').eq(6).html(`
				<svg xmlns="http://www.w3.org/2000/svg" width="23" height="23" fill="currentColor" class="bi-person-circle" viewBox="0 0 16 16">
					<path d="M11 6a3 3 0 1 1-6 0 3 3 0 0 1 6 0"></path>
					<path fill-rule="evenodd" d="M0 8a8 8 0 1 1 16 0A8 8 0 0 1 0 8m8-7a7 7 0 0 0-5.468 11.37C3.242 11.226 4.805 10 8 10s4.757 1.225 5.468 2.37A7 7 0 0 0 8 1"></path>
				</svg>
			`)
		}

		table.row(row).invalidate().draw(false)

		initializeTooltips(row)
	}
}

/*****************************************
 * FORM LOGIC
 *****************************************/

function handleAddUserForm() {
	let isFirstSubmit = true

	$('#addUserModal').on('hidden.bs.modal', function () {
		isFirstSubmit = true
		$('#addUserForm').data('submitted', false)
	})

	$('#addUserForm').on('input change', 'input, select', function () {
		if (!isFirstSubmit) {
			validateAddField($(this))
		}
	})

	$('#addUserForm').on('submit', async function (event) {
		event.preventDefault()

		if ($(this).data('submitted') === true) return
		$(this).data('submitted', true)

		if (isFirstSubmit) isFirstSubmit = false

		const form = this
		let isValid = true

		$(form)
			.find('input, select')
			.not('.bootstrap-select input[type="search"]')
			.each(function () {
				if (!validateAddField($(this))) isValid = false
			})

		if (!isValid) {
			$(form).data('submitted', false)
			return
		}

		const formData = new FormData(form)
		const raw = Object.fromEntries(formData.entries())

		const user = {
			username: raw.addUserUsername,
			email: raw.addUserEmail,
			firstName: raw.addUserFirstName,
			lastName: raw.addUserLastName,
			password: raw.addUserPassword,
			role: raw.addUserRole,
			profilePhotoUrl: null, // 🔜 Preparado para Cloudinary
		}

		const submitButton = $('#addUserBtn')
		toggleButtonLoading(submitButton, true)

		try {
			if (cropper) {
				const photoBlob = await new Promise((resolve) => {
					cropper
						.getCroppedCanvas({ width: 460, height: 460 })
						.toBlob((blob) => resolve(blob), 'image/jpeg', 0.7)
				})

				if (photoBlob) {
					// 🔜 Preparado para Cloudinary
				}
			}

			const response = await fetch('./api/users', {
				method: 'POST',
				headers: {
					'Content-Type': 'application/json',
					Accept: 'application/json',
				},
				body: JSON.stringify(user),
			})

			const json = await response.json()

			if (response.ok && json.success) {
				addRowToTable(json.data)
				$('#addUserModal').modal('hide')
				showToast('Usuario agregado exitosamente.', 'success')
			} else if (
				response.status === 400 &&
				json.errorType === 'validation_error'
			) {
				if (json.errors && Array.isArray(json.errors)) {
					json.errors.forEach((err) => {
						setFieldError(err.field, err.message)
					})
				} else {
					console.warn('Validation error sin detalles de campos:', json)
				}
				$('#addUserForm').data('submitted', false)
			} else {
				console.error(
					`Backend error (${json.errorType} - ${json.statusCode}):`,
					json.message,
				)
				showToast(
					json.message || 'Hubo un error al agregar el usuario.',
					'error',
				)
				$('#addUserModal').modal('hide')
			}
		} catch (err) {
			console.error('Unexpected error:', err)
			showToast('Hubo un error inesperado.', 'error')
			$('#addUserModal').modal('hide')
		} finally {
			toggleButtonLoading(submitButton, false)
		}
	})

	function setFieldError(fieldId, message) {
		const field = $('#' + fieldId)
		field.addClass('is-invalid')
		field.siblings('.invalid-feedback').html(message).show()
	}
}

function validateAddField(field) {
	if (field.attr('type') === 'search') {
		return true
	}

	let errorMessage = 'Este campo es obligatorio.'
	let isValid = true

	// Default validation
	if (!field.val() || (field[0].checkValidity && !field[0].checkValidity())) {
		field.addClass('is-invalid')
		field.siblings('.invalid-feedback').html(errorMessage)
		isValid = false
	} else {
		field.removeClass('is-invalid')
	}

	// Email validation
	if (field.is('#addUserEmail')) {
		const result = isValidEmail(field.val())
		if (!result.valid) {
			errorMessage = result.message
			isValid = false
		}
	}

	// Username validation
	if (field.is('#addUserUsername')) {
		const result = isValidUsername(field.val())
		if (!result.valid) {
			errorMessage = result.message
			isValid = false
		}
	}

	// First name validation
	if (field.is('#addUserFirstName')) {
		const result = isValidText(field.val(), 'nombre')
		if (!result.valid) {
			isValid = false
			errorMessage = result.message
		}
	}

	// Last name validation
	if (field.is('#addUserLastName')) {
		const result = isValidText(field.val(), 'apellido')
		if (!result.valid) {
			isValid = false
			errorMessage = result.message
		}
	}

	// Password validation
	if (field.is('#addUserPassword')) {
		const result = isValidPassword(field.val())
		if (!result.valid) {
			errorMessage = result.message
			isValid = false
		}
	}

	// Confirm password validation
	if (field.is('#addUserConfirmPassword')) {
		const password = $('#addUserPassword').val()
		const result = doPasswordsMatch(password, field.val())
		if (!result.valid) {
			errorMessage = result.message
			isValid = false
		}
	}

	// Profile photo validation
	if (field.is('#addUserProfilePhoto')) {
		const file = field[0].files[0]
		const result = isValidImageFile(file)

		if (!result.valid) {
			isValid = false
			errorMessage = result.message
		} else {
			field.removeClass('is-invalid')
			return true
		}
	}

	// Select validation
	if (field.is('select')) {
		const container = field.closest('.bootstrap-select')
		container.toggleClass('is-invalid', field.hasClass('is-invalid'))
		container.siblings('.invalid-feedback').html(errorMessage)
	}

	if (!isValid) {
		field.addClass('is-invalid')
		field.siblings('.invalid-feedback').html(errorMessage).show()
	} else {
		field.removeClass('is-invalid')
		field.siblings('.invalid-feedback').hide()
	}

	return isValid
}

$('#addUserProfilePhoto, #editUserProfilePhoto').on('change', function () {
	validateImageFileUI($(this))
})

function handleEditUserForm() {
	let isFirstSubmit = true

	$('#editUserModal').on('hidden.bs.modal', function () {
		isFirstSubmit = true
		$('#editUserForm').data('submitted', false)
	})

	$('#editUserForm').on('input change', 'input, select', function () {
		if (!isFirstSubmit) {
			validateEditField($(this))
		}
	})

	$('#editUserForm').on('submit', async function (event) {
		event.preventDefault()

		if ($(this).data('submitted') === true) return
		$(this).data('submitted', true)

		if (isFirstSubmit) isFirstSubmit = false

		const form = this
		let isValid = true

		$(form)
			.find('input, select')
			.not('.bootstrap-select input[type="search"]')
			.each(function () {
				if (!validateEditField($(this))) isValid = false
			})

		if (!isValid) {
			$(this).data('submitted', false)
			return
		}

		const userId = $('#editUserForm').data('userId')
		const formData = new FormData(form)
		const raw = Object.fromEntries(formData.entries())

		const user = {
			userId: parseInt(userId),
			firstName: raw.editUserFirstName,
			lastName: raw.editUserLastName,
			role: raw.editUserRole,
			deletePhoto: deletePhotoFlag || false,
			profilePhotoUrl: null, // 🔜 Preparado para Cloudinary
		}

		const submitButton = $('#editUserBtn')
		toggleButtonLoading(submitButton, true)

		try {
			if (cropper) {
				const photoBlob = await new Promise((resolve) => {
					cropper
						.getCroppedCanvas({ width: 460, height: 460 })
						.toBlob((blob) => resolve(blob), 'image/jpeg', 0.7)
				})

				if (photoBlob) {
					// 🔜 Preparado para Cloudinary
				}
			}

			const response = await fetch('./api/users', {
				method: 'PUT',
				headers: {
					'Content-Type': 'application/json',
					Accept: 'application/json',
				},
				body: JSON.stringify(user),
			})

			const json = await response.json()

			if (response.ok && json.success) {
				updateRowInTable(json.data)
				$('#editUserModal').modal('hide')
				showToast('Usuario actualizado exitosamente.', 'success')
			} else if (
				response.status === 400 &&
				json.errorType === 'validation_error'
			) {
				if (json.errors && Array.isArray(json.errors)) {
					json.errors.forEach((err) => {
						setFieldError(err.field, err.message)
					})
				} else {
					console.warn('Validation error sin detalles de campos:', json)
				}
				$('#editUserForm').data('submitted', false)
			} else {
				console.error(
					`Backend error (${json.errorType} - ${json.statusCode}):`,
					json.message,
				)
				showToast(
					json.message || 'Hubo un error al actualizar el usuario.',
					'error',
				)
				$('#editUserModal').modal('hide')
			}
		} catch (err) {
			console.error('Unexpected error:', err)
			showToast('Hubo un error inesperado.', 'error')
			$('#editUserModal').modal('hide')
		} finally {
			toggleButtonLoading(submitButton, false)
		}
	})

	function setFieldError(fieldId, message) {
		const field = $('#' + fieldId)
		field.addClass('is-invalid')
		field.siblings('.invalid-feedback').html(message).show()
	}
}

function validateEditField(field) {
	if (field.attr('type') === 'search') {
		return true
	}

	let errorMessage = 'Este campo es obligatorio.'
	let isValid = true

	// Default validation
	if (!field.val() || (field[0].checkValidity && !field[0].checkValidity())) {
		field.addClass('is-invalid')
		field.siblings('.invalid-feedback').html(errorMessage)
		isValid = false
	} else {
		field.removeClass('is-invalid')
	}

	// First name validation
	if (field.is('#editUserFirstName')) {
		const result = isValidText(field.val(), 'nombre')
		if (!result.valid) {
			isValid = false
			errorMessage = result.message
		}
	}

	// Last name validation
	if (field.is('#editUserLastName')) {
		const result = isValidText(field.val(), 'apellido')
		if (!result.valid) {
			isValid = false
			errorMessage = result.message
		}
	}

	// Profile photo validation
	if (field.is('#editUserProfilePhoto')) {
		const file = field[0].files[0]
		const result = isValidImageFile(file)

		if (!result.valid) {
			isValid = false
			errorMessage = result.message
		} else {
			field.removeClass('is-invalid')
			return true
		}
	}

	// Select validation
	if (field.is('select')) {
		const container = field.closest('.bootstrap-select')
		container.toggleClass('is-invalid', field.hasClass('is-invalid'))
		container
			.siblings('.invalid-feedback')
			.html('Opción seleccionada inactiva o no existente.')
	}

	if (!isValid) {
		field.addClass('is-invalid')
		field.siblings('.invalid-feedback').html(errorMessage).show()
	} else {
		field.removeClass('is-invalid')
		field.siblings('.invalid-feedback').hide()
	}

	return isValid
}

function handleDeleteUser() {
	let isSubmitted = false

	$('#confirmDeleteUser')
		.off('click')
		.on('click', async function () {
			if (isSubmitted) return
			isSubmitted = true

			const userId = $(this).data('userId')
			const formattedUserId = $(this).data('formattedUserId')

			toggleButtonLoading($(this), true)

			try {
				const response = await fetch(
					`./api/users/${encodeURIComponent(userId)}`,
					{
						method: 'DELETE',
						headers: {
							Accept: 'application/json',
						},
					},
				)

				const json = await response.json()

				if (response.ok && json.success) {
					const table = $('#table').DataTable()
					const row = table
						.rows()
						.nodes()
						.to$()
						.filter(function () {
							return (
								$(this).find('td').eq(0).text().trim() ===
								formattedUserId.toString()
							)
						})

					if (row.length > 0) {
						table.row(row).remove().draw(false)
					}

					$('#deleteUserModal').modal('hide')
					showToast('Usuario eliminado exitosamente.', 'success')
				} else {
					console.error(
						`Backend error (${json.errorType} - ${json.statusCode}):`,
						json.message,
					)
					$('#deleteUserModal').modal('hide')
					showToast(
						json.message || 'Hubo un error al eliminar el usuario.',
						'error',
					)
				}
			} catch (err) {
				console.error('Unexpected error:', err)
				showToast('Hubo un error inesperado.', 'error')
				$('#deleteUserModal').modal('hide')
			} finally {
				toggleButtonLoading($(this), false)
			}
		})
}

/*****************************************
 * MODAL MANAGEMENT
 *****************************************/

function loadModalData() {
	// Add Modal
	$(document).on('click', '[data-bs-target="#addUserModal"]', function () {
		$('#addUserRole')
			.selectpicker('destroy')
			.empty()
			.append(
				$('<option>', {
					value: 'administrador',
					text: 'Administrador',
				}),
				$('<option>', {
					value: 'bibliotecario',
					text: 'Bibliotecario',
				}),
			)
		$('#addUserRole').selectpicker()

		$('#defaultAddPhotoContainer').removeClass('d-none')
		$('#deleteAddPhotoBtn').addClass('d-none')

		$('#addUserForm')[0].reset()
		$('#addUserForm .is-invalid').removeClass('is-invalid')

		$('#cropperContainerAdd').addClass('d-none')

		if (cropper) {
			cropper.destroy()
			cropper = null
		}

		$('#addUserForm .password-field').attr('type', 'password')
		$('#addUserForm .input-group-text')
			.find('i')
			.removeClass('bi-eye-slash')
			.addClass('bi-eye')

		preventSpacesInPasswordField('#addUserPassword, #addUserConfirmPassword')
	})

	// Details Modal
	$(document).on('click', '[data-bs-target="#detailsUserModal"]', function () {
		const userId = $(this).data('id')
		$('#detailsUserModalID').text($(this).data('formatted-id'))

		toggleModalLoading(this, true)

		fetch(`./api/users/${encodeURIComponent(userId)}`, {
			method: 'GET',
			headers: {
				Accept: 'application/json',
			},
		})
			.then(async (response) => {
				if (!response.ok) {
					const errorData = await response.json()
					throw { status: response.status, ...errorData }
				}
				return response.json()
			})
			.then((data) => {
				$('#detailsUserID').text(data.formattedUserId)
				$('#detailsUserUsername').text(data.username)
				$('#detailsUserEmail').text(data.email)
				$('#detailsUserFirstName').text(data.firstName)
				$('#detailsUserLastName').text(data.lastName)

				$('#detailsUserRole').html(
					data.role === 'administrador'
						? '<i class="bi bi-shield-lock me-1"></i> Administrador'
						: '<i class="bi bi-person-workspace me-1"></i> Bibliotecario',
				)

				if (data.profilePhotoUrl) {
					$('#detailsUserImg')
						.attr('src', data.profilePhotoUrl)
						.removeClass('d-none')
					$('#detailsUserSvg').addClass('d-none')
				} else {
					$('#detailsUserImg').addClass('d-none')
					$('#detailsUserSvg').removeClass('d-none')
				}

				toggleModalLoading(this, false)
			})
			.catch((error) => {
				console.error(
					`Error loading user details (${error.errorType || 'unknown'} - ${error.status}):`,
					error.message || error,
				)
				showToast('Hubo un error al cargar los detalles del usuario.', 'error')
				$('#detailsUserModal').modal('hide')
			})
	})

	// Edit Modal
	$(document).on('click', '[data-bs-target="#editUserModal"]', function () {
		const userId = $(this).data('id')
		$('#editUserModalID').text($(this).data('formatted-id'))

		toggleModalLoading(this, true)

		fetch(`./api/users/${encodeURIComponent(userId)}`, {
			method: 'GET',
			headers: {
				Accept: 'application/json',
			},
		})
			.then(async (response) => {
				if (!response.ok) {
					const errorData = await response.json()
					throw { status: response.status, ...errorData }
				}
				return response.json()
			})
			.then((data) => {
				$('#editUserForm').data('userId', data.userId)
				$('#editUserUsername').val(data.username)
				$('#editUserEmail').val(data.email)
				$('#editUserFirstName').val(data.firstName)
				$('#editUserLastName').val(data.lastName)

				$('#editUserRole')
					.selectpicker('destroy')
					.empty()
					.append(
						$('<option>', { value: 'administrador', text: 'Administrador' }),
						$('<option>', { value: 'bibliotecario', text: 'Bibliotecario' }),
					)
				$('#editUserRole').val(data.role)
				$('#editUserRole').selectpicker()

				updateEditImageContainer(data.profilePhotoUrl)

				$('#editUserForm .is-invalid').removeClass('is-invalid')
				placeholderColorEditSelect()

				$('#editUserForm')
					.find('select')
					.each(function () {
						validateEditField($(this), true)
					})

				$('#editUserProfilePhoto').val('')

				toggleModalLoading(this, false)
			})
			.catch((error) => {
				console.error(
					`Error loading user details for editing (${error.errorType || 'unknown'} - ${error.status}):`,
					error.message || error,
				)
				showToast('Hubo un error al cargar los datos del usuario.', 'error')
				$('#editUserModal').modal('hide')
			})

		$('#cropperContainerEdit').addClass('d-none')
		if (cropper) {
			cropper.destroy()
			cropper = null
		}
	})

	// Delete Modal
	$('#deleteUserModal').on('show.bs.modal', function (event) {
		const button = $(event.relatedTarget)
		const userId = button.data('id')
		const formattedUserId = button.data('formatted-id')

		$('#deleteUserModalID').text(formattedUserId)
		$('#confirmDeleteUser').data('userId', userId)
		$('#confirmDeleteUser').data('formattedUserId', formattedUserId)
	})
}

function preventSpacesInPasswordField(selector) {
	$(selector)
		.off('input')
		.on('input', function () {
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
}

function updateEditImageContainer(profilePhotoUrl) {
	const $editImageContainer = $('#currentEditPhotoContainer')
	const $deleteEditPhotoBtn = $('#deleteEditPhotoBtn')

	$editImageContainer.empty()

	if (profilePhotoUrl) {
		$editImageContainer.html(
			`<img src="${profilePhotoUrl}" class="img-fluid rounded-circle" alt="Foto del Usuario">`,
		)
		$deleteEditPhotoBtn.removeClass('d-none')
	} else {
		$editImageContainer.html(
			`<svg xmlns="http://www.w3.org/2000/svg" width="180" height="180" fill="currentColor" class="bi-person-circle" viewBox="0 0 16 16">
				<path d="M11 6a3 3 0 1 1-6 0 3 3 0 0 1 6 0"/>
				<path fill-rule="evenodd" d="M0 8a8 8 0 1 1 16 0A8 8 0 0 1 0 8m8-7a7 7 0 0 0-5.468 11.37C3.242 11.226 4.805 10 8 10s4.757 1.225 5.468 2.37A7 7 0 0 0 8 1"/>
            </svg>`,
		)
		$deleteEditPhotoBtn.addClass('d-none')
	}
	$editImageContainer.removeClass('d-none')
}

$('#deleteAddPhotoBtn').on('click', function () {
	$(this).addClass('d-none')

	if (cropper) {
		cropper.destroy()
		cropper = null
	}
	$('#cropperContainerAdd').addClass('d-none')
	$('#addUserProfilePhoto').val('')
	$('#defaultAddPhotoContainer').removeClass('d-none')
})

$('#deleteEditPhotoBtn').on('click', function () {
	deletePhotoFlag = true
	updateEditImageContainer(null)

	$(this).addClass('d-none')

	if (cropper) {
		cropper.destroy()
		cropper = null
	}
	$('#cropperContainerEdit').addClass('d-none')
	$('#editUserProfilePhoto').val('')
})

let cropper
const $cropperContainerAdd = $('#cropperContainerAdd')
const $imageToCropAdd = $('#imageToCropAdd')
const $cropperContainerEdit = $('#cropperContainerEdit')
const $imageToCropEdit = $('#imageToCropEdit')

$('#addUserProfilePhoto, #editUserProfilePhoto').on('change', function () {
	const file = this.files[0]
	deletePhotoFlag = false

	$('#deleteAddPhotoBtn').addClass('d-none')
	$('#deleteEditPhotoBtn').addClass('d-none')

	if (
		file &&
		['image/jpeg', 'image/png', 'image/gif', 'image/webp'].includes(file.type)
	) {
		$('#defaultAddPhotoContainer').addClass('d-none')
		$('#currentEditPhotoContainer').addClass('d-none')

		$('#deleteAddPhotoBtn').removeClass('d-none')
		$('#deleteEditPhotoBtn').removeClass('d-none')

		let $container, $image
		if ($(this).is('#addUserProfilePhoto')) {
			$container = $cropperContainerAdd
			$image = $imageToCropAdd
		} else {
			$container = $cropperContainerEdit
			$image = $imageToCropEdit
		}
		initializeCropper(file, $container, $image, cropper)
	} else {
		if ($(this).is('#addUserProfilePhoto')) {
			$cropperContainerAdd.addClass('d-none')
			if (cropper) {
				cropper.destroy()
				cropper = null
			}
			$('#defaultAddPhotoContainer').removeClass('d-none')
		} else {
			$cropperContainerEdit.addClass('d-none')
			if (cropper) {
				cropper.destroy()
				cropper = null
			}
			$('#currentEditPhotoContainer').removeClass('d-none')
		}

		if ($('#currentEditPhotoContainer').find('img').length > 0) {
			$('#deleteEditPhotoBtn').removeClass('d-none')
		}
	}
})

function generatePDF(dataTable) {
	const pdfBtn = $('#generatePDF')
	toggleButtonLoading(pdfBtn, true)

	let hasWarnings = false

	try {
		const { jsPDF } = window.jspdf
		const doc = new jsPDF('l', 'mm', 'a4')
		const logoUrl = '/images/bookstudio-logo-no-bg.png'

		const currentDate = new Date()
		const fecha = currentDate.toLocaleDateString('es-ES', {
			day: '2-digit',
			month: 'long',
			year: 'numeric',
		})
		const hora = currentDate.toLocaleTimeString('en-US', {
			hour: '2-digit',
			minute: '2-digit',
			hour12: true,
		})

		const pageWidth = doc.internal.pageSize.getWidth()
		const margin = 10
		const topMargin = 5

		try {
			doc.addImage(logoUrl, 'PNG', margin, topMargin - 5, 30, 30)
		} catch (imgError) {
			console.warn('Logo not available:', imgError)
			showToast('No se pudo cargar el logo. Se continuará sin él.', 'warning')
			hasWarnings = true
		}

		doc.setFont('helvetica', 'bold')
		doc.setFontSize(14)
		doc.text('Lista de usuarios', pageWidth / 2, topMargin + 13, {
			align: 'center',
		})

		doc.setFont('helvetica', 'normal')
		doc.setFontSize(8)
		doc.text(`Fecha: ${fecha}`, pageWidth - margin, topMargin + 10, {
			align: 'right',
		})
		doc.text(`Hora: ${hora}`, pageWidth - margin, topMargin + 15, {
			align: 'right',
		})

		const data = dataTable
			.rows({ search: 'applied' })
			.nodes()
			.toArray()
			.map((row) => {
				return [
					row.cells[0].innerText.trim(),
					row.cells[1].innerText.trim(),
					row.cells[2].innerText.trim(),
					row.cells[3].innerText.trim(),
					row.cells[4].innerText.trim(),
					row.cells[5].innerText.trim(),
				]
			})

		doc.autoTable({
			startY: topMargin + 25,
			margin: { left: margin, right: margin },
			head: [
				[
					'Código',
					'Nombre de usuario',
					'Correo electrónico',
					'Nombres',
					'Apellidos',
					'Rol',
				],
			],
			body: data,
			theme: 'grid',
			headStyles: {
				fillColor: [0, 0, 0],
				textColor: 255,
				fontStyle: 'bold',
				fontSize: 8,
				halign: 'left',
			},
			bodyStyles: {
				font: 'helvetica',
				fontSize: 7,
				halign: 'left',
			},
			didParseCell: function (data) {
				if (data.section === 'body' && data.column.index === 6) {
					data.cell.styles.textColor = [0, 0, 0]
				}
			},
		})

		const filename = `Lista_de_usuarios_bookstudio_${fecha.replace(/\s+/g, '_')}.pdf`

		const pdfBlob = doc.output('blob')
		const blobUrl = URL.createObjectURL(pdfBlob)
		const link = document.createElement('a')
		link.href = blobUrl
		link.download = filename
		document.body.appendChild(link)
		link.click()
		document.body.removeChild(link)

		if (!hasWarnings) {
			showToast('PDF generado exitosamente.', 'success')
		}
	} catch (error) {
		console.error('Error generating PDF file:', error)
		showToast(
			'Ocurrió un error al generar el PDF. Inténtalo nuevamente.',
			'error',
		)
	} finally {
		toggleButtonLoading(pdfBtn, false)
	}
}

function generateExcel(dataTable) {
	const excelBtn = $('#generateExcel')
	toggleButtonLoading(excelBtn, true)

	try {
		const workbook = new ExcelJS.Workbook()
		const worksheet = workbook.addWorksheet('Usuarios')

		const currentDate = new Date()
		const dateStr = currentDate.toLocaleDateString('es-ES', {
			day: '2-digit',
			month: 'long',
			year: 'numeric',
		})
		const timeStr = currentDate.toLocaleTimeString('en-US', {
			hour: '2-digit',
			minute: '2-digit',
			hour12: true,
		})

		worksheet.mergeCells('A1:F1')
		const titleCell = worksheet.getCell('A1')
		titleCell.value = 'Lista de usuarios - BookStudio'
		titleCell.font = { name: 'Arial', size: 14, bold: true }
		titleCell.alignment = { horizontal: 'center' }

		worksheet.mergeCells('A2:F2')
		const dateTimeCell = worksheet.getCell('A2')
		dateTimeCell.value = `Fecha: ${dateStr}  Hora: ${timeStr}`
		dateTimeCell.alignment = { horizontal: 'center' }

		worksheet.columns = [
			{ key: 'id', width: 10 },
			{ key: 'usuario', width: 20 },
			{ key: 'correo', width: 30 },
			{ key: 'nombres', width: 30 },
			{ key: 'apellidos', width: 30 },
			{ key: 'rol', width: 20 },
		]

		const headerRow = worksheet.getRow(4)
		headerRow.values = [
			'Código',
			'Nombre de usuario',
			'Correo electrónico',
			'Nombres',
			'Apellidos',
			'Rol',
		]
		headerRow.eachCell((cell) => {
			cell.font = { bold: true, color: { argb: 'FFFFFF' } }
			cell.fill = {
				type: 'pattern',
				pattern: 'solid',
				fgColor: { argb: '000000' },
			}
			cell.alignment = { horizontal: 'left', vertical: 'middle' }
			cell.border = {
				top: { style: 'thin', color: { argb: 'FFFFFF' } },
				bottom: { style: 'thin', color: { argb: 'FFFFFF' } },
				left: { style: 'thin', color: { argb: 'FFFFFF' } },
				right: { style: 'thin', color: { argb: 'FFFFFF' } },
			}
		})

		const data = dataTable
			.rows({ search: 'applied' })
			.nodes()
			.toArray()
			.map((row) => {
				return {
					id: row.cells[0].innerText.trim(),
					usuario: row.cells[1].innerText.trim(),
					correo: row.cells[2].innerText.trim(),
					nombres: row.cells[3].innerText.trim(),
					apellidos: row.cells[4].innerText.trim(),
					rol: row.cells[5].innerText.trim(),
				}
			})

		data.forEach((item) => worksheet.addRow(item))

		const filename = `Lista_de_usuarios_bookstudio_${dateStr.replace(/\s+/g, '_')}.xlsx`

		workbook.xlsx
			.writeBuffer()
			.then((buffer) => {
				const blob = new Blob([buffer], {
					type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet',
				})
				const link = document.createElement('a')
				link.href = URL.createObjectURL(blob)
				link.download = filename
				link.click()

				showToast('Excel generado exitosamente.', 'success')
			})
			.catch((error) => {
				console.error('Error generating Excel file:', error)
				showToast('Ocurrió un error al generar el Excel.', 'error')
			})
			.finally(() => {
				toggleButtonLoading(excelBtn, false)
			})
	} catch (error) {
		console.error('General error while generating Excel file:', error)
		showToast('Ocurrió un error inesperado al generar el Excel.', 'error')
		toggleButtonLoading(excelBtn, false)
	}
}

/*****************************************
 * INITIALIZATION
 *****************************************/

$(document).ready(function () {
	loadTableData({
		apiUrl: './api/users',
		generateRow,
		generatePDF,
		generateExcel,
	})
	handleAddUserForm()
	handleEditUserForm()
	handleDeleteUser()
	loadModalData()
	$('.selectpicker').selectpicker()
	setupBootstrapSelectDropdownStyles()
	placeholderColorSelect()
	togglePasswordVisibility()
})
