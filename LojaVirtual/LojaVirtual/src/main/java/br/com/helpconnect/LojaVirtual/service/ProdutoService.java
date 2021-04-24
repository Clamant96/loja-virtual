package br.com.helpconnect.LojaVirtual.service;

import java.util.ArrayList;
import java.util.List;
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
			
			/* ADICIONA O PRODUTO AO CARRINHO DO USUARIO */
			produtoExistente.get().getPedidos().add(pedidoExistente.get());
			
			produtoExistente.get().setQtdPedidoProduto(1);
			produtoExistente.get().setEstoque(produtoExistente.get().getEstoque() - 1);
			
			pedidoExistente.get().setValorTotal(pedidoExistente.get().getValorTotal() + (produtoExistente.get().getPreco() * produtoExistente.get().getQtdPedidoProduto()));
			
			System.out.println("Retorno: "+ pedidoExistente.get().getProdutos().contains(produtoExistente.get()));
			
			System.out.println("QTD produtos "+ pedidoExistente.get().getProdutos().size());
			
			/* ARMAZENA A QTD DE PRODUTOS */
			int contador = 0;
			
			System.out.println("Valor: "+ pedidoExistente.get().getProdutos().get((int)idProduto).getId());
			
			/* ARMAZENA OS IDs DOS PRODUTOS LISTADOS DENTRO DO CARRINHO DO USUARIO */
			long[] vetor = new long[pedidoExistente.get().getProdutos().size()];
			
			for(int i = 0; i < pedidoExistente.get().getProdutos().size(); i++) {
				
				vetor[i] = pedidoExistente.get().getProdutos().get(i).getId();
				
				System.out.println("Posicao do vetor ["+ i +"] = "+ vetor[i]);
				
				if(vetor[i] == produtoExistente.get().getId()) {
					contador++;
					
				}
				
				/*if(pedidoExistente.get().getProdutos().get((int)idProduto).getId() == produtoExistente.get().getId()) {

					if(produtoExistente.isPresent()) {
						contador++;
						
						System.out.println("Contem o ID "+ produtoExistente.get().getId());
						
					}else {
						System.out.println("Diferente do ID especificado");
						
					}
					
				}else {
					System.out.println("Fora do IF!!");
					
				}*/
				
			}
			
			/* COMPENSA ACRESCENTANDO O NOVO PRODUTO AO CARRINHO ==> O ID INFORMADO */
			contador++;
			System.out.println("QTD de produto: "+ contador);
			
			
			//pedidoExistente.get().getProdutos().contains(produtoExistente.get());
			/*if(pedidoExistente.get().getProdutos().contains(produtoExistente.get())) {	
				
				double produtoAtual = (produtoExistente.get().getPreco() * produtoExistente.get().getQtdPedidoProduto());
				pedidoExistente.get().setValorTotal(pedidoExistente.get().getValorTotal() - produtoAtual);
				
				// ATRIBUI O NOVO VALOR AO 'VALOR TOTAL' 
				pedidoExistente.get().setValorTotal(pedidoExistente.get().getValorTotal() + (produtoExistente.get().getPreco() * produtoExistente.get().getQtdPedidoProduto()));
				
				produtoExistente.get().setQtdPedidoProduto(produtoExistente.get().getQtdPedidoProduto() + 1);
				
				// ATUALIZA ESTOQUE 
				produtoExistente.get().setEstoque(produtoExistente.get().getEstoque() - 1);
				pedidoExistente.get().setValorTotal(produtoExistente.get().getPreco() * produtoExistente.get().getQtdPedidoProduto());
				
			}else {
				produtoExistente.get().getPedidos().add(pedidoExistente.get());
				
				produtoExistente.get().setQtdPedidoProduto(1);
				produtoExistente.get().setEstoque(produtoExistente.get().getEstoque() - 1);
				
				pedidoExistente.get().setValorTotal(pedidoExistente.get().getValorTotal() + (produtoExistente.get().getPreco() * produtoExistente.get().getQtdPedidoProduto()));
				
			}*/
			
			produtoRepository.save(produtoExistente.get());
			pedidoRepository.save(pedidoExistente.get());
			
			return produtoRepository.save(produtoExistente.get());
			
		}
		
		return null;
	}

}
