package com.bookstudio.worker.application;

import com.bookstudio.role.application.RoleService;
import com.bookstudio.shared.exception.ResourceNotFoundException;
import com.bookstudio.shared.util.SelectOptions;
import com.bookstudio.worker.application.dto.request.CreateWorkerRequest;
import com.bookstudio.worker.application.dto.request.UpdateWorkerRequest;
import com.bookstudio.worker.application.dto.response.WorkerDetailResponse;
import com.bookstudio.worker.application.dto.response.WorkerListResponse;
import com.bookstudio.worker.domain.model.Worker;
import com.bookstudio.worker.infrastructure.repository.WorkerRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WorkerService {
    private final WorkerRepository workerRepository;

    private final RoleService roleService;

    public List<WorkerListResponse> getList(Long loggedId) {
        return workerRepository.findList(loggedId);
    }

    public SelectOptions getSelectOptions() {
        return SelectOptions.builder()
                .roles(roleService.getOptions())
                .build();
    }

    public Optional<Worker> findById(Long id) {
        return workerRepository.findById(id);
    }

    public WorkerDetailResponse getDetailById(Long id) {
        return workerRepository.findDetailById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Worker not found with ID: " + id));
    }

    @Transactional
    public WorkerListResponse create(CreateWorkerRequest request) {
        if (workerRepository.findByUsername(request.username()).isPresent()) {
            throw new IllegalArgumentException("The provided username is already registered.");
        }

        if (workerRepository.findByEmail(request.email()).isPresent()) {
            throw new IllegalArgumentException("The provided email address is already registered.");
        }

        Worker worker = new Worker();
        worker.setRole(roleService.findById(request.roleId())
                .orElseThrow(
                        () -> new ResourceNotFoundException("Role not found with ID: " + request.roleId())));

        worker.setUsername(request.username());
        worker.setEmail(request.email());
        worker.setFirstName(request.firstName());
        worker.setLastName(request.lastName());
        worker.setPassword(request.password());
        worker.setProfilePhotoUrl(request.profilePhotoUrl());
        worker.setStatus(request.status());

        Worker saved = workerRepository.save(worker);
        return toListResponse(saved);
    }

    @Transactional
    public WorkerListResponse update(Long id, UpdateWorkerRequest request) {
        Worker worker = workerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Worker not found with ID: " + id));

        worker.setRole(roleService.findById(request.roleId())
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with ID: " + request.roleId())));

        worker.setFirstName(request.firstName());
        worker.setLastName(request.lastName());
        worker.setProfilePhotoUrl(request.profilePhotoUrl());
        worker.setStatus(request.status());

        Worker updated = workerRepository.save(worker);
        return toListResponse(updated);
    }

    private WorkerListResponse toListResponse(Worker worker) {
        return new WorkerListResponse(
                worker.getId(),
                worker.getProfilePhotoUrl(),
                worker.getUsername(),
                worker.getEmail(),
                worker.getFullName(),

                worker.getRole().getId(),
                worker.getRole().getName(),

                worker.getStatus());
    }
}
