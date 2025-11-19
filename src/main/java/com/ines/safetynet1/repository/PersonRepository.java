package com.ines.safetynet1.repository;

import com.ines.safetynet1.repository.DataHandler;
import com.ines.safetynet1.model.Person;
import com.ines.safetynet1.model.Data;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class PersonRepository {

    private final DataHandler dataHandler;

    public PersonRepository(DataHandler dataHandler) {
        this.dataHandler = dataHandler;
    }

    public List<Person> findAll() {
        return dataHandler.getData().getPersons();
    }

    public Optional<Person> findByName(String firstName, String lastName) {
        return dataHandler.getData().getPersons().stream()
                .filter(p -> p.getFirstName().equalsIgnoreCase(firstName)
                        && p.getLastName().equalsIgnoreCase(lastName))
                .findFirst();
    }

    public Person save(Person person) {
        dataHandler.getData().getPersons().add(person);
        return person;
    }

    public Person update(String firstName, String lastName, Person updated) {
        Optional<Person> existing = findByName(firstName, lastName);
        if (existing.isPresent()) {
            existing.get().setAddress(updated.getAddress());
            existing.get().setCity(updated.getCity());
            existing.get().setZip(updated.getZip());
            existing.get().setPhone(updated.getPhone());
            existing.get().setEmail(updated.getEmail());
            return existing.get();
        }
        return null;
    }

    public boolean delete(String firstName, String lastName) {
        return dataHandler.getData().getPersons().removeIf(p ->
                p.getFirstName().equalsIgnoreCase(firstName) &&
                        p.getLastName().equalsIgnoreCase(lastName)
        );
    }
    public List<Person> findByCity(String city) {
        return dataHandler.getData().getPersons().stream()
                .filter(p -> p.getCity().equalsIgnoreCase(city))
                .collect(Collectors.toList());
    }

}

