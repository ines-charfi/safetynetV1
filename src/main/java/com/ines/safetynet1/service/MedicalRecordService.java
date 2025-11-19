package com.ines.safetynet1.service;

import com.ines.safetynet1.model.MedicalRecord;
import com.ines.safetynet1.repository.MedicalRecordRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicalRecordService {

    private final MedicalRecordRepository repository;

    public MedicalRecordService(MedicalRecordRepository repository) {
        this.repository = repository;
    }

    public List<MedicalRecord> getAllMedicalRecords() {
        return repository.findAll();
    }

    public MedicalRecord getMedicalRecord(String firstName, String lastName) {
        return repository.findByName(firstName, lastName).orElse(null);
    }

    public MedicalRecord createMedicalRecord(MedicalRecord record) {
        return repository.save(record);
    }

    public MedicalRecord updateMedicalRecord(String firstName, String lastName, MedicalRecord record) {
        return repository.update(firstName, lastName, record);
    }

    public boolean deleteMedicalRecord(String firstName, String lastName) {
        return repository.delete(firstName, lastName);
    }
}
