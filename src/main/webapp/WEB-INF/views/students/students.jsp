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
	<!-- ===================== Header ===================== -->
	<jsp:include page="/WEB-INF/includes/header.jsp"></jsp:include>

	<!-- ===================== Sidebar ==================== -->
	<jsp:include page="/WEB-INF/includes/sidebar.jsp">
		<jsp:param name="currentPage" value="/students" />
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
					<i class="bi pe-none me-1 bi-mortarboard"></i>
					Tabla de estudiantes
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

				<!-- Add Button -->
				<button class="btn btn-custom-primary d-flex align-items-center"
					data-bs-toggle="modal" data-bs-target="#addModal"
					aria-label="Agregar estudiante" disabled>
					<i class="bi bi-plus-circle me-2"></i>
					Agregar
				</button>
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
								<th scope="col" class="text-start">DNI</th>
								<th scope="col" class="text-start">Nombres</th>
								<th scope="col" class="text-start">Apellidos</th>
								<th scope="col" class="text-start">Teléfono</th>
								<th scope="col" class="text-start">Correo electrónico</th>
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
	                	Agregar un estudiante
	                </h5>
	                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
	            </header>
	            
	            <!-- Modal Body -->
	            <div class="modal-body">
	                <form id="addForm" accept-charset="UTF-8" novalidate>
	                    <!-- Personal Information Section -->
	                    <div class="row">
	                        <!-- DNI Field -->
	                        <div class="col-md-6 mb-3">
	                            <label for="addDNI" class="form-label">DNI <span class="text-danger">*</span></label>
	                            <input 
	                                type="tel" 
	                                class="form-control" 
	                                id="addDNI" 
                                    name="DNI"
	                                maxlength="8" 
	                                pattern="\d{8}" 
	                                oninput="this.value = this.value.replace(/[^0-9]/g, '').slice(0, 8);" 
	                                placeholder="Ingrese el DNI del estudiante" 
	                                required
	                            >
	                            <div class="invalid-feedback"></div>
	                        </div>
	                        
	                        <!-- First Name Field -->
	                        <div class="col-md-6 mb-3">
	                            <label for="addFirstName" class="form-label">Nombres <span class="text-danger">*</span></label>
	                            <input 
	                                type="text" 
	                                class="form-control" 
	                                id="addFirstName" 
                                    name="firstName"
	                                pattern="[A-Za-zÀ-ÿ\s]+" 
	                                oninput="this.value = this.value.replace(/[^A-Za-zÀ-ÿ\s]/g, '');" 
	                                placeholder="Ingrese los nombres del estudiante" 
	                                required
	                            >
	                            <div class="invalid-feedback"></div>
	                        </div>
	                    </div>
	                    
	                    <!-- Last Name and Address Section -->
	                    <div class="row">
	                        <!-- Last Name Field -->
	                        <div class="col-md-6 mb-3">
	                            <label for="addLastName" class="form-label">Apellidos <span class="text-danger">*</span></label>
	                            <input 
	                                type="text" 
	                                class="form-control" 
	                                id="addLastName" 
                                    name="lastName"
	                                pattern="[A-Za-zÀ-ÿ\s]+" 
	                                oninput="this.value = this.value.replace(/[^A-Za-zÀ-ÿ\s]/g, '');" 
	                                placeholder="Ingrese los apellidos del estudiante" 
	                                required
	                            >
	                            <div class="invalid-feedback"></div>
	                        </div>
	                        
	                        <!-- Address Field -->
	                        <div class="col-md-6 mb-3">
	                            <label for="addAddress" class="form-label">Dirección <span class="text-danger">*</span></label>
	                            <input 
	                                type="text" 
	                                class="form-control" 
	                                id="addAddress" 
                                    name="address"
	                                placeholder="Ingrese la dirección del estudiante" 
	                                required
	                            >
	                            <div class="invalid-feedback"></div>
	                        </div>
	                    </div>
	                    
	                    <!-- Phone and Email Section -->
	                    <div class="row">
	                        <!-- Phone Field -->
	                        <div class="col-md-6 mb-3">
	                            <label for="addPhone" class="form-label">Teléfono <span class="text-danger">*</span></label>
	                            <input 
	                                type="tel" 
	                                class="form-control" 
	                                id="addPhone" 
                                    name="phone"
	                                maxlength="9" 
	                                pattern="\d{9}" 
	                                oninput="this.value = this.value.replace(/[^0-9]/g, '').slice(0, 9);" 
	                                placeholder="Ingrese el teléfono del estudiante" 
	                                required
	                            >
	                            <div class="invalid-feedback"></div>
	                        </div>
	                        
	                        <!-- Email Field -->
	                        <div class="col-md-6 mb-3">
	                            <label for="addEmail" class="form-label">Correo electrónico <span class="text-danger">*</span></label>
	                            <input 
	                                type="email" 
	                                class="form-control" 
	                                id="addEmail" 
                                    name="email"
	                                placeholder="ejemplo@correo.com" 
	                                required
	                            >
	                            <div class="invalid-feedback"></div>
	                        </div>
	                    </div>
	                    
	                    <!-- Birth Date and Gender Section -->
	                    <div class="row">
	                        <!-- Birth Date Field -->
	                        <div class="col-md-6 mb-3">
	                            <label for="addBirthDate" class="form-label">Fecha de nacimiento <span class="text-danger">*</span></label>
	                            <input 
	                                type="date" 
	                                class="form-control" 
	                                id="addBirthDate" 
                                    name="birthDate"
	                                required
	                            >
	                            <div class="invalid-feedback"></div>
	                        </div>
	                        
	                        <!-- Gender Field -->
	                        <div class="col-md-6 mb-3">
	                            <label for="addGender" class="form-label">Género <span class="text-danger">*</span></label>
	                            <select 
	                                class="selectpicker form-control placeholder-color" 
	                                id="addGender" 
                                    name="gender"
	                                title="Seleccione un género" 
	                                required
	                            >
	                                <!-- Options will be dynamically populated via JavaScript -->
	                            </select>
	                            <div class="invalid-feedback"></div>
	                        </div>
	                    </div>
	                    
	                    <!-- Faculty and Status Section -->
	                    <div class="row">
	                        <!-- Faculty Field -->
	                        <div class="col-md-6 mb-3">
	                            <label for="addFaculty" class="form-label">Facultad <span class="text-danger">*</span></label>
	                            <select 
	                                class="selectpicker form-control placeholder-color" 
	                                id="addFaculty" 
                                    name="faculty"
	                                data-live-search="true" 
	                                data-live-search-placeholder="Buscar..." 
	                                title="Seleccione una facultad" 
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
	                    <i class="bi bi-plus-circle me-2"></i>
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
	                	Detalles del estudiante 
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
		                <!-- ID and DNI Section -->
		                <div class="row">
		                    <div class="col-md-6 mb-3">
		                        <h6 class="small text-muted">Código</h6>
		                        <p class="badge bg-body-tertiary text-body-emphasis border" id="detailsID"></p>
		                    </div>
		                    
		                    <div class="col-md-6 mb-3">
		                        <h6 class="small text-muted">DNI</h6>
		                        <p class="badge bg-body-secondary text-body-emphasis border" id="detailsDNI"></p>
		                    </div>
		                </div>
		                
		                <!-- First Name and Last Name Section -->
		                <div class="row">
		                    <div class="col-md-6 mb-3">
		                        <h6 class="small text-muted">Nombres</h6>
		                        <p class="fw-bold" id="detailsFirstName"></p>
		                    </div>
		                    
		                    <div class="col-md-6 mb-3">
		                        <h6 class="small text-muted">Apellidos</h6>
		                        <p class="fw-bold" id="detailsLastName"></p>
		                    </div>
		                </div>
		                
		                <!-- Address and Phone Section -->
		                <div class="row">
		                    <div class="col-md-6 mb-3">
		                        <h6 class="small text-muted">Dirección</h6>
		                        <p class="fw-bold" id="detailsAddress"></p>
		                    </div>
		                    
		                    <div class="col-md-6 mb-3">
		                        <h6 class="small text-muted">Teléfono</h6>
		                        <p class="badge bg-body-secondary text-body-emphasis border" id="detailsPhone"></p>
		                    </div>
		                </div>
		                
		                <!-- Email and Birth Date Section -->
		                <div class="row">
		                    <div class="col-md-6 mb-3">
		                        <h6 class="small text-muted">Correo electrónico</h6>
		                        <p class="fw-bold" id="detailsEmail"></p>
		                    </div>
		                    
		                    <div class="col-md-6 mb-3">
		                        <h6 class="small text-muted">Fecha de nacimiento</h6>
		                        <p class="fw-bold" id="detailsBirthDate"></p>
		                    </div>
		                </div>
		                
		                <!-- Gender and Faculty Section -->
		                <div class="row">
		                    <div class="col-md-6 mb-3">
		                        <h6 class="small text-muted">Género</h6>
		                        <p class="badge bg-body-secondary text-body-emphasis border" id="detailsGender"></p>
		                    </div>
		                    
		                    <div class="col-md-6 mb-3">
		                        <h6 class="small text-muted">Facultad</h6>
		                        <p class="badge bg-body-secondary text-body-emphasis border" id="detailsFaculty"></p>
		                    </div>
		                </div>
		                
		                <!-- Status Section -->
		                <div class="row">
		                    <div class="col-md-6 mb-3">
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
	                	Editar estudiante 
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
	                    <!-- Personal Information Section -->
	                    <div class="row">
	                        <!-- DNI Field -->
	                        <div class="col-md-6 mb-3">
	                            <label for="editDNI" class="form-label">DNI</label>
	                            <input 
	                                type="tel" 
	                                class="form-control" 
	                                id="editDNI" 
	                                disabled
	                            >
	                        </div>
	                        
							<!-- First Name Field -->
	                        <div class="col-md-6 mb-3">
	                            <label for="editFirstName" class="form-label">Nombres <span class="text-danger">*</span></label>
	                            <input 
	                                type="text" 
	                                class="form-control" 
	                                id="editFirstName" 
                                    name="firstName"
	                                pattern="[A-Za-zÀ-ÿ\s]+" 
	                                oninput="this.value = this.value.replace(/[^A-Za-zÀ-ÿ\s]/g, '');" 
	                                placeholder="Ingrese los nombres del estudiante" 
	                                required
	                            >
	                            <div class="invalid-feedback"></div>
	                        </div>
	                    </div>
	                    
	                    <!-- Last Name and Address Section -->
	                    <div class="row">
	                        <!-- Last Name Field -->
	                        <div class="col-md-6 mb-3">
	                            <label for="editLastName" class="form-label">Apellidos <span class="text-danger">*</span></label>
	                            <input 
	                                type="text" 
	                                class="form-control" 
	                                id="editLastName" 
                                    name="lastName"
	                                pattern="[A-Za-zÀ-ÿ\s]+" 
	                                oninput="this.value = this.value.replace(/[^A-Za-zÀ-ÿ\s]/g, '');" 
	                                placeholder="Ingrese los apellidos del estudiante" 
	                                required
	                            >
	                            <div class="invalid-feedback"></div>
	                        </div>
	                        
	                        <!-- Address Field -->
	                        <div class="col-md-6 mb-3">
	                            <label for="editAddress" class="form-label">Dirección <span class="text-danger">*</span></label>
	                            <input 
	                                type="text" 
	                                class="form-control" 
	                                id="editAddress" 
                                    name="address"
	                                placeholder="Ingrese la dirección del estudiante" 
	                                required
	                            >
	                            <div class="invalid-feedback"></div>
	                        </div>
	                    </div>
	                    
	                    <!-- Phone and Email Section -->
	                    <div class="row">
	                        <!-- Phone Field -->
	                        <div class="col-md-6 mb-3">
	                            <label for="editPhone" class="form-label">Teléfono <span class="text-danger">*</span></label>
	                            <input 
	                                type="tel" 
	                                class="form-control" 
	                                id="editPhone" 
                                    name="phone"
	                                maxlength="9" 
	                                pattern="\d{9}" 
	                                oninput="this.value = this.value.replace(/[^0-9]/g, '').slice(0, 9);" 
	                                placeholder="Ingrese el teléfono del estudiante" 
	                                required
	                            >
	                            <div class="invalid-feedback"></div>
	                        </div>
	                        
	                        <!-- Email Field -->
	                        <div class="col-md-6 mb-3">
	                            <label for="editEmail" class="form-label">Correo electrónico <span class="text-danger">*</span></label>
	                            <input 
	                                type="email" 
	                                class="form-control" 
	                                id="editEmail" 
                                    name="email"
	                                placeholder="ejemplo@correo.com" 
	                                required
	                            >
	                            <div class="invalid-feedback"></div>
	                        </div>
	                    </div>
	                    
	                    <!-- Birth Date and Gender Section -->
	                    <div class="row">
	                        <!-- Birth Date Field -->
	                        <div class="col-md-6 mb-3">
	                            <label for="editBirthDate" class="form-label">Fecha de nacimiento <span class="text-danger">*</span></label>
	                            <input 
	                                type="date" 
	                                class="form-control" 
	                                id="editBirthDate" 
                                    name="birthDate"
	                                required
	                            >
	                            <div class="invalid-feedback"></div>
	                        </div>
	                        
	                        <!-- Gender Field -->
	                        <div class="col-md-6 mb-3">
	                            <label for="editGender" class="form-label">Género <span class="text-danger">*</span></label>
	                            <select 
	                                class="selectpicker form-control" 
	                                id="editGender" 
                                    name="gender"
	                                required
	                            >
	                                <!-- Options will be dynamically populated via JavaScript -->
	                            </select>
	                            <div class="invalid-feedback"></div>
	                        </div>
	                    </div>
	                    
	                    <!-- Faculty and Status Section -->
	                    <div class="row">
	                        <!-- Faculty Field -->
	                        <div class="col-md-6 mb-3">
	                            <label for="editFaculty" class="form-label">Facultad <span class="text-danger">*</span></label>
	                            <select 
	                                class="selectpicker form-control" 
	                                id="editFaculty" 
                                    name="faculty"
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
	                <button type="submit" class="btn btn-custom-primary d-flex align-items-center" form="editForm" id="editBtn" disabled>
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
		<jsp:param name="currentPage" value="students" />
	</jsp:include>
</body>
</html>