package com.natanlf.cursomc.resources;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.natanlf.cursomc.domain.Categoria;
import com.natanlf.cursomc.dto.CategoriaDTO;
import com.natanlf.cursomc.services.CategoriaService;

@RestController
@RequestMapping(value="/categorias")
public class CategoriaResource {

	@Autowired
	private CategoriaService service;
	
	@RequestMapping(value="/{id}",method=RequestMethod.GET) //recebe o id enviado
	public ResponseEntity<Categoria> find(@PathVariable Integer id) { //@PathVariable para receber o id enviado		
		Categoria obj = service.find(id);
		return ResponseEntity.ok().body(obj); //tenho como retorno o objeto e  ok é para dizer se foi tudo certo
	}
	
	//void, pois não preciso de um corpo como resposta para salvar uma categoria
	@RequestMapping(method=RequestMethod.POST)
	public ResponseEntity<Void> insert(@RequestBody Categoria obj){ // http de resposta é 201 para inserção, RequestBody faz o json ser convertido para objeto java	
		obj =service.insert(obj); //a operação save retorna um objeto	
		
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}").buildAndExpand(obj.getId()).toUri(); //assim temos a url de requisição
		return ResponseEntity.created(uri).build();
	}
	
	@RequestMapping(value="/{id}" ,method=RequestMethod.PUT)
	public ResponseEntity<Void> update(@RequestBody Categoria obj, @PathVariable Integer id){
		obj.setId(id); //garantindo que vai atualizar a categoria com o id passado
		obj = service.update(obj);
		return ResponseEntity.noContent().build();
	}
	
	@RequestMapping(value="/{id}", method=RequestMethod.DELETE)
	public ResponseEntity<Void> delete(@PathVariable Integer id){
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
	
	@RequestMapping(method=RequestMethod.GET) 
	public ResponseEntity<List<CategoriaDTO>> findAll() { 		
		List<Categoria> list = service.findAll();
		List<CategoriaDTO> listDTO = list.stream().map(obj -> new CategoriaDTO(obj)).collect(Collectors.toList()); //Convertemos uma lista para outra lista
		return ResponseEntity.ok().body(listDTO); //retorna uma lista de categorias
	}
	
	@RequestMapping(value="/page" ,method=RequestMethod.GET) 
	public ResponseEntity<Page<CategoriaDTO>> findPage( //parametro opcionais
			@RequestParam(value="page", defaultValue="0") Integer page, 
			@RequestParam(value="linesPerPage", defaultValue="24") Integer linesPerPage, 
			@RequestParam(value="orderBy", defaultValue="nome") String orderBy, 
			@RequestParam(value="direction", defaultValue="ASC") String direction) { 		
		Page<Categoria> list = service.findPage(page, linesPerPage, orderBy, direction);
		Page<CategoriaDTO> listDTO = list.map(obj -> new CategoriaDTO(obj)); //Convertemos cada objeto da lista para DTO
		return ResponseEntity.ok().body(listDTO); //retorna uma lista de categorias
	}
}
