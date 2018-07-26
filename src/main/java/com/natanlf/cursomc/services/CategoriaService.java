package com.natanlf.cursomc.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.natanlf.cursomc.domain.Categoria;
import com.natanlf.cursomc.repositories.CategoriaRepository;
import com.natanlf.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class CategoriaService {

	@Autowired
	private CategoriaRepository repo;
	
	public Categoria find(Integer id) {
		Optional<Categoria> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado! Id: " + id + ", Tipo: " + Categoria.class.getName(), null)); 
	}
	
	public Categoria insert(Categoria obj) {
		obj.setId(null); //o objeto que será inserido, precisa ter um id nulo, caso contrário achará que é um update
		return repo.save(obj);
	}
	
	public Categoria update(Categoria obj) {
		find(obj.getId()); //se não encontrar esse id, já lança uma exceção e não continua
		return repo.save(obj); //save or update, quando o id é nulo insere, quando não é atualiza
	}
}
