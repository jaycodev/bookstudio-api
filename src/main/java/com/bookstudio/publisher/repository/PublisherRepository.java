package com.bookstudio.publisher.repository;

import com.bookstudio.publisher.model.Publisher;
import com.bookstudio.shared.enums.Status;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PublisherRepository extends JpaRepository<Publisher, Long> {
    List<Publisher> findByStatus(Status status);
}
