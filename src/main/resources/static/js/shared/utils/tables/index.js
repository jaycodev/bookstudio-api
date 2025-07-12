import { showToast, initializeTooltips } from '../ui/index.js'

function toggleTableLoadingState(action) {
	const buttons = $('#buttonGroupHeader button')
	const spinner = $('#spinnerLoad')

	if (action === 'loading') {
		spinner.removeClass('d-none')
		buttons.prop('disabled', true)
	} else if (action === 'loaded') {
		spinner.addClass('d-none')
		buttons.prop('disabled', false)
	}
}

function setupDataTable(tableId) {
	const table = $(tableId).DataTable({
		responsive: true,
		searching: true,
		lengthChange: true,
		paging: true,
		ordering: true,
		pageLength: 15,
		lengthMenu: [
			[10, 15, 25, -1],
			[10, 15, 25, 'Todo'],
		],
		columnDefs: [{ orderable: false, targets: -1 }],
		language: {
			search: '',
			searchPlaceholder: 'Buscar...',
			lengthMenu: 'Mostrar _MENU_ ',
			info: 'Mostrando _START_ a _END_ de _TOTAL_ registros',
			infoEmpty: 'Mostrando 0 a 0 de 0 registros',
			infoFiltered: '(filtrado de _MAX_ registros totales)',
			zeroRecords: 'No se encontraron registros coincidentes',
			emptyTable: 'No hay datos disponibles en la tabla',
		},
		initComplete: function () {
			toggleTableLoadingState('loaded')
			$('#tableContainer').removeClass('d-none')

			const $dtSearch = $('.dt-search')
			$dtSearch.find('label').remove()

			$dtSearch.addClass('input-group input-group-sm')
			$dtSearch.prepend(
				'<span class="input-group-text bg-body-secondary"><i class="bi bi-search text-muted"></i></span>',
			)
		},
	})

	setTimeout(function () {
		if ($('#spinnerLoad').is(':visible')) {
			toggleTableLoadingState('loaded')
			$('#tableContainer').removeClass('d-none')
		}
	}, 3000)

	return table
}

export async function loadTableData({
	apiUrl,
	generateRow,
	generatePDF,
	generateExcel,
}) {
	toggleTableLoadingState('loading')

	const safetyTimer = setTimeout(() => {
		toggleTableLoadingState('loaded')
		$('#tableContainer').removeClass('d-none')
	}, 8000)

	try {
		const response = await fetch(apiUrl, {
			method: 'GET',
			headers: { Accept: 'application/json' },
		})

		clearTimeout(safetyTimer)

		const tableBody = $('#table tbody')
		tableBody.empty()

		if (response.status === 200) {
			const data = await response.json()

			if (data.length > 0) {
				data.forEach((item) => {
					const row = generateRow(item)
					tableBody.append(row)
				})
				initializeTooltips(tableBody)
			}

			$('#generatePDF, #generateExcel').prop('disabled', data.length === 0)
		} else if (response.status === 204) {
			$('#generatePDF, #generateExcel').prop('disabled', true)
		} else {
			try {
				const errorResponse = await response.json()
				console.error(
					`Error listing data (${errorResponse.errorType} - ${response.status}):`,
					errorResponse.message,
				)
			} catch {
				console.error('Unexpected error:', response.status)
			}
			showToast('Hubo un error al listar los datos.', 'error')
		}

		if ($.fn.DataTable.isDataTable('#table')) {
			$('#table').DataTable().destroy()
		}

		const dataTable = setupDataTable('#table')

		dataTable.on('draw', function () {
			const filteredCount = dataTable.rows({ search: 'applied' }).count()
			const noDataMessage = $('#table').find('td.dataTables_empty').length > 0
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
		showToast('Hubo un error al listar los datos.', 'error')

		const tableBody = $('#table tbody')
		tableBody.empty()

		if ($.fn.DataTable.isDataTable('#table')) {
			$('#table').DataTable().destroy()
		}

		setupDataTable('#table')
	}
}
