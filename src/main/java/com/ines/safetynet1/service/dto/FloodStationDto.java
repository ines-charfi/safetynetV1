package com.ines.safetynet1.service.dto;

import lombok.Data;

import java.util.List;
@Data
public class FloodStationDto {
    private String address;
    private List<PersonInfoDto> residents;


}
