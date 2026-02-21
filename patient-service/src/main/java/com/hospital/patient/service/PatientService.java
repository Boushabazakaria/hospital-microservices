package com.hospital.patient.service;

import com.hospital.patient.entity.Patient;
import com.hospital.patient.exception.PatientNotFoundException;
import com.hospital.patient.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service Patient - contient la logique métier.
 * Cette couche fait la jonction entre le contrôleur et le repository.
 */
@Service
@RequiredArgsConstructor  // Injecte les dépendances via constructeur (meilleure pratique)
@Slf4j                    // Active les logs (log.info, log.error, etc.)
public class PatientService {

    private final PatientRepository patientRepository;

    /**
     * Récupère tous les patients enregistrés.
     */
    public List<Patient> getAllPatients() {
        log.info("Récupération de tous les patients");
        return patientRepository.findAll();
    }

    /**
     * Récupère un patient par son ID.
     * Lance une exception si non trouvé.
     */
    public Patient getPatientById(Long id) {
        log.info("Recherche du patient avec l'ID : {}", id);
        return patientRepository.findById(id)
                .orElseThrow(() -> new PatientNotFoundException(id));
    }

    /**
     * Crée un nouveau patient.
     * Vérifie que l'email n'est pas déjà utilisé.
     */
    @Transactional
    public Patient createPatient(Patient patient) {
        log.info("Création d'un nouveau patient : {} {}", patient.getPrenom(), patient.getNom());

        // Vérification de l'unicité de l'email
        if (patient.getEmail() != null && patientRepository.existsByEmail(patient.getEmail())) {
            throw new IllegalArgumentException("Un patient avec cet email existe déjà : " + patient.getEmail());
        }

        Patient savedPatient = patientRepository.save(patient);
        log.info("Patient créé avec succès, ID : {}", savedPatient.getId());
        return savedPatient;
    }

    /**
     * Met à jour les informations d'un patient existant.
     */
    @Transactional
    public Patient updatePatient(Long id, Patient patientDetails) {
        log.info("Mise à jour du patient avec l'ID : {}", id);

        // Vérifie que le patient existe
        Patient existingPatient = getPatientById(id);

        // Met à jour les champs
        existingPatient.setNom(patientDetails.getNom());
        existingPatient.setPrenom(patientDetails.getPrenom());
        existingPatient.setDateNaissance(patientDetails.getDateNaissance());
        existingPatient.setEmail(patientDetails.getEmail());
        existingPatient.setNumeroDeTelephone(patientDetails.getNumeroDeTelephone());
        existingPatient.setAdresse(patientDetails.getAdresse());

        return patientRepository.save(existingPatient);
    }

    /**
     * Supprime un patient par son ID.
     */
    @Transactional
    public void deletePatient(Long id) {
        log.info("Suppression du patient avec l'ID : {}", id);
        // Vérifie d'abord que le patient existe
        getPatientById(id);
        patientRepository.deleteById(id);
    }

    /**
     * Vérifie si un patient existe (utilisé par les autres microservices).
     */
    public boolean patientExists(Long id) {
        return patientRepository.existsById(id);
    }
}
