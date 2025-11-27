package com.ines.safetynet1.controller;

import com.ines.safetynet1.model.Person;
import com.ines.safetynet1.service.PersonService;
import com.ines.safetynet1.service.dto.ChildAlertDto;
import com.ines.safetynet1.service.dto.FloodStationDto;
import com.ines.safetynet1.service.dto.PersonInfoDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
public class PersonController {
@Autowired
    private final PersonService personService;

    public PersonController(PersonService service) {
        this.personService = service;
    }

    @GetMapping("/all")
    public List<Person> getAllPersons() {
        return personService.getAllPersons();
    }

    @PostMapping("/person")
    public Person addPerson(@RequestBody Person person) {
        return personService.createPerson(person);
    }

    @PutMapping("/person")
    public Person updatePerson(@RequestParam String firstName,
                               @RequestParam String lastName,
                               @RequestBody Person person) {
        // Appelle le service pour mettre à jour la personne
        Person updated = personService.updatePerson(firstName, lastName, person);

        // Si la personne n'existe pas, renvoie une exception
        if (updated == null) {
            throw new RuntimeException("Person not found!");
        }

        // Retourne la personne mise à jour
        return updated;
    }


    @DeleteMapping("/person")
    public String deletePerson(@RequestParam String firstName,
                               @RequestParam String lastName) {
        boolean deleted = personService.deletePerson(firstName, lastName);
        return deleted ? "Person supprimée !" : "Person introuvable !";
    }

    @GetMapping("/communityEmails")
    public List<String> getByCommunityEmail(@RequestParam String city) {
        return personService.getEmailsByCity(city);
    }
    @GetMapping("/personInfo")
    public List<PersonInfoDto> getPersonInfo(
            @RequestParam String firstName,
            @RequestParam String lastName) {

        return personService.getPersonInfo(firstName, lastName);
    }
    @GetMapping("/flood/stations")
    public List<FloodStationDto> getFloodByStations(
            @RequestParam List<Integer> stations) {
        return personService.getFloodByStations(stations);
    }
    @GetMapping("/childAlert")
    public List<ChildAlertDto> getChildAlert(@RequestParam String address) {
        return personService.getChildrenByAddress(address);
    }



}

