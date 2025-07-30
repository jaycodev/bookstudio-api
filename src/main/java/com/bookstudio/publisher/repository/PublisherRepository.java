package com.bookstudio.publisher.repository;

import com.bookstudio.publisher.model.Publisher;
import com.bookstudio.publisher.projection.PublisherInfoProjection;
import com.bookstudio.publisher.projection.PublisherListProjection;
import com.bookstudio.publisher.projection.PublisherSelectProjection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PublisherRepository extends JpaRepository<Publisher, Long> {
    @Query("""
        SELECT 
            p.publisherId AS publisherId,
            p.name AS name,
            n.name AS nationalityName,
            p.website AS website,
            p.status AS status,
            p.photoUrl AS photoUrl
        FROM Publisher p
        JOIN p.nationality n
        ORDER BY p.publisherId DESC
    """)
    List<PublisherListProjection> findList();


    @Query("""
        SELECT 
            p.publisherId AS publisherId,
            p.name AS name,
            n.nationalityId AS nationalityId,
            n.name AS nationalityName,
            p.foundationYear AS foundationYear,
            p.website AS website,
            p.address AS address,
            p.status AS status,
            p.photoUrl AS photoUrl
        FROM Publisher p
        JOIN p.nationality n
        WHERE p.publisherId = :id
    """)
    Optional<PublisherInfoProjection> findInfoById(@Param("id") Long id);

    @Query("SELECT p.publisherId AS publisherId, p.name AS name FROM Publisher p WHERE p.status = 'activo'")
    List<PublisherSelectProjection> findForSelect();
}
