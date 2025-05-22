package com.scoutifii.biometric.infrastructure.fingerprint.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.scoutifii.biometric.infrastructure.fingerprint.data.Facility;
import com.scoutifii.biometric.infrastructure.fingerprint.data.PatientName;
import com.scoutifii.biometric.infrastructure.fingerprint.domain.Fingerprint;
import com.scoutifii.biometric.infrastructure.fingerprint.domain.FingerprintDTO;
import com.scoutifii.biometric.infrastructure.fingerprint.exception.FingerPrintNotFoundException;
import com.scoutifii.biometric.infrastructure.fingerprint.repository.FingerprintRepository;

@Service
public class FingerprintService {
  private static final Logger logger = LoggerFactory.getLogger(com.scoutifii.biometric.infrastructure.fingerprint.service.FingerprintService.class);
  @Autowired
  private final FingerprintRepository repository;
  @Autowired
  private ModelMapper modelMapper;
  
  public FingerprintService(FingerprintRepository repository) {
    this.repository = repository;
  }
  
  public List<Fingerprint> retrieveFingerPrints(String patientId) {
    return this.repository.findByPatientId(String.valueOf(patientId));
  }
  
  @Transactional
  public Fingerprint savePatientFingerPrint(String patient, int finger, byte[] fingerprint) {
    Fingerprint dao = new Fingerprint();
    dao.setPatient(patient);
    dao.setFinger(Integer.valueOf(finger));
    dao.setFingerPrint(String.valueOf(fingerprint));
    dao.setDateCreated(new Date());
    return (Fingerprint)this.repository.save(dao);
  }
  
  public String deletePatientFingerPrint(String patientId) throws FingerPrintNotFoundException {
    List<Fingerprint> fingerPrints = this.repository.findByPatientId(String.valueOf(patientId));
    if (fingerPrints == null)
      throw new FingerPrintNotFoundException(patientId); 
    logger.debug("Batch deletion of fingerprint for patient " + patientId + ".....");
    this.repository.deleteAll(fingerPrints);
    return patientId;
  }
  
  public boolean isUUID(String uuid) {
    try {
      UUID.fromString(uuid);
      return true;
    } catch (Exception ex) {
      return false;
    } 
  }
  
  public List<PatientName> getPatientNames(Map<?, ?> jsonObject) {
    List<PatientName> patientNames = new ArrayList<>();
    PatientName patientName = new PatientName();
    patientName.setFamilyName(jsonObject.get("familyName").toString());
    if (jsonObject.get("middleName") != null)
      patientName.setMiddleName(jsonObject.get("middleName").toString()); 
    patientName.setGivenName(jsonObject.get("givenName").toString());
    patientNames.add(patientName);
    return patientNames;
  }
  
  public Facility getPatientFacility(Map<?, ?> map) {
    Facility facility = new Facility();
    facility.setName(map.get("name").toString());
    facility.setFacilityId(map.get("uuid").toString());
    return facility;
  }
  
  public FingerprintDTO getFingerprintDTO(String patientid) {
	  Fingerprint fingerprint = repository.findById(patientid).orElseThrow();
	return modelMapper.map(fingerprint, FingerprintDTO.class);
	  
  }
}
