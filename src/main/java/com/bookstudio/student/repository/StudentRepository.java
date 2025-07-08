package com.bookstudio.student.repository;

import com.bookstudio.shared.enums.Status;
import com.bookstudio.student.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByDni(String dni);
    Optional<Student> findByEmail(String email);
    Optional<Student> findByEmailAndIdNot(String email, Long excludedId);
    List<Student> findByStatus(Status status);
}
