package com.natanlf.cursomc.config;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.natanlf.cursomc.services.DBService;
import com.natanlf.cursomc.services.EmailService;
import com.natanlf.cursomc.services.SmtpEmailService;

//Como na classe Application.propeties está com o profile test ativo, essa classe será usada
@Configuration
@Profile("dev")
public class DevConfig { 
	
	@Autowired
	private DBService dbService; 
	
	//pego o valor da propeties par saber se crio o banco novamente, se atualizo, faço nada...
	@Value("${spring.jpa.hibernate.ddl-auto}")
	private String strategy; 

	@Bean
	public boolean instantiateDatabase() throws ParseException {
		
		//se não for para criar o bd retorta false e não faz nada
		if(!"create".equals(strategy))
			return false;
		
		dbService.instantiateTestDatabase(); //instancio a classe que popula o banco
		return true;
	}
	
	@Bean
	public EmailService emailService() { //Para poder enviar o email
		return new SmtpEmailService();
	}
}
