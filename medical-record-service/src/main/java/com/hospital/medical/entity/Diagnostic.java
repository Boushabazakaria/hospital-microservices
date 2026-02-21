package com.hospital.medical.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Entité Diagnostic - représente un diagnostic médical dans un dossier.
 * Appartient toujours à un MedicalRecord (relation Many-to-One).
 */
@Entity
@Table(name = "diagnostics")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Diagnostic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le titre du diagnostic est obligatoire")
    @Column(nullable = false)
    private String titre;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private LocalDate date = LocalDate.now();

    @Column
    private String medecin;

    @Column
    private String traitement;

    /**
     * Référence vers le dossier médical parent.
     * @JsonIgnore évite la boucle infinie lors de la sérialisation JSON.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dossier_medical_id", nullable = false)
    @JsonIgnore
    private MedicalRecord dossierMedical;
}
