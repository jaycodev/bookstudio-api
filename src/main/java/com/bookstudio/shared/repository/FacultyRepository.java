package com.bookstudio.shared.repository;

import com.bookstudio.shared.model.Faculty;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FacultyRepository extends JpaRepository<Faculty, Long> {
    List<Faculty> findAllByOrderByNameAsc();
}
