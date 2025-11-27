package com.ines.safetynet1.service;

import com.ines.safetynet1.model.FireStation;
import com.ines.safetynet1.model.MedicalRecord;
import com.ines.safetynet1.model.Person;
import com.ines.safetynet1.repository.DataHandler;
import com.ines.safetynet1.repository.FireStationRepository;
import com.ines.safetynet1.service.dto.FireAddressDto;
import com.ines.safetynet1.service.dto.FireStationCoverageDto;
import com.ines.safetynet1.service.dto.PersonInfoDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FireStationServiceTest {

    private FireStationRepository repository;
    private DataHandler dataHandler;
    private FireStationService service;

    @BeforeEach
    void setUp() {
        repository = mock(FireStationRepository.class);
        dataHandler = mock(DataHandler.class);
        service = new FireStationService(repository, dataHandler);
    }

    @Test
    void getPhonesByStation_returnsPhonesForStation() {
        // firestations in repository
        FireStation fs = new FireStation();
        fs.setAddress("123 A St");
        fs.setStation("1");
        List<FireStation> repoList = new ArrayList<>();
        repoList.add(fs);
        when(repository.findAll()).thenReturn(repoList);

        // dataHandler provides persons
        Person p = new Person();
        p.setFirstName("John"); p.setLastName("Doe");
        p.setAddress("123 A St"); p.setPhone("111-222");
        List<Person> persons = new ArrayList<>();
        persons.add(p);

        // mock Data object
        Object dataMock = mock(Object.class); // placeholder to satisfy getData() type
        // Better: mock actual Data class if available. We'll use Mockito to stub getData().getPersons() via deep stubs.
        DataHandler dhSpy = mock(DataHandler.class, RETURNS_DEEP_STUBS);
        when(dhSpy.getData().getPersons()).thenReturn(persons);
        // use service with repository mock and dhSpy
        service = new FireStationService(repository, dhSpy);

        List<String> phones = service.getPhonesByStation("1");
        assertEquals(1, phones.size());
        assertTrue(phones.contains("111-222"));
    }

    @Test
    void getFireByAddress_returnsDtoWithStationAndResidents() {
        // Prepare data: firestation covers address
        FireStation f = new FireStation();
        f.setAddress("200 B St");
        f.setStation("2");
        List<FireStation> firestations = new ArrayList<>();
        firestations.add(f);

        // person at address
        Person p = new Person();
        p.setFirstName("Anna"); p.setLastName("Lee"); p.setAddress("200 B St"); p.setPhone("999");
        List<Person> persons = new ArrayList<>();
        persons.add(p);

        // medical record
        MedicalRecord mr = new MedicalRecord();
        mr.setFirstName("Anna"); mr.setLastName("Lee"); mr.setBirthdate("01/01/2010");
        List<MedicalRecord> mrs = new ArrayList<>();
        mrs.add(mr);

        DataHandler dh = mock(DataHandler.class, RETURNS_DEEP_STUBS);
        when(dh.getData().getFirestations()).thenReturn(firestations);
        when(dh.getData().getPersons()).thenReturn(persons);
        when(dh.getData().getMedicalrecords()).thenReturn(mrs);

        when(repository.findAll()).thenReturn(new ArrayList<>()); // not used here

        service = new FireStationService(repository, dh);

        FireAddressDto dto = service.getFireByAddress("200 B St");
        assertNotNull(dto);
        assertEquals("2", dto.getStation());
        assertNotNull(dto.getResidents());
        assertEquals(1, dto.getResidents().size());
        PersonInfoDto info = dto.getResidents().get(0);
        assertEquals("Anna", info.getFirstName());
        // age calculation uses 2025 - year(2010) = 15
        assertEquals("15", info.getAge());
    }

    @Test
    void getResidentsByStation_countsAdultsAndChildren() {
        // Firestations with station "3" covers two addresses
        FireStation f1 = new FireStation(); f1.setAddress("A1"); f1.setStation("3");
        FireStation f2 = new FireStation(); f2.setAddress("A2"); f2.setStation("3");
        List<FireStation> firestations = new ArrayList<>();
        firestations.add(f1); firestations.add(f2);

        // persons on those addresses
        Person child = new Person(); child.setFirstName("Kid"); child.setLastName("Young"); child.setAddress("A1"); child.setPhone("p1");
        Person adult = new Person(); adult.setFirstName("Adult"); adult.setLastName("Old"); adult.setAddress("A2"); adult.setPhone("p2");
        List<Person> persons = new ArrayList<>();
        persons.add(child); persons.add(adult);

        // medical records: child birth 2015 (age 10), adult birth 1980 (age 45)
        MedicalRecord mrChild = new MedicalRecord(); mrChild.setFirstName("Kid"); mrChild.setLastName("Young"); mrChild.setBirthdate("01/01/2015");
        MedicalRecord mrAdult = new MedicalRecord(); mrAdult.setFirstName("Adult"); mrAdult.setLastName("Old"); mrAdult.setBirthdate("01/01/1980");
        List<MedicalRecord> mrs = new ArrayList<>(); mrs.add(mrChild); mrs.add(mrAdult);

        DataHandler dh = mock(DataHandler.class, RETURNS_DEEP_STUBS);
        when(dh.getData().getFirestations()).thenReturn(firestations);
        when(dh.getData().getPersons()).thenReturn(persons);
        when(dh.getData().getMedicalrecords()).thenReturn(mrs);

        when(repository.findAll()).thenReturn(new ArrayList<>());

        service = new FireStationService(repository, dh);

        FireStationCoverageDto coverage = service.getResidentsByStation(3);
        assertNotNull(coverage);
        assertEquals(2, coverage.getResidents().size());
        assertEquals(1, coverage.getChildren());
        assertEquals(1, coverage.getAdults());
    }
}

