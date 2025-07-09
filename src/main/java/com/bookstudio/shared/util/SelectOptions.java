package com.bookstudio.shared.util;

import java.util.List;

import com.bookstudio.author.projection.AuthorSelectProjection;
import com.bookstudio.book.projection.BookSelectProjection;
import com.bookstudio.course.projection.CourseSelectProjection;
import com.bookstudio.student.projection.StudentSelectProjection;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import com.bookstudio.publisher.projection.PublisherSelectProjection;
import com.bookstudio.shared.model.Faculty;
import com.bookstudio.shared.model.Genre;
import com.bookstudio.shared.model.LiteraryGenre;
import com.bookstudio.shared.model.Nationality;

@Data
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SelectOptions {
    private List<BookSelectProjection> books;
    private List<AuthorSelectProjection> authors;
    private List<PublisherSelectProjection> publishers;
    private List<CourseSelectProjection> courses;
    private List<StudentSelectProjection> students;
    private List<Faculty> faculties;
    private List<Genre> genres;
    private List<Nationality> nationalities;
    private List<LiteraryGenre> literaryGenres;
}
