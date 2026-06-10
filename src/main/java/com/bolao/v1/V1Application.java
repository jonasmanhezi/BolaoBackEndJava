package com.bolao.v1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication(exclude = {
		org.springdoc.core.configuration.SpringDocHateoasConfiguration.class
})
@EnableScheduling
@ComponentScan(basePackages = {
		"com.bolao.v1.core",
		"com.bolao.v1.adapter",
		"com.bolao.v1.infrastructure",
		"com.bolao.v1.api",
		"com.bolao.v1.api.rest",
		"com.bolao.v1.security",
		"com.bolao.v1.shared"
})
public class  V1Application {

	public static void main(String[] args) {
		SpringApplication.run(V1Application.class, args);
	}

}
