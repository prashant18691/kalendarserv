package com.prs.kalendar.kalendarserv;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan("com.prs.kalendar.kalendarserv")
@SpringBootApplication
public class KalendarservApplication {

	public static void main(String[] args) {
		SpringApplication.run(KalendarservApplication.class, args);
	}

}
