package com.bookstudio.student.repository;

import com.bookstudio.student.model.Student;
import com.bookstudio.student.projection.StudentSelectProjection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByDni(String dni);
    Optional<Student> findByEmail(String email);
    Optional<Student> findByEmailAndIdNot(String email, Long excludedId);

    @Query("SELECT s.id as studentId, CONCAT(s.firstName, ' ', s.lastName) as fullName FROM Student s WHERE s.status = 'activo'")
    List<StudentSelectProjection> findForSelect();
}
