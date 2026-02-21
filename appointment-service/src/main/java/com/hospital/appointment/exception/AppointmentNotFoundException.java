package com.hospital.appointment.exception;

public class AppointmentNotFoundException extends RuntimeException {
    public AppointmentNotFoundException(Long id) {
        super("Rendez-vous introuvable avec l'ID : " + id);
    }
}
