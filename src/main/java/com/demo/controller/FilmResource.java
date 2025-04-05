package com.demo.controller;

import com.demo.model.Actor;
import com.demo.model.Film;
import com.demo.repository.FilmRepository;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.stream.Collectors;

@Path("/")
public class FilmResource {
    @Inject
    FilmRepository filmRepository;

    @GET
    @Path("/hello-world")
    @Produces(MediaType.TEXT_PLAIN)
    public String helloWorld() {
        return "Hello World!";
    }

    @GET
    @Path("/film/{filmId}")
    @Produces(MediaType.TEXT_PLAIN)
    public String getFilm(short filmId) {
        Optional<Film> film = filmRepository.getFilm(filmId);
        return film.isPresent() ? film.get().getTitle() : "No film was found!";
    }

    @GET
    @Path("/paged-films/{page}/{minLength}")
    @Produces(MediaType.TEXT_PLAIN)
    public String pagedFilms(long page, short minLength) {
        return filmRepository.paged(page, minLength)
                .map(film -> String.format("%s (%d min)", film.getTitle(), film.getLength()))
                .collect(Collectors.joining("\n"));
    }

    @GET
    @Path("/actors/{startsWith}/{minLength}")
    @Produces(MediaType.TEXT_PLAIN)
    public String actors(String startsWith, short minLength) {
        return filmRepository.actors(startsWith, minLength)
                .map(film -> String.format("%s (%d min): %s",
                        film.getTitle(),
                        film.getLength(),
                        film.getActors()
                                .stream()
                                .map(FilmResource::formatActorName)
                                .collect(Collectors.joining(", "))))
                .collect(Collectors.joining("\n"));
    }

    public static String formatActorName(Actor actor) {
        return String.format("%s %s", actor.getFirstName(), actor.getLastName());
    }

    @PUT
    @Path("/update/{minLength}/{rentalRate}")
    @Produces(MediaType.TEXT_PLAIN)
    public void updateRentalRate(short minLength, BigDecimal rentalRate) {
        filmRepository.updateRentalRate(minLength, rentalRate);
    }
}
