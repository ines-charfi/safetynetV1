package com.ines.safetynet1.model;


import com.ines.safetynet1.model.FireStation;
import com.ines.safetynet1.model.MedicalRecord;
import com.ines.safetynet1.model.Person;
import org.springframework.stereotype.Component;

import java.util.*;


import java.util.ArrayList;
import java.util.List;
@lombok.Data
@Component
public class Data {

    private List<Person> persons = new ArrayList<>();
    private List<FireStation> firestations = new ArrayList<>();
    private List<MedicalRecord> medicalrecords = new ArrayList<>();



}
