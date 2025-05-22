package com.scoutifii.biometric.exception;

public abstract class AbstractPlatformResourceNotFoundException extends RuntimeException {
	 
	private static final long serialVersionUID = 1L;

	private final String globalisationMessageCode;
	  
	private final String defaultUserMessage;
	  
	private final Object[] defaultUserMessageArgs;
	  
	public AbstractPlatformResourceNotFoundException(String globalisationMessageCode, String defaultUserMessage, Object... defaultUserMessageArgs) {
	    this.globalisationMessageCode = globalisationMessageCode;
	    this.defaultUserMessage = defaultUserMessage;
	    this.defaultUserMessageArgs = defaultUserMessageArgs;
	  }
	  
	  public String getGlobalisationMessageCode() {
	    return this.globalisationMessageCode;
	  }
	  
	  public String getDefaultUserMessage() {
	    return this.defaultUserMessage;
	  }
	  
	  public Object[] getDefaultUserMessageArgs() {
	    return this.defaultUserMessageArgs;
	  }
	}
