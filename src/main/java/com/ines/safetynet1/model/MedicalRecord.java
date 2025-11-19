package com.ines.safetynet1.model;

import lombok.Data;


@Data
public class MedicalRecord {

    private String firstName;
    private String lastName;
    private String[] medications;
    private String[] allergies;
    private String birthdate;
}

