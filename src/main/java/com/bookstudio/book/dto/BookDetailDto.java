package com.bookstudio.book.dto;

import java.time.LocalDate;
import java.util.List;

import com.bookstudio.author.dto.AuthorSummaryDto;
import com.bookstudio.category.dto.CategorySummaryDto;
import com.bookstudio.genre.dto.GenreSummaryDto;
import com.bookstudio.language.dto.LanguageSummaryDto;
import com.bookstudio.publisher.dto.PublisherSummaryDto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookDetailDto {
    private Long id;
    private String title;
    private String isbn;
    private LanguageSummaryDto language;
    private String edition;
    private Integer pages;
    private String description;
    private String coverUrl;
    private PublisherSummaryDto publisher;
    private CategorySummaryDto category;
    private LocalDate releaseDate;
    private String status;
    private List<AuthorSummaryDto> authors;
    private List<GenreSummaryDto> genres;
}
