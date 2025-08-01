package com.bookstudio.publisher.repository;

import com.bookstudio.publisher.dto.PublisherListDto;
import com.bookstudio.publisher.dto.PublisherSelectDto;
import com.bookstudio.publisher.model.Publisher;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PublisherRepository extends JpaRepository<Publisher, Long> {
    @Query("""
        SELECT new com.bookstudio.publisher.dto.PublisherListDto(
            p.photoUrl,
            p.name,
            n.name,
            p.website,
            p.status,
            p.publisherId
        )
        FROM Publisher p
        JOIN p.nationality n
        ORDER BY p.publisherId DESC
    """)
    List<PublisherListDto> findList();

    @Query("""
        SELECT new com.bookstudio.publisher.dto.PublisherSelectDto(
            p.publisherId,
            p.name
        )
        FROM Publisher p
        WHERE p.status = com.bookstudio.shared.enums.Status.activo
        ORDER BY p.name ASC
    """)
    List<PublisherSelectDto> findForSelect();
}
