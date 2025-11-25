package com.ines.safetynet1;
import com.ines.safetynet1.model.Person;
import com.ines.safetynet1.repository.PersonRepository;
import com.ines.safetynet1.service.PersonService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
class PersonServiceTest {

    @Mock
    private PersonRepository personRepository;

    @InjectMocks
    private PersonService personService;

    @Test
    void testGetEmailsByCity() {

        // Création des personnes sans constructeur
        Person p1 = new Person();
        p1.setFirstName("John");
        p1.setLastName("Doe");
        p1.setCity("Paris");
        p1.setEmail("john.doe@example.com");

        Person p2 = new Person();
        p2.setFirstName("Alice");
        p2.setLastName("Smith");
        p2.setCity("Paris");
        p2.setEmail("alice.smith@example.com");

        Person p3 = new Person();
        p3.setFirstName("Bob");
        p3.setLastName("Brown");
        p3.setCity("Paris");
        p3.setEmail("john.doe@example.com"); // doublon

        List<Person> personsInCity = new ArrayList<>();
        personsInCity.add(p1);
        personsInCity.add(p2);
        personsInCity.add(p3);

        // Mock repository
        when(personRepository.findByCity("Paris")).thenReturn(personsInCity);

        // Appel service
        List<String> emails = personService.getEmailsByCity("Paris");

        // Vérifications
        assertEquals(2, emails.size());
        assertTrue(emails.contains("john.doe@example.com"));
        assertTrue(emails.contains("alice.smith@example.com"));
        assertFalse(emails.contains("bob@example.com"));
    }

}