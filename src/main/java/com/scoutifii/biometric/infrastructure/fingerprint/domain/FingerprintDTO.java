/* Data Transfer Object for Fingerprint class*/

package com.scoutifii.biometric.infrastructure.fingerprint.domain;

import java.util.Date;


public class FingerprintDTO {
	 private Integer id;
	 private String patientId;
	 private Integer finger;
	 private String fingerPrint;
	 private Date dateCreated;
	 
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getPatientId() {
		return patientId;
	}
	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}
	public Integer getFinger() {
		return finger;
	}
	public void setFinger(Integer finger) {
		this.finger = finger;
	}
	public String getFingerPrint() {
		return fingerPrint;
	}
	public void setFingerPrint(String fingerPrint) {
		this.fingerPrint = fingerPrint;
	}
	public Date getDateCreated() {
		return dateCreated;
	}
	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}
}
