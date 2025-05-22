package com.scoutifii.biometric.infrastructure.fingerprint.data;

import java.io.Serializable;

public class FingerPrintTemplate implements Serializable {
	 
	private static final long serialVersionUID = 1L;

	private String fingerprintId;
	  
	private String finger;
	  
	private String fingerPrint;
	  
	private String dateCreated;
	  
	public FingerPrintTemplate() {}
	  
	public FingerPrintTemplate(String fingerprintId, String finger, String fingerPrint, String dateCreated) {
	    this.fingerprintId = fingerprintId;
	    this.finger = finger;
	    this.fingerPrint = fingerPrint;
	    this.dateCreated = dateCreated;
	  }
	  
	  public String getFingerprintId() {
	    return this.fingerprintId;
	  }
	  
	  public void setFingerprintId(String fingerprintId) {
	    this.fingerprintId = fingerprintId;
	  }
	  
	  public String getFinger() {
	    return this.finger;
	  }
	  
	  public void setFinger(String finger) {
	    this.finger = finger;
	  }
	  
	  public String getFingerPrint() {
	    return this.fingerPrint;
	  }
	  
	  public void setFingerPrint(String fingerPrint) {
	    this.fingerPrint = fingerPrint;
	  }
	  
	  public String getDateCreated() {
	    return this.dateCreated;
	  }
	  
	  public void setDateCreated(String dateCreated) {
	    this.dateCreated = dateCreated;
	  }
	}
