package com.bookstudio.course.model;

import com.bookstudio.shared.enums.Status;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Courses")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Course {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "CourseID")
	private Long id;

	@Column(name = "Name", nullable = false)
	private String name;

	@Column(name = "Level", nullable = false, length = 100)
	private String level;

	@Column(name = "Description", columnDefinition = "TEXT")
	private String description;

    @Enumerated(EnumType.STRING)
	@Column(name = "Status", columnDefinition = "ENUM('activo','inactivo')")
	private Status status;
}
