import { initializeTooltips } from '../ui/index.js'

export function addRowToTable(entity, generateRowFn) {
	if (typeof generateRowFn !== 'function') {
		console.error(
			'Expected generateRowFn to be a function that returns the row HTML.',
		)
		return
	}

	const rowHtml = generateRowFn(entity)
	if (typeof rowHtml !== 'string') {
		console.error('generateRowFn must return a string containing the row HTML.')
		return
	}

	const table = $('#table').DataTable()
	const $row = $(rowHtml)

	table.row.add($row).draw(false)
	initializeTooltips($row)
}
