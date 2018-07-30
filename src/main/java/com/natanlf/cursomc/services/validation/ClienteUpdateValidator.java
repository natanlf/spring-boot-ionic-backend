package com.natanlf.cursomc.services.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerMapping;

import com.natanlf.cursomc.domain.Cliente;
import com.natanlf.cursomc.dto.ClienteDTO;
import com.natanlf.cursomc.repositories.ClienteRepository;
import com.natanlf.cursomc.resources.exception.FieldMessage;

public class ClienteUpdateValidator implements ConstraintValidator<ClienteUpdate, ClienteDTO> {

	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private ClienteRepository repo;
	
	@Override
	public void initialize(ClienteUpdate ann) {
	}
	
	@Override
	public boolean isValid(ClienteDTO objDto, ConstraintValidatorContext context) {
		
		//Os atributos da requisição são armazenados dentro de um Map
		@SuppressWarnings("unchecked")
		Map<String, String> map = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
		Integer uriId = Integer.parseInt(map.get("id")); //Id da url
		
		List<FieldMessage> list = new ArrayList<>(); // inclua os testes aqui, inserindo erros na lista
		
		Cliente aux = repo.findByEmail(objDto.getEmail());
		if(aux!=null && !aux.getId().equals(uriId)) { //Email já existe em outro cliente
			list.add(new FieldMessage("email", "Email já existente"));
		}
		
		//Pego erros da minha lista e coloca na lista do framework
		//É na classe ResourceExceptionHandler que temos a nossa lista de erros personalizadas
		for (FieldMessage e : list) { //percorre a minha lista e para cada objeto diferente, adiciona o erro na lista do framework
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName())
					.addConstraintViolation();
		}
		return list.isEmpty();
	}
}