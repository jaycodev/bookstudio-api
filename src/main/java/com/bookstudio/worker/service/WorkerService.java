package com.bookstudio.worker.service;

import com.bookstudio.auth.util.PasswordUtils;
import com.bookstudio.role.service.RoleService;
import com.bookstudio.shared.util.SelectOptions;
import com.bookstudio.worker.dto.CreateWorkerDto;
import com.bookstudio.worker.dto.UpdateWorkerDto;
import com.bookstudio.worker.dto.WorkerDetailDto;
import com.bookstudio.worker.dto.WorkerListDto;
import com.bookstudio.worker.model.Worker;
import com.bookstudio.worker.repository.WorkerRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WorkerService {

    private final WorkerRepository workerRepository;

    private final RoleService roleService;

    public List<WorkerListDto> getList(Long loggedWorkerId) {
        return workerRepository.findList(loggedWorkerId);
    }

    public Optional<Worker> findById(Long workerId) {
        return workerRepository.findById(workerId);
    }

    public WorkerDetailDto getInfoById(Long workerId) {
        Worker worker = workerRepository.findById(workerId)
                .orElseThrow(() -> new EntityNotFoundException("Worker not found with ID: " + workerId));

        return WorkerDetailDto.builder()
                .id(worker.getWorkerId())
                .username(worker.getUsername())
                .email(worker.getEmail())
                .firstName(worker.getFirstName())
                .lastName(worker.getLastName())
                .profilePhotoUrl(worker.getProfilePhotoUrl())
                .status(worker.getStatus().name())
                .role(roleService.toSummaryDto(worker.getRole()))
                .build();
    }

    @Transactional
    public WorkerListDto create(CreateWorkerDto dto) {
        if (workerRepository.findByUsername(dto.getUsername()).isPresent()) {
            throw new IllegalArgumentException("The provided username is already registered.");
        }

        if (workerRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("The provided email address is already registered.");
        }

        Worker worker = Worker.builder()
                .username(dto.getUsername())
                .email(dto.getEmail())
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .password(PasswordUtils.hashPassword(dto.getPassword()))
                .role(roleService.findById(dto.getRoleId())
                        .orElseThrow(() -> new EntityNotFoundException("Role not found with ID: " + dto.getRoleId())))
                .profilePhotoUrl(dto.getProfilePhotoUrl())
                .status(dto.getStatus())
                .build();

        Worker saved = workerRepository.save(worker);
        return toListDto(saved);
    }

    @Transactional
    public WorkerListDto update(Long workerId, UpdateWorkerDto dto) {
        Worker worker = workerRepository.findById(workerId)
                .orElseThrow(() -> new EntityNotFoundException("Worker not found with ID: " + workerId));

        worker.setRole(roleService.findById(dto.getRoleId())
                .orElseThrow(() -> new EntityNotFoundException("Role not found with ID: " + dto.getRoleId())));

        worker.setFirstName(dto.getFirstName());
        worker.setLastName(dto.getLastName());
        worker.setStatus(dto.getStatus());

        if (dto.isDeletePhoto()) {
            worker.setProfilePhotoUrl(null);
        } else if (dto.getProfilePhotoUrl() != null && !dto.getProfilePhotoUrl().isBlank()) {
            worker.setProfilePhotoUrl(dto.getProfilePhotoUrl());
        }

        Worker saved = workerRepository.save(worker);
        return toListDto(saved);
    }

    public SelectOptions getSelectOptions() {
        return SelectOptions.builder()
                .roles(roleService.getForSelect())
                .build();
    }

    private WorkerListDto toListDto(Worker worker) {
        return new WorkerListDto(
                worker.getWorkerId(),
                worker.getProfilePhotoUrl(),
                worker.getUsername(),
                worker.getEmail(),
                worker.getFirstName(),
                worker.getLastName(),
                worker.getRole().getName(),
                worker.getStatus());
    }
}
