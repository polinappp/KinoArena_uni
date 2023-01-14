package com.kinoarena.kinoarena.model.DTOs.seat;

import com.kinoarena.kinoarena.model.DTOs.hall.HallWithoutSeatsDTO;
import lombok.Data;

@Data
public class SeatWithoutHallDTO {
    private int id;
    private int number;
    private int row;
    private HallWithoutSeatsDTO hall;
}
