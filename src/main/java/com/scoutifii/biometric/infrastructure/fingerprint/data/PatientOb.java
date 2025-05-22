package com.scoutifii.biometric.infrastructure.fingerprint.data;

public class PatientOb {
	  public String uuid;
	  
	  public String concept;
	  
	  public String encounterDate;
	  
	  public String encounterType;
	  
	  public String valueCoded;
	  
	  public String valueText;
	  
	  public String valueNumeric;
	  
	  public String valueDatetime;
	  
	  public String valueDrug;
	  
	  public String valueBoolean;
	  
	  public String valueComplex;
	  
	  public String voided;
	  
	  public String answerSummary;
	  
	  public String getAnswerSummary() {
	    return this.answerSummary;
	  }
	  
	  public void setAnswerSummary(String answerSummary) {
	    this.answerSummary = answerSummary;
	  }
	  
	  public PatientOb() {}
	  
	  public String getConcept() {
	    return this.concept;
	  }
	  
	  public void setConcept(String concept) {
	    this.concept = concept;
	  }
	  
	  public PatientOb(String uuid, String concept, String encounterDate, String encounterType, String valueCoded, String valueText, String valueNumeric, String valueDatetime, String valueDrug, String valueBoolean, String valueComplex, String voided) {
	    this.uuid = uuid;
	    this.encounterDate = encounterDate;
	    this.encounterType = encounterType;
	    this.concept = concept;
	    this.valueCoded = valueCoded;
	    this.valueText = valueText;
	    this.valueNumeric = valueNumeric;
	    this.valueDatetime = valueDatetime;
	    this.valueDrug = valueDrug;
	    this.valueBoolean = valueBoolean;
	    this.valueComplex = valueComplex;
	    this.voided = voided;
	  }
	  
	  public String getUuid() {
	    return this.uuid;
	  }
	  
	  public void setUuid(String uuid) {
	    this.uuid = uuid;
	  }
	  
	  public String getEncounterDate() {
	    return this.encounterDate;
	  }
	  
	  public void setEncounterDate(String encounterDate) {
	    this.encounterDate = encounterDate;
	  }
	  
	  public String getEncounterType() {
	    return this.encounterType;
	  }
	  
	  public void setEncounterType(String encounterType) {
	    this.encounterType = encounterType;
	  }
	  
	  public String getValueCoded() {
	    return this.valueCoded;
	  }
	  
	  public void setValueCoded(String valueCoded) {
	    this.valueCoded = valueCoded;
	  }
	  
	  public String getValueText() {
	    return this.valueText;
	  }
	  
	  public void setValueText(String valueText) {
	    this.valueText = valueText;
	  }
	  
	  public String getValueNumeric() {
	    return this.valueNumeric;
	  }
	  
	  public void setValueNumeric(String valueNumeric) {
	    this.valueNumeric = valueNumeric;
	  }
	  
	  public String getValueDatetime() {
	    return this.valueDatetime;
	  }
	  
	  public void setValueDatetime(String valueDatetime) {
	    this.valueDatetime = valueDatetime;
	  }
	  
	  public String getValueDrug() {
	    return this.valueDrug;
	  }
	  
	  public void setValueDrug(String valueDrug) {
	    this.valueDrug = valueDrug;
	  }
	  
	  public String isValueBoolean() {
	    return this.valueBoolean;
	  }
	  
	  public void setValueBoolean(String valueBoolean) {
	    this.valueBoolean = valueBoolean;
	  }
	  
	  public String getValueBoolean() {
	    return this.valueBoolean;
	  }
	  
	  public String getValueComplex() {
	    return this.valueComplex;
	  }
	  
	  public void setValueComplex(String valueComplex) {
	    this.valueComplex = valueComplex;
	  }
	  
	  public String isVoided() {
	    return this.voided;
	  }
	  
	  public void setVoided(String voided) {
	    this.voided = voided;
	  }
	}
