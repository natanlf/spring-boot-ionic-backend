package com.natanlf.cursomc.config;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.natanlf.cursomc.services.DBService;
import com.natanlf.cursomc.services.EmailService;
import com.natanlf.cursomc.services.MockEmailService;

//Como na classe Application.propeties está com o profile test ativo, essa classe será usada
@Configuration
@Profile("test")
public class TestConfig { 
	
	@Autowired
	private DBService dbService; 

	@Bean
	public boolean instantiateDatabase() throws ParseException {
		dbService.instantiateTestDatabase(); //instancio a classe que popula o banco
		return true;
	}
	
	//Quando fazemos um método com @Bean, o mesmo vai ficar disponível em nosso sistema
	@Bean
	public EmailService emailService() {
		return new MockEmailService();
	}
}
