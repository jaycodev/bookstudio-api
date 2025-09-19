package com.bookstudio.worker.application;

import com.bookstudio.auth.util.PasswordUtils;
import com.bookstudio.role.application.RoleService;
import com.bookstudio.shared.util.SelectOptions;
import com.bookstudio.worker.application.dto.request.CreateWorkerRequest;
import com.bookstudio.worker.application.dto.request.UpdateWorkerRequest;
import com.bookstudio.worker.application.dto.response.WorkerDetailResponse;
import com.bookstudio.worker.application.dto.response.WorkerListResponse;
import com.bookstudio.worker.domain.model.Worker;
import com.bookstudio.worker.infrastructure.repository.WorkerRepository;

import jakarta.persistence.EntityNotFoundException;
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
                .orElseThrow(() -> new EntityNotFoundException("Worker not found with ID: " + id));
    }

    @Transactional
    public WorkerListResponse create(CreateWorkerRequest request) {
        if (workerRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new IllegalArgumentException("The provided username is already registered.");
        }

        if (workerRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("The provided email address is already registered.");
        }

        Worker worker = Worker.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .password(PasswordUtils.hashPassword(request.getPassword()))
                .role(roleService.findById(request.getRoleId())
                        .orElseThrow(
                                () -> new EntityNotFoundException("Role not found with ID: " + request.getRoleId())))
                .profilePhotoUrl(request.getProfilePhotoUrl())
                .status(request.getStatus())
                .build();

        Worker saved = workerRepository.save(worker);
        return toListResponse(saved);
    }

    @Transactional
    public WorkerListResponse update(Long id, UpdateWorkerRequest request) {
        Worker worker = workerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Worker not found with ID: " + id));

        worker.setRole(roleService.findById(request.getRoleId())
                .orElseThrow(() -> new EntityNotFoundException("Role not found with ID: " + request.getRoleId())));

        worker.setFirstName(request.getFirstName());
        worker.setLastName(request.getLastName());
        worker.setStatus(request.getStatus());
        worker.setProfilePhotoUrl(request.getProfilePhotoUrl());

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
