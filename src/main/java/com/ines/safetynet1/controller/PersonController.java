package com.ines.safetynet1.controller;

import com.ines.safetynet1.model.Person;
import com.ines.safetynet1.service.PersonService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PersonController {

    private final PersonService personService;

    public PersonController(PersonService service) {
        this.personService = service;
    }

    // GET toutes les personnes
    @GetMapping("/persons")
    public List<Person> getAllPersons() {
        return personService.getAllPersons();
    }

    // POST ajouter une personne
    @PostMapping("/person")
    public Person addPerson(@RequestBody Person person) {
        return personService.createPerson(person);
    }

    // PUT modifier une personne
    @PutMapping("/person")
    public Person updatePerson(@RequestParam String firstName,
                               @RequestParam String lastName,
                               @RequestBody Person person) {
        Person updated = personService.updatePerson(firstName, lastName, person);
        if (updated == null) throw new RuntimeException("Person not found!");
        return updated;
    }

    // DELETE
    @DeleteMapping("/person")
    public String deletePerson(@RequestParam String firstName,
                               @RequestParam String lastName) {
        boolean deleted = personService.deletePerson(firstName, lastName);
        return deleted ? "Person supprimée !" : "Person introuvable !";
    }

    // GET community email
    @GetMapping("/communityEmail")
    public List<String> getByCommunityEmail(@RequestParam String city) {
        return personService.getEmailsByCity(city);
    }
}
