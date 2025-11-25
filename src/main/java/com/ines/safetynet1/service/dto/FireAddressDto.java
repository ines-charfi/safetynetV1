package com.ines.safetynet1.service.dto;

import java.util.List;

public class FireAddressDto {
    private String station; // numéro de la caserne
    private List<PersonInfoDto> residents; // liste des habitants avec infos

    // Getters et Setters
    public String getStation() { return station; }
    public void setStation(String station) { this.station = station; }

    public List<PersonInfoDto> getResidents() { return residents; }
    public void setResidents(List<PersonInfoDto> residents) { this.residents = residents; }
}
