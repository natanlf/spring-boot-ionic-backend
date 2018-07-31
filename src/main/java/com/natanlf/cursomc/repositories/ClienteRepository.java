package com.natanlf.cursomc.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.natanlf.cursomc.domain.Cliente;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Integer> { //realiza operações de acesso a dados

	//Busca um cliente passando um email como argumento
	@Transactional(readOnly=true)
	Cliente findByEmail(String email);
}
