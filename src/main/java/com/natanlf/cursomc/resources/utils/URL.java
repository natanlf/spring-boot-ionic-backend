package com.natanlf.cursomc.resources.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

public class URL {
	
	//Tirando espaços dos nomes
	public static String decodeParam(String s) {
		try {
			return URLDecoder.decode(s, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return "";
		}
	}

	//Pega a String de números separados por vírgulas e separa, assim coloco os números na lista
	public static List<Integer> decodeIntList(String s){
		String[] vet = s.split(","); //quebro pela vírgula
		List<Integer> list = new ArrayList<>();
		for (int i = 0; i < vet.length; i++) {
			list.add(Integer.parseInt(vet[i]));
		}
		return list;
		//Posso fazer o mesmo com essa linha estranha abaixo WTF, estudar isso
		//return Arrays.asList(s.split(",")).stream().map(x -> Integer.parseInt(x)).collect(Collectors.toList());
	}
}
