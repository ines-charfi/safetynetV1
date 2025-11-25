package com.ines.safetynet1.controller;

import com.ines.safetynet1.model.FireStation;
import com.ines.safetynet1.service.FireStationService;
import com.ines.safetynet1.service.dto.FireAddressDto;
import com.ines.safetynet1.service.dto.FireStationCoverageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class FireStationController {
@Autowired

    private final FireStationService fireStationService;

    public FireStationController(FireStationService service, FireStationService fireStationService) {

        this.fireStationService = fireStationService;
    }

    // Récupérer toutes les firestations
    @GetMapping("/firestation/all")
    public List<FireStation> getAllFireStations() {
        return fireStationService.getAllFireStations();
    }

    // Créer une firestation
    @PostMapping("/firestation")
    public FireStation addFireStation(@RequestBody FireStation fireStation) {
        return fireStationService.createFireStation(fireStation);
    }

    // Mettre à jour une firestation
    @PutMapping("/firestation")
    public FireStation updateFireStation(@RequestParam String address,
                                         @RequestBody FireStation fireStation) {
        FireStation updated = fireStationService.updateFireStation(address, fireStation);
        if (updated == null) throw new RuntimeException("FireStation not found!");
        return updated;
    }

    // Supprimer une firestation
    @DeleteMapping("/firestation")
    public String deleteFireStation(@RequestParam String address,
                                    @RequestParam String station) {
        boolean deleted = fireStationService.deleteFireStation(address, station);
        return deleted ? "FireStation supprimée !" : "FireStation introuvable !";
    }

    // Récupérer les numéros de téléphone par numéro de station
    @GetMapping("/phoneAlert")
    public List<String> phoneAlert(@RequestParam("firestation") String firestation) {
        return fireStationService.getPhonesByStation(firestation);
    }
    @GetMapping("/fire")
    public FireAddressDto getFire(@RequestParam String address) {

        return fireStationService.getFireByAddress(address);
    }

    @GetMapping("/firestation")
    public FireStationCoverageDto getResidentsByStation(
            @RequestParam int stationNumber) {
        return fireStationService.getResidentsByStation(stationNumber);
    }
    /*  @GetMapping("/firestation")
    public FireStationCoverageDto getResidentsByStation(@RequestParam int stationNumber) {
        return fireStationService.getResidentsByStation(stationNumber);
    }*/
    //@GetMapping("/phoneAlert")
    //    public List<String> getPhonesByStation(@RequestParam(name = "firestation") String stationNumber) {
    //        List<FireStation> allStations = fireStationService.getAllFireStation();
    //        return personService.getPhonesByFirestation(stationNumber, allStations);
    //    }
}
