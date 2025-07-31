package com.bookstudio.shared.util;

import java.util.List;

import com.bookstudio.author.dto.AuthorSelectDto;
import com.bookstudio.book.dto.BookSelectDto;
import com.bookstudio.category.projection.CategorySelectProjection;
import com.bookstudio.genre.model.Genre;
import com.bookstudio.language.model.Language;
import com.bookstudio.location.model.Shelf;
import com.bookstudio.nationality.model.Nationality;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import com.bookstudio.publisher.projection.PublisherSelectProjection;
import com.bookstudio.reader.projection.ReaderSelectProjection;
import com.bookstudio.role.projection.RoleSelectProjection;

@Data
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SelectOptions {
    private List<BookSelectDto> books;
    private List<AuthorSelectDto> authors;
    private List<PublisherSelectProjection> publishers;
    private List<CategorySelectProjection> categories;
    private List<ReaderSelectProjection> students;
    private List<RoleSelectProjection> roles;
    private List<Language> languages;
    private List<Genre> genres;
    private List<Nationality> nationalities;
    private List<Shelf> shelves;
}
