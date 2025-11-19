package com.ines.safetynet1.controller;

import com.ines.safetynet1.model.FireStation;
import com.ines.safetynet1.service.FireStationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class FireStationController {

    private final FireStationService service;

    public FireStationController(FireStationService service) {
        this.service = service;
    }

    // Récupérer toutes les firestations
    @GetMapping("/firestation/all")
    public List<FireStation> getAllFireStations() {
        return service.getAllFireStations();
    }

    // Créer une firestation
    @PostMapping("/firestation")
    public FireStation addFireStation(@RequestBody FireStation fireStation) {
        return service.createFireStation(fireStation);
    }

    // Mettre à jour une firestation
    @PutMapping("/firestation")
    public FireStation updateFireStation(@RequestParam String address,
                                         @RequestBody FireStation fireStation) {
        FireStation updated = service.updateFireStation(address, fireStation);
        if (updated == null) throw new RuntimeException("FireStation not found!");
        return updated;
    }

    // Supprimer une firestation
    @DeleteMapping("/firestation")
    public String deleteFireStation(@RequestParam String address,
                                    @RequestParam String station) {
        boolean deleted = service.deleteFireStation(address, station);
        return deleted ? "FireStation supprimée !" : "FireStation introuvable !";
    }

    // Récupérer les numéros de téléphone par numéro de station
    @GetMapping("/phoneAlert")
    public List<String> phoneAlert(@RequestParam("firestation") String firestation) {
        return service.getPhonesByStation(firestation);
    }
}
