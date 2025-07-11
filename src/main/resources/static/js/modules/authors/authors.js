/**
 * authors.js
 *
 * Handles the initialization and behavior of the authors table,
 * including loading data, configuring modals for creating, viewing,
 * editing, and logically deleting author records.
 *
 * Uses the Fetch API to communicate with RESTful endpoints for all author-related
 * CRUD operations. Manages UI components such as placeholders, enhanced dropdowns,
 * validation feedback, loading states, image cropping, and tooltips.
 *
 * Also includes features for generating PDF reports and exporting table data to Excel.
 *
 * @author Jason
 */

import {
	showToast,
	toggleButtonLoading,
	toggleModalLoading,
	populateSelect,
	placeholderColorSelect,
	placeholderColorEditSelect,
	placeholderColorDateInput,
	initializeCropper,
	setupBootstrapSelectDropdownStyles,
	initializeTooltips,
	getCurrentPeruDate,
} from '../../shared/utils/ui/index.js'

import {
	toggleTableLoadingState,
	setupDataTable,
} from '../../shared/utils/tables/index.js'

import {
	isValidText,
	isValidBirthDate,
	isValidImageFile,
	validateImageFileUI,
} from '../../shared/utils/validators/index.js'

/*****************************************
 * GLOBAL VARIABLES AND HELPER FUNCTIONS
 *****************************************/

// Global list of nationalities and literary genres for the selectpickers
let nationalityList = []
let literaryGenreList = []

// Global variable to handle photo deletion in edit modal
let deletePhotoFlag = false

async function populateSelectOptions() {
	try {
		const response = await fetch('./api/authors/select-options', {
			method: 'GET',
			headers: {
				Accept: 'application/json',
			},
		})

		if (response.status === 204) {
			console.warn('No data found for select options.')
			return
		}

		if (!response.ok) {
			throw response
		}

		const data = await response.json()

		nationalityList = data.nationalities
		literaryGenreList = data.literaryGenres
	} catch (error) {
		if (error instanceof Response) {
			try {
				const errData = await error.json()
				console.error(
					`Error fetching select options (${errData.errorType} - ${error.status}):`,
					errData.message,
				)
			} catch {
				console.error('Unexpected error:', error.status, await error.text())
			}
		} else {
			console.error('Unexpected error:', error)
		}
	}
}

/*****************************************
 * TABLE HANDLING
 *****************************************/

function generateRow(author) {
	const userRole = sessionStorage.getItem('userRole')

	return `
		<tr>
			<td class="align-middle text-start">
				<span class="badge bg-body-tertiary text-body-emphasis border">${author.formattedAuthorId}</span>
			</td>
			<td class="align-middle text-start">${author.name}</td>
			<td class="align-middle text-start">
				<span class="badge bg-body-secondary text-body-emphasis border">${author.nationalityName}</span>
			</td>
			<td class="align-middle text-start">
				<span class="badge bg-body-secondary text-body-emphasis border">${author.literaryGenreName}</span>
			</td>
			<td class="align-middle text-center">${moment(author.birthDate).format('DD MMM YYYY')}</td>
			<td class="align-middle text-center">
				${
					author.status === 'activo'
						? '<span class="badge text-success-emphasis bg-success-subtle border border-success-subtle">Activo</span>'
						: '<span class="badge text-danger-emphasis bg-danger-subtle border border-danger-subtle">Inactivo</span>'
				}
			</td>
			<td class="align-middle text-center">
				${
					author.photoUrl
						? `<img src="${author.photoUrl}" alt="Foto del Autor" class="img-fluid rounded-circle" style="width: 23px; height: 23px;">`
						: `<svg xmlns="http://www.w3.org/2000/svg" width="23" height="23" fill="currentColor" class="bi-person-circle" viewBox="0 0 16 16">
						<path d="M11 6a3 3 0 1 1-6 0 3 3 0 0 1 6 0"></path>
						<path fill-rule="evenodd" d="M0 8a8 8 0 1 1 16 0A8 8 0 0 1 0 8m8-7a7 7 0 0 0-5.468 11.37C3.242 11.226 4.805 10 8 10s4.757 1.225 5.468 2.37A7 7 0 0 0 8 1"></path>
					</svg>`
				}
			</td>
			<td class="align-middle text-center">
				<div class="d-inline-flex gap-2">
					<button class="btn btn-sm btn-icon-hover" data-tooltip="tooltip" data-bs-placement="top" title="Detalles"
						data-bs-toggle="modal" data-bs-target="#detailsAuthorModal" data-id="${author.authorId}" data-formatted-id="${author.formattedAuthorId}">
						<i class="bi bi-info-circle"></i>
					</button>
					${
						userRole === 'administrador'
							? `<button class="btn btn-sm btn-icon-hover" data-tooltip="tooltip" data-bs-placement="top" title="Editar"
							data-bs-toggle="modal" data-bs-target="#editAuthorModal" data-id="${author.authorId}" data-formatted-id="${author.formattedAuthorId}">
							<i class="bi bi-pencil"></i>
						</button>`
							: ''
					}
				</div>
			</td>
		</tr>
	`
}

