package br.com.helpconnect.LojaVirtual.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.helpconnect.LojaVirtual.model.ListaDeDesejos;

@Repository
public interface ListaDeDesejosRepository extends JpaRepository<ListaDeDesejos, Long> {

}
