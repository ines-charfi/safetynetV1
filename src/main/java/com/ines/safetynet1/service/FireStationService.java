package com.ines.safetynet1.service;

import com.ines.safetynet1.model.FireStation;
import com.ines.safetynet1.repository.FireStationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FireStationService {

    private final FireStationRepository repository;


    public FireStationService(FireStationRepository repository) {
        this.repository = repository;
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
}

