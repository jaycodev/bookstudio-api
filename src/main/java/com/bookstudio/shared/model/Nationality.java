package com.bookstudio.shared.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Nationalities")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Nationality {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "NationalityID")
	private Long nationalityId;

	@Column(name = "Name", nullable = false, unique = true, length = 100)
	private String name;
}
