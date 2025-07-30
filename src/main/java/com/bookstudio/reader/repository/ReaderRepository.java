package com.bookstudio.reader.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bookstudio.reader.model.Reader;
import com.bookstudio.reader.projection.ReaderInfoProjection;
import com.bookstudio.reader.projection.ReaderListProjection;
import com.bookstudio.reader.projection.ReaderSelectProjection;

import java.util.List;
import java.util.Optional;

public interface ReaderRepository extends JpaRepository<Reader, Long> {
    Optional<Reader> findByDni(String dni);
    Optional<Reader> findByEmail(String email);
    Optional<Reader> findByEmailAndReaderIdNot(String email, Long excludedId);

    @Query("""
        SELECT 
            r.readerId AS readerId,
            r.code AS code,
            r.dni AS dni,
            r.firstName AS firstName,
            r.lastName AS lastName,
            r.phone AS phone,
            r.email AS email,
            r.status AS status
        FROM Reader r
        ORDER BY r.readerId DESC
    """)
    List<ReaderListProjection> findList();

    @Query("""
        SELECT 
            r.readerId AS readerId,
            r.code AS code,
            r.dni AS dni,
            r.firstName AS firstName,
            r.lastName AS lastName,
            r.address AS address,
            r.phone AS phone,
            r.email AS email,
            r.birthDate AS birthDate,
            r.gender AS gender,
            r.status AS status
        FROM Reader r
        WHERE r.readerId = :id
    """)
    Optional<ReaderInfoProjection> findInfoById(@Param("id") Long id);

    @Query("""
        SELECT 
            r.readerId AS readerId,
            CONCAT(r.firstName, ' ', r.lastName) AS fullName
        FROM Reader r
        WHERE r.status = 'activo'
    """)
    List<ReaderSelectProjection> findForSelect();
}
