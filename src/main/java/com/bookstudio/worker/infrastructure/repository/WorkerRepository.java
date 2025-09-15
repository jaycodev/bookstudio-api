package com.bookstudio.worker.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.bookstudio.worker.domain.dto.response.WorkerDetailResponse;
import com.bookstudio.worker.domain.dto.response.WorkerListResponse;
import com.bookstudio.worker.domain.model.Worker;

import java.util.List;
import java.util.Optional;

public interface WorkerRepository extends JpaRepository<Worker, Long> {
    @Query("""
        SELECT new com.bookstudio.worker.domain.dto.response.WorkerListResponse(
            w.id,
            w.profilePhotoUrl,
            w.username,
            w.email,
            w.firstName,
            w.lastName,
            new com.bookstudio.worker.domain.dto.response.WorkerListResponse$Role(
                w.role.name
            ),
            w.status
        )
        FROM Worker w
        WHERE w.id <> :loggedId
        ORDER BY w.id DESC
    """)
    List<WorkerListResponse> findList(Long loggedId);

    @Query("""
        SELECT new com.bookstudio.worker.domain.dto.response.WorkerDetailResponse(
            w.id,
            w.username,
            w.email,
            w.firstName,
            w.lastName,
            new com.bookstudio.worker.domain.dto.response.WorkerDetailResponse$Role(
                w.role.id,
                w.role.name
            ),
            w.profilePhotoUrl,
            w.status
        )
        FROM Worker w
        WHERE w.id = :id
    """)
    Optional<WorkerDetailResponse> findDetailById(Long id);

    Optional<Worker> findByUsername(String username);
    Optional<Worker> findByEmail(String email);
}
