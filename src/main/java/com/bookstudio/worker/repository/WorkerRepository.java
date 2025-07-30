package com.bookstudio.worker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bookstudio.worker.model.Worker;
import com.bookstudio.worker.projection.WorkerViewProjection;

import java.util.List;
import java.util.Optional;

public interface WorkerRepository extends JpaRepository<Worker, Long> {
    Optional<Worker> findByUsername(String username);
    Optional<Worker> findByEmail(String email);

    @Query("""
        SELECT 
            w.workerId AS workerId,
            w.username AS username,
            w.email AS email,
            w.firstName AS firstName,
            w.lastName AS lastName,
            w.role.name AS role,
            w.profilePhotoUrl AS profilePhotoUrl,
            w.status AS status
        FROM Worker w
        WHERE w.workerId <> :loggedWorkerId
        ORDER BY w.workerId DESC
    """)
    List<WorkerViewProjection> findList(@Param("loggedWorkerId") Long loggedWorkerId);

    @Query("""
        SELECT 
            w.workerId AS workerId,
            w.username AS username,
            w.email AS email,
            w.firstName AS firstName,
            w.lastName AS lastName,
            w.role.name AS role,
            w.profilePhotoUrl AS profilePhotoUrl,
            w.status AS status
        FROM Worker w
        WHERE w.workerId = :id
    """)
    Optional<WorkerViewProjection> findInfoById(@Param("id") Long id);
}
