package com.ines.safetynet1.service;

import com.ines.safetynet1.model.MedicalRecord;
import com.ines.safetynet1.model.Person;
import com.ines.safetynet1.model.FireStation;
import com.ines.safetynet1.repository.DataHandler;
import com.ines.safetynet1.repository.PersonRepository;
import com.ines.safetynet1.service.dto.ChildAlertDto;
import com.ines.safetynet1.service.dto.FloodStationDto;
import com.ines.safetynet1.service.dto.PersonInfoDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PersonServiceTest {

    private PersonRepository personRepository;
    private DataHandler dataHandler;
    private PersonService personService;

    @BeforeEach
    void setUp() {
        personRepository = mock(PersonRepository.class);
        dataHandler = mock(DataHandler.class, RETURNS_DEEP_STUBS);
        personService = new PersonService(personRepository, dataHandler);
    }

    // Test getPerson(String, String)
    @Test
    void testGetPerson_existing() {
        Person p = new Person();
        p.setFirstName("John");
        p.setLastName("Doe");
        when(personRepository.findByName("John", "Doe")).thenReturn(java.util.Optional.of(p));

        Person result = personService.getPerson("John", "Doe");
        assertNotNull(result);
        assertEquals("John", result.getFirstName());
    }

    @Test
    void testGetPerson_nonExisting() {
        when(personRepository.findByName("Unknown", "Person")).thenReturn(java.util.Optional.empty());

        Person result = personService.getPerson("Unknown", "Person");
        assertNull(result);
    }

    // Test getChildrenByAddress(String)
    @Test
    void testGetChildrenByAddress() {
        Person child = new Person();
        child.setFirstName("Kid");
        child.setLastName("Young");
        child.setAddress("123 Street");

        Person adult = new Person();
        adult.setFirstName("Adult");
        adult.setLastName("Old");
        adult.setAddress("123 Street");

        MedicalRecord mrChild = new MedicalRecord();
        mrChild.setFirstName("Kid"); mrChild.setLastName("Young"); mrChild.setBirthdate("01/01/2015"); // age 10
        MedicalRecord mrAdult = new MedicalRecord();
        mrAdult.setFirstName("Adult"); mrAdult.setLastName("Old"); mrAdult.setBirthdate("01/01/1980"); // age 45

        when(dataHandler.getData().getPersons()).thenReturn(List.of(child, adult));
        when(dataHandler.getData().getMedicalrecords()).thenReturn(List.of(mrChild, mrAdult));

        List<ChildAlertDto> children = personService.getChildrenByAddress("123 Street");
        assertEquals(1, children.size());
        assertEquals("Kid", children.get(0).getFirstName());
        assertTrue(children.get(0).getOtherHouseholdMembers().contains("Adult Old"));
    }

    // Test getFloodByStations(List)
    @Test
    void testGetFloodByStations() {
        FireStation fs = new FireStation();
        fs.setStation("1");
        fs.setAddress("Addr1");

        Person p = new Person();
        p.setFirstName("John"); p.setLastName("Doe"); p.setAddress("Addr1"); p.setPhone("123");
        MedicalRecord mr = new MedicalRecord();
        mr.setFirstName("John"); mr.setLastName("Doe"); mr.setBirthdate("01/01/2000");

        when(dataHandler.getData().getFirestations()).thenReturn(List.of(fs));
        when(dataHandler.getData().getPersons()).thenReturn(List.of(p));
        when(dataHandler.getData().getMedicalrecords()).thenReturn(List.of(mr));

        List<FloodStationDto> result = personService.getFloodByStations(List.of(1));
        assertEquals(1, result.size());
        assertEquals("Addr1", result.get(0).getAddress());
        assertEquals(1, result.get(0).getResidents().size());
        assertEquals("John", result.get(0).getResidents().get(0).getFirstName());
    }

    // Test toPersonInfoDto(Person) indirectement via getPersonInfo
    @Test
    void testToPersonInfoDto_indirect() {
        Person p = new Person();
        p.setFirstName("Alice"); p.setLastName("Smith"); p.setAddress("AddrX"); p.setPhone("555");

        MedicalRecord mr = new MedicalRecord();
        mr.setFirstName("Alice"); mr.setLastName("Smith"); mr.setBirthdate("01/01/2010");

        when(personRepository.findAll()).thenReturn(List.of(p));
        when(dataHandler.getData().getMedicalrecords()).thenReturn(List.of(mr));

        List<PersonInfoDto> info = personService.getPersonInfo("Alice", "Smith");
        assertEquals(1, info.size());
        PersonInfoDto dto = info.get(0);
        assertEquals("Alice", dto.getFirstName());
        assertEquals("15", dto.getAge()); // 2025 - 2010
    }
}
