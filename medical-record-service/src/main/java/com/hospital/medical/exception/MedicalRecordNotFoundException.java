package com.hospital.medical.exception;

public class MedicalRecordNotFoundException extends RuntimeException {
    public MedicalRecordNotFoundException(String message) {
        super(message);
    }
}
