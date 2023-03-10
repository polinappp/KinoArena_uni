package com.kinoarena.kinoarena.model.repositories;

import com.kinoarena.kinoarena.model.entities.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectionRepository extends JpaRepository<Projection, Integer> {

    Optional<Projection> findFirstByDateAndStartTimeAndHall(LocalDate date, LocalTime time, Hall hall);

    Optional<Projection> findFirstByProjectionTypeAndMovieAndHallAndStartTimeAndDateAndBasePrice
            (ProjectionType type, Movie movie, Hall hall, LocalTime start, LocalDate date, double price);

    List<Projection> findAllByHallAndDate(Hall hall, LocalDate date);

    List<Projection> findAllByMovieId(Integer movieId);
    List<Projection> findFirstByDateAndMovieAndHallCinema(LocalDate date, Movie movie, Cinema cinema);
}
