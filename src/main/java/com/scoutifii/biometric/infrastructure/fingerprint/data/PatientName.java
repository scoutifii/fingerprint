package com.scoutifii.biometric.infrastructure.fingerprint.data;

import java.io.Serializable;

public class PatientName implements Serializable {
	
	private static final long serialVersionUID = 8452164722759735613L;
	private String FamilyName;
	private String GivenName;
	private String MiddleName;	
	
	public PatientName(String familyName, String givenName, String middleName) {
		super();
		FamilyName = familyName;
		GivenName = givenName;
		MiddleName = middleName;
	}

	public PatientName() {
	}

	public String getFamilyName() {
		return FamilyName;
	}

	public String getGivenName() {
		return GivenName;
	}

	public String getMiddleName() {
		return MiddleName;
	}

	public void setFamilyName(String string) {
	}

	public void setGivenName(String string) {
		
	}

	public void setMiddleName(String string) {
		
	}
}
