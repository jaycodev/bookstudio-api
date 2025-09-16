package com.bookstudio.copy.infrastructure.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.bookstudio.copy.domain.dto.response.CopyDetailResponse;
import com.bookstudio.copy.domain.dto.response.CopyListResponse;
import com.bookstudio.copy.domain.model.Copy;
import com.bookstudio.location.domain.model.Location;
import com.bookstudio.shared.domain.dto.response.OptionResponse;

public interface CopyRepository extends JpaRepository<Copy, Long> {
    @Query("""
        SELECT new com.bookstudio.copy.domain.dto.response.CopyListResponse(
            c.id,
            c.code,
            new com.bookstudio.copy.domain.dto.response.CopyListResponse$Book(
                b.id,
                b.coverUrl,
                b.title
            ),
            new com.bookstudio.copy.domain.dto.response.CopyListResponse$Shelf(
                s.code,
                s.floor
            ),
            new com.bookstudio.copy.domain.dto.response.CopyListResponse$Location(
                l.name
            ),
            c.status,
            c.condition
        )
        FROM Copy c
        JOIN c.book b
        JOIN c.shelf s
        JOIN s.location l
        ORDER BY c.id DESC
    """)
    List<CopyListResponse> findList();

    @Query("""
        SELECT
            c.id AS value,
            c.code AS label
        FROM Copy c
        WHERE c.status = com.bookstudio.copy.domain.model.CopyStatus.DISPONIBLE
    """)
    List<OptionResponse> findForOptions();

    @Query("""
        SELECT new com.bookstudio.copy.domain.dto.response.CopyDetailResponse(
            c.id,
            c.code,
            new com.bookstudio.copy.domain.dto.response.CopyDetailResponse$Book(
                b.id,
                b.isbn,
                b.coverUrl,
                b.title
            ),
            new com.bookstudio.copy.domain.dto.response.CopyDetailResponse$Shelf(
                s.id,
                s.code,
                s.floor,
                s.description
            ),
            c.barcode,
            c.status,
            c.condition
        )
        FROM Copy c
        JOIN c.book b
        JOIN c.shelf s
        WHERE c.id = :id
    """)
    Optional<CopyDetailResponse> findDetailById(Long id);

    Long countByShelfLocation(Location location);
}
