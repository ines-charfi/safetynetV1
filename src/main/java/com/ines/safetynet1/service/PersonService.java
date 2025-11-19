package com.ines.safetynet1.service;

import com.ines.safetynet1.model.Person;
import com.ines.safetynet1.repository.PersonRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PersonService {

    private final PersonRepository personRepository;


    public PersonService(PersonRepository repository) {
        this.personRepository = repository;
    }

    public List<Person> getAllPersons() {
        return personRepository.findAll();
    }

    public Person getPerson(String firstName, String lastName) {
        return personRepository.findByName(firstName, lastName).orElse(null);
    }

    public Person createPerson(Person person) {
        return personRepository.save(person);
    }

    public Person updatePerson(String firstName, String lastName, Person updated) {
        return personRepository.update(firstName, lastName, updated);
    }

    public boolean deletePerson(String firstName, String lastName) {
        return personRepository.delete(firstName, lastName);
    }
    //autre methode avec stream
   // public List<String> findAllEmailsByCity(String city) {
    //    return this.personRepository. findAll().stream(). filter( Person p -> p.getCity(). equals(city)).map( Person p-> p.getEmails()).collect(Collectors.toList());
   // }
    //autre methode avec stream plus simple
   // public List<String> findAllEmailsByCity(String city) {
     //   return this.personRepository.findAll().stream()
      //          .filter(p -> p.getCity().equalsIgnoreCase(city))
      //          .map(Person::getEmail)
        //        .distinct()
        //        .collect(Collectors.toList());
  //  }

    public List<String> getEmailsByCity(String city) {
        List<Person> persons = personRepository.findByCity(city);
        List<String> emails = new ArrayList<>();
        for (Person person : persons) {
           // if(person.getCity().equals(city) { ajouter cette condition pour verifier la ville
                emails.add(person.getEmail());
           // }
        }
        // Supprimer les doublons
        List<String> distinctEmails = emails.stream().distinct().collect(Collectors.toList());
        return distinctEmails;
    }



}
