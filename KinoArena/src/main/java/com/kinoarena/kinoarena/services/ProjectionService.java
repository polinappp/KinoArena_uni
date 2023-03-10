package com.kinoarena.kinoarena.services;

import com.kinoarena.kinoarena.model.DAOs.ProjectionDAO;
import com.kinoarena.kinoarena.model.DTOs.hall.HallWithoutSeatsDTO;
import com.kinoarena.kinoarena.model.DTOs.movie.MovieInfoDTO;
import com.kinoarena.kinoarena.model.DTOs.projection.request.FilterProjectionsRequestDTO;
import com.kinoarena.kinoarena.model.DTOs.projection.request.ProjectionEditRequestDTO;
import com.kinoarena.kinoarena.model.DTOs.projection.request.ProjectionRequestDTO;
import com.kinoarena.kinoarena.model.DTOs.projection.response.ProjectionInfoDTO;
import com.kinoarena.kinoarena.model.DTOs.projection.response.ProjectionResponseDTO;
import com.kinoarena.kinoarena.model.entities.*;
import com.kinoarena.kinoarena.model.exceptions.BadRequestException;
import com.kinoarena.kinoarena.model.exceptions.NotFoundException;
import com.kinoarena.kinoarena.model.repositories.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.format.number.money.MonetaryAmountFormatter;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectionService {

    private final ProjectionDAO projectionDAO;
    private final ModelMapper modelMapper;
    private final ProjectionRepository projectionRepository;
    private final HallRepository hallRepository;
    private final MovieRepository movieRepository;
    private final ProjectionTypeRepository projectionTypeRepository;
    private final CinemaRepository cinemaRepository;

    public List<ProjectionInfoDTO> filterProjections(FilterProjectionsRequestDTO request) {
        String cinemaName = request.getCinema();
        String movieName = request.getMovie();
        LocalDate date = request.getDate();

        if (cinemaName == null) {
            throw new BadRequestException("Trying to filter without cinema!");
        }
        if (movieName == null) {
            throw new BadRequestException("Trying to filter without movie title!");
        }
        if (date == null) {
            throw new BadRequestException("Trying to filter without projection date!");
        }

        Optional<Movie> movie = movieRepository.findFirstByName(movieName);
        Optional<Cinema> cinema = cinemaRepository.findFirstByName(cinemaName);

        if(movie.isPresent() && cinema.isPresent()) {
            Movie m = movie.get();
            Cinema c = cinema.get();

            List<Projection> projections = projectionRepository.findFirstByDateAndMovieAndHallCinema(date, m, c);
            List<ProjectionInfoDTO> dto = projections.stream()
                    .map(projection -> modelMapper.map(projection, ProjectionInfoDTO.class))
                    .collect(Collectors.toList());

            return dto;
        } else {
            throw new BadRequestException("Wrong input!");
        }
    }

    public List<ProjectionInfoDTO> getBrandProjections(String brand) {
        return projectionDAO.getBrandProjections(brand);
    }

    public List<ProjectionInfoDTO> filterBrandProjectionsByCinema(String brand, String cinema) {
        String[] cinemaNameSplit = cinema.split("-");
        String cinemaName = String.join(" ", cinemaNameSplit);
        return projectionDAO.filterBrandProjections(brand, cinemaName);
    }

    public ProjectionResponseDTO add(ProjectionRequestDTO p) {
        if (cinemaRepository.findFirstByName(p.getCinemaName()).isEmpty()) {
            throw new NotFoundException("Cinema " + p.getCinemaName() + " doesn't exist!");
        }

        ProjectionType type = projectionTypeRepository.findFirstByType(p.getProjectionTypeName())
                .orElseThrow(() -> new NotFoundException("Projection type doesn't exist"));
        Movie movie = movieRepository.findFirstByName(p.getMovieName())
                .orElseThrow(() -> new NotFoundException("Movie doesn't exist"));
        Hall hall = hallRepository.findFirstByCinemaNameAndNumber(p.getCinemaName(), p.getHallNumber())
                .orElseThrow(() -> new NotFoundException("Hall in cinema " + p.getCinemaName() + " doesn't exist!"));

        LocalTime start = p.getStartTime();
        LocalTime end = start.plusMinutes(movie.getDuration());

        checkIfHallIsAvailable(hall, p.getDate(), start, end);

        return getProjectionResponseDTO(p, hall, movie, movie.getDuration(), start, p.getDate(), type);
    }

    private ProjectionResponseDTO getProjectionResponseDTO(ProjectionRequestDTO p, Hall hall, Movie movie, int movieDuration, LocalTime start, LocalDate projectionDate, ProjectionType projectionType) {
        ProjectionResponseDTO projectionResponse =
                buildProjectionResponseDTO(p, hall, movie, start, projectionDate, projectionType);

        Projection projection = modelMapper.map(projectionResponse, Projection.class);

        if (projectionRepository.findFirstByDateAndStartTimeAndHall(projectionDate, start, hall).isEmpty()) {
            projection = projectionRepository.save(projection);
        } else throw new BadRequestException("Projection already exists!");

        projectionResponse.setId(projection.getId());
        return projectionResponse;
    }

    private ProjectionResponseDTO buildProjectionResponseDTO
            (ProjectionRequestDTO p, Hall hall, Movie movie, LocalTime start,
             LocalDate projectionDate, ProjectionType projectionType) {
        return ProjectionResponseDTO
                .builder()
                .startTime(start)
                .date(projectionDate)
                .projectionType(projectionType)
                .movie(modelMapper.map(movie, MovieInfoDTO.class))
                .hall(modelMapper.map(hall, HallWithoutSeatsDTO.class))
                .basePrice(p.getBasePrice())
                .build();
    }

    private void checkIfHallIsAvailable(Hall hall, LocalDate date, LocalTime start, LocalTime end) {
        List<Projection> projections = projectionRepository.findAllByHallAndDate(hall, date);
        projections.forEach(currProjection -> {
            LocalTime currProjectionStart = currProjection.getStartTime();
            int duration = currProjection.getMovie().getDuration();
            LocalTime currProjectionEnd = currProjectionStart.plusMinutes(duration);
            if (hallIsTakenAtTime(start, end, currProjectionStart, currProjectionEnd)) {
                throw new BadRequestException
                        ("Hall is taken from " + currProjectionStart + " to " + currProjectionEnd);
            }
        });
    }

    private boolean hallIsTakenAtTime(LocalTime start, LocalTime end,
                                      LocalTime currProjectionStart, LocalTime currProjectionEnd) {
        boolean startsAfterCurrStart = currProjectionStart.isBefore(start) || currProjectionStart.equals(start);
        boolean startsBeforeCurrEnd = currProjectionEnd.isAfter(start) || currProjectionEnd.equals(start);
        boolean endsBeforeCurrEnd = currProjectionEnd.isAfter(end) || currProjectionEnd.equals(end);
        boolean endsAfterCurrStart = currProjectionStart.isBefore(end) || currProjectionStart.equals(end);
        return (startsAfterCurrStart && startsBeforeCurrEnd) ||
                (endsBeforeCurrEnd && endsAfterCurrStart);
    }

    @Transactional
    public ProjectionResponseDTO edit(ProjectionEditRequestDTO p) {
        if (cinemaRepository.findFirstByName(p.getCinemaName()).isEmpty()) {
            throw new NotFoundException("Cinema " + p.getCinemaName() + " doesn't exist!");
        }
        ProjectionType type = projectionTypeRepository.findFirstByType(p.getProjectionTypeName())
                .orElseThrow(() -> new NotFoundException("Projection type doesn't exist"));
        Movie movie = movieRepository.findFirstByName(p.getMovieName())
                .orElseThrow(() -> new NotFoundException("Movie doesn't exist"));
        Hall hall = hallRepository.findFirstByCinemaNameAndNumber(p.getCinemaName(), p.getHallNumber())
                .orElseThrow(() -> new NotFoundException("Hall in cinema " + p.getCinemaName() + " doesn't exist!"));

        Projection projection = projectionRepository
                .findFirstByProjectionTypeAndMovieAndHallAndStartTimeAndDateAndBasePrice
                        (type, movie, hall, p.getStartTime(), p.getDate(), p.getBasePrice())
                .orElseThrow(() -> new NotFoundException("Projection not found!"));

        projection.setStartTime(p.getNewStartTime());
        projection.setDate(p.getNewDate());
        projection.setBasePrice(p.getNewBasePrice());

        return modelMapper.map(projection, ProjectionResponseDTO.class);
    }

    public String delete(ProjectionRequestDTO p) {
        if (cinemaRepository.findFirstByName(p.getCinemaName()).isEmpty()) {
            throw new NotFoundException("Cinema " + p.getCinemaName() + " doesn't exist!");
        }
        ProjectionType type = projectionTypeRepository.findFirstByType(p.getProjectionTypeName())
                .orElseThrow(() -> new NotFoundException("Projection type doesn't exist"));
        Movie movie = movieRepository.findFirstByName(p.getMovieName())
                .orElseThrow(() -> new NotFoundException("Movie doesn't exist"));
        Hall hall = hallRepository.findFirstByCinemaNameAndNumber(p.getCinemaName(), p.getHallNumber())
                .orElseThrow(() -> new NotFoundException("Hall in cinema " + p.getCinemaName() + " doesn't exist!"));

        Projection projection = projectionRepository
                .findFirstByProjectionTypeAndMovieAndHallAndStartTimeAndDateAndBasePrice
                        (type, movie, hall, p.getStartTime(), p.getDate(), p.getBasePrice())
                .orElseThrow(() -> new NotFoundException("Projection not found!"));

        projectionRepository.delete(projection);
        return "Projection deleted successfully!";
    }
}
