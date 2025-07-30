package com.bookstudio.shared.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "nationalities")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Nationality {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long nationalityId;

	@Column(nullable = false, unique = true)
	private String name;
}
