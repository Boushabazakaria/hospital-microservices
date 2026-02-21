package com.hospital.appointment.controller;

import com.hospital.appointment.entity.Appointment;
import com.hospital.appointment.exception.AppointmentNotFoundException;
import com.hospital.appointment.service.AppointmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Contrôleur REST Appointment.
 * Expose les endpoints de gestion des rendez-vous.
 */
@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;

    /** GET /api/appointments - Tous les rendez-vous */
    @GetMapping
    public ResponseEntity<List<Appointment>> getAllAppointments() {
        return ResponseEntity.ok(appointmentService.getAllAppointments());
    }

    /** GET /api/appointments/{id} - Un rendez-vous par ID */
    @GetMapping("/{id}")
    public ResponseEntity<Appointment> getAppointmentById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(appointmentService.getAppointmentById(id));
        } catch (AppointmentNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /** GET /api/appointments/patient/{patientId} - RDV d'un patient */
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<Appointment>> getAppointmentsByPatient(@PathVariable Long patientId) {
        return ResponseEntity.ok(appointmentService.getAppointmentsByPatientId(patientId));
    }

    /**
     * POST /api/appointments - Crée un rendez-vous.
     * Vérifie d'abord l'existence du patient via le Patient Service.
     */
    @PostMapping
    public ResponseEntity<?> createAppointment(@Valid @RequestBody Appointment appointment) {
        try {
            Appointment created = appointmentService.createAppointment(appointment);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (IllegalArgumentException e) {
            // Patient introuvable
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            // Service indisponible (Circuit Breaker ouvert)
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(e.getMessage());
        }
    }

    /** PATCH /api/appointments/{id}/annuler - Annule un rendez-vous */
    @PatchMapping("/{id}/annuler")
    public ResponseEntity<Appointment> cancelAppointment(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(appointmentService.cancelAppointment(id));
        } catch (AppointmentNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /** PATCH /api/appointments/{id}/statut - Met à jour le statut */
    @PatchMapping("/{id}/statut")
    public ResponseEntity<Appointment> updateStatut(@PathVariable Long id,
                                                     @RequestParam Appointment.StatutRendezVous statut) {
        try {
            return ResponseEntity.ok(appointmentService.updateStatut(id, statut));
        } catch (AppointmentNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
