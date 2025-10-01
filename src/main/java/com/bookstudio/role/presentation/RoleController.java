package com.bookstudio.role.presentation;

import com.bookstudio.role.application.RoleService;
import com.bookstudio.role.application.dto.request.CreateRoleRequest;
import com.bookstudio.role.application.dto.request.UpdateRoleRequest;
import com.bookstudio.role.application.dto.response.RoleDetailResponse;
import com.bookstudio.role.application.dto.response.RoleListResponse;
import com.bookstudio.shared.application.dto.response.ApiErrorResponse;
import com.bookstudio.shared.application.dto.response.ApiResponse;
import com.bookstudio.shared.application.dto.response.OptionResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
@Tag(name = "Roles", description = "Operations related to roles")
public class RoleController {

    private final RoleService roleService;

    @GetMapping
    @Operation(summary = "List all roles")
    public ResponseEntity<?> list() {
        List<RoleListResponse> roles = roleService.getList();
        if (roles.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(new ApiErrorResponse(false, "No roles found.", "no_content", 204));
        }
        return ResponseEntity.ok(roles);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a role by ID")
    public ResponseEntity<?> get(@PathVariable Long id) {
        try {
            RoleDetailResponse role = roleService.getDetailById(id);
            return ResponseEntity.ok(role);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiErrorResponse(false, "Role not found.", "not_found", 404));
        }
    }

    @PostMapping
    @Operation(summary = "Create a new role")
    public ResponseEntity<?> create(@RequestBody CreateRoleRequest request) {
        try {
            RoleListResponse created = roleService.create(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse(true, created));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiErrorResponse(false, e.getMessage(), "creation_failed", 400));
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a role by ID")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody UpdateRoleRequest request) {
        try {
            RoleListResponse updated = roleService.update(id, request);
            return ResponseEntity.ok(new ApiResponse(true, updated));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiErrorResponse(false, e.getMessage(), "update_failed", 404));
        }
    }

    @GetMapping("/filter-options")
    @Operation(summary = "Get role filter options")
    public ResponseEntity<?> filterOptions() {
        try {
            List<OptionResponse> roles = roleService.getOptions();
            if (roles.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT)
                        .body(new ApiErrorResponse(false, "No roles found for filter.", "no_content", 204));
            }
            return ResponseEntity.ok(roles);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiErrorResponse(false, "Error fetching role filter options.", "server_error", 500));
        }
    }
}
