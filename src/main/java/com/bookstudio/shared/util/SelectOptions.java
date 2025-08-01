package com.bookstudio.shared.util;

import java.util.List;

import com.bookstudio.author.dto.AuthorSelectDto;
import com.bookstudio.book.dto.BookSelectDto;
import com.bookstudio.category.dto.CategorySelectDto;
import com.bookstudio.genre.model.Genre;
import com.bookstudio.language.model.Language;
import com.bookstudio.location.model.Shelf;
import com.bookstudio.nationality.model.Nationality;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import com.bookstudio.publisher.dto.PublisherSelectDto;
import com.bookstudio.reader.dto.ReaderSelectDto;
import com.bookstudio.role.projection.RoleSelectProjection;

@Data
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SelectOptions {
    private List<BookSelectDto> books;
    private List<AuthorSelectDto> authors;
    private List<PublisherSelectDto> publishers;
    private List<CategorySelectDto> categories;
    private List<ReaderSelectDto> students;
    private List<RoleSelectProjection> roles;
    private List<Language> languages;
    private List<Genre> genres;
    private List<Nationality> nationalities;
    private List<Shelf> shelves;
}
