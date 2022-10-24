package com.kinoarena.kinoarena.model.DTOs.cinema;

import com.kinoarena.kinoarena.model.DTOs.city.CityInfoDTO;
import com.kinoarena.kinoarena.model.entities.City;
import lombok.Data;

@Data
public class CinemaInfoDTO {
    private int id;
    private String name;
    private String address;
    private CityInfoDTO city;
}
