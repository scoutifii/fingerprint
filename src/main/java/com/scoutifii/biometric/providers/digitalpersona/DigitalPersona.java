package com.scoutifii.biometric.providers.digitalpersona;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

import javax.imageio.ImageIO;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import com.digitalpersona.onetouch.DPFPDataPurpose;
import com.digitalpersona.onetouch.DPFPFeatureSet;
import com.digitalpersona.onetouch.DPFPFingerIndex;
import com.digitalpersona.onetouch.DPFPGlobal;
import com.digitalpersona.onetouch.DPFPSample;
import com.digitalpersona.onetouch.DPFPTemplate;
import com.digitalpersona.onetouch.capture.DPFPCapture;
import com.digitalpersona.onetouch.capture.DPFPCapturePriority;
import com.digitalpersona.onetouch.capture.event.DPFPDataEvent;
import com.digitalpersona.onetouch.capture.event.DPFPDataListener;
import com.digitalpersona.onetouch.capture.event.DPFPReaderStatusAdapter;
import com.digitalpersona.onetouch.capture.event.DPFPReaderStatusEvent;
import com.digitalpersona.onetouch.capture.event.DPFPReaderStatusListener;
import com.digitalpersona.onetouch.processing.DPFPEnrollment;
import com.digitalpersona.onetouch.processing.DPFPFeatureExtraction;
import com.digitalpersona.onetouch.processing.DPFPImageQualityException;
import com.digitalpersona.onetouch.readers.DPFPReaderDescription;
import com.digitalpersona.onetouch.readers.DPFPReadersCollection;
import com.digitalpersona.onetouch.verification.DPFPVerification;
import com.digitalpersona.onetouch.verification.DPFPVerificationResult;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;

@Component
public class DigitalPersona {
  private final SimpMessagingTemplate websocket;
  
  @Autowired
  public DigitalPersona(SimpMessagingTemplate websocket) {
    this.websocket = websocket;
  }
  
  public Connection cn() {
    Connection conn = null;
    String username = "root";
    String password = "";
    String driver = "com.mysql.cj.jdbc.Driver";
    String url = "jdbc:mysql://localhost:3306/openemr";
    try {
      Class.forName(driver);
      conn = DriverManager.getConnection(url, username, password);
    } catch (Exception e) {
      System.out.println("SQL Connection Exception:" + e.getMessage());
    } 
    return conn;
  }
  
  public void insert(String patient, int finger, byte[] fingerprint, String dateCreated) {
    String name = String.valueOf(DPFPFingerIndex.values()[finger]);
    try {
      PreparedStatement st = cn().prepareStatement("INSERT INTO FingerPrint(FingerPrintName, PatientNo,FingerIndex,FingerPrint,DateEnrolled) VALUES(?, ?, ?, ?, ?)");
      st.setString(1, name);
      st.setString(2, patient);
      st.setInt(3, finger);
      st.setBytes(4, fingerprint);
      st.setString(5, dateCreated);
      st.executeUpdate();
    } catch (SQLException e) {
      System.out.println("SQLException -->" + e.getMessage());
    } 
  }
  
  public void delete(String patientId) {
    try {
      PreparedStatement st = cn().prepareStatement("delete from Fingerprint where patient ='" + patientId + "'");
      st.executeUpdate();
    } catch (SQLException e) {
      System.out.println("SQLException -->" + e.getMessage());
    } 
  }
  
  public void createTable() {
    String statement = "CREATE TABLE IF NOT EXISTS FingerPrint (\n  FingerPrintId INT(32) AUTO_INCREMENT,\n  PatientNo   CHAR(38),\n  FingerIndex         INT(1),\n  FingerPrint    TEXT,\n  PRIMARY KEY (FingerPrintId))";
    try {
      Statement st = cn().createStatement();
      st.execute(statement);
    } catch (SQLException e) {
      e.printStackTrace();
    } 
  }
  
  public ListMultimap<String, byte[]> get() {
    ArrayListMultimap<String, byte[]> arrayListMultimap = ArrayListMultimap.create();
    try {
      PreparedStatement st = cn().prepareStatement("SELECT * FROM FingerPrint");
      ResultSet rs = st.executeQuery();
      while (rs.next()) {
        String fingerprint = rs.getString("FingerPrint");
        byte[] b = Base64.decodeBase64(fingerprint);
        arrayListMultimap.put(rs.getString("PatientNo"), b);
      } 
    } catch (Exception e) {
      System.out.println("Exception " + e.getMessage());
    } 
    return (ListMultimap<String, byte[]>)arrayListMultimap;
  }
  
