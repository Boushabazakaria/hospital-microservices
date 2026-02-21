package com.hospital.appointment.repository;

import com.hospital.appointment.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository Appointment - accès à la base de données des rendez-vous.
 */
@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    // Tous les rendez-vous d'un patient donné
    List<Appointment> findByPatientId(Long patientId);

    // Rendez-vous d'un patient avec un statut précis
    List<Appointment> findByPatientIdAndStatut(Long patientId, Appointment.StatutRendezVous statut);
}
