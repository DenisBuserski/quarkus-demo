package com.demo.repository;

import com.demo.model.Film;
import com.demo.model.Film$;
import com.speedment.jpastreamer.application.JPAStreamer;
import com.speedment.jpastreamer.projection.Projection;
import com.speedment.jpastreamer.streamconfiguration.StreamConfiguration;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.stream.Stream;

@ApplicationScoped
public class FilmRepository {

    @Inject
    JPAStreamer jpaStreamer;

    private static final int PAGE_SIZE = 20;

    public Optional<Film> getFilm(short filmId) {
        return jpaStreamer.stream(Film.class)
                .filter(Film$.id.equal(filmId))
                .findFirst();
    }

    public Stream<Film> paged(long page, short minLength) {
        return jpaStreamer.stream(Projection.select(Film$.id, Film$.title, Film$.length))
                .filter(Film$.length.greaterThan(minLength))
                .sorted(Film$.length)
                .skip(page * PAGE_SIZE)
                // If you're on page 0 with PAGE_SIZE = 10, the method will return the first 10 films with a length > minLength, sorted by their length.
                // If you're on page 1, it will skip the first 10 films and return the next 10 films that meet the filtering condition.
                .limit(PAGE_SIZE);
    }

    public Stream<Film> actors(String startsWith, short minLength) {
        final StreamConfiguration<Film> joining = StreamConfiguration.of(Film.class).joining(Film$.actors);
        return jpaStreamer.stream(joining)
                .filter(Film$.title.startsWith(startsWith)
                        .and(Film$.length.greaterThan(minLength)))
                .sorted(Film$.length.reversed());

    }

    @Transactional
    public void updateRentalRate(short minLength, BigDecimal rentalRate) {
        jpaStreamer.stream(Film.class)
                .filter(Film$.length.greaterThan(minLength))
                .forEach(film -> film.setRentalRate(rentalRate));
    }
}
