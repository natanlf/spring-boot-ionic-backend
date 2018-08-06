package com.natanlf.cursomc.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.natanlf.cursomc.domain.Cliente;
import com.natanlf.cursomc.repositories.ClienteRepository;
import com.natanlf.cursomc.security.UserSS;

@Service
public class UserDetailsServiceImpl implements UserDetailsService { //Implementa um método do Spring Security

	@Autowired
	private ClienteRepository repo;
	
	//Vai receber o usuário e vai retornar o UserDetails
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Cliente cli = repo.findByEmail(email);
		if(cli==null) {
			throw new UsernameNotFoundException(email);
		}
		
		return new UserSS(cli.getId(), cli.getEmail(), cli.getSenha(), cli.getPerfil());
	}

}
