package com.natanlf.cursomc.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.natanlf.cursomc.domain.Cliente;
import com.natanlf.cursomc.dto.ClienteDTO;
import com.natanlf.cursomc.repositories.ClienteRepository;
import com.natanlf.cursomc.services.exceptions.DataIntegrityException;
import com.natanlf.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class ClienteService {

	@Autowired
	private ClienteRepository repo;
	
	public Cliente find(Integer id) {
		Optional<Cliente> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado! Id: " + id + ", Tipo: " + Cliente.class.getName(), null)); 
	}
	
	public Cliente update(Cliente obj) {
		Cliente newObj = find(obj.getId()); //se não encontrar esse id, já lança uma exceção e não continua
		updateData(newObj, obj);
		return repo.save(newObj); //save or update, quando o id é nulo insere, quando não é atualiza
	}
	
	public void delete(Integer id) {
		find(id); //se não encontrar já retorna uam exception
		try {
			repo.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não é possível excluir porque há entidades relacionadas");
		}
	}
	
	public List<Cliente> findAll() {
		return repo.findAll();
	}
	
	//Buscando categorias com paginação
	public Page<Cliente> findPage(Integer page, Integer linesPerPage, String orderBy, String direction){
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		return repo.findAll(pageRequest);
	}
	
	public Cliente fromDTO(ClienteDTO objDto) { //instancio a partir de um DTO
		return new Cliente(objDto.getId(), objDto.getNome(), objDto.getEmail(), null, null);
	}
	
	private void updateData(Cliente newObj, Cliente obj) {
		newObj.setNome(obj.getNome());
		newObj.setEmail(obj.getEmail());
	}
}