function addRowToTable(author) {
	const table = $('#authorTable').DataTable()
	const rowHtml = generateRow(author)
	const $row = $(rowHtml)

	table.row.add($row).draw(false)

	initializeTooltips($row)
}

async function loadAuthors() {
	toggleTableLoadingState('loading')

	const safetyTimer = setTimeout(() => {
		toggleTableLoadingState('loaded')
		$('#tableContainer').removeClass('d-none')
	}, 8000)

	try {
		const response = await fetch('./api/authors', {
			method: 'GET',
			headers: {
				Accept: 'application/json',
			},
		})

		clearTimeout(safetyTimer)

		const tableBody = $('#bodyAuthors')
		tableBody.empty()

		if (response.status === 200) {
			const data = await response.json()

			if (data.length > 0) {
				data.forEach((author) => {
					const row = generateRow(author)
					tableBody.append(row)
				})
				initializeTooltips(tableBody)
			}

			$('#generatePDF, #generateExcel').prop('disabled', data.length === 0)
		} else if (response.status === 204) {
			$('#generatePDF, #generateExcel').prop('disabled', true)
		} else {
			let errorResponse
			try {
				errorResponse = await response.json()
				console.error(
					`Error listing author data (${errorResponse.errorType} - ${response.status}):`,
					errorResponse.message,
				)
				showToast('Hubo un error al listar los datos de los autores.', 'error')
			} catch {
				console.error(
					'Unexpected error:',
					response.status,
					await response.text(),
				)
				showToast('Hubo un error inesperado.', 'error')
			}
		}

		if ($.fn.DataTable.isDataTable('#authorTable')) {
			$('#authorTable').DataTable().destroy()
		}

		const dataTable = setupDataTable('#authorTable')

		dataTable.on('draw', function () {
			const filteredCount = dataTable.rows({ search: 'applied' }).count()
			const noDataMessage =
				$('#authorTable').find('td.dataTables_empty').length > 0
			$('#generatePDF, #generateExcel').prop(
				'disabled',
				filteredCount === 0 || noDataMessage,
			)
		})

		$('#generatePDF')
			.off('click')
			.on('click', () => generatePDF(dataTable))

		$('#generateExcel')
			.off('click')
			.on('click', () => generateExcel(dataTable))
	} catch (err) {
		clearTimeout(safetyTimer)

		console.error('Unexpected error:', err)
		showToast('Hubo un error inesperado.', 'error')

		const tableBody = $('#bodyAuthors')
		tableBody.empty()

		if ($.fn.DataTable.isDataTable('#authorTable')) {
			$('#authorTable').DataTable().destroy()
		}

		setupDataTable('#authorTable')
	}
}

