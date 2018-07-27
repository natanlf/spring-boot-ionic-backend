package com.natanlf.cursomc.resources.exception;

import java.util.ArrayList;
import java.util.List;

//Além de herdar da classe StandardError vamos ter uma lista de FieldMessage
public class ValidationError extends StandardError { //assim temos todos os dados da classe e Standar e mais a lista de mensagens

	private static final long serialVersionUID = 1L;
	
	private List<FieldMessage> errors = new ArrayList<>();

	public ValidationError(Integer status, String msg, Long timeStamp) {
		super(status, msg, timeStamp);
	}

	public List<FieldMessage> getErrors() {
		return errors;
	}

	public void setError(String fieldName, String message) {
		errors.add(new FieldMessage(fieldName, message));
	}
	
	
	
}
