package com.ines.safetynet1.controller;

import com.ines.safetynet1.model.FireStation;
import com.ines.safetynet1.service.FireStationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/firestation")
public class FireStationController {

    private final FireStationService service;

    public FireStationController(FireStationService service) {
        this.service = service;
    }

    @GetMapping("/all")
    public List<FireStation> getAllFireStations() {
        return service.getAllFireStations();
    }

    @PostMapping
    public FireStation addFireStation(@RequestBody FireStation fireStation) {
        return service.createFireStation(fireStation);
    }

    @PutMapping
    public FireStation updateFireStation(@RequestParam String address,
                                         @RequestBody FireStation fireStation) {
        FireStation updated = service.updateFireStation(address, fireStation);
        if (updated == null) throw new RuntimeException("FireStation not found!");
        return updated;
    }

    @DeleteMapping
    public String deleteFireStation(@RequestParam String address,
                                    @RequestParam String station) {
        boolean deleted = service.deleteFireStation(address, station);
        return deleted ? "FireStation supprimée !" : "FireStation introuvable !";
    }
}

