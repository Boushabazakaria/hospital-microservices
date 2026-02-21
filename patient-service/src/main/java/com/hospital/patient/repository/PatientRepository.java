package com.hospital.patient.repository;

import com.hospital.patient.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository Patient - couche d'accès aux données.
 * Spring Data JPA génère automatiquement les requêtes SQL.
 */
@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

    // Rechercher un patient par email (utile pour éviter les doublons)
    Optional<Patient> findByEmail(String email);

    // Vérifier si un email existe déjà
    boolean existsByEmail(String email);
}
