package com.bookstudio.book.repository;

import com.bookstudio.book.model.Book;
import com.bookstudio.shared.enums.Status;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    @Query("SELECT b FROM Book b WHERE b.status = :status AND b.totalCopies > b.loanedCopies")
    List<Book> findAvailableBooksByStatus(@Param("status") Status status);
}
