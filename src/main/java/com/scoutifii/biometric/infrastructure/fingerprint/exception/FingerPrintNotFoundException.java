package com.scoutifii.biometric.infrastructure.fingerprint.exception;

import com.scoutifii.biometric.exception.AbstractPlatformResourceNotFoundException;

public class FingerPrintNotFoundException extends AbstractPlatformResourceNotFoundException {
	  
	private static final long serialVersionUID = 1L;

	public FingerPrintNotFoundException(String patientId) {
	    super("error.msg.patient.id.invalid", "Patient with identifier " + patientId + " does not exist", new Object[] { patientId });
	  }
	  
	public FingerPrintNotFoundException(String fingerprintId, String patientId) {
	    super("error.msg.fingerprint.patient.id.invalid", "Fingerprint with identifier " + fingerprintId + " does not exist", new Object[] { patientId });
	  }
}
