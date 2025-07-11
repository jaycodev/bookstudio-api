package com.bookstudio.student.service;

import com.bookstudio.shared.model.Faculty;
import com.bookstudio.shared.repository.FacultyRepository;
import com.bookstudio.shared.service.FacultyService;
import com.bookstudio.shared.util.SelectOptions;
import com.bookstudio.student.dto.CreateStudentDto;
import com.bookstudio.student.dto.StudentResponseDto;
import com.bookstudio.student.dto.UpdateStudentDto;
import com.bookstudio.student.model.Student;
import com.bookstudio.student.projection.StudentInfoProjection;
import com.bookstudio.student.projection.StudentListProjection;
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

    public List<StudentListProjection> getList() {
        return studentRepository.findList();
    }

    public Optional<Student> findById(Long studentId) {
        return studentRepository.findById(studentId);
    }

    public Optional<StudentInfoProjection> getInfoById(Long studentId) {
        return studentRepository.findInfoById(studentId);
    }

    @Transactional
    public StudentResponseDto create(CreateStudentDto dto) {
        if (studentRepository.findByDni(dto.getDni()).isPresent()) {
            throw new RuntimeException("El DNI ingresado ya ha sido registrado.");
        }

        if (studentRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new RuntimeException("El correo electrónico ingresado ya ha sido registrado.");
        }

        Faculty faculty = facultyRepository.findById(dto.getFacultyId())
                .orElseThrow(() -> new RuntimeException("Facultad no encontrada"));

        Student student = new Student();
        student.setDni(dto.getDni());
        student.setFirstName(dto.getFirstName());
        student.setLastName(dto.getLastName());
        student.setAddress(dto.getAddress());
        student.setPhone(dto.getPhone());
        student.setEmail(dto.getEmail());
        student.setBirthDate(dto.getBirthDate());
        student.setGender(dto.getGender());
        student.setStatus(dto.getStatus());
        student.setFaculty(faculty);

        Student saved = studentRepository.save(student);

        return new StudentResponseDto(
                saved.getId(),
                saved.getDni(),
                saved.getFirstName(),
                saved.getLastName(),
                saved.getPhone(),
                saved.getEmail(),
                saved.getStatus().name()
        );
    }

    @Transactional
    public StudentResponseDto update(UpdateStudentDto dto) {
        Student student = studentRepository.findById(dto.getStudentId())
                .orElseThrow(() -> new RuntimeException("Estudiante no encontrado con ID: " + dto.getStudentId()));

        if (studentRepository.findByEmailAndIdNot(dto.getEmail(), dto.getStudentId()).isPresent()) {
            throw new RuntimeException("El correo electrónico ingresado ya ha sido registrado.");
        }

        Faculty faculty = facultyRepository.findById(dto.getFacultyId())
                .orElseThrow(() -> new RuntimeException("Facultad no encontrada"));

        student.setFirstName(dto.getFirstName());
        student.setLastName(dto.getLastName());
        student.setAddress(dto.getAddress());
        student.setPhone(dto.getPhone());
        student.setEmail(dto.getEmail());
        student.setBirthDate(dto.getBirthDate());
        student.setGender(dto.getGender());
        student.setStatus(dto.getStatus());
        student.setFaculty(faculty);

        Student saved = studentRepository.save(student);

        return new StudentResponseDto(
                saved.getId(),
                saved.getDni(),
                saved.getFirstName(),
                saved.getLastName(),
                saved.getPhone(),
                saved.getEmail(),
                saved.getStatus().name()
        );
    }

	public List<StudentSelectProjection> getForSelect() {
		return studentRepository.findForSelect();
	}

	public SelectOptions getSelectOptions() {
		return SelectOptions.builder()
				.faculties(facultyService.getForSelect())
				.build();
	}
}
