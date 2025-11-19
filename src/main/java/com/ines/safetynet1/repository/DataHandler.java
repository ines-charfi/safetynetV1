package com.ines.safetynet1.repository;
import com.ines.safetynet1.model.Data;
import com.jsoniter.JsonIterator;
import com.jsoniter.output.JsonStream;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import com.ines.safetynet1.model.Data;

import static java.nio.file.Files.write;
@lombok.Data
@Component
public class DataHandler {

    @Autowired
    private Data data;

    @PostConstruct
    public void init() throws IOException {
        String temp = getFromResource("data.json");

        Data loaded = JsonIterator.deserialize(temp, Data.class);

        // On recopie les données dans le bean Spring existant
        data.setPersons(loaded.getPersons());
        data.setFirestations(loaded.getFirestations());
        data.setMedicalrecords(loaded.getMedicalrecords());
    }

    private String getFromResource(String s) throws IOException {
        InputStream is = new ClassPathResource(s).getInputStream();
        return IOUtils.toString(is, StandardCharsets.UTF_8);
    }
}