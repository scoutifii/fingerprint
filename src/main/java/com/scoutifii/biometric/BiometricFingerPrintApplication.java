package com.scoutifii.biometric;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.scoutifii.biometric"})
public class BiometricFingerPrintApplication {

	public static void main(String[] args) {
		SpringApplication.run(BiometricFingerPrintApplication.class, args);
		
		System.out.println("Biometric FingerPrint Enrollment");

	}

}
