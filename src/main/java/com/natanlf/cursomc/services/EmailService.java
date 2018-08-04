package com.natanlf.cursomc.services;

import javax.mail.internet.MimeMessage;

import org.springframework.mail.SimpleMailMessage;

import com.natanlf.cursomc.domain.Pedido;

//Usamos interface pois temos polimorfismo, vamos usar MockEmailService e SmtpEmailService
public interface EmailService {

	//Enviar email texto plano
	void senderOrderConfirmationEmail(Pedido obj);
	
	void sendEmail(SimpleMailMessage msg);
	
	//Enviar email html
	void sendOrderConfirmationHtmlEmail(Pedido obj); 
	
	void sendHtmlEmail(MimeMessage msg); 
}
