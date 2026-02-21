package com.hospital.patient.exception;

/**
 * Exception levée quand un patient n'est pas trouvé dans la base de données.
 */
public class PatientNotFoundException extends RuntimeException {

    public PatientNotFoundException(Long id) {
        super("Patient introuvable avec l'ID : " + id);
    }

    public PatientNotFoundException(String message) {
        super(message);
    }
}