  public void listReaders() {
    DPFPReadersCollection readers = DPFPGlobal.getReadersFactory().getReaders();
    if (readers == null || readers.size() == 0) {
      System.out.printf("There are no readers available.\n", new Object[0]);
      this.websocket.convertAndSend("/topic/showResult", new Result("There are no readers available.\n"));
      return;
    } 
    System.out.printf("Available readers:\n", new Object[0]);
    for (DPFPReaderDescription readerDescription : readers)
      System.out.println("Reader SerialNumber: " + readerDescription.getSerialNumber()); 
  }
  
  public static final EnumMap<DPFPFingerIndex, String> fingerNames = new EnumMap<>(DPFPFingerIndex.class);
  
  static {
    fingerNames.put(DPFPFingerIndex.LEFT_PINKY, "left pinky");
    fingerNames.put(DPFPFingerIndex.LEFT_RING, "left ring");
    fingerNames.put(DPFPFingerIndex.LEFT_MIDDLE, "left middle");
    fingerNames.put(DPFPFingerIndex.LEFT_INDEX, "left index");
    fingerNames.put(DPFPFingerIndex.LEFT_THUMB, "left thumb");
    fingerNames.put(DPFPFingerIndex.RIGHT_PINKY, "right pinky");
    fingerNames.put(DPFPFingerIndex.RIGHT_RING, "right ring");
    fingerNames.put(DPFPFingerIndex.RIGHT_MIDDLE, "right middle");
    fingerNames.put(DPFPFingerIndex.RIGHT_INDEX, "right index");
    fingerNames.put(DPFPFingerIndex.RIGHT_THUMB, "right thumb");
  }
  
  public DPFPTemplate getTemplate(String activeReader, int nFinger) {
    this.websocket.convertAndSend("/topic/showResult", new Result("Device is already busy enrolling a fingerprint...\n"));
    DPFPTemplate template = null;
    try {
      DPFPFingerIndex finger = DPFPFingerIndex.values()[nFinger];
      DPFPFeatureExtraction featureExtractor = DPFPGlobal.getFeatureExtractionFactory().createFeatureExtraction();
      DPFPEnrollment enrollment = DPFPGlobal.getEnrollmentFactory().createEnrollment();
      while (enrollment.getFeaturesNeeded() > 0) {
        DPFPFeatureSet featureSet;
        DPFPSample sample = getSample(activeReader, 
            String.format("Scan your %s finger (%d remaining)\n", new Object[] { fingerName(finger), Integer.valueOf(enrollment.getFeaturesNeeded()) }));
        if (sample == null)
          continue; 
        try {
          featureSet = featureExtractor.createFeatureSet(sample, DPFPDataPurpose.DATA_PURPOSE_ENROLLMENT);
        } catch (DPFPImageQualityException e) {
          this.websocket.convertAndSend("/topic/showResult", new Result(String.format("Bad image quality: %s. Try again. \n", new Object[] { e.getCaptureFeedback().toString() })));
          continue;
        } 
        enrollment.addFeatures(featureSet);
      } 
      template = enrollment.getTemplate();
      this.websocket.convertAndSend("/topic/showResult", new Result(String.format("The %s was enrolled.\n", new Object[] { fingerprintName(finger) })));
    } catch (DPFPImageQualityException e) {
      this.websocket.convertAndSend("/topic/showResult", new Result("Failed to enroll the finger.\n"));
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    } 
    return template;
  }
  
