package br.com.helpconnect.LojaVirtual.controller;

import java.util.List;

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

import br.com.helpconnect.LojaVirtual.model.Configuracao;
import br.com.helpconnect.LojaVirtual.repository.ConfiguracaoRepository;

@RestController
@RequestMapping("/configuracao")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ConfiguracaoController {

	@Autowired
	private ConfiguracaoRepository repository;
	
	@GetMapping
	public ResponseEntity<List<Configuracao>> findAllByConfiguracoes() {
		
		return ResponseEntity.ok(repository.findAll());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Configuracao> findByIdConfiguracao(@PathVariable long id) {
		
		return repository.findById(id)
				.map(resp -> ResponseEntity.ok(resp))
				.orElse(ResponseEntity.notFound().build());
	}
	
	@PostMapping
	public ResponseEntity<Configuracao> postConfiguracao(@RequestBody Configuracao configuracao) {
		
		return ResponseEntity.status(HttpStatus.CREATED).body(repository.save(configuracao));
	}
	
	@PutMapping
	public ResponseEntity<Configuracao> putConfiguracao(@RequestBody Configuracao configuracao) {
		
		return ResponseEntity.ok(repository.save(configuracao));
	}
	
	@DeleteMapping("/{id}")
	public void deleteConfiguracao(@PathVariable long id) {
		
		repository.deleteById(id);
	}
	
}
