package com.bookstudio.book.dto;

import java.time.LocalDate;
import java.util.List;

import com.bookstudio.author.dto.AuthorDto;
import com.bookstudio.category.dto.CategoryDto;
import com.bookstudio.genre.dto.GenreDto;
import com.bookstudio.language.dto.LanguageDto;
import com.bookstudio.publisher.dto.PublisherDto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookDto {
    private Long id;
    private String title;
    private String isbn;
    private LanguageDto language;
    private String edition;
    private Integer pages;
    private String description;
    private String coverUrl;
    private PublisherDto publisher;
    private CategoryDto category;
    private LocalDate releaseDate;
    private String status;
    private List<AuthorDto> authors;
    private List<GenreDto> genres;
}
