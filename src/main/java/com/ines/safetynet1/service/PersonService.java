package com.ines.safetynet1.service;

import com.ines.safetynet1.model.FireStation;
import com.ines.safetynet1.model.MedicalRecord;
import com.ines.safetynet1.model.Person;
import com.ines.safetynet1.repository.DataHandler;
import com.ines.safetynet1.repository.PersonRepository;
import com.ines.safetynet1.service.dto.ChildAlertDto;
import com.ines.safetynet1.service.dto.FloodStationDto;
import com.ines.safetynet1.service.dto.PersonInfoDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PersonService {
@Autowired
    private final PersonRepository personRepository;
    private final DataHandler dataHandler;

    public PersonService(PersonRepository repository, DataHandler dataHandler) {
        this.personRepository = repository;
        this.dataHandler = dataHandler;
    }

    // CRUD Person
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

    public List<String> getEmailsByCity(String city) {
        return personRepository.findByCity(city).stream()
                .map(Person::getEmail)
                .distinct()
                .collect(Collectors.toList());
    }

    //  Conversion Person → PersonInfoDto
    private PersonInfoDto toPersonInfoDto(Person p) {
        MedicalRecord record = dataHandler.getData().getMedicalrecords().stream()
                .filter(mr -> mr.getFirstName().equalsIgnoreCase(p.getFirstName())
                        && mr.getLastName().equalsIgnoreCase(p.getLastName()))
                .findFirst()
                .orElse(null);

        PersonInfoDto dto = new PersonInfoDto();
        dto.setFirstName(p.getFirstName());
        dto.setLastName(p.getLastName());
        dto.setAddress(p.getAddress());
        dto.setPhone(p.getPhone());

        dto.setAge(record != null ? String.valueOf(ageFromBirthdate(record.getBirthdate())) : "0");
        dto.setMedications(record != null ? record.getMedications() : new String[]{});
        dto.setAllergies(record != null ? record.getAllergies() : new String[]{});
        return dto;
    }

    private int ageFromBirthdate(String birthdate) {
        try {
            int year = Integer.parseInt(birthdate.split("/")[2]);
            return 2025 - year; // simplification
        } catch (Exception e) {
            return 0;
        }
    }

    // Person Info par Nom
    public List<PersonInfoDto> getPersonInfo(String firstName, String lastName) {
        return personRepository.findAll().stream()
                .filter(p -> p.getFirstName().equalsIgnoreCase(firstName)
                        && p.getLastName().equalsIgnoreCase(lastName))
                .map(this::toPersonInfoDto)
                .collect(Collectors.toList());
    }

    // Flood Stations
    public List<FloodStationDto> getFloodByStations(List<Integer> stationNumbers) {
        List<FloodStationDto> result = new ArrayList<>();

        // 1. Récupérer toutes les adresses couvertes par les stations demandées
        List<String> addresses = dataHandler.getData().getFirestations().stream()
                .filter(f -> stationNumbers.contains(Integer.parseInt(f.getStation())))
                .map(FireStation::getAddress)
                .distinct()
                .collect(Collectors.toList());

        // 2. Pour chaque adresse, créer le DTO foyer
        for (String address : addresses) {
            List<PersonInfoDto> residents = dataHandler.getData().getPersons().stream()
                    .filter(p -> p.getAddress().equalsIgnoreCase(address))
                    .map(p -> {
                        PersonInfoDto dto = new PersonInfoDto();
                        dto.setFirstName(p.getFirstName());
                        dto.setLastName(p.getLastName());
                        dto.setPhone(p.getPhone());
                        dto.setAddress(p.getAddress());

                        // Récupérer dossier médical
                        MedicalRecord record = dataHandler.getData().getMedicalrecords().stream()
                                .filter(mr -> mr.getFirstName().equalsIgnoreCase(p.getFirstName())
                                        && mr.getLastName().equalsIgnoreCase(p.getLastName()))
                                .findFirst()
                                .orElse(null);

                        int age = record != null ? ageFromBirthdate(record.getBirthdate()) : 0;
                        dto.setAge(String.valueOf(age));
                        dto.setMedications(record != null ? record.getMedications() : new String[]{});
                        dto.setAllergies(record != null ? record.getAllergies() : new String[]{});
                        return dto;
                    })
                    .collect(Collectors.toList());

            FloodStationDto dto = new FloodStationDto();
            dto.setAddress(address);
            dto.setResidents(residents);
            result.add(dto);
        }

        return result;
    }


    //  Child Alert
    public List<ChildAlertDto> getChildrenByAddress(String address) {

        // Récupère toutes les personnes vivant à l’adresse
        List<Person> residents = dataHandler.getData().getPersons().stream()
                .filter(p -> p.getAddress().equalsIgnoreCase(address))
                .collect(Collectors.toList());

        List<ChildAlertDto> children = new ArrayList<>();

        for (Person p : residents) {

            // Récupère son dossier médical pour calculer l’âge
            MedicalRecord record = dataHandler.getData().getMedicalrecords().stream()
                    .filter(mr -> mr.getFirstName().equals(p.getFirstName())
                            && mr.getLastName().equals(p.getLastName()))
                    .findFirst()
                    .orElse(null);

            int age = record != null ? ageFromBirthdate(record.getBirthdate()) : 0;

            // Si c’est un enfant (≤ 18)
            if (age <= 18) {
                ChildAlertDto dto = new ChildAlertDto();
                dto.setFirstName(p.getFirstName());
                dto.setLastName(p.getLastName());
                dto.setAge(age);
//membres de foyer
                List<String> members = residents.stream()
                        .filter(other -> !other.equals(p))
                        .map(other -> other.getFirstName() + " " + other.getLastName())
                        .collect(Collectors.toList());

                dto.setOtherHouseholdMembers(members);

                children.add(dto);
            }
        }

        return children; // si vide → Spring renvoie []
    }

}
//public List<String> getPhonesByFirestation(String stationNumber, List<FireStation> fireStations) {
//        List<String> phones = new ArrayList<>();
//
//        for (FireStation fs : fireStations) {
//            if (fs.getStation().equalsIgnoreCase(stationNumber)) {
//                for (Person p : personRepository.findAll()) {
//                    if (p.getAddress().equalsIgnoreCase(fs.getAddress())) {
//                        phones.add(p.getPhone());
//                    }
//                }
//            }
//        }
//
//        return phones;
//    }
// for each imbriquée
//for(Person person: persons{
//for (FireStation firestation : sortedfirestation){
//if(person.getAdress().equals(firestation.getAdress()){
//phones.add(person.getphone());
//}
//}
//return phones
//}
/*// Child Alert simplifié
public List<ChildAlertDto> getChildrenByAddress(String address) {
    List<ChildAlertDto> children = new ArrayList<>();

    // 1. Récupère toutes les personnes vivant à l’adresse
    List<Person> residents = new ArrayList<>();
    for (Person p : dataHandler.getData().getPersons()) {
        if (p.getAddress().equalsIgnoreCase(address)) {
            residents.add(p);
        }
    }

    // 2. Parcourt chaque résident pour vérifier s'il est enfant
    for (Person p : residents) {
        // Trouve le MedicalRecord pour calculer l'âge
        MedicalRecord record = null;
        for (MedicalRecord mr : dataHandler.getData().getMedicalrecords()) {
            if (mr.getFirstName().equalsIgnoreCase(p.getFirstName()) &&
                mr.getLastName().equalsIgnoreCase(p.getLastName())) {
                record = mr;
                break;
            }
        }

        int age = 0;
        if (record != null) {
            age = ageFromBirthdate(record.getBirthdate());
        }

        // Si c'est un enfant (≤ 18 ans)
        if (age <= 18) {
            ChildAlertDto dto = new ChildAlertDto();
            dto.setFirstName(p.getFirstName());
            dto.setLastName(p.getLastName());
            dto.setAge(age);

            // Liste des autres membres du foyer
            List<String> others = new ArrayList<>();
            for (Person other : residents) {
                if (!other.equals(p)) {
                    others.add(other.getFirstName() + " " + other.getLastName());
                }
            }
            dto.setOtherHouseholdMembers(others);

            children.add(dto);
        }
    }

    return children; // renvoie une liste vide si aucun enfant
}
*/
/*childAlert
public List<ChildAlertDto> findAllchildsUnder18ByAddress(String address) { 1usage ±jcb826


List<ChildAlertDto> result = new ArrayList<>();
récuperer la liste des peronnes habitants à cette adresse

List<Person> persons = personRepository. findAllpersonByAddress (address);
recuperer la liste des medical records de - de 18 ans

List<MedicalRecord> medicalRecords = medicalRecordsRepository. findAllMedicalRecordsUnder18();

// pour chaque élément de personne rechercher dans la liste des - 18 ans
// je crée une troisieme liste et je fait rentrer les noms qui correspondent
for (Person person : persons)
MedicalRecord medicalRecord = medicalRecordsContainsPerson(medicalRecords, person);
if (medicalRecord != null) {
ChildAlertDto dto = new ChildAlertDto();
dto.setFirstName(person.getFirstName());
dto. setLastName(person.getLastName());
dto.setAge(String.value0f(computeAge(medicalRecord.getBirthdate())));
dto. setHouseholds (persons. stream(). filter( Person p -> !p.getFirstName(). equals(person.getFirstName()))
result.add(dto);

return result;
*/
