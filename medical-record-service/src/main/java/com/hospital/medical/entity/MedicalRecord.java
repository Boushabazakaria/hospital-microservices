package com.hospital.medical.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Entité Dossier Médical.
 * Un dossier contient l'historique des diagnostics d'un patient.
 * La relation avec Patient est gérée uniquement par patientId (architecture microservices).
 */
@Entity
@Table(name = "medical_records")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicalRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "L'ID du patient est obligatoire")
    @Column(name = "patient_id", nullable = false, unique = true)
    private Long patientId;  // Chaque patient a un seul dossier médical

    @Column(name = "date_creation", nullable = false)
    private LocalDate dateCreation = LocalDate.now();

    @Column(name = "groupe_sanguin")
    private String groupeSanguin;

    @Column
    private String allergies;

    @Column(name = "antecedents_medicaux", columnDefinition = "TEXT")
    private String antecedentsMedicaux;

    /**
     * Liste des diagnostics associés à ce dossier.
     * CascadeType.ALL : les diagnostics sont sauvegardés/supprimés avec le dossier.
     * orphanRemoval : supprime les diagnostics qui ne référencent plus ce dossier.
     */
    @OneToMany(mappedBy = "dossierMedical", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Diagnostic> diagnostics = new ArrayList<>();
}
