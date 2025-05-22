package com.scoutifii.biometric.infrastructure.fingerprint.remoteserver;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.scoutifii.biometric.helpers.GlobalProperty;

@Component
public class FingerPrintGlobalProperties {
  private static final Logger logger = LoggerFactory.getLogger(com.scoutifii.biometric.infrastructure.fingerprint.remoteserver.FingerPrintGlobalProperties.class);
  
  public void setFingerPrintGlobalProperties() {}
  
  public List<GlobalProperty> configureGlobalProperties() {
    List<GlobalProperty> properties = new ArrayList<>();
    properties.add(new GlobalProperty("fingerprint.enableRightPinky", "true"));
    properties.add(new GlobalProperty("fingerprint.enableRightRing", "true"));
    properties.add(new GlobalProperty("fingerprint.enableRightMiddle", "true"));
    properties.add(new GlobalProperty("fingerprint.enableRightIndex", "true"));
    properties.add(new GlobalProperty("fingerprint.enableRightThumb", "true"));
    properties.add(new GlobalProperty("fingerprint.enableLeftPinky", "true"));
    properties.add(new GlobalProperty("fingerprint.enableLeftRing", "true"));
    properties.add(new GlobalProperty("fingerprint.enableLeftMiddle", "true"));
    properties.add(new GlobalProperty("fingerprint.enableLeftIndex", "true"));
    properties.add(new GlobalProperty("fingerprint.enableLeftThumb", "true"));
    logger.info("Finger print accepted");
    return properties;
  }
}
