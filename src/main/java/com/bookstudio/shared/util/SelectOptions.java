package com.bookstudio.shared.util;

import java.util.List;

import com.bookstudio.author.dto.AuthorOptionDto;
import com.bookstudio.book.dto.BookOptionDto;
import com.bookstudio.category.dto.CategoryOptionDto;
import com.bookstudio.genre.model.Genre;
import com.bookstudio.language.dto.LanguageOptionDto;
import com.bookstudio.location.dto.ShelfOptionDto;
import com.bookstudio.nationality.dto.NationalityOptionDto;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import com.bookstudio.publisher.dto.PublisherOptionDto;
import com.bookstudio.reader.dto.ReaderOptionDto;
import com.bookstudio.role.dto.RoleOptionDto;

@Data
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SelectOptions {
    private List<BookOptionDto> books;
    private List<AuthorOptionDto> authors;
    private List<PublisherOptionDto> publishers;
    private List<CategoryOptionDto> categories;
    private List<ReaderOptionDto> students;
    private List<RoleOptionDto> roles;
    private List<LanguageOptionDto> languages;
    private List<Genre> genres;
    private List<NationalityOptionDto> nationalities;
    private List<ShelfOptionDto> shelves;
}
