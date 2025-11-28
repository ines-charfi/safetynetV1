package com.ines.safetynet1.service;

import com.ines.safetynet1.model.FireStation;
import com.ines.safetynet1.model.MedicalRecord;
import com.ines.safetynet1.model.Person;
import com.ines.safetynet1.repository.DataHandler;
import com.ines.safetynet1.repository.FireStationRepository;
import com.ines.safetynet1.service.dto.FireAddressDto;
import com.ines.safetynet1.service.dto.FireStationCoverageDto;
import com.ines.safetynet1.service.dto.PersonInfoDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FireStationService {
@Autowired
    private final FireStationRepository repository;
    private final DataHandler dataHandler;

    public FireStationService(FireStationRepository repository, DataHandler dataHandler) {
        this.repository = repository;
        this.dataHandler = dataHandler;
    }

    // CRUD FireStation

    public List<FireStation> getAllFireStations() {
        return repository.findAll();
    }

    public FireStation createFireStation(FireStation fireStation) {
        return repository.save(fireStation);
    }

    public FireStation updateFireStation(String address, FireStation fireStation) {
        return repository.update(address, fireStation);
    }

    public boolean deleteFireStation(String address, String station) {
        return repository.delete(address, station);
    }

    //  PhoneAlert
    public List<String> getPhonesByStation(String stationNumber) {
        List<String> addresses = repository.findAll().stream()
                .filter(fs -> fs.getStation().equals(stationNumber))
                .map(FireStation::getAddress)
                .toList();

        return dataHandler.getData().getPersons().stream()
                .filter(p -> addresses.contains(p.getAddress()))
                .map(Person::getPhone)
                .distinct()
                .toList();
    }

    //  Fire by Address
    public FireAddressDto getFireByAddress(String address) {
        String stationNumber = dataHandler.getData().getFirestations().stream()
                .filter(f -> f.getAddress().equals(address))
                .map(FireStation::getStation)
                .findFirst()
                .orElse("Inconnu");

        List<PersonInfoDto> residents = dataHandler.getData().getPersons().stream()
                .filter(p -> p.getAddress().equals(address))
                .map(this::toPersonInfoDto)
                .collect(Collectors.toList());

        FireAddressDto fireDto = new FireAddressDto();
        fireDto.setStation(stationNumber);
        fireDto.setResidents(residents);

        return fireDto;
    }

    // FireStation Coverage
    public FireStationCoverageDto getResidentsByStation(int stationNumber) {

        // Trouver toutes les adresses couvertes par cette station
        List<String> addresses = new ArrayList<>();
        for (FireStation fs : dataHandler.getData().getFirestations()) {
            if (Integer.parseInt(fs.getStation()) == stationNumber) {
                addresses.add(fs.getAddress());
            }
        }

        //  Trouver les habitants de ces adresses
        List<PersonInfoDto> residents = new ArrayList<>();
        int adults = 0;
        int children = 0;

        for (Person p : dataHandler.getData().getPersons()) {
            if (addresses.contains(p.getAddress())) {

                // Créer le DTO simplifié
                PersonInfoDto dto = new PersonInfoDto();
                dto.setFirstName(p.getFirstName());
                dto.setLastName(p.getLastName());
                dto.setAddress(p.getAddress());
                dto.setPhone(p.getPhone());

                // Calcul âge
                MedicalRecord record = dataHandler.getData().getMedicalrecords().stream()
                        .filter(mr -> mr.getFirstName().equalsIgnoreCase(p.getFirstName())
                                && mr.getLastName().equalsIgnoreCase(p.getLastName()))
                        .findFirst()
                        .orElse(null);

                int age = record != null ? ageFromBirthdate(record.getBirthdate()) : 0;
                dto.setAge(String.valueOf(age));

                // Ne pas mettre medications et allergies
                dto.setMedications(null);
                dto.setAllergies(null);

                residents.add(dto);

                if (age > 18) adults++;
                else children++;
            }
        }

        // Construire le DTO final
        FireStationCoverageDto coverageDto = new FireStationCoverageDto();
        coverageDto.setResidents(residents);
        coverageDto.setAdults(adults);
        coverageDto.setChildren(children);

        return coverageDto;
    }


    // Utility
    private PersonInfoDto toPersonInfoDto(Person p) {
        MedicalRecord record = dataHandler.getData().getMedicalrecords().stream()
                .filter(mr -> mr.getFirstName().equalsIgnoreCase(p.getFirstName())
                        && mr.getLastName().equalsIgnoreCase(p.getLastName()))
                .findFirst()
                .orElse(null);

        PersonInfoDto dto = new PersonInfoDto();
        dto.setFirstName(p.getFirstName());
        dto.setLastName(p.getLastName());
        dto.setAddress(p.getAddress());
        dto.setPhone(p.getPhone());
        dto.setAge(record != null ? String.valueOf(ageFromBirthdate(record.getBirthdate())) : "0");
        dto.setMedications(record != null ? record.getMedications() : new String[]{});
        dto.setAllergies(record != null ? record.getAllergies() : new String[]{});
        return dto;
    }

    private int ageFromBirthdate(String birthdate) {
        try {
            int year = Integer.parseInt(birthdate.split("/")[2]);
            return 2025 - year;
        } catch (Exception e) {
            return 0;
        }
    }
}
/*// Fire by Address - version simple
public FireAddressDto getFireByAddress(String address) {
    String stationNumber = "Inconnu";

    // Chercher la station qui couvre l'adresse
    for (FireStation fs : dataHandler.getData().getFirestations()) {
        if (fs.getAddress().equalsIgnoreCase(address)) {
            stationNumber = fs.getStation();
            break;
        }
    }

    // Chercher les habitants de cette adresse
    List<PersonInfoDto> residents = new ArrayList<>();
    for (Person p : dataHandler.getData().getPersons()) {
        if (p.getAddress().equalsIgnoreCase(address)) {
            residents.add(toPersonInfoDto(p));
        }
    }

    // Créer le DTO à retourner
    FireAddressDto fireDto = new FireAddressDto();
    fireDto.setStation(stationNumber);
    fireDto.setResidents(residents);

    return fireDto;
}

// FireStation Coverage - version simple
public FireStationCoverageDto getResidentsByStation(int stationNumber) {
    // 1. Trouver toutes les adresses couvertes par cette station
    List<String> addresses = new ArrayList<>();
    for (FireStation fs : dataHandler.getData().getFirestations()) {
        if (Integer.parseInt(fs.getStation()) == stationNumber) {
            addresses.add(fs.getAddress());
        }
    }

    // 2. Trouver les habitants de ces adresses
    List<PersonInfoDto> residents = new ArrayList<>();
    for (Person p : dataHandler.getData().getPersons()) {
        if (addresses.contains(p.getAddress())) {
            residents.add(toPersonInfoDto(p));
        }
    }

    // 3. Compter enfants et adultes
    int adults = 0;
    int children = 0;
    for (PersonInfoDto info : residents) {
        int age = Integer.parseInt(info.getAge());
        if (age > 18) adults++;
        else children++;
    }

    // 4. Construire le DTO à retourner
    FireStationCoverageDto dto = new FireStationCoverageDto();
    dto.setResidents(residents);
    dto.setAdults(adults);
    dto.setChildren(children);

    return dto;
}
*/
