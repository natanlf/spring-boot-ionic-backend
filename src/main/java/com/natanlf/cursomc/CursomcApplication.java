package com.natanlf.cursomc;

import java.text.SimpleDateFormat;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.natanlf.cursomc.domain.Categoria;
import com.natanlf.cursomc.domain.Cidade;
import com.natanlf.cursomc.domain.Cliente;
import com.natanlf.cursomc.domain.Endereco;
import com.natanlf.cursomc.domain.Estado;
import com.natanlf.cursomc.domain.ItemPedido;
import com.natanlf.cursomc.domain.Pagamento;
import com.natanlf.cursomc.domain.PagamentoComBoleto;
import com.natanlf.cursomc.domain.PagamentoComCartao;
import com.natanlf.cursomc.domain.Pedido;
import com.natanlf.cursomc.domain.Produto;
import com.natanlf.cursomc.domain.enums.EstadoPagamento;
import com.natanlf.cursomc.domain.enums.TipoCliente;
import com.natanlf.cursomc.repositories.CategoriaRepository;
import com.natanlf.cursomc.repositories.CidadeRepository;
import com.natanlf.cursomc.repositories.ClienteRepository;
import com.natanlf.cursomc.repositories.EnderecoRepository;
import com.natanlf.cursomc.repositories.EstadoRepository;
import com.natanlf.cursomc.repositories.ItemPedidoRepository;
import com.natanlf.cursomc.repositories.PagamentoRepository;
import com.natanlf.cursomc.repositories.PedidoRepository;
import com.natanlf.cursomc.repositories.ProdutoRepository;

@SpringBootApplication
public class CursomcApplication implements CommandLineRunner {

	@Autowired
	private CategoriaRepository categoriaRepository; //camada de acesso a dados
	
	@Autowired
	private ProdutoRepository produtoRepository; //camada de acesso a dados
	
	@Autowired
	private EstadoRepository estadoRepository;
	
	@Autowired 
	private CidadeRepository cidadeRepository;
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private EnderecoRepository enderecoRepository;
	
	@Autowired
	private PedidoRepository pedidoRepository;
	
	@Autowired
	private PagamentoRepository pagamentoRepository;
	
	@Autowired
	private ItemPedidoRepository itemPedidoRepository;
	
	public static void main(String[] args) {
		SpringApplication.run(CursomcApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception { //roda automaticamente
		
		Categoria cat1 = new Categoria(null, "Informática");
		Categoria cat2 = new Categoria(null, "Escritório");
		Categoria cat3 = new Categoria(null, "Casa mesa e banho");
		Categoria cat4 = new Categoria(null, "Eletrônicos");
		Categoria cat5 = new Categoria(null, "Jardinagem");
		Categoria cat6 = new Categoria(null, "Decoração");
		Categoria cat7 = new Categoria(null, "Perfumaria");
		
		
		
		Produto p1 = new Produto(null, "Computador", 2000.00);
		Produto p2 = new Produto(null, "Impressora", 800.00);
		Produto p3 = new Produto(null, "Mouse", 80.00);
		
		Estado est1 = new Estado(null, "Minas Gerais");
		Estado est2 = new Estado(null, "São Paulo");
		
		Cidade c1 = new Cidade(null, "Uberlândia", est1);
		Cidade c2 = new Cidade(null, "São Paulo", est2);
		Cidade c3 = new Cidade(null, "Campinas", est2);
		
		//Associando
		cat1.getProdutos().addAll(Arrays.asList(p1, p2, p3)); //Digo que a categoria 1 tem os produtos informados
		cat2.getProdutos().addAll(Arrays.asList(p2)); //Digo que a categoria 2 tem os produtos informados
		
		p1.getCategorias().addAll(Arrays.asList(cat1)); //p1 pertence a categoria cat1
		p2.getCategorias().addAll(Arrays.asList(cat1, cat2)); //p2 pertence a categoria 1 e 2
		p3.getCategorias().addAll(Arrays.asList(cat1)); //p3 pertence a categoria cat1
		
		est1.getCidades().addAll(Arrays.asList(c1));
		est2.getCidades().addAll(Arrays.asList(c2,c3));
		
		categoriaRepository.saveAll(Arrays.asList(cat1, cat2, cat3, cat4, cat5, cat6, cat7)); //coloca meus objetos em lista para salvar
		produtoRepository.saveAll(Arrays.asList(p1, p2, p3));
		
		estadoRepository.saveAll(Arrays.asList(est1, est2));
		cidadeRepository.saveAll(Arrays.asList(c1, c2, c3));
		
		Cliente cli1 = new Cliente(null, "Maria Silva", "maria@gmail.com", "23545885254", TipoCliente.PESSOAFISICA);
		
		cli1.getTelefones().addAll(Arrays.asList("25789812","37146785"));
		
		Endereco e1 = new Endereco(null, "Rua Flores", "30", "Apt 540", "Jardim", "25784441", cli1, c1);
		Endereco e2 = new Endereco(null, "Avenida Matos", "105", "Sala 800", "Centro", "20989557", cli1, c2);
		
		cli1.getEnderecos().addAll(Arrays.asList(e1,e2));
		
		clienteRepository.saveAll(Arrays.asList(cli1));
		
		enderecoRepository.saveAll(Arrays.asList(e1,e2));
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		
		Pedido ped1 = new Pedido(null, sdf.parse("30/09/2017 10:32"), cli1, e1);
		Pedido ped2 = new Pedido(null, sdf.parse("10/10/2017 19:35"), cli1, e2);
		
		//o id do pagamento vai ser o mesmo do pedido e isso vai ser gerado pelo JPA
		Pagamento pagto1 = new PagamentoComCartao(null, EstadoPagamento.QUITADO, ped1, 6);
		ped1.setPagamento(pagto1);
		
		Pagamento pagto2 = new PagamentoComBoleto(null, EstadoPagamento.PENDENTE, ped2, sdf.parse("20/10/2017 00:00"), null);
		ped2.setPagamento(pagto2);
		
		cli1.getPedidos().addAll(Arrays.asList(ped1, ped2));
		
		pedidoRepository.saveAll(Arrays.asList(ped1, ped2));
		pagamentoRepository.saveAll(Arrays.asList(pagto1, pagto2));
		
		ItemPedido ip1 = new ItemPedido(ped1, p1, 0.00, 1, 2000.00);
		ItemPedido ip2 = new ItemPedido(ped1, p3, 0.00, 2, 80.00);
		ItemPedido ip3 = new ItemPedido(ped2, p2, 100.00, 1, 800.00);
		
		ped1.getItens().addAll(Arrays.asList(ip1, ip2));
		ped2.getItens().addAll(Arrays.asList(ip3));
		
		p1.getItens().addAll(Arrays.asList(ip1));
		p2.getItens().addAll(Arrays.asList(ip3));
		p3.getItens().addAll(Arrays.asList(ip2));
		
		itemPedidoRepository.saveAll(Arrays.asList(ip1, ip2, ip3));
	}
	

}
