package com.hospital.patient.controller;

import com.hospital.patient.entity.Patient;
import com.hospital.patient.exception.PatientNotFoundException;
import com.hospital.patient.service.PatientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Contrôleur REST Patient - expose les endpoints de l'API.
 * Toutes les routes commencent par /api/patients
 */
@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
@Slf4j
public class PatientController {

    private final PatientService patientService;

    /**
     * GET /api/patients - Retourne tous les patients
     */
    @GetMapping
    public ResponseEntity<List<Patient>> getAllPatients() {
        return ResponseEntity.ok(patientService.getAllPatients());
    }

    /**
     * GET /api/patients/{id} - Retourne un patient par son ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Patient> getPatientById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(patientService.getPatientById(id));
        } catch (PatientNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * POST /api/patients - Crée un nouveau patient
     * @Valid déclenche la validation des contraintes de l'entité
     */
    @PostMapping
    public ResponseEntity<Patient> createPatient(@Valid @RequestBody Patient patient) {
        Patient created = patientService.createPatient(patient);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * PUT /api/patients/{id} - Met à jour un patient existant
     */
    @PutMapping("/{id}")
    public ResponseEntity<Patient> updatePatient(@PathVariable Long id,
                                                  @Valid @RequestBody Patient patient) {
        try {
            return ResponseEntity.ok(patientService.updatePatient(id, patient));
        } catch (PatientNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * DELETE /api/patients/{id} - Supprime un patient
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable Long id) {
        try {
            patientService.deletePatient(id);
            return ResponseEntity.noContent().build();
        } catch (PatientNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * GET /api/patients/{id}/exists - Vérifie l'existence d'un patient.
     * Endpoint interne utilisé par Appointment et Medical Record services.
     */
    @GetMapping("/{id}/exists")
    public ResponseEntity<Map<String, Boolean>> checkPatientExists(@PathVariable Long id) {
        boolean exists = patientService.patientExists(id);
        return ResponseEntity.ok(Map.of("exists", exists));
    }
}
