package com.zosh;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.zosh"})
public class EcommerceMultivendorApplication {

	public static void main(String[] args) {
		SpringApplication.run(EcommerceMultivendorApplication.class, args);
	}

}
