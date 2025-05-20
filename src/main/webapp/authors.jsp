<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es" data-bs-theme="auto">
<head>
	<meta charset="UTF-8">
	<meta name="description" content="BookStudio es un sistema de librería para gestionar préstamos, libros, autores y otros recursos, facilitando la administración y seguimiento de los préstamos.">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <jsp:include page="WEB-INF/includes/styles.jsp"></jsp:include>
    <title>BookStudio</title>
    <link href="images/logo-dark.png" rel="icon" media="(prefers-color-scheme: light)">
    <link href="images/logo-light.png" rel="icon" media="(prefers-color-scheme: dark)">
</head>
<body>
	<!-- Set user role from session -->
	<c:set var="userRole" value="${sessionScope.role}" />

	<!-- ===================== Header ===================== -->
	<jsp:include page="WEB-INF/includes/header.jsp"></jsp:include>

	<!-- ===================== Sidebar ==================== -->
	<jsp:include page="WEB-INF/includes/sidebar.jsp">
		<jsp:param name="currentPage" value="authors.jsp" />
	</jsp:include>

	<!-- ===================== Main Content ==================== -->
	<main class="p-4 bg-body">
		<!-- Card Container -->
		<section id="cardContainer" class="card border">
			<!-- Card Header -->
			<header
				class="card-header d-flex align-items-center position-relative"
				id="buttonGroupHeader">
				<h5 class="card-title text-body-emphasis mb-2 mt-2">Tabla de autores</h5>

				<!-- Excel Button -->
				<button
					class="btn btn-custom-secondary excel d-flex align-items-center me-2"
					id="generateExcel" aria-label="Generar Excel" disabled>
					<i class="bi bi-file-earmark-excel text-success me-2"></i>
					Excel
				</button>

				<!-- PDF Button -->
				<button
					class="btn btn-custom-secondary d-flex align-items-center me-2"
					id="generatePDF" aria-label="Generar PDF" disabled>
					<i class="bi bi-file-earmark-pdf text-danger me-2"></i>
					PDF
				</button>

				<c:if test="${userRole == 'administrador'}">
					<!-- Add Button -->
					<button class="btn btn-custom-primary d-flex align-items-center"
						data-bs-toggle="modal" data-bs-target="#addAuthorModal"
						aria-label="Agregar autor" disabled>
						<i class="bi bi-plus-lg me-2"></i>
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
					<table id="authorTable" class="table table-sm">
						<thead>
							<tr>
								<th scope="col" class="text-start">ID</th>
								<th scope="col" class="text-start">Nombre</th>
								<th scope="col" class="text-start">Nacionalidad</th>
								<th scope="col" class="text-start">Género literario</th>
								<th scope="col" class="text-center">Estado</th>
								<th scope="col" class="text-center">Foto</th>
								<th scope="col" class="text-center"></th>
							</tr>
						</thead>
						<tbody id="bodyAuthors">
							<!-- Data will be populated here via JavaScript -->
						</tbody>
					</table>
				</section>
			</div>
		</section>
	</main>

	<!-- Add Author Modal -->
    <div class="modal fade" id="addAuthorModal" tabindex="-1" aria-labelledby="addAuthorModalLabel" aria-hidden="true" data-bs-backdrop="static">
        <div class="modal-dialog modal-lg">
            <div class="modal-content">
            	<!-- Modal Header -->
                <header class="modal-header">
                    <h5 class="modal-title text-body-emphasis" id="addAuthorModalLabel">Agregar un autor</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </header>
                
                <!-- Modal Body -->
                <div class="modal-body">
                    <form id="addAuthorForm" enctype="multipart/form-data" novalidate>
                    	<!-- Personal Information Section -->
                        <div class="row">
                        	<!-- Author Name Field -->
                            <div class="col-md-6 mb-3">
	                            <label for="addAuthorName" class="form-label">Nombre <span class="text-danger">*</span></label>
	                            <input 
	                                type="text" 
	                                class="form-control" 
	                                id="addAuthorName" 
	                                name="addAuthorName" 
	                                pattern="[A-Za-zÀ-ÿ\s.]+" 
	                                oninput="this.value = this.value.replace(/[^A-Za-zÀ-ÿ\s.]/g, '');" 
	                                placeholder="Ingrese el nombre del autor" 
	                                required
	                            >
	                            <div class="invalid-feedback"></div>
	                        </div>
                            
                            <!-- Nationality Field -->
                            <div class="col-md-6 mb-3">
	                            <label for="addAuthorNationality" class="form-label">Nacionalidad <span class="text-danger">*</span></label>
	                            <select 
	                                class="selectpicker form-control placeholder-color" 
	                                id="addAuthorNationality" 
	                                name="addAuthorNationality" 
	                                data-live-search="true" 
	                                data-live-search-placeholder="Buscar..." 
	                                title="Seleccione una nacionalidad" 
	                                required
	                            >
	                            	<!-- Options will be dynamically populated via JavaScript -->
	                            </select>
	                            <div class="invalid-feedback"></div>
	                        </div>
                        </div>
                        
                        <!-- Author Details Section -->
	                    <div class="row">
	                    	<!-- Literary Genre Field -->
	                        <div class="col-md-6 mb-3">
	                            <label for="addLiteraryGenre" class="form-label">Género literario <span class="text-danger">*</span></label>
	                            <select 
	                                class="selectpicker form-control placeholder-color" 
	                                id="addLiteraryGenre" 
	                                name="addLiteraryGenre" 
	                                data-live-search="true" 
	                                data-live-search-placeholder="Buscar..." 
	                                title="Seleccione un género literario" 
	                                required
	                            >
	                                <!-- Options will be dynamically populated via JavaScript -->
	                            </select>
	                            <div class="invalid-feedback"></div>
	                        </div>
	                        
	                        <!-- Birth Date Field -->
	                        <div class="col-md-6 mb-3">
	                            <label for="addAuthorBirthDate" class="form-label">Fecha de nacimiento <span class="text-danger">*</span></label>
	                            <input 
	                                type="date" 
	                                class="form-control" 
	                                id="addAuthorBirthDate" 
	                                name="addAuthorBirthDate" 
	                                required
	                            >
	                            <div class="invalid-feedback"></div>
	                        </div>
	                    </div>
	                    
	                    <!-- Additional Information Section -->
	                    <div class="row">
	                        <!-- Biography Field -->
	                        <div class="col-md-6 mb-3">
	                            <label for="addAuthorBiography" class="form-label">Biografía</label>
	                            <textarea 
	                                class="form-control" 
	                                id="addAuthorBiography" 
	                                name="addAuthorBiography" 
	                                rows="1" 
	                                placeholder="Escribe una breve biografía"
	                            ></textarea>
	                        </div>
	                        
	                        <!-- Status Field -->
	                        <div class="col-md-6 mb-3">
	                            <label for="addAuthorStatus" class="form-label">Estado <span class="text-danger">*</span></label>
	                            <select 
	                                class="selectpicker form-control placeholder-color" 
	                                id="addAuthorStatus" 
	                                name="addAuthorStatus" 
	                                title="Seleccione un estado" 
	                                required
	                            >
	                                <!-- Options will be dynamically populated via JavaScript -->
	                            </select>
	                            <div class="invalid-feedback"></div>
	                        </div>
	                    </div>
	                    
	                    <!-- Photo Upload Section -->
	                    <div class="row">
	                        <div class="col-md-12 mb-3">
	                            <label for="addAuthorPhoto" class="form-label">Foto</label>
	                            <input 
	                                type="file" 
	                                class="form-control" 
	                                id="addAuthorPhoto" 
	                                name="addAuthorPhoto" 
	                                accept="image/*"
	                            >
	                            <div class="invalid-feedback"></div>
	                        </div>
	                    </div>
	                    
	                    <!-- Default Photo Section -->
						<div class="row">
							<div class="col-md-12 d-flex justify-content-center">
								<div id="defaultAddPhotoContainer" class="text-center" style="max-width: 180px;">
									<svg xmlns="http://www.w3.org/2000/svg" width="180" height="180" fill="currentColor" class="bi-person-circle" viewBox="0 0 16 16">
										<path d="M11 6a3 3 0 1 1-6 0 3 3 0 0 1 6 0"/>
										<path fill-rule="evenodd" d="M0 8a8 8 0 1 1 16 0A8 8 0 0 1 0 8m8-7a7 7 0 0 0-5.468 11.37C3.242 11.226 4.805 10 8 10s4.757 1.225 5.468 2.37A7 7 0 0 0 8 1"/>
						            </svg>
								</div>
							</div>
						</div>
	                    
	                    <!-- Image Cropper Section -->
	                    <div id="cropperContainerAdd" class="d-none">
	                        <img id="imageToCropAdd" src="#" alt="Imagen" />
	                    </div>
	                    
	                    <!-- Delete Photo Section -->
						<div class="row">
							<div class="col-md-12 d-flex justify-content-center">
								<!-- Delete Photo Button -->
								<button type="button" class="btn btn-sm btn-danger d-flex align-items-center mt-2 d-none" id="deleteAddPhotoBtn">
									<i class="bi bi-trash me-2"></i>
									Eliminar foto
								</button>
							</div>
						</div>
	                </form>
	            </div>
	            
	            <!-- Modal Footer -->
	            <footer class="modal-footer">
	            	<!-- Cancel Button -->
	                <button type="button" class="btn btn-custom-secondary" data-bs-dismiss="modal">Cancelar</button>
	                
	                <!-- Add Button -->
	                <button type="submit" class="btn btn-custom-primary d-flex align-items-center" form="addAuthorForm" id="addAuthorBtn">
	                    <span id="addAuthorIcon" class="me-2"><i class="bi bi-plus-lg"></i></span>
	                    <span id="addAuthorSpinnerBtn" class="spinner-border spinner-border-sm me-2 d-none" role="status" aria-hidden="true"></span>
	                    Agregar
	                </button>
	            </footer>
	        </div>
	    </div>
	</div>
    
    <!-- Author Details Modal -->
	<div class="modal fade" id="detailsAuthorModal" tabindex="-1" aria-labelledby="detailsAuthorModalLabel" aria-hidden="true">
	    <div class="modal-dialog modal-lg">
	        <div class="modal-content">
	            <!-- Modal Header -->
	            <header class="modal-header">
	                <h5 class="modal-title text-body-emphasis" id="detailsAuthorModalLabel">Detalles del autor</h5>
	                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
	            </header>
	            
	            <!-- Modal Body -->
	            <div class="modal-body">
	            	<div id="detailsAuthorSpinner" class="text-center my-5">
						<div class="spinner-border" role="status">
						  <span class="visually-hidden">Cargando...</span>
						</div>
					</div>
					
	            	<div id="detailsAuthorContent" class="d-none">
		                <!-- ID and Name Section -->
		                <div class="row">
		                    <div class="col-md-6 mb-3">
		                        <h6 class="small text-muted">ID</h6>
		                        <p class="fw-bold" id="detailsAuthorID"></p>
		                    </div>
		                    
		                    <div class="col-md-6 mb-3">
		                        <h6 class="small text-muted">Nombre</h6>
		                        <p class="fw-bold" id="detailsAuthorName"></p>
		                    </div>
		                </div>
		                
		                <!-- Nationality and Genre Section -->
		                <div class="row">
		                    <div class="col-md-6 mb-3">
		                        <h6 class="small text-muted">Nacionalidad</h6>
		                        <p class="fw-bold" id="detailsAuthorNationality"></p>
		                    </div>
		                    
		                    <div class="col-md-6 mb-3">
		                        <h6 class="small text-muted">Género literario</h6>
		                        <p class="fw-bold" id="detailsAuthorGenre"></p>
		                    </div>
		                </div>
		                
		                <!-- Birth Date and Biography Section -->
		                <div class="row">
		                    <div class="col-md-6 mb-3">
		                        <h6 class="small text-muted">Fecha de nacimiento</h6>
		                        <p class="fw-bold" id="detailsAuthorBirthDate"></p>
		                    </div>
		                    
		                    <div class="col-md-6 mb-3">
		                        <h6 class="small text-muted">Biografía</h6>
		                        <p class="fw-bold" id="detailsAuthorBiography"></p>
		                    </div>
		                </div>
		                
		                <!-- Status and Photo Section -->
		                <div class="row">  
		                    <div class="col-md-6 mb-3">
		                        <h6 class="small text-muted">Estado</h6>
		                        <p id="detailsAuthorStatus"></p>
		                    </div>
		                    
		                    <div class="col-md-6 mb-3">
		                        <h6 class="fw-bold">Foto</h6>
								<svg id="detailsAuthorSvg" xmlns="http://www.w3.org/2000/svg" width="120" height="120" fill="currentColor" class="bi-person-circle" viewBox="0 0 16 16">
									<path d="M11 6a3 3 0 1 1-6 0 3 3 0 0 1 6 0"/>
									<path fill-rule="evenodd" d="M0 8a8 8 0 1 1 16 0A8 8 0 0 1 0 8m8-7a7 7 0 0 0-5.468 11.37C3.242 11.226 4.805 10 8 10s4.757 1.225 5.468 2.37A7 7 0 0 0 8 1"/>
					            </svg>
					            <img id="detailsAuthorImg" src="" class="img-fluid rounded-circle" style="width: 120px; height: 120px;" alt="Foto del Autor">
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
	
	<!-- Edit Author Modal -->
	<div class="modal fade" id="editAuthorModal" tabindex="-1" aria-labelledby="editAuthorModalLabel" aria-hidden="true" data-bs-backdrop="static">
	    <div class="modal-dialog modal-lg">
	        <div class="modal-content">
	            <!-- Modal Header -->
	            <header class="modal-header">
	                <h5 class="modal-title text-body-emphasis" id="editAuthorModalLabel">Editar autor</h5>
	                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
	            </header>
	            
	            <!-- Modal Body -->
	            <div class="modal-body">
					<div id="editAuthorSpinner" class="text-center my-5">
						<div class="spinner-border" role="status">
						  <span class="visually-hidden">Cargando...</span>
						</div>
					</div>
	            	
	                <form id="editAuthorForm" class="d-none" enctype="multipart/form-data" novalidate>
	                    <!-- Personal Information Section -->
	                    <div class="row">
	                        <!-- Author Name Field -->
	                        <div class="col-md-6 mb-3">
	                            <label for="editAuthorName" class="form-label">Nombre <span class="text-danger">*</span></label>
	                            <input 
	                                type="text" 
	                                class="form-control" 
	                                id="editAuthorName" 
	                                name="editAuthorName" 
	                                pattern="[A-Za-zÀ-ÿ\s.]+" 
	                                oninput="this.value = this.value.replace(/[^A-Za-zÀ-ÿ\s.]/g, '');" 
	                                placeholder="Ingrese el nombre del autor" 
	                                required
	                            >
	                            <div class="invalid-feedback"></div>
	                        </div>
	                        
	                        <!-- Nationality Field -->
	                        <div class="col-md-6 mb-3">
	                            <label for="editAuthorNationality" class="form-label">Nacionalidad <span class="text-danger">*</span></label>
	                            <select 
	                                class="selectpicker form-control" 
	                                id="editAuthorNationality" 
	                                name="editAuthorNationality" 
	                                data-live-search="true" 
	                                data-live-search-placeholder="Buscar..." 
	                                required
	                            >
	                            	<!-- Options will be dynamically populated via JavaScript -->
	                            </select>
	                            <div class="invalid-feedback"></div>
	                        </div>
	                    </div>
	                    
	                    <!-- Author Details Section -->
	                    <div class="row">
	                        <!-- Literary Genre Field -->
	                        <div class="col-md-6 mb-3">
	                            <label for="editLiteraryGenre" class="form-label">Género literario <span class="text-danger">*</span></label>
	                            <select 
	                                class="selectpicker form-control" 
	                                id="editLiteraryGenre" 
	                                name="editLiteraryGenre" 
	                                data-live-search="true" 
	                                data-live-search-placeholder="Buscar..." 
	                                required
	                            >
	                                <!-- Options will be dynamically populated via JavaScript -->
	                            </select>
	                            <div class="invalid-feedback"></div>
	                        </div>
	                        
	                        <!-- Birth Date Field -->
	                        <div class="col-md-6 mb-3">
	                            <label for="editAuthorBirthDate" class="form-label">Fecha de nacimiento <span class="text-danger">*</span></label>
	                            <input 
	                                type="date" 
	                                class="form-control" 
	                                id="editAuthorBirthDate" 
	                                name="editAuthorBirthDate" 
	                                required
	                            >
	                            <div class="invalid-feedback"></div>
	                        </div>
	                    </div>
	                    
	                    <!-- Additional Information Section -->
	                    <div class="row">
	                        <!-- Biography Field -->
	                        <div class="col-md-6 mb-3">
	                            <label for="editAuthorBiography" class="form-label">Biografía</label>
	                            <textarea 
	                                class="form-control" 
	                                id="editAuthorBiography" 
	                                name="editAuthorBiography" 
	                                rows="1" 
	                                placeholder="Escribe una breve biografía"
	                            ></textarea>
	                        </div>
	                        
	                        <!-- Status Field -->
	                        <div class="col-md-6 mb-3">
	                            <label for="editAuthorStatus" class="form-label">Estado <span class="text-danger">*</span></label>
	                            <select 
	                                class="selectpicker form-control" 
	                                id="editAuthorStatus" 
	                                name="editAuthorStatus" 
	                                required
	                            >
	                                <!-- Options will be dynamically populated via JavaScript -->
	                            </select>
	                        </div>      
	                    </div>
	                    
	                    <!-- Photo Upload Section -->
	                    <div class="row">
	                        <div class="col-md-12 mb-3">
	                            <label for="editAuthorPhoto" class="form-label">Foto</label>
	                            <input 
	                            	type="file" 
	                                class="form-control" 
	                                id="editAuthorPhoto" 
	                                name="editAuthorPhoto" 
	                                accept="image/*"
	                            >
	                            <div class="invalid-feedback"></div>
	                        </div>
						</div>

						<!-- Current Photo Section -->
						<div class="row">
							<div class="col-md-12 d-flex justify-content-center">
								<div id="currentEditPhotoContainer" class="text-center" style="max-width: 180px;">
									<!-- The current photo will be displayed here -->
								</div>
							</div>
						</div>

						<!-- Image Cropper Section -->
						<div id="cropperContainerEdit" class="d-none">
							<img id="imageToCropEdit" src="#" alt="Imagen" />
						</div>

						<!-- Delete Photo Section -->
						<div class="row">
							<div class="col-md-12 d-flex justify-content-center">
								<!-- Delete Photo Button -->
								<button type="button" class="btn btn-sm btn-danger d-flex align-items-center mt-2 d-none" id="deleteEditPhotoBtn">
									<i class="bi bi-trash me-2"></i>
									Eliminar foto
								</button>
							</div>
						</div>
					</form>
	            </div>

				<!-- Modal Footer -->
				<footer class="modal-footer">
					<!-- Cancel Button -->
					<button type="button" class="btn btn-custom-secondary" data-bs-dismiss="modal">Cancelar</button>

					<!-- Update Button -->
					<button type="submit" class="btn btn-custom-primary d-flex align-items-center" form="editAuthorForm" id="editAuthorBtn" disabled>
						<span id="editAuthorIcon" class="me-2"><i class="bi bi-floppy"></i></span>
						<span id="editAuthorSpinnerBtn" class="spinner-border spinner-border-sm me-2 d-none" role="status" aria-hidden="true"></span>
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

	<jsp:include page="WEB-INF/includes/scripts.jsp">
		<jsp:param name="currentPage" value="authors.js" />
	</jsp:include>
</body>
</html>