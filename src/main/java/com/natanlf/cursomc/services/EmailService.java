package com.natanlf.cursomc.services;

import org.springframework.mail.SimpleMailMessage;

import com.natanlf.cursomc.domain.Pedido;

//Usamos interface pois temos polimorfismo, vamos usar MockEmailService e SmtpEmailService
public interface EmailService {

	void senderOrderConfirmationEmail(Pedido obj);
	
	void sendEmail(SimpleMailMessage msg);
}
