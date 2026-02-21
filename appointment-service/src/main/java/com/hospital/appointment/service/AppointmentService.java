package com.hospital.appointment.service;

import com.hospital.appointment.client.PatientServiceClient;
import com.hospital.appointment.entity.Appointment;
import com.hospital.appointment.exception.AppointmentNotFoundException;
import com.hospital.appointment.repository.AppointmentRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Service Appointment - logique métier des rendez-vous.
 *
 * Ce service communique avec le Patient Service via Feign Client.
 * Les annotations Resilience4j gèrent la résilience :
 * - @CircuitBreaker : ouvre le circuit après trop d'échecs
 * - @Retry : réessaie automatiquement en cas d'échec temporaire
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientServiceClient patientServiceClient;  // Client Feign

    /**
     * Récupère tous les rendez-vous d'un patient.
     */
    public List<Appointment> getAppointmentsByPatientId(Long patientId) {
        log.info("Récupération des rendez-vous pour le patient ID: {}", patientId);
        return appointmentRepository.findByPatientId(patientId);
    }

    /**
     * Récupère un rendez-vous par son ID.
     */
    public Appointment getAppointmentById(Long id) {
        return appointmentRepository.findById(id)
                .orElseThrow(() -> new AppointmentNotFoundException(id));
    }

    /**
     * Crée un nouveau rendez-vous après vérification de l'existence du patient.
     *
     * @CircuitBreaker : si le Patient Service échoue trop souvent, le circuit s'ouvre
     *   et la méthode fallback est appelée directement (sans attendre).
     * @Retry : en cas d'échec ponctuel, réessaie 3 fois avant de déclencher le fallback.
     */
    @Transactional
    @CircuitBreaker(name = "patientService", fallbackMethod = "createAppointmentFallback")
    @Retry(name = "patientService")
    public Appointment createAppointment(Appointment appointment) {
        log.info("Tentative de création d'un rendez-vous pour le patient ID: {}", appointment.getPatientId());

        // Appel au Patient Service pour vérifier l'existence du patient
        Map<String, Boolean> response = patientServiceClient.checkPatientExists(appointment.getPatientId());
        boolean patientExists = response.getOrDefault("exists", false);

        if (!patientExists) {
            throw new IllegalArgumentException(
                "Le patient avec l'ID " + appointment.getPatientId() + " n'existe pas ou est inaccessible.");
        }

        Appointment saved = appointmentRepository.save(appointment);
        log.info("Rendez-vous créé avec succès, ID: {}", saved.getId());
        return saved;
    }

    /**
     * Méthode Fallback pour createAppointment.
     * Appelée automatiquement quand le Circuit Breaker est ouvert.
     * La signature doit être identique + un paramètre Throwable à la fin.
     */
    public Appointment createAppointmentFallback(Appointment appointment, Throwable ex) {
        log.error("[CIRCUIT BREAKER OUVERT] Impossible de créer le rendez-vous. Patient Service indisponible. Erreur: {}", ex.getMessage());
        throw new RuntimeException(
            "Service temporairement indisponible. Impossible de vérifier l'existence du patient. " +
            "Veuillez réessayer dans quelques instants.");
    }

    /**
     * Met à jour le statut d'un rendez-vous.
     */
    @Transactional
    public Appointment updateStatut(Long id, Appointment.StatutRendezVous newStatut) {
        Appointment appointment = getAppointmentById(id);
        appointment.setStatut(newStatut);
        return appointmentRepository.save(appointment);
    }

    /**
     * Annule un rendez-vous.
     */
    @Transactional
    public Appointment cancelAppointment(Long id) {
        return updateStatut(id, Appointment.StatutRendezVous.ANNULE);
    }

    /**
     * Récupère tous les rendez-vous.
     */
    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }
}
