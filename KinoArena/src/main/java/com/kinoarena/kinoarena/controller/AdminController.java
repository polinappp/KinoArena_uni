//package com.kinoarena.kinoarena.controller;
//
//import com.kinoarena.kinoarena.model.DTOs.cinema.CinemaDeleteRequestDTO;
//import com.kinoarena.kinoarena.model.DTOs.cinema.CinemaRequestDTO;
//import com.kinoarena.kinoarena.model.DTOs.hall.HallEditRequestDTO;
//import com.kinoarena.kinoarena.model.DTOs.hall.HallRequestDTO;
//import com.kinoarena.kinoarena.model.DTOs.hall.HallWithoutSeatsDTO;
//import com.kinoarena.kinoarena.model.DTOs.projection.request.ProjectionEditRequestDTO;
//import com.kinoarena.kinoarena.model.DTOs.projection.request.ProjectionRequestDTO;
//import com.kinoarena.kinoarena.model.DTOs.projection.response.ProjectionResponseDTO;
//import com.kinoarena.kinoarena.model.entities.Cinema;
//import com.kinoarena.kinoarena.model.entities.Hall;
//import com.kinoarena.kinoarena.services.CinemaService;
//import com.kinoarena.kinoarena.services.HallService;
//import com.kinoarena.kinoarena.services.ProjectionService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.web.bind.annotation.*;
//
//
//@RestController
//@RequiredArgsConstructor
//@PreAuthorize("hasRole('ROLE_ADMIN')")
//@RequestMapping(value = "/admin")
//public class AdminController extends AbstractController {
//
//    private final CinemaService cinemaService;
//
//    private final HallService hallService;
//
//    private final ProjectionService projectionService;
//

//    @PostMapping("/cinemas")
//    public Cinema addCinema(@RequestBody CinemaRequestDTO cinema) {
//        return cinemaService.add(cinema);
//    }
//
//    @PutMapping("/cinemas")
//    public Cinema editCinema(@RequestBody CinemaRequestDTO cinema) {
//        return cinemaService.edit(cinema);
//    }

//    @DeleteMapping("/cinemas")
//    public String deleteCinema(@RequestBody CinemaDeleteRequestDTO cinema) {
//        return cinemaService.delete(cinema);
//    }
//
//    @PostMapping("/halls")
//    public HallWithoutSeatsDTO addHall(@RequestBody HallRequestDTO hall) {
//        return hallService.add(hall);
//    }
//
//    @PutMapping("/halls")
//    public HallWithoutSeatsDTO editHall(@RequestBody HallEditRequestDTO hall) {
//        return hallService.edit(hall);
//    }
//
//    @DeleteMapping("/halls")
//    public String deleteHall(@RequestBody HallRequestDTO hall) {
//        return hallService.delete(hall);
//    }

//    @PostMapping("/projections")
//    public ProjectionResponseDTO addProjection(@RequestBody ProjectionRequestDTO projection) {
//        return projectionService.add(projection);
//    }
//
//    @PutMapping("/projections")
//    public ProjectionResponseDTO editProjection(@RequestBody ProjectionEditRequestDTO projection) {
//        return projectionService.edit(projection);
//    }
//
//    @DeleteMapping("/projections")
//    public String deleteProjection(@RequestBody ProjectionRequestDTO projection) {
//        return projectionService.delete(projection);
//    }
//}
