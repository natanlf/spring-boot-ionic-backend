package com.natanlf.cursomc.services.exceptions;

public class DataIntegrityException extends RuntimeException { //Tratamento de exceptions

	private static final long serialVersionUID = 1L;

	public DataIntegrityException(String msg) {
		super(msg);
	}
	
	public DataIntegrityException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
