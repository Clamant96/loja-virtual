package br.com.helpconnect.LojaVirtual.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.StreamingHttpOutputMessage.Body;
import org.springframework.stereotype.Service;

import br.com.helpconnect.LojaVirtual.model.Cliente;
import br.com.helpconnect.LojaVirtual.model.Pedido;
import br.com.helpconnect.LojaVirtual.model.Produto;
import br.com.helpconnect.LojaVirtual.repository.ClienteRepository;
import br.com.helpconnect.LojaVirtual.repository.PedidoRepository;
import br.com.helpconnect.LojaVirtual.repository.ProdutoRepository;

@Service
public class ProdutoService {
	
	@Autowired
	private PedidoRepository pedidoRepository;
	
	@Autowired
	private ProdutoRepository produtoRepository;
	
	public Produto compraProduto(long idProduto, long idPedido/*, int qtdProduto*/) {
			
		Optional<Produto> produtoExistente = produtoRepository.findById(idProduto);
		Optional<Pedido> pedidoExistente = pedidoRepository.findById(idPedido);
		
		/* SE O 'PRODUTO' E 'PEDIDO' EXISTIREM, E SE O 'ESTOQUE' CONTEM PRODUTOS DISPONIVEIS ENTRA NA CONDICAO */
		if(produtoExistente.isPresent() && pedidoExistente.isPresent() && produtoExistente.get().getEstoque() >= 0) {
			
			if(pedidoExistente.get().getProdutos().contains(produtoExistente.get())) {	
				
				double produtoAtual = (produtoExistente.get().getPreco() * produtoExistente.get().getQtdPedidoProduto());
				pedidoExistente.get().setValorTotal(pedidoExistente.get().getValorTotal() - produtoAtual);
				
				/* ATRIBUI O NOVO VALOR AO 'VALOR TOTAL' */
				pedidoExistente.get().setValorTotal(pedidoExistente.get().getValorTotal() + (produtoExistente.get().getPreco() * produtoExistente.get().getQtdPedidoProduto()));
				
				produtoExistente.get().setQtdPedidoProduto(produtoExistente.get().getQtdPedidoProduto() + 1);
				
				/* ATUALIZA ESTOQUE */
				produtoExistente.get().setEstoque(produtoExistente.get().getEstoque() - 1);
				pedidoExistente.get().setValorTotal(produtoExistente.get().getPreco() * produtoExistente.get().getQtdPedidoProduto());
				
			}else {
				produtoExistente.get().getPedidos().add(pedidoExistente.get());
				
				produtoExistente.get().setQtdPedidoProduto(1);
				produtoExistente.get().setEstoque(produtoExistente.get().getEstoque() - 1);
				
				pedidoExistente.get().setValorTotal(pedidoExistente.get().getValorTotal() + (produtoExistente.get().getPreco() * produtoExistente.get().getQtdPedidoProduto()));
				
			}
			
			produtoRepository.save(produtoExistente.get());
			pedidoRepository.save(pedidoExistente.get());
			
			return produtoRepository.save(produtoExistente.get());
			
		}
		
		return null;
	}

}
