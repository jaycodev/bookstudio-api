package com.bookstudio.worker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bookstudio.worker.dto.WorkerListDto;
import com.bookstudio.worker.model.Worker;

import java.util.List;
import java.util.Optional;

public interface WorkerRepository extends JpaRepository<Worker, Long> {
    Optional<Worker> findByUsername(String username);
    Optional<Worker> findByEmail(String email);

    @Query("""
        SELECT new com.bookstudio.worker.dto.WorkerListDto(
            w.workerId,
            w.profilePhotoUrl,
            w.username,
            w.email,
            w.firstName,
            w.lastName,
            w.role.name,
            w.status
        )
        FROM Worker w
        WHERE w.workerId <> :loggedWorkerId
        ORDER BY w.workerId DESC
    """)
    List<WorkerListDto> findList(@Param("loggedWorkerId") Long loggedWorkerId);
}
