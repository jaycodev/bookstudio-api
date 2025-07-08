package com.bookstudio.course.model;

import com.bookstudio.shared.enums.Status;
import com.bookstudio.shared.util.IdFormatter;
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

	@Column(name = "Level", nullable = false)
	private String level;

	@Column(name = "Description", columnDefinition = "TEXT")
	private String description;

	@Column(name = "Status", columnDefinition = "ENUM('activo','inactivo')")
	private Status status;

	@Transient
	public String getFormattedId() {
		return IdFormatter.formatId(String.valueOf(id), "C");
	}
}
