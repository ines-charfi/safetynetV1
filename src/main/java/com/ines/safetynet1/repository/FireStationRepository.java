package com.ines.safetynet1.repository;

import com.ines.safetynet1.repository.DataHandler;
import com.ines.safetynet1.model.FireStation;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Data
@Repository
public class FireStationRepository {
@Autowired
    private final DataHandler dataHandler;

    public FireStationRepository(DataHandler dataHandler) {
        this.dataHandler = dataHandler;
    }

    public List<FireStation> findAll() {
        return dataHandler.getData().getFirestations();
    }

    public Optional<FireStation> findByAddress(String address) {
        return dataHandler.getData().getFirestations().stream()
                .filter(fs -> fs.getAddress().equals(address))
                .findFirst();
    }

    public FireStation save(FireStation fireStation) {
        dataHandler.getData().getFirestations().add(fireStation);
        return fireStation;
    }

    public FireStation update(String address, FireStation updated) {
        Optional<FireStation> existing = findByAddress(address);
        if (existing.isPresent()) {
            existing.get().setStation(updated.getStation());
            return existing.get();
        }
        return null;
    }

    public boolean delete(String address, String station) {
        return dataHandler.getData().getFirestations().removeIf(fs ->
                fs.getAddress().equals(address) &&
                        fs.getStation().equals(station)
        );
    }
}

