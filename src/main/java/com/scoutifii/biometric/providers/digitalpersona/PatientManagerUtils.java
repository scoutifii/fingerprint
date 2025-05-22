package com.scoutifii.biometric.providers.digitalpersona;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class PatientManagerUtils {
	  public static String getAttributeSearch(String searchType, String attributeType, String attributeValue) {
	    return String.format("query=PatientNo(%s:{t:\"%s\",v:\"%s\"}){uuid}", new Object[] { searchType, attributeType, attributeValue });
	  }
	  
	  public static String getFingerprintSearch(String fingerprint) {
	    return String.format("query=PatientNo(FingerPrint:%s){uuid}", new Object[] { fingerprint });
	  }
	  
	  public static String getFingerprint(String fingerprint) {
	    return String.format("FingerPrint=%s", new Object[] { fingerprint });
	  }
	  
	  public static boolean testInternet(String site) {
	    Socket sock = new Socket();
	    InetSocketAddress addr = new InetSocketAddress(site, 80);
	    try {
	      sock.connect(addr, 3000);
	      return true;
	    } catch (IOException e) {
	      return false;
	    } finally {
	      try {
	        sock.close();
	      } catch (IOException iOException) {}
	    } 
	  }
	}
