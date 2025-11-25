package com.ines.safetynet1.service.dto;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL) // ignore les champs null
public class FireStationCoverageDto {
    private List<PersonInfoDto> residents;
    private int adults;
    private int children;
    private String phone;

    public int getAdults() {
        return adults;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public void setResidents(List<PersonInfoDto> residents) {
        this.residents = residents;
    }
}

