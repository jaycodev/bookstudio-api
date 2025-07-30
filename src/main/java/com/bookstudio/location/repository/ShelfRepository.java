package com.bookstudio.location.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bookstudio.location.model.Shelf;

public interface ShelfRepository extends JpaRepository<Shelf, Long> {
    List<Shelf> findAllByOrderByShelfIdDesc();
}
