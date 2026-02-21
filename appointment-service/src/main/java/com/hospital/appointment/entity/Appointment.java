package com.hospital.appointment.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entité Rendez-vous.
 * Stocke uniquement l'ID du patient (pas l'objet entier)
 * car les données patient vivent dans un autre microservice.
 */
@Entity
@Table(name = "appointments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ID du patient (référence vers le Patient Service, pas une clé étrangère JPA)
    @NotNull(message = "L'ID du patient est obligatoire")
    @Column(name = "patient_id", nullable = false)
    private Long patientId;

    @NotNull(message = "La date et heure du rendez-vous sont obligatoires")
    @Column(name = "date_heure", nullable = false)
    private LocalDateTime dateHeure;

    @Column(nullable = false)
    private String motif;

    @Column(name = "medecin_nom")
    private String medecinNom;

    // Statut du rendez-vous
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutRendezVous statut = StatutRendezVous.PLANIFIE;

    @Column
    private String notes;

    /**
     * Enum des statuts possibles d'un rendez-vous.
     */
    public enum StatutRendezVous {
        PLANIFIE,   // Rendez-vous programmé
        CONFIRME,   // Confirmé par le médecin
        ANNULE,     // Annulé
        TERMINE     // Consultation terminée
    }
}