function updateRowInTable(author) {
	const table = $('#authorTable').DataTable()

	const row = table
		.rows()
		.nodes()
		.to$()
		.filter(function () {
			return (
				$(this).find('td').eq(0).text().trim() ===
				author.formattedAuthorId.toString()
			)
		})

	if (row.length > 0) {
		row.find('td').eq(1).text(author.name)
		row.find('td').eq(2).find('span').text(author.nationalityName)
		row.find('td').eq(3).find('span').text(author.literaryGenreName)
		row.find('td').eq(4).text(moment(author.birthDate).format('DD MMM YYYY'))
		row
			.find('td')
			.eq(5)
			.html(
				author.status === 'activo'
					? '<span class="badge text-success-emphasis bg-success-subtle border border-success-subtle">Activo</span>'
					: '<span class="badge text-danger-emphasis bg-danger-subtle border border-danger-subtle">Inactivo</span>',
			)

		if (author.photoUrl && author.photoUrl.trim() !== '') {
			row
				.find('td')
				.eq(6)
				.html(
					`<img src="${author.photoUrl}" alt="Foto del Autor" class="img-fluid rounded-circle" style="width: 23px; height: 23px;">`,
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

function handleAddAuthorForm() {
	let isFirstSubmit = true

	$('#addAuthorModal').on('hidden.bs.modal', function () {
		isFirstSubmit = true
		$('#addAuthorForm').data('submitted', false)
	})

	$('#addAuthorForm').on('input change', 'input, select', function () {
		if (!isFirstSubmit) {
			validateAddField($(this))
		}
	})

	$('#addAuthorForm').on('submit', async function (event) {
		event.preventDefault()

		if ($(this).data('submitted') === true) return
		$(this).data('submitted', true)

		if (isFirstSubmit) isFirstSubmit = false

		const form = $(this)[0]
		let isValid = true

		$(form)
			.find('input, select')
			.not('.bootstrap-select input[type="search"]')
			.each(function () {
				if (!validateAddField($(this))) isValid = false
			})

		if (!isValid) {
			$(this).data('submitted', false)
			return
		}

		const formData = new FormData(form)
		const raw = Object.fromEntries(formData.entries())

		const authorDto = {
			name: raw.addAuthorName,
			nationalityId: parseInt(raw.addAuthorNationality),
			literaryGenreId: parseInt(raw.addLiteraryGenre),
			birthDate: raw.addAuthorBirthDate,
			biography: raw.addAuthorBiography || '',
			status: raw.addAuthorStatus,
			photoUrl: null, // 🔜 Preparado para Cloudinary
		}

		const submitButton = $('#addAuthorBtn')
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

			const response = await fetch('./api/authors', {
				method: 'POST',
				headers: {
					'Content-Type': 'application/json',
					Accept: 'application/json',
				},
				body: JSON.stringify(authorDto),
			})

			const json = await response.json()

			if (response.ok && json.success) {
				addRowToTable(json.data)
				$('#addAuthorModal').modal('hide')
				showToast('Autor agregado exitosamente.', 'success')
			} else {
				console.error(
					`Backend error (${json.errorType} - ${json.statusCode}):`,
					json.message,
				)
				$('#addAuthorModal').modal('hide')
				showToast('Hubo un error al agregar el autor.', 'error')
			}
		} catch (err) {
			console.error('Unexpected error:', err)
			showToast('Hubo un error inesperado.', 'error')
			$('#addAuthorModal').modal('hide')
		} finally {
			toggleButtonLoading(submitButton, false)
		}
	})
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

	// Name validation
	if (field.is('#addAuthorName')) {
		const result = isValidText(field.val(), 'nombre')
		if (!result.valid) {
			isValid = false
			errorMessage = result.message
		}
	}

	// Birth date validation
	if (field.is('#addAuthorBirthDate')) {
		const result = isValidBirthDate(field.val())
		if (!result.valid) {
			isValid = false
			errorMessage = result.message
		}
	}

	// Photo validation
	if (field.is('#addAuthorPhoto')) {
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

$('#addAuthorPhoto, #editAuthorPhoto').on('change', function () {
	validateImageFileUI($(this))
})

function handleEditAuthorForm() {
	let isFirstSubmit = true

	$('#editAuthorModal').on('hidden.bs.modal', function () {
		isFirstSubmit = true
		$('#editAuthorForm').data('submitted', false)
	})

	$('#editAuthorForm').on('input change', 'input, select', function () {
		if (!isFirstSubmit) {
			validateEditField($(this))
		}
	})

	$('#editAuthorForm').on('submit', async function (event) {
		event.preventDefault()

		if ($(this).data('submitted') === true) return
		$(this).data('submitted', true)

		if (isFirstSubmit) isFirstSubmit = false

		const form = $(this)[0]
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

		const authorId = $('#editAuthorForm').data('authorId')
		const formData = new FormData(form)
		const raw = Object.fromEntries(formData.entries())

		const author = {
			authorId: parseInt(authorId),
			name: raw.editAuthorName,
			nationalityId: parseInt(raw.editAuthorNationality),
			literaryGenreId: parseInt(raw.editLiteraryGenre),
			birthDate: raw.editAuthorBirthDate,
			biography: raw.editAuthorBiography || '',
			status: raw.editAuthorStatus,
			deletePhoto: deletePhotoFlag || false,
			photoUrl: null, // 🔜 Preparado para Cloudinary
		}

		const submitButton = $('#editAuthorBtn')
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

			const response = await fetch('./api/authors', {
				method: 'PUT',
				headers: {
					'Content-Type': 'application/json',
					Accept: 'application/json',
				},
				body: JSON.stringify(author),
			})

			const json = await response.json()

			if (response.ok && json.success) {
				updateRowInTable(json.data)
				$('#editAuthorModal').modal('hide')
				showToast('Autor actualizado exitosamente.', 'success')
			} else {
				console.error(
					`Backend error (${json.errorType} - ${json.statusCode}):`,
					json.message,
				)
				showToast(
					json.message || 'Hubo un error al actualizar el autor.',
					'error',
				)
				$('#editAuthorModal').modal('hide')
			}
		} catch (err) {
			console.error('Unexpected error:', err)
			showToast('Hubo un error inesperado.', 'error')
			$('#editAuthorModal').modal('hide')
		} finally {
			toggleButtonLoading(submitButton, false)
		}
	})
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

	// Name validation
	if (field.is('#editAuthorName')) {
		const result = isValidText(field.val(), 'nombre')
		if (!result.valid) {
			isValid = false
			errorMessage = result.message
		}
	}

	// Birth date validation
	if (field.is('#editAuthorBirthDate')) {
		const result = isValidBirthDate(field.val())
		if (!result.valid) {
			isValid = false
			errorMessage = result.message
		}
	}

	// Photo validation
	if (field.is('#editAuthorPhoto')) {
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

/*****************************************
 * MODAL MANAGEMENT
 *****************************************/

function loadModalData() {
	// Add Modal
	$(document).on('click', '[data-bs-target="#addAuthorModal"]', function () {
		populateSelect(
			'#addAuthorNationality',
			nationalityList,
			'nationalityId',
			'name',
		)
		$('#addAuthorNationality').selectpicker()

		populateSelect(
			'#addLiteraryGenre',
			literaryGenreList,
			'literaryGenreId',
			'name',
		)
		$('#addLiteraryGenre').selectpicker()

		$('#addAuthorStatus')
			.selectpicker('destroy')
			.empty()
			.append(
				$('<option>', {
					value: 'activo',
					text: 'Activo',
				}),
				$('<option>', {
					value: 'inactivo',
					text: 'Inactivo',
				}),
			)
		$('#addAuthorStatus').selectpicker()

		$('#defaultAddPhotoContainer').removeClass('d-none')
		$('#deleteAddPhotoBtn').addClass('d-none')

		$('#addAuthorForm')[0].reset()
		$('#addAuthorForm .is-invalid').removeClass('is-invalid')

		const today = getCurrentPeruDate()
		const maxDate = new Date(
			today.getFullYear() - 10,
			today.getMonth(),
			today.getDate(),
		)
		const maxDateStr = maxDate.toISOString().split('T')[0]
		$('#addAuthorBirthDate').attr('max', maxDateStr)

		placeholderColorDateInput()

		$('#cropperContainerAdd').addClass('d-none')
		if (cropper) {
			cropper.destroy()
			cropper = null
		}
	})

	// Details Modal
	$(document).on(
		'click',
		'[data-bs-target="#detailsAuthorModal"]',
		function () {
			const authorId = $(this).data('id')
			$('#detailsAuthorModalID').text($(this).data('formatted-id'))

			toggleModalLoading(this, true)

			fetch(`./api/authors/${encodeURIComponent(authorId)}`, {
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
					$('#detailsAuthorID').text(data.formattedAuthorId)
					$('#detailsAuthorName').text(data.name)
					$('#detailsAuthorNationality').text(data.nationalityName)
					$('#detailsAuthorGenre').text(data.literaryGenreName)
					$('#detailsAuthorBirthDate').text(
						moment(data.birthDate).format('DD MMM YYYY'),
					)
					$('#detailsAuthorBiography').text(data.biography || '')

					$('#detailsAuthorStatus').html(
						data.status === 'activo'
							? '<span class="badge text-success-emphasis bg-success-subtle border border-success-subtle">Activo</span>'
							: '<span class="badge text-danger-emphasis bg-danger-subtle border border-danger-subtle">Inactivo</span>',
					)

					if (data.photoUrl) {
						$('#detailsAuthorImg')
							.attr('src', data.photoUrl)
							.removeClass('d-none')
						$('#detailsAuthorSvg').addClass('d-none')
					} else {
						$('#detailsAuthorImg').addClass('d-none')
						$('#detailsAuthorSvg').removeClass('d-none')
					}

					toggleModalLoading(this, false)
				})
				.catch((error) => {
					console.error(
						`Error loading author details (${error.errorType || 'unknown'} - ${error.status}):`,
						error.message || error,
					)
					showToast('Hubo un error al cargar los detalles del autor.', 'error')
					$('#detailsAuthorModal').modal('hide')
				})
		},
	)

	// Edit Modal
	$(document).on('click', '[data-bs-target="#editAuthorModal"]', function () {
		const authorId = $(this).data('id')
		$('#editAuthorModalID').text($(this).data('formatted-id'))

		toggleModalLoading(this, true)

		fetch(`./api/authors/${encodeURIComponent(authorId)}`, {
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
				$('#editAuthorForm').data('authorId', data.authorId)
				$('#editAuthorName').val(data.name)

				populateSelect(
					'#editAuthorNationality',
					nationalityList,
					'nationalityId',
					'name',
				)
				$('#editAuthorNationality').val(data.nationalityId).selectpicker()

				populateSelect(
					'#editLiteraryGenre',
					literaryGenreList,
					'literaryGenreId',
					'name',
				)
				$('#editLiteraryGenre').val(data.literaryGenreId).selectpicker()

				$('#editAuthorBirthDate').val(
					moment(data.birthDate).format('YYYY-MM-DD'),
				)
				const today = getCurrentPeruDate()
				const maxDate = new Date(
					today.getFullYear() - 10,
					today.getMonth(),
					today.getDate(),
				)
				const maxDateStr = maxDate.toISOString().split('T')[0]
				$('#editAuthorBirthDate').attr('max', maxDateStr)

				$('#editAuthorBiography').val(data.biography)

				$('#editAuthorStatus')
					.selectpicker('destroy')
					.empty()
					.append(
						$('<option>', { value: 'activo', text: 'Activo' }),
						$('<option>', { value: 'inactivo', text: 'Inactivo' }),
					)
				$('#editAuthorStatus').val(data.status).selectpicker()

				updateEditImageContainer(data.photoUrl)

				$('#editAuthorForm .is-invalid').removeClass('is-invalid')
				placeholderColorEditSelect()
				placeholderColorDateInput()

				$('#editAuthorForm')
					.find('select')
					.each(function () {
						validateEditField($(this), true)
					})

				$('#editAuthorPhoto').val('')

				toggleModalLoading(this, false)
			})
			.catch((error) => {
				console.error(
					`Error loading author details for editing (${error.errorType || 'unknown'} - ${error.status}):`,
					error.message || error,
				)
				showToast('Hubo un error al cargar los datos del autor.', 'error')
				$('#editAuthorModal').modal('hide')
			})

		$('#cropperContainerEdit').addClass('d-none')
		if (cropper) {
			cropper.destroy()
			cropper = null
		}
	})
}

function updateEditImageContainer(photoUrl) {
	const $editImageContainer = $('#currentEditPhotoContainer')
	const $deleteEditPhotoBtn = $('#deleteEditPhotoBtn')

	$editImageContainer.empty()

	if (photoUrl) {
		$editImageContainer.html(
			`<img src="${photoUrl}" class="img-fluid rounded-circle" alt="Foto del Autor">`,
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
	$('#addAuthorPhoto').val('')
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
	$('#editAuthorPhoto').val('')
})

let cropper
const $cropperContainerAdd = $('#cropperContainerAdd')
const $imageToCropAdd = $('#imageToCropAdd')
const $cropperContainerEdit = $('#cropperContainerEdit')
const $imageToCropEdit = $('#imageToCropEdit')

$('#addAuthorPhoto, #editAuthorPhoto').on('change', function () {
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
		if ($(this).is('#addAuthorPhoto')) {
			$container = $cropperContainerAdd
			$image = $imageToCropAdd
		} else {
			$container = $cropperContainerEdit
			$image = $imageToCropEdit
		}
		initializeCropper(file, $container, $image, cropper)
	} else {
		if ($(this).is('#addAuthorPhoto')) {
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
		const doc = new jsPDF('p', 'mm', 'a4')
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
		doc.text('Lista de autores', pageWidth / 2, topMargin + 13, {
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
				let estado = row.cells[5].innerText.trim()
				estado = estado.includes('Activo') ? 'Activo' : 'Inactivo'

				return [
					row.cells[0].innerText.trim(),
					row.cells[1].innerText.trim(),
					row.cells[2].innerText.trim(),
					row.cells[3].innerText.trim(),
					row.cells[4].innerText.trim(),
					estado,
				]
			})

		doc.autoTable({
			startY: topMargin + 25,
			margin: { left: margin, right: margin },
			head: [
				[
					'Código',
					'Nombre',
					'Nacionalidad',
					'Género literario',
					'Fecha nacimiento',
					'Estado',
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
				if (data.section === 'body' && data.column.index === 5) {
					data.cell.styles.textColor =
						data.cell.raw === 'Activo' ? [0, 128, 0] : [255, 0, 0]
				}
			},
		})

		const filename = `Lista_de_autores_bookstudio_${fecha.replace(/\s+/g, '_')}.pdf`

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
		const worksheet = workbook.addWorksheet('Autores')

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
		titleCell.value = 'Lista de autores - BookStudio'
		titleCell.font = {
			name: 'Arial',
			size: 16,
			bold: true,
		}
		titleCell.alignment = { horizontal: 'center' }

		worksheet.mergeCells('A2:F2')
		const dateTimeCell = worksheet.getCell('A2')
		dateTimeCell.value = `Fecha: ${dateStr}  Hora: ${timeStr}`
		dateTimeCell.alignment = { horizontal: 'center' }

		worksheet.columns = [
			{ key: 'id', width: 10 },
			{ key: 'nombre', width: 30 },
			{ key: 'nacionalidad', width: 30 },
			{ key: 'genero', width: 30 },
			{ key: 'nacimiento', width: 25 },
			{ key: 'estado', width: 15 },
		]

		const headerRow = worksheet.getRow(4)
		headerRow.values = [
			'Código',
			'Nombre',
			'Nacionalidad',
			'Género literario',
			'Fecha nacimiento',
			'Estado',
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
				let estado = row.cells[5].innerText.trim()
				estado = estado.includes('Activo') ? 'Activo' : 'Inactivo'

				return {
					id: row.cells[0].innerText.trim(),
					nombre: row.cells[1].innerText.trim(),
					nacionalidad: row.cells[2].innerText.trim(),
					genero: row.cells[3].innerText.trim(),
					nacimiento: row.cells[4].innerText.trim(),
					estado: estado,
				}
			})

		data.forEach((item) => {
			const row = worksheet.addRow(item)
			const estadoCell = row.getCell(6)
			if (estadoCell.value === 'Activo') {
				estadoCell.font = { color: { argb: '008000' } }
				estadoCell.fill = {
					type: 'pattern',
					pattern: 'solid',
					fgColor: { argb: 'E6F2E6' },
				}
			} else {
				estadoCell.font = { color: { argb: 'FF0000' } }
				estadoCell.fill = {
					type: 'pattern',
					pattern: 'solid',
					fgColor: { argb: 'FFE6E6' },
				}
			}
		})

		const filename = `Lista_de_autores_bookstudio_${dateStr.replace(/\s+/g, '_')}.xlsx`

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
	loadAuthors()
	handleAddAuthorForm()
	handleEditAuthorForm()
	loadModalData()
	populateSelectOptions()
	$('.selectpicker').selectpicker()
	setupBootstrapSelectDropdownStyles()
	placeholderColorSelect()
})
