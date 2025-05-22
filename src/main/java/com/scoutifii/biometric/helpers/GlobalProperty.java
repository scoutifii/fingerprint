package com.scoutifii.biometric.helpers;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class GlobalProperty {
	  private String property = "";
	  
	  private String propertyValue = "";
	  
	  private transient Object typedValue;
	  
	  private boolean dirty = false;
	  
	  private String description = "";
	  
	  private String datatypeClassname;
	  
	  private String datatypeConfig;
	  
	  private String preferredHandlerClassname;
	  
	  private String handlerConfig;
	  
	  private Date dateChanged;
	  
	  public GlobalProperty() {}
	  
	  public GlobalProperty(String property) {
	    this.property = property;
	  }
	  
	  public GlobalProperty(String property, String value) {
	    this(property);
	    this.propertyValue = value;
	  }
	  
	  public GlobalProperty(String property, String value, String description) {
	    this(property, value);
	    this.description = description;
	  }
	  
	  public GlobalProperty(String property, String value, String description, String datatypeConfig) {
	    this(property, value, description);
	    this.datatypeConfig = datatypeConfig;
	  }
	  
	  public String getProperty() {
	    return this.property;
	  }
	  
	  public void setProperty(String property) {
	    this.property = property;
	  }
	  
	  public String getPropertyValue() {
	    return this.propertyValue;
	  }
	  
	  public void setPropertyValue(String propertyValue) {
	    this.propertyValue = propertyValue;
	  }
	  
	  public String getDescription() {
	    return this.description;
	  }
	  
	  public void setDescription(String description) {
	    this.description = description;
	  }
	  
	  public Integer getId() {
	    throw new UnsupportedOperationException();
	  }
	  
	  public void setId(Integer id) {
	    throw new UnsupportedOperationException();
	  }
	  
	  public String getDatatypeClassname() {
	    return this.datatypeClassname;
	  }
	  
	  public void setDatatypeClassname(String datatypeClassname) {
	    this.datatypeClassname = datatypeClassname;
	  }
	  
	  public String getDatatypeConfig() {
	    return this.datatypeConfig;
	  }
	  
	  public void setDatatypeConfig(String datatypeConfig) {
	    this.datatypeConfig = datatypeConfig;
	  }
	  
	  public String getPreferredHandlerClassname() {
	    return this.preferredHandlerClassname;
	  }
	  
	  public void setPreferredHandlerClassname(String preferredHandlerClassname) {
	    this.preferredHandlerClassname = preferredHandlerClassname;
	  }
	  
	  public String getHandlerConfig() {
	    return this.handlerConfig;
	  }
	  
	  public void setHandlerConfig(String handlerConfig) {
	    this.handlerConfig = handlerConfig;
	  }
	  
	  public String toString() {
	    return "property: " + getProperty() + " value: " + getPropertyValue();
	  }
	  
	  public com.scoutifii.biometric.helpers.GlobalProperty getDescriptor() {
	    return this;
	  }
	  
	  public String getValueReference() {
	    return getPropertyValue();
	  }
	  
	  @Deprecated
	  @JsonIgnore
	  public boolean isDirty() {
	    return getDirty();
	  }
	  
	  public boolean getDirty() {
	    return this.dirty;
	  }
	  
	  public Date getDateChanged() {
	    return this.dateChanged;
	  }
	  
	  public void setDateChanged(Date dateChanged) {
	    this.dateChanged = dateChanged;
	  }

	public Object getTypedValue() {
		return typedValue;
	}

	public void setTypedValue(Object typedValue) {
		this.typedValue = typedValue;
	}
}
