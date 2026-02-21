package com.hospital.medical.controller;

import com.hospital.medical.entity.Diagnostic;
import com.hospital.medical.entity.MedicalRecord;
import com.hospital.medical.exception.MedicalRecordNotFoundException;
import com.hospital.medical.service.MedicalRecordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Contrôleur REST pour les dossiers médicaux.
 */
@RestController
@RequestMapping("/api/medical-records")
@RequiredArgsConstructor
public class MedicalRecordController {

    private final MedicalRecordService medicalRecordService;

    /** GET /api/medical-records - Tous les dossiers */
    @GetMapping
    public ResponseEntity<List<MedicalRecord>> getAllRecords() {
        return ResponseEntity.ok(medicalRecordService.getAllRecords());
    }

    /** GET /api/medical-records/{id} - Un dossier par son ID */
    @GetMapping("/{id}")
    public ResponseEntity<?> getRecordById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(medicalRecordService.getRecordById(id));
        } catch (MedicalRecordNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /** GET /api/medical-records/patient/{patientId} - Dossier d'un patient */
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<?> getRecordByPatientId(@PathVariable Long patientId) {
        try {
            return ResponseEntity.ok(medicalRecordService.getRecordByPatientId(patientId));
        } catch (MedicalRecordNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /** POST /api/medical-records - Crée un dossier médical */
    @PostMapping
    public ResponseEntity<?> createMedicalRecord(@Valid @RequestBody MedicalRecord record) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(medicalRecordService.createMedicalRecord(record));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(e.getMessage());
        }
    }

    /** POST /api/medical-records/{id}/diagnostics - Ajoute un diagnostic */
    @PostMapping("/{id}/diagnostics")
    public ResponseEntity<?> addDiagnostic(@PathVariable Long id,
                                            @Valid @RequestBody Diagnostic diagnostic) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(medicalRecordService.addDiagnostic(id, diagnostic));
        } catch (MedicalRecordNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /** GET /api/medical-records/{id}/diagnostics - Historique des diagnostics */
    @GetMapping("/{id}/diagnostics")
    public ResponseEntity<?> getDiagnostics(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(medicalRecordService.getDiagnosticsByDossierId(id));
        } catch (MedicalRecordNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
