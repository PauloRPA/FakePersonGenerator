package com.prpa.FakePersonGenerator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class FakePersonGeneratorApplication {

	public static void main(String[] args) {
		SpringApplication.run(FakePersonGeneratorApplication.class, args);
	}

}
