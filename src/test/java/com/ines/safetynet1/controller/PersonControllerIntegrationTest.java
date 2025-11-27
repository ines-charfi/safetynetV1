package com.ines.safetynet1.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ines.safetynet1.model.FireStation;
import com.ines.safetynet1.model.MedicalRecord;
import com.ines.safetynet1.model.Person;
import com.ines.safetynet1.repository.DataHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class PersonControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DataHandler dataHandler;

    private final ObjectMapper mapper = new ObjectMapper();

    private Person testPerson;

    @BeforeEach
    void setUp() {
        // Clear previous data
        dataHandler.getData().getPersons().clear();
        dataHandler.getData().getFirestations().clear();
        dataHandler.getData().getMedicalrecords().clear();

        // Create a test person
        testPerson = new Person();
        testPerson.setFirstName("John");
        testPerson.setLastName("Doe");
        testPerson.setAddress("123 Main Street");
        testPerson.setCity("Cityville");
        testPerson.setZip("12345");
        testPerson.setPhone("123-456-7890");
        testPerson.setEmail("john.doe@example.com");

        dataHandler.getData().getPersons().add(testPerson);

        // Optional: Add corresponding FireStation
        FireStation fireStation = new FireStation();
        fireStation.setAddress("123 Main Street");
        fireStation.setStation("1");
        dataHandler.getData().getFirestations().add(fireStation);

        // Optional: Add corresponding MedicalRecord
        MedicalRecord record = new MedicalRecord();
        record.setFirstName("John");
        record.setLastName("Doe");
        record.setBirthdate("01/01/2000");
        record.setMedications(new String[]{"med1"});
        record.setAllergies(new String[]{"allergy1"});
        dataHandler.getData().getMedicalrecords().add(record);
    }

    @Test
    void testGetAllPersons() throws Exception {
        mockMvc.perform(get("/personInfo")
                        .param("firstName", "John")
                        .param("lastName", "Doe"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[0].lastName").value("Doe"));

    }

    @Test
    void testAddPerson() throws Exception {
        Person newPerson = new Person();
        newPerson.setFirstName("Alice");
        newPerson.setLastName("Smith");
        newPerson.setAddress("456 Elm Street");
        newPerson.setCity("Townsville");
        newPerson.setZip("67890");
        newPerson.setPhone("987-654-3210");
        newPerson.setEmail("alice.smith@example.com");

        mockMvc.perform(post("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(newPerson)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Alice"))
                .andExpect(jsonPath("$.lastName").value("Smith"));
    }

    @Test
    void testUpdatePerson() throws Exception {
        testPerson.setPhone("111-222-3333");

        mockMvc.perform(put("/person")
                        .param("firstName", "John")
                        .param("lastName", "Doe")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(testPerson)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.phone").value("111-222-3333"));
    }

    @Test
    void testDeletePerson() throws Exception {
        mockMvc.perform(delete("/person")
                        .param("firstName", "John")
                        .param("lastName", "Doe"))
                .andExpect(status().isOk())
                .andExpect(content().string("Person supprimée !"));
    }

    @Test
    void testPersonInfo() throws Exception {
        mockMvc.perform(get("/personInfo")
                        .param("firstName", "John")
                        .param("lastName", "Doe"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[0].lastName").value("Doe"))
                .andExpect(jsonPath("$[0].address").value("123 Main Street"))
                .andExpect(jsonPath("$[0].age").value("25"))
                .andExpect(jsonPath("$[0].medications[0]").value("med1"))
                .andExpect(jsonPath("$[0].allergies[0]").value("allergy1"));

    }
}

