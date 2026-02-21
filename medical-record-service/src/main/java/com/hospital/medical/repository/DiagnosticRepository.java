package com.hospital.medical.repository;

import com.hospital.medical.entity.Diagnostic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiagnosticRepository extends JpaRepository<Diagnostic, Long> {
    List<Diagnostic> findByDossierMedicalId(Long dossierId);
}
