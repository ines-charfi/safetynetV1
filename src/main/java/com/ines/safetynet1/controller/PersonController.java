package com.ines.safetynet1.controller;

import com.ines.safetynet1.model.Person;
import com.ines.safetynet1.service.PersonService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/person")
public class PersonController {

    private final PersonService personService;
    private String city;

    public PersonController(PersonService service) {
        this.personService = service;
    }

    @GetMapping("/all")
    public List<Person> getAllPersons() {
        return personService.getAllPersons();
    }

    @PostMapping
    public Person addPerson(@RequestBody Person person) {
        return personService.createPerson(person);
    }

    @PutMapping
    public Person updatePerson(@RequestParam String firstName,
                               @RequestParam String lastName,
                               @RequestBody Person person) {
        Person updated = personService.updatePerson(firstName, lastName, person);
        if (updated == null) throw new RuntimeException("Person not found!");
        return updated;
    }

    @DeleteMapping
    public String deletePerson(@RequestParam String firstName,
                               @RequestParam String lastName) {
        boolean deleted = personService.deletePerson(firstName, lastName);
        return deleted ? "Person supprimée !" : "Person introuvable !";
    }
    @GetMapping("/communityEmails")
    public List<String> getByCommunityEmail(@RequestParam String city) {
       
        return personService.getEmailsByCity(city);
    }
    //Autre methode pour communityEmails
    //Get an email list
    //@RequestMapping(value = "communityEmail", method = RequestMethod.GET)
    //public List<String> listeEmails(@RequestParam(name = "city") String city) {
    //return personService. findAllEmailsByCity(city);
}

