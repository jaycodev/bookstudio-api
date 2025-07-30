package com.bookstudio.publisher.relation;

import java.io.Serializable;

import jakarta.persistence.*;
import lombok.*;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PublisherGenreId implements Serializable {
    private Long publisherId;
    private Long genreId;
}