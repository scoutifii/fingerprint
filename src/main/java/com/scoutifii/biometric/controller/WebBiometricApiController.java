package com.scoutifii.biometric.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map.Entry;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;

import com.digitalpersona.onetouch.DPFPGlobal;
import com.digitalpersona.onetouch.DPFPSample;
import com.digitalpersona.onetouch.DPFPTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.scoutifii.biometric.infrastructure.fingerprint.domain.Fingerprint;
import com.scoutifii.biometric.providers.digitalpersona.DigitalPersona;
import com.scoutifii.biometric.providers.digitalpersona.Result;

@Controller
public class WebBiometricApiController {
	@Autowired
	private final DigitalPersona digitalPersona;
	  
	public WebBiometricApiController(DigitalPersona digitalPersona) {
	  this.digitalPersona = digitalPersona;
	}
	@MessageMapping({"/init"})
	@SendTo({"/topic/showResult"})
	public Result fingerprint() throws Exception {
	 DPFPSample sample = this.digitalPersona.getSample(null, "Please swipe your finger");
	  return new Result(Base64.encodeBase64String(sample.serialize()), "sample");
	}
	  
	@MessageMapping({"/enroll"})
	@SendTo({"/topic/showResult"})
	public Result addFingerprint(Fingerprint input) throws Exception {
	  DPFPTemplate temp = this.digitalPersona.getTemplate(null, input.getFinger().intValue());
	  byte[] b = temp.serialize();
	  return new Result(Base64.encodeBase64String(b), "template", input.getPatient(), input.getFinger(), b, null);
	}
	  
	@MessageMapping({"/save"})
	@SendTo({"/topic/showResult"})
	public HashMap<String, String> saveFingerprint(@Payload String json) {
	  ObjectMapper mapper = new ObjectMapper();
	  HashMap<String, String> responseObj = new HashMap<>();
	  try {
	      Fingerprint dao = (Fingerprint)mapper.readerFor(Fingerprint.class).readValue(json);
	      this.digitalPersona.insert(dao.getPatient(), dao.getFinger().intValue(), dao.getFingerPrint().getBytes(), getTimestamp());
	      responseObj.put("id", String.valueOf(dao.getIndex()));
	      responseObj.put("patient", String.valueOf(dao.getPatient()));
	      responseObj.put("finger", String.valueOf(dao.getFinger()));
	      responseObj.put("message", "Patient fingerprint saved");
	    } catch (IOException e) {
	      e.printStackTrace();
	    } 
	    return responseObj;
	}
	  
	  @MessageMapping({"/search"})
	  @SendTo({"/topic/showResult"})
	  public Result search() throws Exception {
	    ListMultimap<String, byte[]> others = this.digitalPersona.get();
	    ArrayListMultimap<String, DPFPTemplate> arrayListMultimap = ArrayListMultimap.create();
	    for (Entry<String, byte[]> entry : others.entries()) {
	      String key = String.valueOf(entry.getKey());
	      byte[] val = (byte[])entry.getValue();
	      DPFPTemplate temp2 = DPFPGlobal.getTemplateFactory().createTemplate();
	      temp2.deserialize(val);
	      arrayListMultimap.put(key, temp2);
	    } 
	    return this.digitalPersona.verify(null, (ListMultimap<String, DPFPTemplate>)arrayListMultimap);
	  }
	  
	  @MessageMapping({"/delete"})
	  public HashMap<String, String> deleteFingerprint(@RequestParam(value = "patient", required = false) String patient) {
	    this.digitalPersona.delete(patient);
	    HashMap<String, String> obj = new HashMap<>();
	    obj.put("result", "Patient fingerprint Deleted");
	    return obj;
	  }
	  
	  private String getTimestamp() {
	    LocalDateTime date = LocalDateTime.now();
	    return date.format(DateTimeFormatter.ISO_DATE_TIME);
	  }
	  
	  @MessageExceptionHandler
	  @SendTo({"/queue/errors"})
	  public Result handleException(Throwable exception) {
	    return new Result(exception
	        .toString(), "error");
	  }
}
