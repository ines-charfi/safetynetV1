package com.ines.safetynet1.service;

import com.ines.safetynet1.model.FireStation;
import com.ines.safetynet1.model.Person;
import com.ines.safetynet1.repository.DataHandler;
import com.ines.safetynet1.repository.FireStationRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FireStationService {

    private final FireStationRepository repository;
    private final DataHandler dataHandler;


    public FireStationService(FireStationRepository repository, DataHandler dataHandler) {
        this.repository = repository;
        this.dataHandler = dataHandler;
    }

    public List<FireStation> getAllFireStations() {
        return repository.findAll();
    }

    public FireStation getFireStation(String address) {
        return repository.findByAddress(address).orElse(null);
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

    public List<String> getPhonesByStation(String stationNumber) {
        // 1. Récupérer toutes les adresses couvertes par cette station
        List<String> addresses = repository.findAll().stream()
                .filter(fs -> fs.getStation().equals(stationNumber))
                .map(FireStation::getAddress)
                .toList();

        // 2. Récupérer toutes les personnes vivant à ces adresses
        List<String> phones = new ArrayList<>();
        for (Person p : dataHandler.getData().getPersons()) {
            if (addresses.contains(p.getAddress())) {
                phones.add(p.getPhone());
            }
        }

        // Supprimer les doublons et renvoyer la liste
        return phones.stream().distinct().toList();
    }


}






