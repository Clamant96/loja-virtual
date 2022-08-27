package br.com.helpconnect.LojaVirtual.controller;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.helpconnect.LojaVirtual.model.Cliente;
import br.com.helpconnect.LojaVirtual.model.Compras;
import br.com.helpconnect.LojaVirtual.model.Produto;
import br.com.helpconnect.LojaVirtual.repository.CompraRepository;
import br.com.helpconnect.LojaVirtual.service.CompraService;
import br.com.helpconnect.LojaVirtual.service.CorreioService;
import br.com.helpconnect.LojaVirtual.service.SendMailService;

@RestController
@RequestMapping("/compras")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class CompraController {

	@Autowired
	private CompraRepository repository;
	
	@Autowired
	private CompraService service;
	
	@Autowired
	private CorreioService correioService;
	
	@Autowired
	private SendMailService sendMailService;
	
	@GetMapping
	public ResponseEntity<List<Compras>> findAllCompras() {
		
		return ResponseEntity.ok(repository.findAll());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Compras> findByIdCompra(@PathVariable long id) {
		
		Optional<Compras> compra = repository.findById(id);
		
		// AJUSTA VALOR CARRINHO
		double total = compra.get().getValorTotal();
		NumberFormat formatter = new DecimalFormat("#0.00");
		compra.get().setValorTotal(Double.parseDouble(formatter.format(total).replace(",", ".")));

		compra.get().setMeuPedido(service.retirraDuplicidadeCarrinho(compra));
		
		System.out.println(compra.get().getValorTotal());

		try {
			return ResponseEntity.ok(compra.get());
			
		}catch(Exception e) {
			return ResponseEntity.badRequest().build();
			
		}
		
	}
	
	@PostMapping("/produto_compra/compra/{idCompra}/frete/{frete}")
	public ResponseEntity<Boolean> putProduto(@RequestBody List<Produto> produtos, @PathVariable long idCompra, @PathVariable("frete") double frete) {
		
		boolean retorno = false;
		
		for(Produto p : produtos) {
			System.out.println("QTD PRODUTO: "+ p.getQtdPedidoProduto());
			for (int i = 0; i < p.getQtdPedidoProduto(); i++) {
				System.out.println(p.getNome());
				retorno = service.compraProduto(p.getId(), idCompra);
				System.out.println("RETORNO: "+ retorno);
				
			}
			
		}
		
		retorno = correioService.InsereFrete(idCompra, frete);
		System.out.println("FRETE: "+ retorno);
		
		return ResponseEntity.ok(retorno);
	}
	
	@PostMapping
	public ResponseEntity<Compras> postCompra(@RequestBody Compras compra) {
		
		compra.setNumeroPedido(service.geraNumeroCompra(compra));
		
		return ResponseEntity.status(HttpStatus.CREATED).body(repository.save(compra));
	}
	
	@GetMapping("/email/{id}")
	public ResponseEntity<Boolean> enviarEmail(@PathVariable("id") long id) {
		
		Optional<Compras> compra = repository.findById(id);
		
		// AJUSTA VALOR CARRINHO
		double total = compra.get().getValorTotal();
		NumberFormat formatter = new DecimalFormat("#0.00");
		compra.get().setValorTotal(Double.parseDouble(formatter.format(total).replace(",", ".")));

		compra.get().setMeuPedido(service.retirraDuplicidadeCarrinho(compra));
		
		boolean retorno = false;
		
		retorno = sendMailService.sendMail(compra.get());
		
		return ResponseEntity.ok(retorno);
	}
	
	@GetMapping("/reescrever-email/{id}")
	public ResponseEntity<String> lerArquivo(@PathVariable("id") long id) {
		
		Optional<Compras> compraExistente = repository.findById(id);
		
		return ResponseEntity.ok(sendMailService.GerandoEmailStatusPedido(compraExistente.get()));
	}
	
	@PutMapping
	public ResponseEntity<Compras> putCompra(@RequestBody Compras compra) {
		
		return ResponseEntity.ok(repository.save(compra));
	}
	
	@DeleteMapping("/{id}")
	public void deleteCompra(@PathVariable long id) {
		
		repository.deleteById(id);
	}
	
}
