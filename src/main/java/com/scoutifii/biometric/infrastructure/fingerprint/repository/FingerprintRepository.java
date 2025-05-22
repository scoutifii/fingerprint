package com.scoutifii.biometric.infrastructure.fingerprint.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.scoutifii.biometric.infrastructure.fingerprint.domain.Fingerprint;

@Repository
public interface FingerprintRepository extends JpaRepository<Fingerprint, String>, JpaSpecificationExecutor<Fingerprint> {
  List<Fingerprint> findByPatientId(@Param("patientId") String paramString);
}