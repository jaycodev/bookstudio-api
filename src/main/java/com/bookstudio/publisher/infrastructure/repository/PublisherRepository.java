package com.bookstudio.publisher.infrastructure.repository;

import com.bookstudio.publisher.application.dto.response.PublisherDetailResponse;
import com.bookstudio.publisher.application.dto.response.PublisherListResponse;
import com.bookstudio.publisher.domain.model.Publisher;
import com.bookstudio.shared.application.dto.response.OptionResponse;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PublisherRepository extends JpaRepository<Publisher, Long> {
    @Query("""
        SELECT new com.bookstudio.publisher.application.dto.response.PublisherListResponse(
            p.id,
            p.photoUrl,
            p.name,
            new com.bookstudio.publisher.application.dto.response.PublisherListResponse$Nationality(
                n.id,
                n.code,
                n.name
            ),
            p.website,
            p.address,
            p.status
        )
        FROM Publisher p
        JOIN p.nationality n
        ORDER BY p.id DESC
    """)
    List<PublisherListResponse> findList();


    @Query("""
        SELECT
            p.id AS value,
            p.name AS label
        FROM Publisher p
        WHERE p.status = com.bookstudio.shared.domain.model.type.Status.ACTIVO
        ORDER BY p.name ASC
    """)
    List<OptionResponse> findForOptions();

    @Query("""
        SELECT new com.bookstudio.publisher.application.dto.response.PublisherDetailResponse(
            p.id,
            p.name,
            new com.bookstudio.publisher.application.dto.response.PublisherDetailResponse$Nationality(
                n.id,
                n.code,
                n.name
            ),
            p.foundationYear,
            p.website,
            p.address,
            p.status,
            p.photoUrl,
            NULL
        )
        FROM Publisher p
        JOIN p.nationality n
        WHERE p.id = :id
    """)
    Optional<PublisherDetailResponse> findDetailById(Long id);
}
