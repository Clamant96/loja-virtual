package br.com.helpconnect.LojaVirtual.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.helpconnect.LojaVirtual.model.Configuracao;

@Repository
public interface ConfiguracaoRepository extends JpaRepository<Configuracao, Long> {

}
