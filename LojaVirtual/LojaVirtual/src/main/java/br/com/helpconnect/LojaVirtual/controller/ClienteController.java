package br.com.helpconnect.LojaVirtual.controller;

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
import br.com.helpconnect.LojaVirtual.model.ClienteLogin;
import br.com.helpconnect.LojaVirtual.repository.ClienteRepository;
import br.com.helpconnect.LojaVirtual.service.ClienteService;

@RestController
@RequestMapping("/clientes")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ClienteController {
	
	@Autowired
	private ClienteRepository repository;
	
	@Autowired
	private ClienteService service;
	
	@GetMapping
	public ResponseEntity<List<Cliente>> findAllByCliente() {
		
		return ResponseEntity.ok(repository.findAll());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Cliente> findByIdCliente(@PathVariable long id) {
		
		return repository.findById(id)
				.map(resp -> ResponseEntity.ok(resp))
				.orElse(ResponseEntity.notFound().build());
	}
	
	/*@PostMapping
	public ResponseEntity<Cliente> postCliente(@RequestBody Cliente cliente) {
		
		return ResponseEntity.status(HttpStatus.CREATED).body(repository.save(cliente));
	}*/
	
	/*@PostMapping
	public ResponseEntity<Cliente> postCliente(@RequestBody Cliente cliente) {
		
		return ResponseEntity.status(HttpStatus.CREATED).body(service.cadastrarCliente(cliente));
	}*/
	
	/* PARA LOGARMOS NO SISITEMA TRABALHAMOS COM A CLASSE 'UserLogin' */
	@PostMapping("/logar")
	public ResponseEntity<ClienteLogin> Autentication(@RequestBody Optional<ClienteLogin> user) {
		return service.Logar(user).map(resp -> ResponseEntity.ok(resp))
				/* CASO SEU USUARIO SEJA INVALIDO VOCE RECEBERA UM ERRO DE NAO AUTORIZADO */
				.orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
	}
	
	/* CHAMA O METODO DE CADASTRAR USUARIOS QUE E RESPONSAVEL POR VERIFICAR SE O USUARIO INSERIDO JA SE ENCONTRA NA BASE DE DADOS, CODIFICAR A SENHA INSERIDA E SALVAR OS DADOS CADASTRADO NA BASE DE DADOS */
	@PostMapping("/cadastrar")
	public ResponseEntity<Cliente> Post(@RequestBody Cliente usuario) {
		Optional<Cliente> user = service.CadastrarCliente(usuario);
		
		try {
			return ResponseEntity.ok(user.get());
			
		}catch(Exception e) {
			return ResponseEntity.badRequest().build();
			
		}
		
	}

	
	@PutMapping
	public ResponseEntity<Cliente> putCliente(@RequestBody Cliente cliente) {
		
		return ResponseEntity.ok(repository.save(cliente));
	}
	
	@DeleteMapping("/{id}")
	public void deletaCliente(@PathVariable long id) {
		
		repository.deleteById(id);
	}

}
