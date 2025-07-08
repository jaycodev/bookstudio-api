package com.bookstudio.shared.util;

import java.util.List;

import com.bookstudio.author.model.Author;
import com.bookstudio.book.model.Book;
import com.bookstudio.course.model.Course;
import com.bookstudio.student.model.Student;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import com.bookstudio.publisher.model.Publisher;
import com.bookstudio.shared.model.Faculty;
import com.bookstudio.shared.model.Genre;
import com.bookstudio.shared.model.LiteraryGenre;
import com.bookstudio.shared.model.Nationality;

@Data
@AllArgsConstructor
@Builder
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
}
