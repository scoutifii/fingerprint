package com.scoutifii.biometric.infrastructure.fingerprint.data;

import java.io.Serializable;

public class Facility implements Serializable {

	private static final long serialVersionUID = 9205757337763200192L;

	String name;
	  
	String uuid;
	  
	public Facility() {}
	  
	public Facility(String name, String uuid) {
	    this.name = name;
	    this.uuid = uuid;
	  }
	  
	  public String getName() {
	    return this.name;
	  }
	  
	  public void setName(String name) {
	    this.name = name;
	  }
	  
	  public String getFacilityId() {
	    return this.uuid;
	  }
	  
	  public void setFacilityId(String uuid) {
	    this.uuid = uuid;
	  }
}
