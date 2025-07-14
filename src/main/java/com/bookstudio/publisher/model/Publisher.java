package com.bookstudio.publisher.model;

import com.bookstudio.shared.enums.Status;
import com.bookstudio.shared.model.LiteraryGenre;
import com.bookstudio.shared.model.Nationality;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Publishers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Publisher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PublisherID")
    private Long id;

    @Column(name = "Name", nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "NationalityID", nullable = false)
    private Nationality nationality;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "LiteraryGenreID", nullable = false)
    private LiteraryGenre literaryGenre;

    @Column(name = "FoundationYear", nullable = false)
    private Integer foundationYear;

    @Column(name = "Website")
    private String website;

    @Column(name = "Address")
    private String address;

    @Enumerated(EnumType.STRING)
    @Column(name = "Status", columnDefinition = "ENUM('activo','inactivo')")
    private Status status;

    @Column(name = "PhotoUrl", length = 512)
    private String photoUrl;

    @Transient
    private String photoBase64;
}
