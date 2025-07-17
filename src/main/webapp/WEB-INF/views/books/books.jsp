<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es" data-bs-theme="auto">
<head>
	<meta charset="UTF-8">
	<meta name="description" content="BookStudio es un sistema de biblioteca para gestionar préstamos, libros, autores y otros recursos, facilitando la administración y seguimiento de los préstamos.">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
	<%@ include file="/WEB-INF/includes/styles.jspf" %>
    <title>BookStudio</title>
    <link href="/images/logo-dark.png" rel="icon" media="(prefers-color-scheme: light)">
    <link href="/images/logo-light.png" rel="icon" media="(prefers-color-scheme: dark)">
</head>
<body>
	<c:set var="userRole" value="${sessionScope.role}" />

	<!-- ===================== Header ===================== -->
	<jsp:include page="/WEB-INF/includes/header.jsp"></jsp:include>

	<!-- ===================== Sidebar ==================== -->
	<jsp:include page="/WEB-INF/includes/sidebar.jsp">
		<jsp:param name="currentPage" value="/books" />
	</jsp:include>

	<!-- ===================== Main Content ==================== -->
	<main class="p-4 bg-body">
		<!-- Card Container -->
		<section id="cardContainer" class="card border">
			<!-- Card Header -->
			<header
				class="card-header d-flex align-items-center position-relative"
				id="buttonGroupHeader">
				<h5 class="card-title text-body-emphasis mb-2 mt-2">
					<i class="bi pe-none me-1 bi-journal-bookmark"></i>
					Tabla de libros
				</h5>

				<!-- Excel Button -->
				<button
					class="btn btn-custom-secondary excel d-flex align-items-center me-2"
					id="generateExcel" aria-label="Generar Excel" disabled>
					<span class="me-2"><i class="bi bi-file-earmark-excel text-success"></i></span>
				    <span class="spinner-border spinner-border-sm me-2 text-success d-none" role="status" aria-hidden="true"></span>
					Excel
				</button>

				<!-- PDF Button -->
				<button
					class="btn btn-custom-secondary d-flex align-items-center me-2"
					id="generatePDF" aria-label="Generar PDF" disabled>
					<span class="me-2"><i class="bi bi-file-earmark-pdf text-danger"></i></span>
				    <span class="spinner-border spinner-border-sm me-2 text-danger d-none" role="status" aria-hidden="true"></span>
					PDF
				</button>

				<c:if test="${userRole == 'administrador'}">
					<!-- Add Button -->
					<button class="btn btn-custom-primary d-flex align-items-center"
						data-bs-toggle="modal" data-bs-target="#addModal"
						aria-label="Agregar libro" disabled>
						<i class="bi-plus-circle me-2"></i>
						Agregar
					</button>
				</c:if>
			</header>

			<!-- Card Body -->
			<div class="card-body">
				<!-- Loading Spinner -->
				<div id="spinnerLoad"
					class="d-flex justify-content-center align-items-center my-5">
					<div class="spinner-border" role="status">
						<span class="visually-hidden">Cargando...</span>
					</div>
				</div>

				<!-- Table Container -->
				<section id="tableContainer" class="d-none small">
					<table id="table" class="table table-sm">
						<thead>
							<tr>
								<th scope="col" class="text-start">Código</th>
								<th scope="col" class="text-start">Título</th>
								<th scope="col" class="text-center">Ej. disp.</th>
								<th scope="col" class="text-center">Ej. prest.</th>
								<th scope="col" class="text-start">Autor</th>
								<th scope="col" class="text-start">Editorial</th>
								<th scope="col" class="text-center">Estado</th>
								<th scope="col" class="text-center"></th>
							</tr>
						</thead>
						<tbody></tbody>
					</table>
				</section>
			</div>
		</section>
	</main>

	<!-- Add Modal -->
	<div class="modal fade" id="addModal" tabindex="-1" aria-labelledby="addModalLabel" aria-hidden="true" data-bs-backdrop="static">
	    <div class="modal-dialog modal-lg">
	        <div class="modal-content">
	            <!-- Modal Header -->
	            <header class="modal-header">
	                <h5 class="modal-title text-body-emphasis" id="addModalLabel">
	                	<i class="bi bi-plus-circle me-1"></i> 
	                	Agregar un libro
	                </h5>
	                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
	            </header>
	            
	            <!-- Modal Body -->
	            <div class="modal-body">
	                <form id="addForm" accept-charset="UTF-8" novalidate>
	                    <!-- Title and Total Copies Section -->
	                    <div class="row">
	                        <!-- Title Field -->
	                        <div class="col-md-6 mb-3">
	                            <label for="addTitle" class="form-label">Título <span class="text-danger">*</span></label>
	                            <input 
	                                type="text" 
	                                class="form-control" 
	                                id="addTitle" 
                                    name="title"
	                                placeholder="Ingrese el título del libro" 
	                                required
	                            >
	                            <div class="invalid-feedback"></div>
	                        </div>
	                        
	                        <!-- Total Copies Field -->
	                        <div class="col-md-6 mb-3">
	                            <label for="addTotalCopies" class="form-label">Ejemplares totales <span class="text-danger">*</span></label>
	                            <input 
	                                type="number" 
	                                class="form-control" 
	                                id="addTotalCopies" 
                                    name="totalCopies"
	                                min="1" 
	                                max="1000" 
	                                placeholder="Ingrese la cantidad de ejemplares" 
	                                required
	                            >
	                            <div class="invalid-feedback"></div>
	                        </div>
	                    </div>
	                    
	                    <!-- Author and Publisher Section -->
	                    <div class="row">
	                        <!-- Author Field -->
	                        <div class="col-md-6 mb-3">
	                            <label for="addAuthor" class="form-label">Autor <span class="text-danger">*</span></label>
	                            <select 
	                                class="selectpicker form-control placeholder-color" 
	                                id="addAuthor" 
                                    name="author"
	                                data-live-search="true" 
	                                data-live-search-placeholder="Buscar..." 
	                                title="Seleccione un autor" 
	                                required
	                            >
	                                <!-- Options will be dynamically populated via JavaScript -->
	                            </select>
	                            <div class="invalid-feedback"></div>
	                        </div>
	                        
	                        <!-- Publisher Field -->
	                        <div class="col-md-6 mb-3">
	                            <label for="addPublisher" class="form-label">Editorial <span class="text-danger">*</span></label>
	                            <select 
	                                class="selectpicker form-control placeholder-color" 
	                                id="addPublisher" 
                                    name="publisher"
	                                data-live-search="true" 
	                                data-live-search-placeholder="Buscar..." 
	                                title="Seleccione una editorial" 
	                                required
	                            >
	                                <!-- Options will be dynamically populated via JavaScript -->
	                            </select>
	                            <div class="invalid-feedback"></div>
	                        </div>
	                    </div>
	                    
	                    <!-- Course and Release Date Section -->
	                    <div class="row">
	                        <!-- Course Field -->
	                        <div class="col-md-6 mb-3">
	                            <label for="addCourse" class="form-label">Curso <span class="text-danger">*</span></label>
	                            <select 
	                                class="selectpicker form-control placeholder-color" 
	                                id="addCourse" 
                                    name="course"
	                                data-live-search="true" 
	                                data-live-search-placeholder="Buscar..." 
	                                title="Seleccione un curso" 
	                                required
	                            >
	                                <!-- Options will be dynamically populated via JavaScript -->
	                            </select>
	                            <div class="invalid-feedback"></div>
	                        </div>
	                        
	                        <!-- Release Date Field -->
	                        <div class="col-md-6 mb-3">
	                            <label for="addReleaseDate" class="form-label">Fecha de lanzamiento <span class="text-danger">*</span></label>
	                            <input 
	                                type="date" 
	                                class="form-control" 
	                                id="addReleaseDate" 
                                    name="releaseDate"
	                                required
	                            >
	                            <div class="invalid-feedback"></div>
	                        </div>   
	                    </div>
	                    
	                    <!-- Genre and Status Section -->
	                    <div class="row">
	                        <!-- Genre Field -->
	                        <div class="col-md-6 mb-3">
	                            <label for="addGenre" class="form-label">Género <span class="text-danger">*</span></label>
	                            <select 
	                                class="selectpicker form-control placeholder-color" 
	                                id="addGenre" 
                                    name="genre"
	                                data-live-search="true" 
	                                data-live-search-placeholder="Buscar..." 
	                                title="Seleccione un género" 
	                                required
	                            >
	                                <!-- Options will be dynamically populated via JavaScript -->
	                            </select>
	                            <div class="invalid-feedback"></div>
	                        </div>
	                        
	                        <!-- Status Field -->
	                        <div class="col-md-6 mb-3">
	                            <label for="addStatus" class="form-label">Estado <span class="text-danger">*</span></label>
	                            <select 
	                                class="selectpicker form-control placeholder-color" 
	                                id="addStatus" 
                                    name="status"
	                                title="Seleccione un estado" 
	                                required
	                            >
	                                <!-- Options will be dynamically populated via JavaScript -->
	                            </select>
	                            <div class="invalid-feedback"></div>
	                        </div>
	                    </div>
	                </form>
	            </div>
	            
	            <!-- Modal Footer -->
	            <footer class="modal-footer">
	            	<!-- Cancel Button -->
	                <button type="button" class="btn btn-custom-secondary" data-bs-dismiss="modal">Cancelar</button>
	                
	                <!-- Add Button -->
	                <button type="submit" class="btn btn-custom-primary d-flex align-items-center" form="addForm" id="addBtn">
	                    <i class="bi-plus-circle me-2"></i>
	                    <span class="spinner-border spinner-border-sm me-2 d-none" role="status" aria-hidden="true"></span>
	                    Agregar
	                </button>
	            </footer>
	        </div>
	    </div>
	</div>
	
	<!-- Details Modal -->
	<div class="modal fade" id="detailsModal" tabindex="-1" aria-labelledby="detailsModalLabel" aria-hidden="true">
	    <div class="modal-dialog modal-lg">
	        <div class="modal-content">
	        	<!-- Modal Header -->
	            <header class="modal-header">
	                <h5 class="modal-title text-body-emphasis" id="detailsModalLabel">
	                	<i class="bi bi-info-circle me-1"></i> 
	                	Detalles del libro 
	                	<span class="badge bg-body-tertiary text-body-emphasis border ms-1" id="detailsModalID"></span>
	                </h5>
	                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
	            </header>
	            
	            <!-- Modal Body -->
	            <div class="modal-body">
	            	<div id="detailsSpinner" class="text-center my-5">
						<div class="spinner-border" role="status">
						  <span class="visually-hidden">Cargando...</span>
						</div>
					</div>
					
	            	<div id="detailsContent" class="d-none">
		            	<!-- ID and Title Section -->
		                <div class="row">
		                    <div class="col-md-6 mb-3">
		                        <h6 class="small text-muted">Código</h6>
		                        <p class="badge bg-body-tertiary text-body-emphasis border" id="detailsID"></p>
		                    </div>
		                    
		                    <div class="col-md-6 mb-3">
		                        <h6 class="small text-muted">Título</h6>
		                        <p class="fw-bold" id="detailsTitle"></p>
		                    </div>
		                </div>
		                
		                <!-- Avaible and Loaned Copies Section -->
		                <div class="row">
		                    <div class="col-md-6 mb-3">
		                        <h6 class="small text-muted">Ejemplares disponibles</h6>
		                        <p class="badge text-success-emphasis bg-success-subtle border border-success-subtle" id="detailsAvaibleCopies"></p>
		                    </div>
		                    
		                    <div class="col-md-6 mb-3">
		                        <h6 class="small text-muted">Ejemplares prestados</h6>
		                        <p class="badge text-warning-emphasis bg-warning-subtle border border-warning-subtle" id="detailsLoanedCopies"></p>
		                    </div>
		                </div>
		                
		                <!-- Author and Publisher Section -->
		                <div class="row">
		                	<div class="col-md-6 mb-3">
		                        <h6 class="small text-muted">Autor</h6>
		                        <p class="fw-bold" id="detailsAuthor"></p>
		                    </div>
		                    
		                    <div class="col-md-6 mb-3">
		                        <h6 class="small text-muted">Editorial</h6>
		                        <p class="fw-bold" id="detailsPublisher"></p>
		                    </div>
		                </div>
		                
		                <!-- Course and Release Date Section -->
		                <div class="row">
		                    <div class="col-md-6 mb-3">
		                        <h6 class="small text-muted">Curso</h6>
		                        <p class="fw-bold" id="detailsCourse"></p>
		                    </div>
		                    
		                    <div class="col-md-6 mb-3">
		                        <h6 class="small text-muted">Fecha de lanzamiento</h6>
		                        <p class="fw-bold" id="detailsReleaseDate"></p>
		                    </div>
		                </div>
		                
		                <!-- Genre and Status Section -->
		                <div class="row">   
		                    <div class="col-md-6">
		                        <h6 class="small text-muted">Género</h6>
		                        <p class="badge bg-body-secondary text-body-emphasis border" id="detailsGenre"></p>
		                    </div>
		                    
		                    <div class="col-md-6">
		                        <h6 class="small text-muted">Estado</h6>
		                        <p id="detailsStatus"></p>
		                    </div>
		                </div>
					</div>
	            </div>
	            
	            <!-- Modal Footer -->
	            <footer class="modal-footer">
	            	<!-- Close Button -->
	                <button type="button" class="btn btn-custom-secondary" data-bs-dismiss="modal">Cerrar</button>
	            </footer>
	        </div>
	    </div>
	</div>
	
	<!-- Edit Modal -->
	<div class="modal fade" id="editModal" tabindex="-1" aria-labelledby="editModalLabel" aria-hidden="true" data-bs-backdrop="static">
	    <div class="modal-dialog modal-lg">
	        <div class="modal-content">
	            <!-- Modal Header -->
	            <header class="modal-header">
	                <h5 class="modal-title text-body-emphasis" id="editModalLabel">
	                	<i class="bi bi-pencil me-1"></i> 
	                	Editar libro 
	                	<span class="badge bg-body-tertiary text-body-emphasis border ms-1" id="editModalID"></span>
	                </h5>
	                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
	            </header>
	            
	            <!-- Modal Body -->
	            <div class="modal-body">
	            	<div id="editSpinner" class="text-center my-5">
						<div class="spinner-border" role="status">
						  <span class="visually-hidden">Cargando...</span>
						</div>
					</div>
	            
	                <form id="editForm" class="d-none" accept-charset="UTF-8" novalidate>
	                    <!-- Title and Total Copies Section -->
	                    <div class="row">
	                        <!-- Title Field -->
	                        <div class="col-md-6 mb-3">
	                            <label for="editTitle" class="form-label">Título <span class="text-danger">*</span></label>
	                            <input 
	                                type="text" 
	                                class="form-control" 
	                                id="editTitle" 
                                    name="title"
	                                placeholder="Ingrese el título del libro" 
	                                required
	                            >
	                            <div class="invalid-feedback"></div>
	                        </div>
	                        
	                        <!-- Total Copies Field -->
	                        <div class="col-md-6 mb-3">
	                            <label for="editTotalCopies" class="form-label">Ejemplares totales <span class="text-danger">*</span></label>
	                            <input 
	                                type="number" 
	                                class="form-control" 
	                                id="editTotalCopies" 
                                    name="totalCopies"
	                                min="1" 
	                                max="1000" 
	                                placeholder="Ingrese la cantidad de ejemplares" 
	                                required
	                            >
	                            <div class="invalid-feedback"></div>
	                        </div>
	                    </div>
	                    
	                    <!-- Author and Publisher Section -->
	                    <div class="row">
	                        <!-- Author Field -->
	                        <div class="col-md-6 mb-3">
	                            <label for="editAuthor" class="form-label">Autor <span class="text-danger">*</span></label>
	                            <select 
	                                class="selectpicker form-control" 
	                                id="editAuthor" 
                                    name="author"
	                                data-live-search="true" 
	                                data-live-search-placeholder="Buscar..." 
	                                required
	                            >
	                                <!-- Options will be dynamically populated via JavaScript -->
	                            </select>
	                            <div class="invalid-feedback"></div>
	                        </div>
	                        
	                        <!-- Publisher Field -->
	                        <div class="col-md-6 mb-3">
	                            <label for="editPublisher" class="form-label">Editorial <span class="text-danger">*</span></label>
	                            <select 
	                                class="selectpicker form-control" 
	                                id="editPublisher" 
                                    name="publisher"
	                                data-live-search="true" 
	                                data-live-search-placeholder="Buscar..." 
	                                required
	                            >
	                                <!-- Options will be dynamically populated via JavaScript -->
	                            </select>
	                            <div class="invalid-feedback"></div>
	                        </div>
	                    </div>
	                    
	                    <!-- Course and Release Date Section -->
	                    <div class="row">
	                        <!-- Course Field -->
	                        <div class="col-md-6 mb-3">
	                            <label for="editCourse" class="form-label">Curso <span class="text-danger">*</span></label>
	                            <select 
	                                class="selectpicker form-control" 
	                                id="editCourse" 
                                    name="course"
	                                data-live-search="true" 
	                                data-live-search-placeholder="Buscar..." 
	                                required
	                            >
	                                <!-- Options will be dynamically populated via JavaScript -->
	                            </select>
	                            <div class="invalid-feedback"></div>
	                        </div>
	                        
	                        <!-- Release Date Field -->
	                        <div class="col-md-6 mb-3">
	                            <label for="editReleaseDate" class="form-label">Fecha de lanzamiento <span class="text-danger">*</span></label>
	                            <input 
	                                type="date" 
	                                class="form-control" 
	                                id="editReleaseDate" 
                                    name="releaseDate"
	                                required
	                            >
	                            <div class="invalid-feedback"></div>
	                        </div> 
	                    </div>
	                    
	                    <!-- Genre and Status Section -->
	                    <div class="row">
	                        <!-- Genre Field -->
	                        <div class="col-md-6 mb-3">
	                            <label for="editGenre" class="form-label">Género <span class="text-danger">*</span></label>
	                            <select 
	                                class="selectpicker form-control" 
	                                id="editGenre" 
                                    name="genre"
	                                data-live-search="true" 
	                                data-live-search-placeholder="Buscar..." 
	                                required
	                            >
	                                <!-- Options will be dynamically populated via JavaScript -->
	                            </select>
	                            <div class="invalid-feedback"></div>
	                        </div>
	                        
	                        <!-- Status Field -->
	                        <div class="col-md-6 mb-3">
	                            <label for="editStatus" class="form-label">Estado <span class="text-danger">*</span></label>
	                            <select 
	                                class="selectpicker form-control" 
	                                id="editStatus" 
                                    name="status"
	                                required
	                            >
	                                <!-- Options will be dynamically populated via JavaScript -->
	                            </select>
	                        </div>
	                    </div>
	                </form>
	            </div>
	            
	            <!-- Modal Footer -->
	            <footer class="modal-footer">
	            	<!-- Cancel Button -->
	                <button type="button" class="btn btn-custom-secondary" data-bs-dismiss="modal">Cancelar</button>
	                
	                <!-- Update Button -->
	                <button type="submit" class="btn btn-custom-primary d-flex align-items-center" form="editForm" id="updateBtn" disabled>
	                    <i class="bi bi-floppy me-2"></i>
	                    <span class="spinner-border spinner-border-sm me-2 d-none" role="status" aria-hidden="true"></span>
	                    Actualizar
	                </button>
	            </footer>
	        </div>
	    </div>
	</div>

	<!-- Toast Container -->
	<div class="toast-container" id="toast-container">
		<!-- Toasts will be added here by JavaScript -->
	</div>

	<jsp:include page="/WEB-INF/includes/scripts.jsp">
		<jsp:param name="currentPage" value="books" />
	</jsp:include>
</body>
</html>