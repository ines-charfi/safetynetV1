package com.ines.safetynet1.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ines.safetynet1.model.MedicalRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class MedicalRecordControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private MedicalRecord testRecord;
    private ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setup() throws Exception {
        testRecord = new MedicalRecord();
        testRecord.setFirstName("John");
        testRecord.setLastName("Doe");
        testRecord.setBirthdate("01/01/2000");
        testRecord.setMedications(List.of("med1").toArray(new String[0]));
        testRecord.setAllergies(List.of("allergie1").toArray(new String[0]));

        mockMvc.perform(post("/medicalRecord")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(testRecord)))
                .andExpect(status().isOk());
    }

    @Test
    void testCreateMedicalRecord() throws Exception {
        mockMvc.perform(post("/medicalRecord")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(testRecord)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));
    }

    @Test
    void testGetAllMedicalRecords() throws Exception {
        mockMvc.perform(get("/medicalRecord/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testUpdateMedicalRecord() throws Exception {
        MedicalRecord updated = new MedicalRecord();
        updated.setFirstName("John");
        updated.setLastName("Doe");
        updated.setBirthdate("01/01/1999");
        updated.setMedications(List.of("newMed").toArray(new String[0]));
        updated.setAllergies(List.of("newAllergy").toArray(new String[0]));

        mockMvc.perform(put("/medicalRecord")
                        .param("firstName", "John")
                        .param("lastName", "Doe")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.birthdate").value("01/01/1999"));
    }

    @Test
    void testDeleteMedicalRecord() throws Exception {
        mockMvc.perform(delete("/medicalRecord")
                        .param("firstName", "John")
                        .param("lastName", "Doe"))
                .andExpect(status().isOk())
                .andExpect(content().string("MedicalRecord supprimé !"));
    }
}

