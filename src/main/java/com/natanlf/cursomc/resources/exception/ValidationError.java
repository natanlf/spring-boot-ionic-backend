package com.natanlf.cursomc.resources.exception;

import java.util.ArrayList;
import java.util.List;

//Além de herdar da classe StandardError vamos ter uma lista de FieldMessage
public class ValidationError extends StandardError { //assim temos todos os dados da classe e Standar e mais a lista de mensagens

	private static final long serialVersionUID = 1L;
	
	private List<FieldMessage> errors = new ArrayList<>();

	

	public ValidationError(Long timeStamp, Integer status, String error, String message, String path) {
		super(timeStamp, status, error, message, path);
	}

	public List<FieldMessage> getErrors() {
		return errors;
	}

	public void setError(String fieldName, String message) {
		errors.add(new FieldMessage(fieldName, message));
	}
	
	
	
}
