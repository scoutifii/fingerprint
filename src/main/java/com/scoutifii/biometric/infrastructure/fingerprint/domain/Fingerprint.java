package com.scoutifii.biometric.infrastructure.fingerprint.domain;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "FingerPrint")
public class Fingerprint implements Serializable {
  
	private static final long serialVersionUID = 3363701641751979643L;

	@Id
	@GeneratedValue
	@Column(name = "FingerPrintId")
   private Integer id;
  
  @JoinColumn(name = "PatientNo")
  private String patientId;
  
  @Basic
  @Column(name = "FingerIndex")
  private Integer finger;
  
  @Column(name = "FingerPrint", columnDefinition = "blob")
  private String fingerPrint;
  
  @Column(name = "DateEnrolled", nullable = true)
  @Temporal(TemporalType.TIMESTAMP)
  private Date dateCreated;
  
  public Fingerprint() {}
  
  public Integer getIndex() {
    return this.id;
  }
  
  public String getPatient() {
    return this.patientId;
  }
  
  public void setPatient(String patientId) {
    this.patientId = patientId;
  }
  
  public Integer getFinger() {
    return this.finger;
  }
  
  public void setFinger(Integer finger) {
    this.finger = finger;
  }
  
  public String getFingerPrint() {
    return this.fingerPrint;
  }
  
  public void setFingerPrint(String fingerPrint) {
    this.fingerPrint = fingerPrint;
  }
  
  public Fingerprint(String patientId, int finger, String fingerPrint, Date dateCreated) {
    this.id = getIndex();
    this.patientId = patientId;
    this.finger = Integer.valueOf(finger);
    this.fingerPrint = fingerPrint;
    this.dateCreated = dateCreated;
  }
 
  public Date getDateCreated() {
    return this.dateCreated;
  }
  
  public void setDateCreated(Date dateCreated) {
    this.dateCreated = dateCreated;
  }
}
