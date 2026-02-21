package com.hospital.patient.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Entité Patient - représente un patient dans la base de données.
 * Contient les informations administratives uniquement.
 */
@Entity
@Table(name = "patients")
@Data                  // Génère getters, setters, equals, hashCode, toString
@NoArgsConstructor     // Constructeur sans argument (requis par JPA)
@AllArgsConstructor    // Constructeur avec tous les arguments
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le nom est obligatoire")
    @Column(nullable = false)
    private String nom;

    @NotBlank(message = "Le prénom est obligatoire")
    @Column(nullable = false)
    private String prenom;

    @NotNull(message = "La date de naissance est obligatoire")
    @Column(name = "date_naissance", nullable = false)
    private LocalDate dateNaissance;

    @Column(unique = true)
    private String email;

    @Column(name = "numero_telephone")
    private String numeroDeTelephone;

    @Column
    private String adresse;
}
