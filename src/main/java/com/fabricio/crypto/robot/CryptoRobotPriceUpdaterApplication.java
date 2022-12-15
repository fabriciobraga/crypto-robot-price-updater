package com.fabricio.crypto.robot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;


@EnableAutoConfiguration
@EnableAsync
@EnableScheduling
@SpringBootApplication
public class CryptoRobotPriceUpdaterApplication {

	public static void main(String[] args) {
		SpringApplication.run(CryptoRobotPriceUpdaterApplication.class, args);
	}

}
