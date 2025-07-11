package com.bookstudio.book.model;

import com.bookstudio.author.model.Author;
import com.bookstudio.course.model.Course;
import com.bookstudio.publisher.model.Publisher;
import com.bookstudio.shared.enums.Status;
import com.bookstudio.shared.model.Genre;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "Books")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Book {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "BookID")
	private Long id;

	@Column(name = "Title", nullable = false)
	private String title;

	@Column(name = "TotalCopies", nullable = false)
	private int totalCopies;

	@Column(name = "LoanedCopies", nullable = false)
	private int loanedCopies;

	@Column(name = "ReleaseDate", nullable = false)
	private LocalDate releaseDate;

    @Enumerated(EnumType.STRING)
	@Column(name = "Status", columnDefinition = "ENUM('activo', 'inactivo')")
	private Status status;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "AuthorID", nullable = false)
	private Author author;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PublisherID", nullable = false)
	private Publisher publisher;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CourseID", nullable = false)
	private Course course;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "GenreID", nullable = false)
	private Genre genre;

	@Transient
	public int getAvailableCopies() {
		return totalCopies - loanedCopies;
	}
}
