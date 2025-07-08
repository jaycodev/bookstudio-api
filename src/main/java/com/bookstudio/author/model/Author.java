package com.bookstudio.author.model;

import com.bookstudio.shared.enums.Status;
import com.bookstudio.shared.model.LiteraryGenre;
import com.bookstudio.shared.model.Nationality;
import com.bookstudio.shared.util.IdFormatter;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "Authors")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Author {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "AuthorID")
	private Long id;

	@Column(name = "Name", nullable = false)
	private String name;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "NationalityID", nullable = false)
	private Nationality nationality;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "LiteraryGenreID", nullable = false)
	private LiteraryGenre literaryGenre;

	@Column(name = "BirthDate", nullable = false)
	private LocalDate birthDate;

	@Column(name = "Biography", columnDefinition = "TEXT")
	private String biography;

	@Column(name = "Status", columnDefinition = "ENUM('activo','inactivo')")
	private Status status;

	@Lob
	@Column(name = "Photo")
	private byte[] photo;

	@Transient
	public String getFormattedId() {
		return IdFormatter.formatId(String.valueOf(id), "A");
	}

	@Transient
	private String photoBase64;
}
