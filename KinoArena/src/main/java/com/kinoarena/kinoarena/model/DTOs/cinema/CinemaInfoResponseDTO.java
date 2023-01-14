package com.kinoarena.kinoarena.model.DTOs.cinema;

import com.kinoarena.kinoarena.model.DTOs.city.CityInfoResponseDTO;
import com.kinoarena.kinoarena.model.entities.Media;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CinemaInfoResponseDTO {
    private int id;
    private String name;
    private String address;
    private CityInfoResponseDTO city;
    private String contacts;
    private String workHours;
    private int hallsNumber;
    private List<Media> images;
}
