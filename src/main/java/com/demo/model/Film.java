package com.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "film", schema = "sakila")
@Getter
@Setter
@NoArgsConstructor
public class Film {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "film_id", nullable = false)
    private Short id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", columnDefinition = "TINYTEXT")
    private String description;

    @Column(name = "release_year", columnDefinition = "YEAR")
    private Integer releaseYear;

    @ColumnDefault("3")
    @Column(name = "rental_duration", nullable = false)
    private Short rentalDuration;

    @ColumnDefault("4.99")
    @Column(name = "rental_rate", nullable = false, precision = 4, scale = 2)
    private BigDecimal rentalRate;

    @Column(name = "length")
    private Short length;

    @ColumnDefault("19.99")
    @Column(name = "replacement_cost", nullable = false, precision = 5, scale = 2)
    private BigDecimal replacementCost;

    @ColumnDefault("'G'")
    @Lob
    @Column(name = "rating", columnDefinition = "enum('G', 'PG', 'PG-13', 'R', 'NC-17')")
    private String rating;

    @Lob
    @Column(name = "special_features", columnDefinition = "set('Trailers', 'Commentaries', 'Deleted Scenes', 'Behind the Scenes')")
    private String specialFeatures;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "last_update", nullable = false)
    private Instant lastUpdate;

    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(name = "film_actor",
            joinColumns = { @JoinColumn(name = "film_id") },
            inverseJoinColumns = { @JoinColumn(name = "actor_id") }
    )
    private List<Actor> actors = new ArrayList<>();

    public Film(short filmId, String title, short length) {
        this.id = filmId;
        this.title = title;
        this.length = length;
    }



}