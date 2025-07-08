package com.bookstudio.publisher.model;

import com.bookstudio.shared.util.IdFormatter;
import com.bookstudio.shared.enums.Status;
import com.bookstudio.shared.model.LiteraryGenre;
import com.bookstudio.shared.model.Nationality;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Publishers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Publisher {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "PublisherID")
	private Long id;

	@Column(name = "Name", nullable = false)
	private String name;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "NationalityID", nullable = false)
	private Nationality nationality;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "LiteraryGenreID", nullable = false)
	private LiteraryGenre literaryGenre;

	@Column(name = "FoundationYear", nullable = false)
	private Integer foundationYear;

	@Column(name = "Website")
	private String website;

	@Column(name = "Address")
	private String address;

	@Column(name = "Status", columnDefinition = "ENUM('activo','inactivo')")
	private Status status;

	@Lob
	@Column(name = "Photo")
	private byte[] photo;

	@Transient
	public String getFormattedId() {
		return IdFormatter.formatId(String.valueOf(id), "ED");
	}

	@Transient
	private String photoBase64;
}
