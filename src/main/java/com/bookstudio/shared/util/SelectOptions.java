package com.bookstudio.shared.util;

import java.util.List;

import com.bookstudio.author.projection.AuthorSelectProjection;
import com.bookstudio.book.projection.BookSelectProjection;
import com.bookstudio.category.projection.CategorySelectProjection;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import com.bookstudio.publisher.projection.PublisherSelectProjection;
import com.bookstudio.reader.projection.ReaderSelectProjection;
import com.bookstudio.role.projection.RoleSelectProjection;
import com.bookstudio.shared.model.Genre;
import com.bookstudio.shared.model.Language;
import com.bookstudio.shared.model.Nationality;
import com.bookstudio.shared.model.Shelf;

@Data
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SelectOptions {
    private List<BookSelectProjection> books;
    private List<AuthorSelectProjection> authors;
    private List<PublisherSelectProjection> publishers;
    private List<CategorySelectProjection> categories;
    private List<ReaderSelectProjection> students;
    private List<RoleSelectProjection> roles;
    private List<Language> languages;
    private List<Genre> genres;
    private List<Nationality> nationalities;
    private List<Shelf> shelves;
}
