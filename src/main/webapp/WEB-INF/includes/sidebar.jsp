<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.bookstudio.auth.util.LoginConstants" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
    String currentPage = (String) request.getParameter("currentPage");
	String role = (String) session.getAttribute(LoginConstants.ROLE);
	
	out.println("<script>sessionStorage.setItem('userRole', '" + role + "');</script>");
%>

<%
    java.util.Calendar calendar = java.util.Calendar.getInstance(java.util.TimeZone.getTimeZone("America/Lima"));
    int hour = calendar.get(java.util.Calendar.HOUR_OF_DAY);
    String greeting;

    if (hour >= 5 && hour < 12) {
        greeting = "Buenos d�as";
    } else if (hour >= 12 && hour < 18) {
        greeting = "Buenas tardes";
    } else {
        greeting = "Buenas noches";
    }

    request.setAttribute("greetingMessage", greeting);
%>

<!-- Sidebar (Visible on Medium and Large Screens) -->
<aside class="position-fixed d-none d-md-flex flex-column border-end bg-body-secondary" id="sidebar" data-current-page="<%= currentPage %>">
    <!-- Navigation Links -->
    <ul class="nav flex-column p-3 flex-grow-1">
        <li class="mb-1">
            <a href="/" aria-label="Dashboard" class="nav-link text-body-emphasis d-flex align-items-center rounded-2 hovered">
                <i class="bi pe-none me-2 bi-bar-chart"></i>
                <span class="sidebar-link">Dashboard</span>
            </a>
        </li>
        <li class="mb-1">
            <a href="/loans" aria-label="Pr�stamos" class="nav-link text-body-emphasis d-flex align-items-center rounded-2 hovered">
                <i class="bi pe-none me-2 bi-file-earmark-text"></i>
                <span class="sidebar-link">Préstamos</span>
            </a>
        </li>
        <li class="mb-1">
            <a href="/books" aria-label="Libros" class="nav-link text-body-emphasis d-flex align-items-center rounded-2 hovered">
                <i class="bi pe-none me-2 bi-journal-bookmark"></i>
                <span class="sidebar-link">Libros</span>
            </a>
        </li>
        <li class="mb-1">
            <a href="/authors" aria-label="Autores" class="nav-link text-body-emphasis d-flex align-items-center rounded-2 hovered">
                <i class="bi pe-none me-2 bi-person-plus"></i>
                <span class="sidebar-link">Autores</span>
            </a>
        </li>
        <li class="mb-1">
            <a href="/publishers" aria-label="Editoriales" class="nav-link text-body-emphasis d-flex align-items-center rounded-2 hovered">
                <i class="bi pe-none me-2 bi-map"></i>
                <span class="sidebar-link">Editoriales</span>
            </a>
        </li>
        <li class="mb-1">
            <a href="/courses" aria-label="Cursos" class="nav-link text-body-emphasis d-flex align-items-center rounded-2 hovered">
                <i class="bi pe-none me-2 bi-stickies"></i>
                <span class="sidebar-link">Cursos</span>
            </a>
        </li>
        <li class="mb-1">
            <a href="/students" aria-label="Estudiantes" class="nav-link text-body-emphasis d-flex align-items-center rounded-2 hovered">
                <i class="bi pe-none me-2 bi-mortarboard"></i>
                <span class="sidebar-link">Estudiantes</span>
            </a>
        </li>
        <c:if test="${role == 'administrador'}">
	        <li class="mb-1">
	            <a href="/users" aria-label="Usuarios" class="nav-link text-body-emphasis d-flex align-items-center rounded-2 hovered">
	                <i class="bi pe-none me-2 bi-people"></i>
	                <span class="sidebar-link">Usuarios</span>
	            </a>
	        </li>
	    </c:if>
    </ul>

	<!-- Footer Sidebar -->
	<footer class="py-2 d-flex justify-content-center border-top">
		<!-- Collapse Sidebar Button -->
		<button type="button" class="btn btn-custom-secondary border-0 text-body-emphasis d-flex align-items-center text-nowrap px-3" id="toggleSidebar" aria-label="Cerrar barra lateral">
			<i class="bi bi-arrow-bar-left" id="sidebarArrow"></i>
			<span class="ms-2 sidebar-link">Cerrar barra lateral</span>
		</button>
	</footer>
</aside>

<!-- Sidebar (Offcanvas - Visible on Small Screens) -->
<aside class="offcanvas offcanvas-start border-end bg-body-secondary" id="offcanvasSidebar" aria-labelledby="offcanvasSidebarLabel">
    <!-- Offcanvas Header -->
    <header class="offcanvas-header">
        <h4 id="offcanvasSidebarLabel">BookStudio</h4>
        <button type="button" class="btn-close" data-bs-dismiss="offcanvas" aria-label="Cerrar"></button>
    </header>

    <!-- Offcanvas Navigation -->
    <nav class="offcanvas-body d-flex flex-column p-0">
        <ul class="nav flex-column p-3 flex-grow-1">
            <li class="mb-1">
                <a href="/" class="nav-link text-body-emphasis d-flex align-items-center rounded-2 hovered">
                    <i class="bi pe-none me-2 bi-bar-chart"></i>
                    Dashboard
                </a>
            </li>
            <li class="mb-1">
                <a href="/loans" class="nav-link text-body-emphasis d-flex align-items-center rounded-2 hovered">
                    <i class="bi pe-none me-2 bi-file-earmark-text"></i>
                    Préstamos
                </a>
            </li>
            <li class="mb-1">
                <a href="/books" class="nav-link text-body-emphasis d-flex align-items-center rounded-2 hovered">
                    <i class="bi pe-none me-2 bi-journal-bookmark"></i>
                    Libros
                </a>
            </li>
            <li class="mb-1">
                <a href="/authors" class="nav-link text-body-emphasis d-flex align-items-center rounded-2 hovered">
                    <i class="bi pe-none me-2 bi-person-plus"></i>
                    Autores
                </a>
            </li>
            <li class="mb-1">
                <a href="/publishers" class="nav-link text-body-emphasis d-flex align-items-center rounded-2 hovered">
                    <i class="bi pe-none me-2 bi-map"></i>
                    Editoriales
                </a>
            </li>
            <li class="mb-1">
                <a href="/courses" class="nav-link text-body-emphasis d-flex align-items-center rounded-2 hovered">
                    <i class="bi pe-none me-2 bi-stickies"></i>
                    Cursos
                </a>
            </li>
            <li class="mb-1">
                <a href="/students" class="nav-link text-body-emphasis d-flex align-items-center rounded-2 hovered">
                    <i class="bi pe-none me-2 bi-mortarboard"></i>
                    Estudiantes
                </a>
            </li>
            <c:if test="${role == 'administrador'}">
	            <li class="mb-1">
	                <a href="/users" class="nav-link text-body-emphasis d-flex align-items-center rounded-2 hovered">
	                    <i class="bi pe-none me-2 bi-people"></i>
	                    Usuarios
	                </a>
	            </li>
            </c:if>
        </ul>

        <!-- User Information Footer -->
        <footer class="p-4 mt-auto">
            <p class="text-muted mb-0">${greetingMessage}, <span class="text-body-emphasis">${sessionScope.username}</span></p>
        </footer>
    </nav>
</aside>