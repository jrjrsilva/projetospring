package br.com.solucoescjm.mc;

import java.text.SimpleDateFormat;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import br.com.solucoescjm.mc.domain.Categoria;
import br.com.solucoescjm.mc.domain.Cidade;
import br.com.solucoescjm.mc.domain.Cliente;
import br.com.solucoescjm.mc.domain.Endereco;
import br.com.solucoescjm.mc.domain.Estado;
import br.com.solucoescjm.mc.domain.ItemPedido;
import br.com.solucoescjm.mc.domain.Pagamento;
import br.com.solucoescjm.mc.domain.PagamentoComBoleto;
import br.com.solucoescjm.mc.domain.PagamentoComCartao;
import br.com.solucoescjm.mc.domain.Pedido;
import br.com.solucoescjm.mc.domain.Produto;
import br.com.solucoescjm.mc.domain.enums.EstadoPagamento;
import br.com.solucoescjm.mc.domain.enums.TipoCliente;
import br.com.solucoescjm.mc.repositories.CategoriaRepository;
import br.com.solucoescjm.mc.repositories.CidadeRepository;
import br.com.solucoescjm.mc.repositories.ClienteRepository;
import br.com.solucoescjm.mc.repositories.EnderecoRepository;
import br.com.solucoescjm.mc.repositories.EstadoRepository;
import br.com.solucoescjm.mc.repositories.ItemPedidoRepository;
import br.com.solucoescjm.mc.repositories.PagamentoRepository;
import br.com.solucoescjm.mc.repositories.PedidoRepository;
import br.com.solucoescjm.mc.repositories.ProdutoRepository;

@SpringBootApplication
public class ApirestApplication implements CommandLineRunner{

	@Autowired
	private CategoriaRepository repo;

	@Autowired
	private ProdutoRepository repoProduto;

	@Autowired
	private EstadoRepository repoEstado;
	
	@Autowired
	private CidadeRepository repoCidade;

	@Autowired
	private ClienteRepository repoCliente;

	@Autowired
	private EnderecoRepository repoEndereco;

	@Autowired
	private PedidoRepository repoPedido;
	
	@Autowired
	private PagamentoRepository repoPagamento;
	
	@Autowired
	private ItemPedidoRepository repoItemPedido;
	
	public static void main(String[] args) {
		SpringApplication.run(ApirestApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		
		  Categoria cat1 = new Categoria(null, "Informática"); Categoria cat2 = new
		  Categoria(null, "Escritório");
		  
		  Produto p1 = new Produto(null,"Computador", 2000.00 );
		  Produto p2 = new Produto(null,"Impressora", 800.00 );
		  Produto p3 = new Produto(null,"Mouse", 80.00 );

		  cat1.getProdutos().addAll(Arrays.asList(p1,p2,p3));
		  cat2.getProdutos().addAll(Arrays.asList(p2));
		  
		  p1.getCategorias().addAll(Arrays.asList(cat1));
		  p2.getCategorias().addAll(Arrays.asList(cat1,cat2));
		  p3.getCategorias().addAll(Arrays.asList(cat1));
		  
		  repo.saveAll(Arrays.asList(cat1, cat2));
		  repoProduto.saveAll(Arrays.asList(p1,p2,p3));

		  Estado est1 = new Estado(null,"Minas Gerais");
		  Estado est2 = new Estado(null,"São Paulo");
		  
		  Cidade c1 = new Cidade(null,"Uberlândia",est1);
		  Cidade c2 = new Cidade(null,"São Paulo",est2);
		  Cidade c3 = new Cidade(null,"Campinas",est2);
		  
		  est1.getCidades().addAll(Arrays.asList(c1));
		  est2.getCidades().addAll(Arrays.asList(c2,c3));
		  
		  repoEstado.saveAll(Arrays.asList(est1,est2));
		  repoCidade.saveAll(Arrays.asList(c1,c2,c3));
		  
		  Cliente cli1 = new Cliente(null,"Maria Silva","maria@gmail.com","1452562878",TipoCliente.PESSOAFISICA);
		  cli1.getTelefones().addAll(Arrays.asList("14785225","89652314"));
		  
		  Endereco e1 = new Endereco(null,"Rua Flores","300","Apto 203","Jardim","41852987",cli1,c1);
		  Endereco e2 = new Endereco(null,"Av Matos","250","Sala 321","Centro","48747854",cli1,c2);
		  
		  cli1.getEnderecos().addAll(Arrays.asList(e1,e2)); 
		  
		  repoCliente.saveAll(Arrays.asList(cli1));
		  repoEndereco.saveAll(Arrays.asList(e1,e2));
		  SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		  Pedido ped1 = new Pedido(null,sdf.parse("16/01/2022 09:55"),cli1,e1);
		  Pedido ped2 = new Pedido(null,sdf.parse("16/01/2022 10:55"),cli1,e2);
		  
		  Pagamento pgto1 = new PagamentoComCartao(null,EstadoPagamento.QUITADO,ped1,6);
		  ped1.setPagamento(pgto1);
		  
		  Pagamento pgto2 = new PagamentoComBoleto(null, EstadoPagamento.PENDENTE, ped2, sdf.parse("20/02/2022 00:00"),null);
		  ped2.setPagamento(pgto2);
		  
		  cli1.getPedidos().addAll(Arrays.asList(ped1,ped2));
		  
		  repoPedido.saveAll(Arrays.asList(ped1,ped2));
		  repoPagamento.saveAll(Arrays.asList(pgto1,pgto2));
		  
		  
		  ItemPedido  ip1 = new ItemPedido(ped1,p1,1,0.00,2000.00);
		  ItemPedido  ip2 = new ItemPedido(ped1,p3,2,0.00,80.00);
		  ItemPedido  ip3 = new ItemPedido(ped1,p2,1,0.00,800.00);
		  
		  ped1.getItens().addAll(Arrays.asList(ip1,ip2));
		  ped2.getItens().addAll(Arrays.asList(ip3));
		  
		  p1.getItens().addAll(Arrays.asList(ip1));
		  p2.getItens().addAll(Arrays.asList(ip3));
		  p3.getItens().addAll(Arrays.asList(ip2));
		  
		  repoItemPedido.saveAll(Arrays.asList(ip1,ip2,ip3));
	}

}
