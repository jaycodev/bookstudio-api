package com.bookstudio.copy.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bookstudio.copy.model.Copy;
import com.bookstudio.copy.projection.CopyInfoProjection;
import com.bookstudio.copy.projection.CopyListProjection;
import com.bookstudio.copy.projection.CopySelectProjection;

public interface CopyRepository extends JpaRepository<Copy, Long> {
    @Query("""
        SELECT
            c.copyId AS copyId,
            c.code AS code,
            b.title AS bookName,
            CONCAT(s.code, ' - ', l.name) AS shelfLocation,
            c.isAvailable AS isAvailable,
            c.condition AS condition
        FROM Copy c
        JOIN c.book b
        JOIN c.shelf s
        JOIN s.location l
        ORDER BY c.copyId DESC
    """)
    List<CopyListProjection> findList();

    @Query("""
        SELECT
            c.copyId AS copyId,
            c.code AS code,
            b.title AS bookName,
            CONCAT(s.code, ' - ', l.name) AS shelfLocation,
            c.barcode AS barcode,
            c.isAvailable AS isAvailable,
            c.condition AS condition
        FROM Copy c
        JOIN c.book b
        JOIN c.shelf s
        JOIN s.location l
        WHERE c.copyId = :id
    """)
    Optional<CopyInfoProjection> findInfoById(@Param("id") Long id);

    @Query("""
        SELECT
            c.copyId AS copyId,
            c.code AS code
        FROM Copy c
        WHERE c.isAvailable = true
    """)
    List<CopySelectProjection> findForSelect();
}
