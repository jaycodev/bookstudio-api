$(document).ready(function() {
	const currentPage = $('#sidebar').attr('data-current-page');

	$('.nav-link').each(function() {
		const $link = $(this);
		const $liElement = $link.closest('li');
		const $icon = $link.find('i');

		if ($link.attr('href') === currentPage) {
			$liElement.addClass('list-item-with-line');
			$link.addClass('active-effect');

			if ($icon.length) {
				const iconClass = $icon.attr('class').split(' ').pop();
				if (!iconClass.endsWith('-fill')) {
					$icon.removeClass(iconClass).addClass(iconClass + '-fill');
				}
			}
		} else {
			$liElement.removeClass('list-item-with-line');
			$link.removeClass('active-effect');
		}
	});
	
	function initNavTooltips() {
		$('#sidebar .nav-link').each(function() {
			if (bootstrap.Tooltip.getInstance(this)) {
				bootstrap.Tooltip.getInstance(this).dispose();
			}
		});
		$('#sidebar .nav-link').each(function() {
			var tooltipText = $(this).find('.sidebar-link').text().trim();
			if (tooltipText !== "") {
				if (!$(this).attr('title')) {
					$(this).attr('title', tooltipText);
				}
				new bootstrap.Tooltip(this, {
					trigger: 'hover',
					placement: 'right'
				});
			}
		});
	}

	function disposeNavTooltips() {
		$('#sidebar .nav-link').each(function() {
			if (bootstrap.Tooltip.getInstance(this)) {
				bootstrap.Tooltip.getInstance(this).dispose();
			}
		});
	}

	function initButtonTooltip() {
		if (bootstrap.Tooltip.getInstance($('#colapseSidebar')[0])) {
			bootstrap.Tooltip.getInstance($('#colapseSidebar')[0]).dispose();
		}
		$('#colapseSidebar').attr('title', 'Abrir barra lateral');
		new bootstrap.Tooltip($('#colapseSidebar')[0], {
			trigger: 'hover',
			placement: 'right'
		});
	}

	function disposeButtonTooltip() {
		if (bootstrap.Tooltip.getInstance($('#colapseSidebar')[0])) {
			bootstrap.Tooltip.getInstance($('#colapseSidebar')[0]).dispose();
		}
		$('#colapseSidebar').removeAttr('title');
	}

	if (localStorage.getItem('sidebarCollapsed') === 'true') {
		$('#sidebar, main').addClass('collapsed');
		$('#sidebarArrow').removeClass('bi-arrow-bar-left').addClass('bi-arrow-bar-right');
		initNavTooltips();
		initButtonTooltip();
	}

	$('#colapseSidebar').click(function() {
		$('#sidebar, main').toggleClass('collapsed');
		$('#sidebarArrow').toggleClass('bi-arrow-bar-left bi-arrow-bar-right');

		if ($('#sidebar').hasClass('collapsed')) {
			localStorage.setItem('sidebarCollapsed', 'true');
			initNavTooltips();
			initButtonTooltip();
		} else {
			localStorage.setItem('sidebarCollapsed', 'false');
			disposeNavTooltips();
			disposeButtonTooltip();
		}
	});
});