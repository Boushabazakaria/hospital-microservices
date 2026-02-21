package com.hospital.medical.repository;

import com.hospital.medical.entity.MedicalRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Long> {
    Optional<MedicalRecord> findByPatientId(Long patientId);
    boolean existsByPatientId(Long patientId);
}
