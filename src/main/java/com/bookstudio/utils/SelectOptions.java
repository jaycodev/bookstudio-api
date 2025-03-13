package com.bookstudio.utils;

import java.util.List;

import com.bookstudio.models.Author;
import com.bookstudio.models.Book;
import com.bookstudio.models.Publisher;
import com.bookstudio.models.Course;
import com.bookstudio.models.Student;
import com.bookstudio.models.Faculty;
import com.bookstudio.models.Genre;
import com.bookstudio.models.Nationality;
import com.bookstudio.models.LiteraryGenre;

public class SelectOptions {
	private List<Book> books;
	private List<Author> authors;
	private List<Publisher> publishers;
	private List<Course> courses;
	private List<Student> students;
	private List<Faculty> faculties;
	private List<Genre> genres;
	private List<Nationality> nationalities;
	private List<LiteraryGenre> literaryGenres;

	public List<Book> getBooks() {
		return books;
	}

	public void setBooks(List<Book> books) {
		this.books = books;
	}

	public List<Author> getAuthors() {
		return authors;
	}

	public void setAuthors(List<Author> authors) {
		this.authors = authors;
	}

	public List<Publisher> getPublishers() {
		return publishers;
	}

	public void setPublishers(List<Publisher> publishers) {
		this.publishers = publishers;
	}

	public List<Course> getCourses() {
		return courses;
	}

	public void setCourses(List<Course> courses) {
		this.courses = courses;
	}

	public List<Student> getStudents() {
		return students;
	}

	public void setStudents(List<Student> students) {
		this.students = students;
	}

	public List<Faculty> getFaculties() {
		return faculties;
	}

	public void setFaculties(List<Faculty> faculties) {
		this.faculties = faculties;
	}

	public List<Genre> getGenres() {
		return genres;
	}

	public void setGenres(List<Genre> genres) {
		this.genres = genres;
	}

	public List<Nationality> getNationalities() {
		return nationalities;
	}

	public void setNationalities(List<Nationality> nationalities) {
		this.nationalities = nationalities;
	}

	public List<LiteraryGenre> getLiteraryGenres() {
		return literaryGenres;
	}

	public void setLiteraryGenres(List<LiteraryGenre> literaryGenres) {
		this.literaryGenres = literaryGenres;
	}
}
