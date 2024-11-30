package com.example.userregistration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

/**
 * @author norulshahlam.mohsen
 */
@SpringBootApplication
@ConfigurationPropertiesScan
public class TutorialsDataAuditApplication {

	public static void main(String[] args) {
		SpringApplication.run(TutorialsDataAuditApplication.class, args);
	}

}
