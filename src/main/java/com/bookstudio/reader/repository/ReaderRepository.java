package com.bookstudio.reader.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.bookstudio.reader.dto.ReaderListDto;
import com.bookstudio.reader.dto.ReaderOptionDto;
import com.bookstudio.reader.model.Reader;

import java.util.List;
import java.util.Optional;

public interface ReaderRepository extends JpaRepository<Reader, Long> {
    Optional<Reader> findByDni(String dni);
    Optional<Reader> findByEmail(String email);
    Optional<Reader> findByEmailAndReaderIdNot(String email, Long excludedId);
    
    @Query("""
        SELECT new com.bookstudio.reader.dto.ReaderListDto(
            r.readerId,
            r.code,
            CONCAT(r.firstName, ' ', r.lastName),
            r.dni,
            r.phone,
            r.email,
            r.type,
            r.status
        )
        FROM Reader r
        ORDER BY r.readerId DESC
    """)
    List<ReaderListDto> findList();

    @Query("""
        SELECT new com.bookstudio.reader.dto.ReaderOptionDto(
            r.readerId,
            CONCAT(r.firstName, ' ', r.lastName)
        )
        FROM Reader r
        WHERE r.status = com.bookstudio.reader.model.ReaderStatus.ACTIVO
        ORDER BY r.firstName, r.lastName
    """)
    List<ReaderOptionDto> findForOptions();
}
