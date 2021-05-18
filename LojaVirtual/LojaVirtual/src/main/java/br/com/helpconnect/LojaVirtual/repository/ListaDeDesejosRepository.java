package br.com.helpconnect.LojaVirtual.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.helpconnect.LojaVirtual.model.ListaDeDesejos;
import br.com.helpconnect.LojaVirtual.model.Produto;

@Repository
public interface ListaDeDesejosRepository extends JpaRepository<ListaDeDesejos, Long> {
	
	//public List<Produto> findAllByProdutosContainingIgnoreCase(String produtos);

}
