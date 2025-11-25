package com.ines.safetynet1.repository;
import com.ines.safetynet1.repository.DataHandler;
import com.ines.safetynet1.model.MedicalRecord;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
@Data
@Repository
public class MedicalRecordRepository {

@Autowired
    private final DataHandler dataHandler;

    public MedicalRecordRepository(DataHandler dataHandler) {

        this.dataHandler = dataHandler;
    }

    public List<MedicalRecord> findAll() {
        return dataHandler.getData().getMedicalrecords();
    }

    public Optional<MedicalRecord> findByName(String firstName, String lastName) {
        return dataHandler.getData().getMedicalrecords().stream()
                .filter(r -> r.getFirstName().equals(firstName)
                        && r.getLastName().equals(lastName))
                .findFirst();
    }

    public MedicalRecord save(MedicalRecord record) {
        dataHandler.getData().getMedicalrecords().add(record);
        return record;
    }

    public MedicalRecord update(String firstName, String lastName, MedicalRecord updated) {
        Optional<MedicalRecord> existing = findByName(firstName, lastName);
        if (existing.isPresent()) {
            existing.get().setBirthdate(updated.getBirthdate());
            existing.get().setAllergies(updated.getAllergies());
            existing.get().setMedications(updated.getMedications());
            return existing.get();
        }
        return null;
    }

    public boolean delete(String firstName, String lastName) {
        return dataHandler.getData().getMedicalrecords().removeIf(r ->
                r.getFirstName().equalsIgnoreCase(firstName) &&
                        r.getLastName().equalsIgnoreCase(lastName)
        );
    }
}
