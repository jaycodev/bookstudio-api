package com.bookstudio.student.model;

import com.bookstudio.shared.enums.Status;
import com.bookstudio.shared.model.Faculty;
import com.bookstudio.shared.util.IdFormatter;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "Students")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Student {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "StudentID")
	private Long id;

	@Column(name = "DNI", nullable = false, unique = true, length = 8)
	private String dni;

	@Column(name = "FirstName", nullable = false)
	private String firstName;

	@Column(name = "LastName", nullable = false)
	private String lastName;

	@Column(name = "Address", nullable = false)
	private String address;

	@Column(name = "Phone", nullable = false, length = 9)
	private String phone;

	@Column(name = "Email", nullable = false, unique = true, length = 100)
	private String email;

	@Column(name = "BirthDate", nullable = false)
	private LocalDate birthDate;

	@Column(name = "Gender", nullable = false, length = 10)
	private String gender;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FacultyID", nullable = false)
	private Faculty faculty;

    @Enumerated(EnumType.STRING)
	@Column(name = "Status", columnDefinition = "ENUM('activo', 'inactivo')")
	private Status status;

	@Transient
	public String getFormattedId() {
		return IdFormatter.formatId(String.valueOf(id), "ES");
	}

	@Transient
	public String getFullName() {
		return firstName + " " + lastName;
	}
}