  public Result verify(String activeReader, ListMultimap<String, DPFPTemplate> templates) {
    Result result = new Result();
    try {
      DPFPSample sample = getSample(activeReader, "Scan your finger\n");
      if (sample == null)
        throw new Exception(); 
      DPFPFeatureExtraction featureExtractor = DPFPGlobal.getFeatureExtractionFactory().createFeatureExtraction();
      DPFPFeatureSet featureSet = featureExtractor.createFeatureSet(sample, DPFPDataPurpose.DATA_PURPOSE_VERIFICATION);
      DPFPVerification matcher = DPFPGlobal.getVerificationFactory().createVerification();
      matcher.setFARRequested(21474);
      for (DPFPFingerIndex finger : DPFPFingerIndex.values()) {
        for (Map.Entry<String, DPFPTemplate> entry : templates.entries()) {
          String key = String.valueOf(entry.getKey());
          DPFPTemplate template = (DPFPTemplate)entry.getValue();
          if (template != null) {
            DPFPVerificationResult verificationResult = matcher.verify(featureSet, template);
            this.websocket.convertAndSend("/topic/showResult", new Result(String.format("Received input, verifying and matching ", new Object[] { fingerName(finger) })));
            if (verificationResult.isVerified()) {
              this.websocket.convertAndSend("/topic/showResult", new Result(String.format("Matching finger: %s, FAR achieved: %g.\n", new Object[] { fingerName(finger), Double.valueOf(verificationResult.getFalseAcceptRate() / 2.147483647E9D) })));
              result.setPatient(key);
              result.setType("local");
              return result;
            } 
          } 
        } 
      } 
      if (PatientManagerUtils.testInternet("google.com")) {
        this.websocket.convertAndSend("/topic/showResult", new Result("Your fingerprint couldn't be recognized, try using a different finger"));
        FingerPrintHttpURLConnection fingerPrintHttpURLConnection = new FingerPrintHttpURLConnection();
        Result resultToServer = new Result();
        resultToServer.setFingerprint(sample.serialize());
        Result serverResult = fingerPrintHttpURLConnection.postReal(resultToServer);
        if (serverResult.getPatient() != null) {
          this.websocket.convertAndSend("/topic/showResult", new Result("Patient found online"));
          serverResult.setType("online");
          return serverResult;
        } 
        this.websocket.convertAndSend("/topic/showResult", new Result("No patient found online"));
      } else {
        this.websocket.convertAndSend("/topic/showResult", new Result("No internet connectivity, so aborting online search"));
      } 
    } catch (Exception e) {
      return null;
    } 
    return result;
  }
  
  public DPFPSample getSample(String activeReader, String prompt) throws InterruptedException {
    final LinkedBlockingQueue<DPFPSample> samples = new LinkedBlockingQueue<>();
    DPFPCapture capture = DPFPGlobal.getCaptureFactory().createCapture();
    capture.setReaderSerialNumber(activeReader);
    capture.setPriority(DPFPCapturePriority.CAPTURE_PRIORITY_LOW);
    capture.addDataListener(new DPFPDataListener() {
          public void dataAcquired(DPFPDataEvent e) {
            if (e != null && e.getSample() != null)
              try {
                DPFPSample fromDevice = e.getSample();
                DigitalPersona.this.websocket.convertAndSend("/topic/showResult", new Result(DigitalPersona.this.encodeToString(fromDevice, "png"), "image"));
                samples.put(e.getSample());
              } catch (InterruptedException e1) {
                e1.printStackTrace();
              }  
          }
        });
    capture.addReaderStatusListener((DPFPReaderStatusListener)new DPFPReaderStatusAdapter() {
          int lastStatus = 3;
          
          public void readerConnected(DPFPReaderStatusEvent e) {
            if (this.lastStatus != e.getReaderStatus())
              DigitalPersona.this.websocket.convertAndSend("/topic/showResult", new Result("Reader is connected")); 
            this.lastStatus = e.getReaderStatus();
          }
          
          public void readerDisconnected(DPFPReaderStatusEvent e) {
            if (this.lastStatus != e.getReaderStatus())
              DigitalPersona.this.websocket.convertAndSend("/topic/showResult", new Result("Reader is disconnected")); 
            this.lastStatus = e.getReaderStatus();
          }
        });
    try {
      capture.startCapture();
      this.websocket.convertAndSend("/topic/showResult", new Result(prompt));
      DPFPSample template = samples.take();
      return template;
    } catch (RuntimeException e) {
      this.websocket.convertAndSend("/topic/showResult", new Result("Failed to start capture. Check that reader is not used by another application."));
      throw e;
    } finally {
      capture.stopCapture();
    } 
  }
  
  public String fingerName(DPFPFingerIndex finger) {
    return fingerNames.get(finger);
  }
  
  public String fingerprintName(DPFPFingerIndex finger) {
    return (String)fingerNames.get(finger) + " fingerprint";
  }
  
  public String encodeToString(DPFPSample sample, String type) {
    String imageString = null;
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    BufferedImage image = (BufferedImage)DPFPGlobal.getSampleConversionFactory().createImage(sample);
    try {
      ImageIO.write(image, type, bos);
      byte[] imageBytes = bos.toByteArray();
      imageString = Base64.encodeBase64String(imageBytes);
      bos.close();
    } catch (IOException e) {
      e.printStackTrace();
    } 
    return imageString;
  }
  
  public byte[] encodeToByte(DPFPSample sample, String type) {
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    BufferedImage image = (BufferedImage)DPFPGlobal.getSampleConversionFactory().createImage(sample);
    try {
      ImageIO.write(image, type, bos);
      byte[] imageBytes = bos.toByteArray();
      return imageBytes;
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    } 
  }
 }
