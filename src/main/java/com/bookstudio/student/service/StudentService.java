package com.bookstudio.student.service;

import com.bookstudio.shared.model.Faculty;
import com.bookstudio.shared.repository.FacultyRepository;
import com.bookstudio.shared.service.FacultyService;
import com.bookstudio.shared.util.SelectOptions;
import com.bookstudio.student.model.Student;
import com.bookstudio.student.projection.StudentSelectProjection;
import com.bookstudio.student.repository.StudentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StudentService {

	private final StudentRepository studentRepository;
	private final FacultyRepository facultyRepository;

	private final FacultyService facultyService;

	public List<Student> listStudents() {
		return studentRepository.findAll();
	}

	public Optional<Student> getStudent(Long id) {
		return studentRepository.findById(id);
	}

	@Transactional
	public Student createStudent(Student student) {
		if (studentRepository.findByDni(student.getDni()).isPresent()) {
			throw new RuntimeException("El DNI ingresado ya ha sido registrado.");
		}

		if (studentRepository.findByEmail(student.getEmail()).isPresent()) {
			throw new RuntimeException("El correo electrónico ingresado ya ha sido registrado.");
		}

		Faculty faculty = facultyRepository.findById(student.getFaculty().getFacultyId())
				.orElseThrow(() -> new RuntimeException("Facultad no encontrada"));
		student.setFaculty(faculty);

		return studentRepository.save(student);
	}

	@Transactional
	public Student updateStudent(Long id, Student updatedData) {
		Student student = studentRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Estudiante no encontrado"));

		if (studentRepository.findByEmailAndIdNot(updatedData.getEmail(), id).isPresent()) {
			throw new RuntimeException("El correo electrónico ingresado ya ha sido registrado.");
		}

		Faculty faculty = facultyRepository.findById(updatedData.getFaculty().getFacultyId())
				.orElseThrow(() -> new RuntimeException("Facultad no encontrada"));

		student.setFirstName(updatedData.getFirstName());
		student.setLastName(updatedData.getLastName());
		student.setAddress(updatedData.getAddress());
		student.setPhone(updatedData.getPhone());
		student.setBirthDate(updatedData.getBirthDate());
		student.setEmail(updatedData.getEmail());
		student.setGender(updatedData.getGender());
		student.setStatus(updatedData.getStatus());
		student.setFaculty(faculty);

		return studentRepository.save(student);
	}

	public List<StudentSelectProjection> getStudentsForSelect() {
		return studentRepository.findForSelect();
	}

	public SelectOptions populateSelects() {
		return SelectOptions.builder()
				.faculties(facultyService.getFacultiesForSelect())
				.build();
	}
}
