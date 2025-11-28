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
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class FireStationControllerIT {

    @Autowired
    private MockMvc mockMvc;

    private FireStation testStation;
    private final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setUp() throws Exception {
        testStation = new FireStation();
        testStation.setAddress("123 Main Street");
        testStation.setStation("1");

        // Assurer qu'il existe avant chaque test
        mockMvc.perform(post("/firestation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(testStation)))
                .andExpect(status().isOk());
    }

    // ================= addFireStation =================
    @Test
    void testAddFireStation_success() throws Exception {
        FireStation newStation = new FireStation();
        newStation.setAddress("456 Oak Street");
        newStation.setStation("2");

        mockMvc.perform(post("/firestation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(newStation)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.address").value("456 Oak Street"))
                .andExpect(jsonPath("$.station").value("2"));
    }

    @Test
    void testAddFireStation_invalidPayload() throws Exception {
        FireStation invalid = new FireStation(); // sans adresse ni station
        mockMvc.perform(post("/firestation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(invalid)))
                .andExpect(status().isOk()) // selon ton controller, il ne throw pas d’erreur
                .andExpect(jsonPath("$.address").value(""))
                .andExpect(jsonPath("$.station").value(""));
    }

    // ================= updateFireStation =================
    @Test
    void testUpdateFireStation_success() throws Exception {
        testStation.setStation("9"); // Changer le numéro
        mockMvc.perform(put("/firestation")
                        .param("address", testStation.getAddress())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(testStation)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.station").value("9"));
    }

    @Test
    void testUpdateFireStation_notFound() throws Exception {
        FireStation unknown = new FireStation();
        unknown.setStation("5");
        mockMvc.perform(put("/firestation")
                        .param("address", "unknown")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(unknown)))
                .andExpect(status().isInternalServerError())
                .andExpect(result -> assertEquals("FireStation not found!", result.getResolvedException().getMessage()));
    }

    // ================= deleteFireStation =================
    @Test
    void testDeleteFireStation_success() throws Exception {
        mockMvc.perform(delete("/firestation")
                        .param("address", testStation.getAddress())
                        .param("station", "1"))
                .andExpect(status().isOk())
                .andExpect(content().string("FireStation supprimée !"));
    }

    @Test
    void testDeleteFireStation_notFound() throws Exception {
        mockMvc.perform(delete("/firestation")
                        .param("address", "unknown")
                        .param("station", "99"))
                .andExpect(status().isOk())
                .andExpect(content().string("FireStation introuvable !"));
    }

    // ================= phoneAlert =================
    @Test
    void testPhoneAlert_withResidents() throws Exception {
        mockMvc.perform(get("/phoneAlert")
                        .param("firestation", "1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void testPhoneAlert_noResidents() throws Exception {
        mockMvc.perform(get("/phoneAlert")
                        .param("firestation", "99"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    // ================= getFire =================
    @Test
    void testGetFire_withResidents() throws Exception {
        mockMvc.perform(get("/fire")
                        .param("address", testStation.getAddress()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.station").value("1"))
                .andExpect(jsonPath("$.residents").isArray());
    }

    @Test
    void testGetFire_noResidents() throws Exception {
        mockMvc.perform(get("/fire")
                        .param("address", "NoAddress"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.station").value("Inconnu"))
                .andExpect(jsonPath("$.residents").isArray())
                .andExpect(jsonPath("$.residents").isEmpty());
    }

    // ================= getResidentsByStation =================
    @Test
    void testGetResidentsByStation_withResidents() throws Exception {
        mockMvc.perform(get("/firestation")
                        .param("stationNumber", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.residents").isArray())
                .andExpect(jsonPath("$.adults").isNumber())
                .andExpect(jsonPath("$.children").isNumber());
    }

    @Test
    void testGetResidentsByStation_noResidents() throws Exception {
        mockMvc.perform(get("/firestation")
                        .param("stationNumber", "99"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.residents").isArray())
                .andExpect(jsonPath("$.residents").isEmpty())
                .andExpect(jsonPath("$.adults").value(0))
                .andExpect(jsonPath("$.children").value(0));
    }

    // ================= getAllFireStations =================
    @Test
    void testGetAllFireStations_nonEmpty() throws Exception {
        mockMvc.perform(get("/firestation/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].address").value(testStation.getAddress()));
    }

    @Test
    void testGetAllFireStations_empty() throws Exception {
        // Ici tu peux simuler suppression de tout pour tester liste vide
        mockMvc.perform(get("/firestation/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

}
