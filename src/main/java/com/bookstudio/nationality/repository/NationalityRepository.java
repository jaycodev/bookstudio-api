package com.bookstudio.nationality.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.bookstudio.nationality.dto.NationalityOptionDto;
import com.bookstudio.nationality.model.Nationality;

public interface NationalityRepository extends JpaRepository<Nationality, Long> {
    @Query("""
        SELECT 
            new com.bookstudio.nationality.dto.NationalityOptionDto(
                n.nationalityId, 
                n.name
            )
        FROM Nationality n 
        ORDER BY n.name ASC
    """)
    List<NationalityOptionDto> findForOptions();
}
