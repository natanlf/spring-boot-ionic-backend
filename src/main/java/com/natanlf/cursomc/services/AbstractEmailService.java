package com.natanlf.cursomc.services;

import java.util.Date;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.natanlf.cursomc.domain.Pedido;

public abstract class AbstractEmailService implements EmailService{
	
	@Value("${default.sender}")
	private String sender;
	
	@Autowired
	private TemplateEngine templateEngine;
	
	@Autowired
	private JavaMailSender javaMailSender;

	//Estou sobrescrevendo o método de minha interface EmailService
	@Override
	public void senderOrderConfirmationEmail(Pedido obj) {
		SimpleMailMessage sm =  prepareSimpleMailMessageFromPedido(obj); //preparo o email
		sendEmail(sm); //método da minha interface EmailService
	}

	//protected para poder ser acessado por minhas subclasses
	protected SimpleMailMessage prepareSimpleMailMessageFromPedido(Pedido obj) {
		SimpleMailMessage sm = new SimpleMailMessage();
		sm.setTo(obj.getCliente().getEmail());
		sm.setFrom(sender);
		sm.setSubject("Pedido confirmado! Código: "+obj.getId());
		sm.setSentDate(new Date(System.currentTimeMillis())); //currentTime pega a data do servidor
		sm.setText(obj.toString());	
		return sm;
	}
	
	//envio de html
	protected String htmlFromTemplatePedido(Pedido obj) {
		Context context = new Context();
		//tenho que enviar o objeto pedido para o template
		context.setVariable("pedido", obj); //pedido é o apelido do objeto que coloquei no template
		//caminho do arquivo começa em templates, então coloque o que está dentor desse diretório, não precisa colocar .html
		return templateEngine.process("email/confirmacaoPedido", context);
	}
	
	//É parecido com o método acima que envia o email com texto plano
	@Override
	public void sendOrderConfirmationHtmlEmail(Pedido obj) {
		try {
			MimeMessage mm =  prepareMimeMessageFromPedido(obj); //preparo o email
		sendHtmlEmail(mm); //método da minha interface EmailService
		} catch (MessagingException e) {
			senderOrderConfirmationEmail(obj);
		}
		
	}

	protected MimeMessage prepareMimeMessageFromPedido(Pedido obj) throws MessagingException {
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		MimeMessageHelper mmh = new MimeMessageHelper(mimeMessage, true);
		mmh.setTo(obj.getCliente().getEmail());
		mmh.setFrom(sender);
		mmh.setSubject("Pedido confirmado! Código: "+obj.getId());
		mmh.setSentDate(new Date(System.currentTimeMillis()));
		mmh.setText(htmlFromTemplatePedido(obj), true);
		return mimeMessage;
	}
}
