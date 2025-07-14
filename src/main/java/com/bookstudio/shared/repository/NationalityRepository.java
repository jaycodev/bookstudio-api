package com.bookstudio.shared.repository;

import com.bookstudio.shared.model.Nationality;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NationalityRepository extends JpaRepository<Nationality, Long> {
}
