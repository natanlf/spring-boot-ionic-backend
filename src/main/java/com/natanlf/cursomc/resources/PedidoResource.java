package com.natanlf.cursomc.resources;

import java.net.URI;

import javax.validation.Valid;

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

import com.natanlf.cursomc.domain.Pedido;
import com.natanlf.cursomc.services.PedidoService;

@RestController
@RequestMapping(value="/pedidos")
public class PedidoResource {

	@Autowired
	private PedidoService service;
	
	@RequestMapping(value="/{id}",method=RequestMethod.GET) //recebe o id enviado
	public ResponseEntity<Pedido> find(@PathVariable Integer id) { //@PathVariable para receber o id enviado		
		Pedido obj = service.buscar(id);
		return ResponseEntity.ok().body(obj); //tenho como retorno o objeto e  ok é para dizer se foi tudo certo
	}
	
	@RequestMapping(method=RequestMethod.POST)
	public ResponseEntity<Void> insert(@Valid @RequestBody Pedido obj){ // http de resposta é 201 para inserção, RequestBody faz o json ser convertido para objeto java	
		obj =service.insert(obj); //a operação save retorna um objeto	
		
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}").buildAndExpand(obj.getId()).toUri(); //assim temos a url de requisição
		return ResponseEntity.created(uri).build();
	}
	
	@RequestMapping(method=RequestMethod.GET)
	public ResponseEntity<Page<Pedido>> findPage(
			@RequestParam(value="page", defaultValue="0") Integer page, 
			@RequestParam(value="linesPerPage", defaultValue="24") Integer linesPerPage, 
			@RequestParam(value="orderBy", defaultValue="instante") String orderBy, 
			@RequestParam(value="direction", defaultValue="DESC") String direction) {
		Page<Pedido> list = service.findPage(page, linesPerPage, orderBy, direction);
		return ResponseEntity.ok().body(list);
	}
}
