package com.ines.safetynet1.service.dto;

import java.util.List;

public class FireStationCoverageDto {
    private List<PersonInfoDto> residents;
    private int adults;
    private int children;

    public int getAdults() {
        return adults;
    }

    public void setAdults(int adults) {
        this.adults = adults;
    }

    public int getChildren() {
        return children;
    }

    public void setChildren(int children) {
        this.children = children;
    }

    public List<PersonInfoDto> getResidents() {
        return residents;
    }

    public void setResidents(List<FireStationPersonDto> residents) {
        this.residents = residents;
    }
}

