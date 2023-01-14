package com.kinoarena.kinoarena.model.DTOs.movie;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.kinoarena.kinoarena.model.DTOs.genre.GenreWithoutMoviesDTO;
import com.kinoarena.kinoarena.model.DTOs.projection.ProjectionWithoutHallDTO;
import com.kinoarena.kinoarena.model.entities.AgeRestriction;
import com.kinoarena.kinoarena.model.entities.Media;
import com.kinoarena.kinoarena.model.entities.Rating;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
public class MovieInfoDTO {
    private int id;
    private String name;
    private String description;
    private AgeRestriction ageRestriction;
    private int duration;
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonFormat(pattern = "dd/MM/yyyy", shape = JsonFormat.Shape.STRING)
    private LocalDate premiere;
    private String director;
    private String actors;
    private List<GenreWithoutMoviesDTO> genres;
    private List<ProjectionWithoutHallDTO> projections;
    private String writer;
    private String country;
    private String awards;
    private int metascore;
    private int imbdRating;
    private int imbdVotes;
    private String dvd;
    private int boxOffice;
    private String production;
    private String webstring;
    private boolean response;
    private Rating rating;
    private List<Media> images;
}
