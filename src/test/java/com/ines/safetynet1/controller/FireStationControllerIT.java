package com.ines.safetynet1.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ines.safetynet1.model.FireStation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class FireStationControllerIT {

    @Autowired
    private MockMvc mockMvc;

    private FireStation testStation;


    @BeforeEach
    void setUp() throws Exception {
        testStation = new FireStation();
        testStation.setAddress("123 Main Street");
        testStation.setStation("1");

        ObjectMapper mapper = new ObjectMapper();

        // Créer la firestation avant chaque test
        mockMvc.perform(post("/firestation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(testStation)))
                .andExpect(status().isOk());
    }





    @Test
    void testAddFireStation() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        mockMvc.perform(post("/firestation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(testStation)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.address").value("123 Main Street"))
                .andExpect(jsonPath("$.station").value("1"));
    }

    @Test
    void testGetAllFireStations() throws Exception {
        mockMvc.perform(get("/firestation/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testUpdateFireStation() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        FireStation updatedStation = new FireStation();
        updatedStation.setAddress("123 Main Street");
        updatedStation.setStation("2"); // changer le numéro de station

        mockMvc.perform(put("/firestation")
                        .param("address", "123 Main Street")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(updatedStation)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.station").value("2"));
    }

    @Test
    void testDeleteFireStation() throws Exception {
        mockMvc.perform(delete("/firestation")
                        .param("address", "123 Main Street")
                        .param("station", "2"))
                .andExpect(status().isOk())
                .andExpect(content().string("FireStation supprimée !"));
    }

    @Test
    void testPhoneAlert() throws Exception {
        mockMvc.perform(get("/phoneAlert")
                        .param("firestation", "1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
    @Test
    void testUpdateFireStation_NotFound() throws Exception {
        FireStation updatedStation = new FireStation();
        updatedStation.setAddress("NonExistant Street");
        updatedStation.setStation("99");

        ObjectMapper mapper = new ObjectMapper();

        mockMvc.perform(put("/firestation")
                        .param("address", "NonExistant Street")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(updatedStation)))
                .andExpect(status().isInternalServerError())
                .andExpect(result ->
                        assertEquals("FireStation not found!", result.getResolvedException().getMessage())
                );
    }

    @Test
    void testDeleteFireStation_NotFound() throws Exception {
        mockMvc.perform(delete("/firestation")
                        .param("address", "Fake Street")
                        .param("station", "99"))
                .andExpect(status().isOk())
                .andExpect(content().string("FireStation introuvable !"));
    }
}

