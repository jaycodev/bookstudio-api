package com.bookstudio.shared.repository;

import com.bookstudio.shared.model.Shelf;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ShelfRepository extends JpaRepository<Shelf, Long> {
    List<Shelf> findAllByOrderByShelfIdDesc();
}
