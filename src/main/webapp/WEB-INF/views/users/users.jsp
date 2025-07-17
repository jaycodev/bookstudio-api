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
		<jsp:param name="currentPage" value="/users" />
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
					<i class="bi pe-none me-1 bi-people"></i>
					Tabla de usuarios
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
					aria-label="Agregar usuario" disabled>
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
								<th scope="col" class="text-start">Nombre de usuario</th>
								<th scope="col" class="text-start">Correo electrónico</th>
								<th scope="col" class="text-start">Nombres</th>
								<th scope="col" class="text-start">Apellidos</th>
								<th scope="col" class="text-start">Rol</th>
								<th scope="col" class="text-center">Foto de perfil</th>
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
	                	Agregar usuario
	                </h5>
	                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
	            </header>
	            
	            <!-- Modal Body -->
	            <div class="modal-body">
	                <form id="addForm" enctype="multipart/form-data" novalidate>
	                    <!-- Username and Email Section -->
	                    <div class="row">
	                        <!-- Username Field -->
	                        <div class="col-md-6 mb-3">
	                            <label for="addUsername" class="form-label">Nombre de usuario <span class="text-danger">*</span></label>
	                            <input 
	                                type="text" 
	                                class="form-control" 
	                                id="addUsername" 
                                    name="username"
	                                placeholder="Ingrese su nombre de usuario" 
	                                pattern="[A-Za-zÀ-ÿ0-9_]+" 
									oninput="this.value = this.value.replace(/[^A-Za-zÀ-ÿ0-9_]/g, '');" 
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
	                    
	                    <!-- First Name and Last Name Section -->
	                    <div class="row">
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
	                                placeholder="Ingrese sus nombres" 
	                                required
	                            >
	                            <div class="invalid-feedback"></div>
	                        </div>
	                        
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
	                                placeholder="Ingrese sus apellidos" 
	                                autocomplete="family-name" 
	                                required
	                            >
	                            <div class="invalid-feedback"></div>
	                        </div>
	                    </div>
	                    
	                    <!-- Password and Confirm Password Section -->
	                    <div class="row">
	                        <!-- Password Field -->
	                        <div class="col-md-6 mb-3">
	                            <label for="addPassword" class="form-label">Contraseña <span class="text-danger">*</span></label>
	                            <div class="input-group">
	                                <input 
	                                    type="password" 
	                                    class="form-control password-field" 
	                                    data-toggle-id="1" 
	                                    id="addPassword" 
                                        name="password"
	                                    placeholder="Ingrese una contraseña" 
	                                    autocomplete="new-password" 
	                                    required
	                                >
	                                <span class="input-group-text cursor-pointer" data-toggle-id="1">
	                                    <i class="bi bi-eye"></i>
	                                </span>
	                                <div class="invalid-feedback"></div>
	                            </div>
	                        </div>
	                        
	                        <!-- Confirm Password Field -->
	                        <div class="col-md-6 mb-3">
	                            <label for="addConfirmPassword" class="form-label">Confirmar contraseña <span class="text-danger">*</span></label>
	                            <div class="input-group">
	                                <input 
	                                    type="password" 
	                                    class="form-control password-field" 
	                                    data-toggle-id="2" 
	                                    id="addConfirmPassword" 
	                                    placeholder="Confirme su contraseña" 
	                                    autocomplete="new-password" 
	                                    required
	                                >
	                                <span class="input-group-text cursor-pointer" data-toggle-id="2">
	                                    <i class="bi bi-eye"></i>
	                                </span>
	                                <div class="invalid-feedback"></div>
	                            </div>
	                        </div>
	                    </div>
	                    
	                    <!-- Role and Profile Photo Section -->
	                    <div class="row">
	                        <!-- Role Field -->
	                        <div class="col-md-6 mb-3">
	                            <label for="addRole" class="form-label">Rol <span class="text-danger">*</span></label>
	                            <select 
	                                class="selectpicker form-control placeholder-color" 
	                                id="addRole" 
                                    name="role"
	                                title="Seleccione un rol" 
	                                required
	                            >
	                                <!-- Options will be dynamically populated via JavaScript -->
	                            </select>
	                            <div class="invalid-feedback"></div>
	                        </div>
	                        
	                        <!-- Profile Photo Field -->
	                        <div class="col-md-6 mb-3">
	                            <label for="addProfilePhoto" class="form-label">Foto de perfil</label>
	                            <input 
	                                type="file" 
	                                class="form-control" 
	                                id="addProfilePhoto" 
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
	                	Detalles del usuario 
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
		                <!-- ID and Username Section -->
		                <div class="row">
		                    <div class="col-md-6 mb-3">
		                        <h6 class="small text-muted">Código</h6>
		                        <p class="badge bg-body-tertiary text-body-emphasis border" id="detailsID"></p>
		                    </div>
		                    
		                    <div class="col-md-6 mb-3">
		                        <h6 class="small text-muted">Nombre de usuario</h6>
		                        <p class="fw-bold" id="detailsUsername"></p>
		                    </div>
		                </div>
		                
		                <!-- Email and First Name Section -->
		                <div class="row">
		                    <div class="col-md-6 mb-3">
		                        <h6 class="small text-muted">Correo electrónico</h6>
		                        <p class="fw-bold" id="detailsEmail"></p>
		                    </div>
		                    
		                    <div class="col-md-6 mb-3">
		                        <h6 class="small text-muted">Nombres</h6>
		                        <p class="fw-bold" id="detailsFirstName"></p>
		                    </div>
		                </div>
		                
		                <!-- Last Name and Password Section -->
		                <div class="row">
		                    <div class="col-md-6 mb-3">
		                        <h6 class="small text-muted">Apellidos</h6>
		                        <p class="fw-bold" id="detailsLastName"></p>
		                    </div>
		                    
		                    <div class="col-md-6 mb-3">
		                        <h6 class="small text-muted">Rol</h6>
		                        <p class="badge bg-body-secondary text-body-emphasis border" id="detailsRole"></p>
		                    </div>
		                </div>
		                
		                <!-- Role and Profile Photo Section -->
		                <div class="row">
		                    <div class="col-md-6 mb-3">
		                        <h6 class="small text-muted">Foto de perfil</h6>
		                        <svg id="detailsSvg" xmlns="http://www.w3.org/2000/svg" width="120" height="120" fill="currentColor" class="bi-person-circle" viewBox="0 0 16 16">
									<path d="M11 6a3 3 0 1 1-6 0 3 3 0 0 1 6 0"/>
									<path fill-rule="evenodd" d="M0 8a8 8 0 1 1 16 0A8 8 0 0 1 0 8m8-7a7 7 0 0 0-5.468 11.37C3.242 11.226 4.805 10 8 10s4.757 1.225 5.468 2.37A7 7 0 0 0 8 1"/>
					            </svg>
					            <img id="detailsImg" src="" class="img-fluid rounded-circle" style="width: 120px; height: 120px;" alt="Foto del Usuario">
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
	                	Editar usuario 
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
					
	                <form id="editForm" class="d-none" enctype="multipart/form-data" novalidate>
	                    <!-- Username and Email Section -->
	                    <div class="row">
	                        <!-- Username Field -->
	                        <div class="col-md-6 mb-3">
	                            <label for="editUsername" class="form-label">Nombre de usuario</label>
	                            <input 
	                                type="text" 
	                                class="form-control" 
	                                id="editUsername" 
	                                disabled
	                            >
	                        </div>
	                        
	                        <!-- Email Field -->
	                        <div class="col-md-6 mb-3">
	                            <label for="editEmail" class="form-label">Correo electrónico</label>
	                            <input 
	                                type="email" 
	                                class="form-control" 
	                                id="editEmail" 
	                                disabled
	                            >
	                        </div>
	                    </div>
	                    
	                    <!-- First Name and Last Name Section -->
	                    <div class="row">
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
	                                placeholder="Ingrese sus nombres" 
	                                required
	                            >
	                            <div class="invalid-feedback"></div>
	                        </div>
	                        
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
	                                placeholder="Ingrese sus apellidos" 
	                                autocomplete="family-name" 
	                                required
	                            >
	                            <div class="invalid-feedback"></div>
	                        </div>
	                    </div>
	                    
	                    <!-- Password and Role Section -->
	                    <div class="row">      
	                        <!-- Role Field -->
	                        <div class="col-md-6 mb-3">
	                            <label for="editRole" class="form-label">Rol <span class="text-danger">*</span></label>
	                            <select 
	                                class="selectpicker form-control" 
	                                id="editRole" 
                                    name="role"
	                                required
	                            >
	                                <!-- Options will be dynamically populated via JavaScript -->
	                            </select>
	                        </div>
	                        
	                        <!-- Profile Photo Field -->
	                        <div class="col-md-6 mb-3">
	                            <label for="editProfilePhoto" class="form-label">Foto de perfil</label>
	                            <input 
	                                type="file" 
	                                class="form-control" 
	                                id="editProfilePhoto" 
	                                accept="image/*"
	                            >
	                            <div class="invalid-feedback"></div>
	                        </div>
	                    </div>
						
						<!-- Current Profile Photo Section -->
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
	                    
	                    <!-- Delete Profile Photo Section -->
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
	                <button type="submit" class="btn btn-custom-primary d-flex align-items-center" form="editForm" id="updateBtn" disabled>
	                    <i class="bi bi-floppy me-2"></i>
	                    <span class="spinner-border spinner-border-sm me-2 d-none" role="status" aria-hidden="true"></span>
	                    Actualizar
	                </button>
	            </footer>
	        </div>
	    </div>
	</div>
	
	<!-- Delete Modal -->
	<div class="modal fade" id="deleteModal" tabindex="-1" aria-labelledby="deleteModalLabel" aria-hidden="true" data-bs-backdrop="static">
	    <div class="modal-dialog">
	        <div class="modal-content">
	            <!-- Modal Header -->
	            <div class="modal-header">
	                <h5 class="modal-title text-body-emphasis" id="deleteModalLabel">
	                	Confirmación de usuario 
	                	<span class="badge bg-body-tertiary text-body-emphasis border ms-1" id="deleteModalID"></span>
	                </h5>
	                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
	            </div>
	            
	            <!-- Modal Body -->
	            <div class="modal-body">
	                <p>¿Estás seguro de que deseas eliminar este usuario?</p>
	            </div>
	            
	            <!-- Modal Footer -->
	            <div class="modal-footer">
	                <!-- Cancel Button -->
	                <button type="button" class="btn btn-custom-secondary d-flex align-items-center" data-bs-dismiss="modal">
	                    Cancelar
	                </button>
	                
	                <!-- Delete Button -->
	                <button type="button" class="btn btn-danger d-flex align-items-center" id="deleteBtn">
	                    <i class="bi bi-trash me-2"></i>
	                    <span class="spinner-border spinner-border-sm me-2 d-none" role="status" aria-hidden="true"></span>
	                    Eliminar
	                </button>
	            </div>
	        </div>
	    </div>
	</div>

	<!-- Toast Container -->
	<div class="toast-container" id="toast-container">
		<!-- Toasts will be added here by JavaScript -->
	</div>

	<jsp:include page="/WEB-INF/includes/scripts.jsp">
		<jsp:param name="currentPage" value="users" />
	</jsp:include>
</body>
</html>