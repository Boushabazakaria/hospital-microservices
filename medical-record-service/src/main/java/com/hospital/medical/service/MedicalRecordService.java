package com.hospital.medical.service;

import com.hospital.medical.client.PatientClient;
import com.hospital.medical.entity.Diagnostic;
import com.hospital.medical.entity.MedicalRecord;
import com.hospital.medical.exception.MedicalRecordNotFoundException;
import com.hospital.medical.repository.DiagnosticRepository;
import com.hospital.medical.repository.MedicalRecordRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service de gestion des dossiers médicaux.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class MedicalRecordService {

    private final MedicalRecordRepository medicalRecordRepository;
    private final DiagnosticRepository diagnosticRepository;
    private final PatientClient patientClient;

    /**
     * Crée un nouveau dossier médical pour un patient.
     * Vérifie l'existence du patient avec Circuit Breaker.
     */
    @Transactional
    @CircuitBreaker(name = "patientService", fallbackMethod = "createRecordFallback")
    public MedicalRecord createMedicalRecord(MedicalRecord record) {
        log.info("Création du dossier médical pour patient ID: {}", record.getPatientId());

        // Un patient ne peut avoir qu'un seul dossier médical
        if (medicalRecordRepository.existsByPatientId(record.getPatientId())) {
            throw new IllegalArgumentException("Un dossier médical existe déjà pour le patient ID: " + record.getPatientId());
        }

        // Vérification que le patient existe
        boolean patientExists = patientClient.checkPatientExists(record.getPatientId())
                .getOrDefault("exists", false);

        if (!patientExists) {
            throw new IllegalArgumentException("Patient introuvable avec l'ID: " + record.getPatientId());
        }

        return medicalRecordRepository.save(record);
    }

    /** Fallback pour createMedicalRecord */
    public MedicalRecord createRecordFallback(MedicalRecord record, Throwable ex) {
        log.error("[CIRCUIT BREAKER] Patient Service indisponible: {}", ex.getMessage());
        throw new RuntimeException("Service temporairement indisponible. Réessayez plus tard.");
    }

    /**
     * Récupère le dossier médical d'un patient par son patientId.
     */
    public MedicalRecord getRecordByPatientId(Long patientId) {
        log.info("Récupération du dossier médical pour patient ID: {}", patientId);
        return medicalRecordRepository.findByPatientId(patientId)
                .orElseThrow(() -> new MedicalRecordNotFoundException("Aucun dossier médical pour le patient ID: " + patientId));
    }

    /**
     * Récupère un dossier médical par son propre ID.
     */
    public MedicalRecord getRecordById(Long id) {
        return medicalRecordRepository.findById(id)
                .orElseThrow(() -> new MedicalRecordNotFoundException("Dossier médical introuvable, ID: " + id));
    }

    /**
     * Ajoute un diagnostic à un dossier médical existant.
     */
    @Transactional
    public Diagnostic addDiagnostic(Long dossierId, Diagnostic diagnostic) {
        log.info("Ajout d'un diagnostic au dossier ID: {}", dossierId);

        MedicalRecord dossier = getRecordById(dossierId);
        diagnostic.setDossierMedical(dossier);  // Associe le diagnostic au dossier

        return diagnosticRepository.save(diagnostic);
    }

    /**
     * Récupère tous les diagnostics d'un dossier médical.
     */
    public List<Diagnostic> getDiagnosticsByDossierId(Long dossierId) {
        // Vérifie que le dossier existe
        getRecordById(dossierId);
        return diagnosticRepository.findByDossierMedicalId(dossierId);
    }

    /** Récupère tous les dossiers */
    public List<MedicalRecord> getAllRecords() {
        return medicalRecordRepository.findAll();
    }
}
