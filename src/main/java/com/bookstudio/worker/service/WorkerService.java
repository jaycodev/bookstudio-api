package com.bookstudio.worker.service;

import com.bookstudio.auth.util.PasswordUtils;
import com.bookstudio.role.service.RoleService;
import com.bookstudio.shared.util.SelectOptions;
import com.bookstudio.worker.dto.CreateWorkerDto;
import com.bookstudio.worker.dto.UpdateWorkerDto;
import com.bookstudio.worker.dto.WorkerResponseDto;
import com.bookstudio.worker.model.Worker;
import com.bookstudio.worker.projection.WorkerViewProjection;
import com.bookstudio.worker.repository.WorkerRepository;

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

    public List<WorkerViewProjection> getList(Long loggedWorkerId) {
        return workerRepository.findList(loggedWorkerId);
    }

    public Optional<Worker> findById(Long workerId) {
        return workerRepository.findById(workerId);
    }

    public Optional<WorkerViewProjection> getInfoById(Long workerId) {
        return workerRepository.findInfoById(workerId);
    }

    @Transactional
    public WorkerResponseDto create(CreateWorkerDto dto) {
        if (workerRepository.findByUsername(dto.getUsername()).isPresent()) {
            throw new RuntimeException("El nombre de usuario ingresado ya ha sido registrado.");
        }

        if (workerRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new RuntimeException("El correo electrónico ingresado ya ha sido registrado.");
        }

        Worker user = new Worker();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setPassword(PasswordUtils.hashPassword(dto.getPassword()));
        user.setRole(dto.getRole());
        user.setProfilePhotoUrl(dto.getProfilePhotoUrl());

        Worker saved = workerRepository.save(user);

        return new WorkerResponseDto(
                saved.getWorkerId(),
                saved.getUsername(),
                saved.getEmail(),
                saved.getFirstName(),
                saved.getLastName(),
                saved.getRole().getName(),
                saved.getProfilePhotoUrl(),
                saved.getStatus().name());
    }

    @Transactional
    public WorkerResponseDto update(UpdateWorkerDto dto) {
        Worker worker = workerRepository.findById(dto.getWorkerId())
                .orElseThrow(() -> new RuntimeException("Trabajador no encontrado con ID: " + dto.getWorkerId()));

        worker.setFirstName(dto.getFirstName());
        worker.setLastName(dto.getLastName());
        worker.setRole(dto.getRole());

        if (dto.isDeletePhoto()) {
            worker.setProfilePhotoUrl(null);
        } else if (dto.getProfilePhotoUrl() != null && !dto.getProfilePhotoUrl().isBlank()) {
            worker.setProfilePhotoUrl(dto.getProfilePhotoUrl());
        }

        Worker saved = workerRepository.save(worker);

        return new WorkerResponseDto(
                saved.getWorkerId(),
                saved.getUsername(),
                saved.getEmail(),
                saved.getFirstName(),
                saved.getLastName(),
                saved.getRole().getName(),
                saved.getProfilePhotoUrl(),
                saved.getStatus().name());
    }

    @Transactional
    public boolean delete(Long workerId) {
        if (!workerRepository.existsById(workerId))
            return false;
        workerRepository.deleteById(workerId);
        return true;
    }

    public SelectOptions getSelectOptions() {
        return SelectOptions.builder()
                .roles(roleService.getForSelect())
                .build();
    }
}
