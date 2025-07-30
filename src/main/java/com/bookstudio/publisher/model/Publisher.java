package com.bookstudio.publisher.model;

import com.bookstudio.shared.enums.Status;
import com.bookstudio.shared.model.Nationality;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "publishers", schema = "bookstudio_db")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Publisher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long publisherId;

    @Column(nullable = false, unique = true)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nationality_id", nullable = false)
    private Nationality nationality;

    @Column(name = "foundation_year", nullable = false)
    private Integer foundationYear;

    private String website;

    private String address;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(length = 512)
    private String photoUrl;
}
