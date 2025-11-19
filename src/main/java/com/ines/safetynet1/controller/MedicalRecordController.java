package com.ines.safetynet1.controller;

import com.ines.safetynet1.model.MedicalRecord;
import com.ines.safetynet1.service.MedicalRecordService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/medicalRecord")
public class MedicalRecordController {

    private final MedicalRecordService service;

    public MedicalRecordController(MedicalRecordService service) {
        this.service = service;
    }

    @GetMapping("/all")
    public List<MedicalRecord> getAllMedicalRecords() {
        return service.getAllMedicalRecords();
    }

    @PostMapping
    public MedicalRecord addRecord(@RequestBody MedicalRecord record) {
        return service.createMedicalRecord(record);
    }

    @PutMapping
    public MedicalRecord updateRecord(@RequestParam String firstName,
                                      @RequestParam String lastName,
                                      @RequestBody MedicalRecord record) {
        MedicalRecord updated = service.updateMedicalRecord(firstName, lastName, record);
        if (updated == null) throw new RuntimeException("MedicalRecord not found!");
        return updated;
    }

    @DeleteMapping
    public String deleteRecord(@RequestParam String firstName,
                               @RequestParam String lastName) {
        boolean deleted = service.deleteMedicalRecord(firstName, lastName);
        return deleted ? "MedicalRecord supprimé !" : "MedicalRecord introuvable !";
    }
}
