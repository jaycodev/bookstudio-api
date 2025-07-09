package com.bookstudio.publisher.repository;

import com.bookstudio.publisher.model.Publisher;
import com.bookstudio.publisher.projection.PublisherSelectProjection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PublisherRepository extends JpaRepository<Publisher, Long> {
    @Query("SELECT p.id as publisherId, p.name as name FROM Publisher p WHERE p.status = 'activo'")
    List<PublisherSelectProjection> findForSelect();
}
