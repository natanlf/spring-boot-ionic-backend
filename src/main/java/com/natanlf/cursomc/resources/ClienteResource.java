package com.natanlf.cursomc.resources;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.natanlf.cursomc.domain.Cliente;
import com.natanlf.cursomc.dto.ClienteDTO;
import com.natanlf.cursomc.dto.ClienteNewDTO;
import com.natanlf.cursomc.services.ClienteService;

@RestController
@RequestMapping(value="/clientes")
public class ClienteResource {

	@Autowired
	private ClienteService service;
	
	@RequestMapping(value="/{id}",method=RequestMethod.GET) //recebe o id enviado
	public ResponseEntity<Cliente> find(@PathVariable Integer id) { //@PathVariable para receber o id enviado		
		Cliente obj = service.find(id);
		return ResponseEntity.ok().body(obj); //tenho como retorno o objeto e  ok é para dizer se foi tudo certo
	}
	
	@RequestMapping(value="/{id}" ,method=RequestMethod.PUT)
	public ResponseEntity<Void> update(@Valid @RequestBody ClienteDTO objDto, @PathVariable Integer id){
		Cliente obj = service.fromDTO(objDto);
		obj.setId(id); //garantindo que vai atualizar a categoria com o id passado
		obj = service.update(obj);
		return ResponseEntity.noContent().build();
	}
	
	@PreAuthorize("hasAnyRole('ADMIN')") 
	@RequestMapping(value="/{id}", method=RequestMethod.DELETE)
	public ResponseEntity<Void> delete(@PathVariable Integer id){
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
	
	@RequestMapping(method=RequestMethod.GET) 
	public ResponseEntity<List<ClienteDTO>> findAll() { 		
		List<Cliente> list = service.findAll();
		List<ClienteDTO> listDTO = list.stream().map(obj -> new ClienteDTO(obj)).collect(Collectors.toList()); //Convertemos uma lista para outra lista
		return ResponseEntity.ok().body(listDTO); //retorna uma lista de categorias
	}
	
	@PreAuthorize("hasAnyRole('ADMIN')") 
	@RequestMapping(value="/page" ,method=RequestMethod.GET) 
	public ResponseEntity<Page<ClienteDTO>> findPage( //parametro opcionais
			@RequestParam(value="page", defaultValue="0") Integer page, 
			@RequestParam(value="linesPerPage", defaultValue="24") Integer linesPerPage, 
			@RequestParam(value="orderBy", defaultValue="nome") String orderBy, 
			@RequestParam(value="direction", defaultValue="ASC") String direction) { 		
		Page<Cliente> list = service.findPage(page, linesPerPage, orderBy, direction);
		Page<ClienteDTO> listDTO = list.map(obj -> new ClienteDTO(obj)); //Convertemos cada objeto da lista para DTO
		return ResponseEntity.ok().body(listDTO); //retorna uma lista de categorias
	}
	
	@RequestMapping(method=RequestMethod.POST)
	public ResponseEntity<Void> insert(@Valid @RequestBody ClienteNewDTO objDto){ // http de resposta é 201 para inserção, RequestBody faz o json ser convertido para objeto java	
		Cliente obj = service.fromDTO(objDto);
		obj =service.insert(obj); //a operação save retorna um objeto	
		
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}").buildAndExpand(obj.getId()).toUri(); //assim temos a url de requisição
		return ResponseEntity.created(uri).build();
	}
	
	@RequestMapping(value="/picture", method=RequestMethod.POST)
	public ResponseEntity<Void> uploadProfilePicture(@RequestParam MultipartFile file){ // http de resposta é 201 para inserção, RequestBody faz o json ser convertido para objeto java	
		URI uri = service.uploadProfilePicture(file);
		return ResponseEntity.created(uri).build();
	}
}
