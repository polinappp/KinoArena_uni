package com.kinoarena.kinoarena.controller;

import com.kinoarena.kinoarena.model.DTOs.movie.MovieInfoDTO;
import com.kinoarena.kinoarena.model.entities.Movie;
import com.kinoarena.kinoarena.services.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MovieController extends AbstractController {

    private final MovieService movieService;

    @GetMapping(value = "/movies/{mid}")
    public MovieInfoDTO getMovieInfo(@PathVariable int mid) {
        return movieService.getMovieInfo(mid);
    }

    @GetMapping(value = "/movies")
    public List<MovieInfoDTO> getAllMoviesInfo() {return movieService.getAllMoviesInfo();
    }

}
