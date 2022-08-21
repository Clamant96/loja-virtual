package br.com.helpconnect.LojaVirtual.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.helpconnect.LojaVirtual.model.Correio;
import br.com.helpconnect.LojaVirtual.model.EnderecoEntrega;
import br.com.helpconnect.LojaVirtual.service.CorreioService;
import io.swagger.models.Response;

@RestController
@RequestMapping("/correios")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class CorreioController {

	@Autowired
	private CorreioService correioService;
	
	@GetMapping("/{cep}")
	public ResponseEntity<EnderecoEntrega> enderecoCEP(@PathVariable("cep") String cep) {
		
		return ResponseEntity.ok(correioService.GetEnderecoPorCEP(cep));
	}
	
	@PostMapping("/frete")
	public ResponseEntity<String> calculaFrete(@RequestBody Correio correio) {
		
		return ResponseEntity.ok(correioService.CalculoFrete(correio));
	}
	
}
