package com.scoutifii.biometric.providers.digitalpersona;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import org.apache.commons.io.IOUtils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;

public class FingerPrintHttpURLConnection {
	  public StringBuffer getResponseString(BufferedReader bufferedReader) throws IOException {
	    StringBuffer response = new StringBuffer();
	    String inputLine;
	    while ((inputLine = bufferedReader.readLine()) != null)
	      response.append(inputLine); 
	    return response;
	  }
	  
	  public Result postReal(Result r) throws IOException {
	    CloseableHttpClient closeableHttpClient = HttpClientBuilder.create().build();
	    ObjectMapper mapper = new ObjectMapper();
	    String json = mapper.writeValueAsString(r);
	    StringEntity requestEntity = new StringEntity(json, ContentType.APPLICATION_JSON);
	    String url = "http://localhost:9191";
	    HttpPost postMethod = new HttpPost(url);
	    postMethod.setEntity((HttpEntity)requestEntity);
	    HttpResponse response = closeableHttpClient.execute((HttpUriRequest)postMethod);
	    if (response.getStatusLine().getStatusCode() == 200 || response.getStatusLine().getStatusCode() == 201) {
	      BufferedReader br = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
	      String message = IOUtils.toString(br);
	      Result returnResult = (Result)mapper.readValue(message, Result.class);
	      return returnResult;
	    } 
	    throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
	  }
	}

