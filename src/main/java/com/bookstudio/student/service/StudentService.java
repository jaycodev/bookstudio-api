package com.bookstudio.student.service;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

import com.bookstudio.shared.dao.FacultyDao;
import com.bookstudio.shared.dao.FacultyDaoImpl;
import com.bookstudio.shared.util.SelectOptions;
import com.bookstudio.student.dao.StudentDao;
import com.bookstudio.student.dao.StudentDaoImpl;
import com.bookstudio.student.model.Student;

public class StudentService {
	private StudentDao studentDao = new StudentDaoImpl();
	private FacultyDao facultyDao = new FacultyDaoImpl();

	public List<Student> listStudents() throws SQLException {
		return studentDao.listAll();
	}

	public Student getStudent(String studentId) throws SQLException {
		return studentDao.getById(studentId);
	}

	public Student createStudent(HttpServletRequest request) throws Exception {
		String dni = request.getParameter("addStudentDNI");
		String firstName = request.getParameter("addStudentFirstName");
		String lastName = request.getParameter("addStudentLastName");
		String address = request.getParameter("addStudentAddress");
		String phone = request.getParameter("addStudentPhone");
		String email = request.getParameter("addStudentEmail");
		LocalDate birthDate = LocalDate.parse(request.getParameter("addStudentBirthDate"));
		String gender = request.getParameter("addStudentGender");
		String facultyId = request.getParameter("addStudentFaculty");
		String status = request.getParameter("addStudentStatus");

		Student student = new Student();
		student.setDni(dni);
		student.setFirstName(firstName);
		student.setLastName(lastName);
		student.setAddress(address);
		student.setPhone(phone);
		student.setEmail(email);
		student.setBirthDate(birthDate);
		student.setGender(gender);
		student.setFacultyId(facultyId);
		student.setStatus(status);

		return studentDao.create(student);
	}

	public Student updateStudent(String studentId, HttpServletRequest request) throws Exception {
		String firstName = request.getParameter("editStudentFirstName");
		String lastName = request.getParameter("editStudentLastName");
		String address = request.getParameter("editStudentAddress");
		String phone = request.getParameter("editStudentPhone");
		String email = request.getParameter("editStudentEmail");
		LocalDate birthDate = LocalDate.parse(request.getParameter("editStudentBirthDate"));
		String gender = request.getParameter("editStudentGender");
		String facultyId = request.getParameter("editStudentFaculty");
		String status = request.getParameter("editStudentStatus");

		Student student = new Student();
		student.setStudentId(studentId);
		student.setFirstName(firstName);
		student.setLastName(lastName);
		student.setAddress(address);
		student.setPhone(phone);
		student.setEmail(email);
		student.setBirthDate(birthDate);
		student.setGender(gender);
		student.setFacultyId(facultyId);
		student.setStatus(status);

		return studentDao.update(student);
	}

	public SelectOptions populateSelects() throws SQLException {
		SelectOptions selectOptions = new SelectOptions();

		selectOptions.setFaculties(facultyDao.populateFacultySelect());

		return selectOptions;
	}
}
