package com.ines.safetynet1.controller;

import com.ines.safetynet1.model.MedicalRecord;
import com.ines.safetynet1.service.MedicalRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/medicalRecord")
public class MedicalRecordController {
@Autowired
    private final MedicalRecordService medicalRecordService;

    public MedicalRecordController(MedicalRecordService medicalRecordService) {
        this.medicalRecordService = medicalRecordService;
    }

    @GetMapping("/all")
    public List<MedicalRecord> getAllMedicalRecords() {
        return medicalRecordService.getAllMedicalRecords();
    }

    @PostMapping
    public MedicalRecord addRecord(@RequestBody MedicalRecord record) {
        return medicalRecordService.createMedicalRecord(record);
    }

    @PutMapping
    public MedicalRecord updateRecord(@RequestParam String firstName,
                                      @RequestParam String lastName,
                                      @RequestBody MedicalRecord record) {
        MedicalRecord updated = medicalRecordService.updateMedicalRecord(firstName, lastName, record);
        if (updated == null) throw new RuntimeException("MedicalRecord not found!");
        return updated;
    }

    @DeleteMapping
    public String deleteRecord(@RequestParam String firstName,
                               @RequestParam String lastName) {
        boolean deleted = medicalRecordService.deleteMedicalRecord(firstName, lastName);
        return deleted ? "MedicalRecord supprimé !" : "MedicalRecord introuvable !";
    }
}
