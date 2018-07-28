package com.natanlf.cursomc.services;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.natanlf.cursomc.domain.Cidade;
import com.natanlf.cursomc.domain.Cliente;
import com.natanlf.cursomc.domain.Endereco;
import com.natanlf.cursomc.domain.enums.TipoCliente;
import com.natanlf.cursomc.dto.ClienteDTO;
import com.natanlf.cursomc.dto.ClienteNewDTO;
import com.natanlf.cursomc.repositories.ClienteRepository;
import com.natanlf.cursomc.repositories.EnderecoRepository;
import com.natanlf.cursomc.services.exceptions.DataIntegrityException;
import com.natanlf.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class ClienteService {

	@Autowired
	private ClienteRepository repo;
	
	@Autowired
	private EnderecoRepository enderecoRepository;
	
	public Cliente find(Integer id) {
		Optional<Cliente> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado! Id: " + id + ", Tipo: " + Cliente.class.getName(), null)); 
	}
	
	public Cliente insert(Cliente obj) {
		obj.setId(null); //o objeto que será inserido, precisa ter um id nulo, caso contrário achará que é um update
		obj = repo.save(obj);
		enderecoRepository.saveAll(obj.getEnderecos());
		return obj;
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
	
	public Cliente fromDTO(ClienteNewDTO objDto) { //instancio a partir de um DTO
		Cliente cli = new Cliente(null, objDto.getNome(), objDto.getEmail(), objDto.getCpfOuCnpj(), TipoCliente.toEnum(objDto.getTipo()));
		Cidade cid = new Cidade(objDto.getCidadeId(), null, null);
		Endereco end = new Endereco(null, objDto.getLogradouro(), objDto.getNumero(), objDto.getComplemento(), objDto.getBairro(), objDto.getCep(), cli, cid);
		cli.getEnderecos().add(end);
		cli.getTelefones().add(objDto.getTelefone1());
		
		if(objDto.getTelefone2()!=null) //Tel 2 e 3 não são obrigatórios
			cli.getTelefones().add(objDto.getTelefone2());
		if(objDto.getTelefone3()!=null)
			cli.getTelefones().add(objDto.getTelefone3());
		
		return cli;
	}
	
	private void updateData(Cliente newObj, Cliente obj) {
		newObj.setNome(obj.getNome());
		newObj.setEmail(obj.getEmail());
	}
}
