package com.natanlf.cursomc.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.natanlf.cursomc.domain.Produto;
import com.natanlf.cursomc.dto.ProdutoDTO;
import com.natanlf.cursomc.resources.utils.URL;
import com.natanlf.cursomc.services.ProdutoService;

@RestController
@RequestMapping(value="/produtos")
public class ProdutoResource {

	@Autowired
	private ProdutoService service;
	
	@RequestMapping(value="/{id}",method=RequestMethod.GET) //recebe o id enviado
	public ResponseEntity<Produto> find(@PathVariable Integer id) { //@PathVariable para receber o id enviado		
		Produto obj = service.buscar(id);
		return ResponseEntity.ok().body(obj); //tenho como retorno o objeto e  ok é para dizer se foi tudo certo
	}
	
	@RequestMapping(method=RequestMethod.GET) 
	public ResponseEntity<Page<ProdutoDTO>> findPage( //parametro opcionais
			@RequestParam(value="nome", defaultValue="") String nome,
			@RequestParam(value="categorias", defaultValue="") String categorias,
			@RequestParam(value="page", defaultValue="0") Integer page, 
			@RequestParam(value="linesPerPage", defaultValue="24") Integer linesPerPage, 
			@RequestParam(value="orderBy", defaultValue="nome") String orderBy, 
			@RequestParam(value="direction", defaultValue="ASC") String direction) { 		
		String nomeDecoded =  URL.decodeParam(nome);
		List<Integer> ids = URL.decodeIntList(categorias);
		Page<Produto> list = service.search(nomeDecoded, ids,page, linesPerPage, orderBy, direction);
		Page<ProdutoDTO> listDTO = list.map(obj -> new ProdutoDTO(obj)); //Convertemos cada objeto da lista para DTO
		return ResponseEntity.ok().body(listDTO); //retorna uma lista de categorias
	}
}
