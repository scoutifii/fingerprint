package com.scoutifii.biometric.providers.digitalpersona;

import java.io.Serializable;
import java.util.Map;

public class Result implements Serializable{
	private static final long serialVersionUID = 1L;
	private String result;
	private String type;
	  
	private String patient;
	  
	private Integer finger;
	  
	private byte[] fingerprint;
	  
	Map<String, String> patientSummary;
	
	public Result() {}
	  
	public Result(String result) {
	  this.result = result;
	  this.type = "string";
	}
	  
	public Result(String result, String type) {
	  this.result = result;
	  this.type = type;
	}
	  
	public Result(String result, String type, String integer, Integer finger, byte[] fingerprint, Map<String, String> patientSummary) {
		super();
		this.result = result;
		this.type = type;
		this.patient = integer;
		this.finger = finger;
		this.fingerprint = fingerprint;
		this.patientSummary = patientSummary;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPatient() {
		return patient;
	}

	public void setPatient(String patient) {
		this.patient = patient;
	}

	public Integer getFinger() {
		return finger;
	}

	public void setFinger(Integer finger) {
		this.finger = finger;
	}

	public byte[] getFingerprint() {
		return fingerprint;
	}

	public void setFingerprint(byte[] fingerprint) {
		this.fingerprint = fingerprint;
	}

	public Map<String, String> getPatientSummary() {
		return this.patientSummary;
	}

	public void setPatientSummary(Map<String, String> patientSummary) {
		this.patientSummary = patientSummary;
	}
	public void addPatientAttribute(String key, String value) {
	    getPatientSummary().put(key, value);
	  }
}
