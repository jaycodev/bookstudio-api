package com.bookstudio.student.repository;

import com.bookstudio.student.model.Student;
import com.bookstudio.student.projection.StudentInfoProjection;
import com.bookstudio.student.projection.StudentListProjection;
import com.bookstudio.student.projection.StudentSelectProjection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByDni(String dni);
    Optional<Student> findByEmail(String email);
    Optional<Student> findByEmailAndIdNot(String email, Long excludedId);

    @Query("""
        SELECT 
            s.id AS studentId,
            s.dni AS dni,
            s.firstName AS firstName,
            s.lastName AS lastName,
            s.phone AS phone,
            s.email AS email,
            s.status AS status
        FROM Student s
    """)
    List<StudentListProjection> findList();

    @Query("""
        SELECT 
            s.id AS studentId,
            s.dni AS dni,
            s.firstName AS firstName,
            s.lastName AS lastName,
            s.address AS address,
            s.phone AS phone,
            s.email AS email,
            s.birthDate AS birthDate,
            s.gender AS gender,

            f.facultyId AS facultyId,
            f.name AS facultyName,

            s.status AS status
        FROM Student s
        JOIN s.faculty f
        WHERE s.id = :id
    """)
    Optional<StudentInfoProjection> findInfoById(@Param("id") Long id);

    @Query("SELECT s.id as studentId, CONCAT(s.firstName, ' ', s.lastName) as fullName FROM Student s WHERE s.status = 'activo'")
    List<StudentSelectProjection> findForSelect();
}
