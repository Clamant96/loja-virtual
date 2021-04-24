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
		if(produtoExistente.isPresent() && pedidoExistente.isPresent() && produtoExistente.get().getEstoque() >= 0 && !(pedidoExistente.get().getProdutos().isEmpty())) {
			
			/* ADICIONA O PRODUTO AO CARRINHO DO USUARIO */
			produtoExistente.get().getPedidos().add(pedidoExistente.get());
			
			/*produtoExistente.get().setQtdPedidoProduto(1);
			produtoExistente.get().setEstoque(produtoExistente.get().getEstoque() - 1);*/
			
			//pedidoExistente.get().setValorTotal(pedidoExistente.get().getValorTotal() + (produtoExistente.get().getPreco() * produtoExistente.get().getQtdPedidoProduto()));
			
			/* AJUSTA O CARRINHO RETIRANDO O VALOR DO PRODUTO DE DENTRO DO VALOR DO CARRINHO DO USUARIO */
			//pedidoExistente.get().setValorTotal(pedidoExistente.get().getValorTotal() - (produtoExistente.get().getPreco() * produtoExistente.get().getQtdPedidoProduto()));
			
			System.out.println("Retorno: "+ pedidoExistente.get().getProdutos().contains(produtoExistente.get()));
			
			System.out.println("QTD produtos "+ pedidoExistente.get().getProdutos().size());
			
			/* ARMAZENA A QTD DE PRODUTOS */
			int contador = 0;
			
			//System.out.println("Valor: "+ pedidoExistente.get().getProdutos().get((int)idProduto).getId());
			
			/* ARMAZENA OS IDs DOS PRODUTOS LISTADOS DENTRO DO CARRINHO DO USUARIO */
			long[] vetor = new long[pedidoExistente.get().getProdutos().size()];
			
			for(int i = 0; i < pedidoExistente.get().getProdutos().size(); i++) {
				
				vetor[i] = pedidoExistente.get().getProdutos().get(i).getId();
				
				System.out.println("Posicao do vetor ["+ i +"] = "+ vetor[i]);
				System.out.println("Produto ID: "+ produtoExistente.get().getId());
				
				if(vetor[i] == produtoExistente.get().getId()) {
					contador++;
					
				}
				
			}
			
			System.out.println("Valor Total a Pagar ATUAL "+ pedidoExistente.get().getValorTotal());
			
			pedidoExistente.get().setValorTotal(pedidoExistente.get().getValorTotal() - (produtoExistente.get().getPreco() * contador));
			
			/* COMPENSA ACRESCENTANDO O NOVO PRODUTO AO CARRINHO ==> O ID INFORMADO */
			contador++;
			System.out.println("QTD de produto: "+ contador);
			
			/* INSERE O VALOR DO CONTADOR == QTD DE PRODUTOS POR ID */
			produtoExistente.get().setQtdPedidoProduto(contador);
			/* DEBITA O ESTOQUE SEMPRE QUE E INSERIDO UM PRODUTO A UM CARRINHO */
			produtoExistente.get().setEstoque(produtoExistente.get().getEstoque() - 1);
			
			System.out.println("Valor Total a Pagar ZERADO "+ pedidoExistente.get().getValorTotal());
			
			/* AJUSTA O VALOR DO CARRINHO DE UM USUARIO ESPECIFICO */
			pedidoExistente.get().setValorTotal(pedidoExistente.get().getValorTotal() + (produtoExistente.get().getPreco() * produtoExistente.get().getQtdPedidoProduto()));
			
			System.out.println("Valor Total a Pagar ATUALIZADO "+ pedidoExistente.get().getValorTotal());
			
			System.out.println("Contador: "+ contador);
			System.out.println("QTD Produtos Comprados: "+ produtoExistente.get().getQtdPedidoProduto());
			System.out.println("Valor Produto: "+ produtoExistente.get().getPreco());
			System.out.println("Valor Carrinho: "+ pedidoExistente.get().getValorTotal());
			
			System.out.println("IF");
			System.out.println("ID: "+ pedidoExistente.get().getId() +" | Valor a pagar: "+ pedidoExistente.get().getValorTotal());
			
			produtoRepository.save(produtoExistente.get());
			pedidoRepository.save(pedidoExistente.get());
			pedidoRepository.save(pedidoExistente.get()).getValorTotal();
			
			return produtoRepository.save(produtoExistente.get());
			
		}else {
			/* ADICIONA O PRODUTO AO CARRINHO DO USUARIO */
			produtoExistente.get().getPedidos().add(pedidoExistente.get());
			
			/* ADICIONA UM PRODUTO AO QTD PRODUTOS DENTRO DE PRODUTO */
			produtoExistente.get().setQtdPedidoProduto(1);
			/* GERENCIA O ESTOQUE DEBITNADO UM PRODUTO DO ESTOQUE */
			produtoExistente.get().setEstoque(produtoExistente.get().getEstoque() - 1);
			
			/* ATUALIZA O VALOR DO CARRINHO DO USUARIO */
			pedidoExistente.get().setValorTotal(pedidoExistente.get().getValorTotal() + (produtoExistente.get().getPreco() * produtoExistente.get().getQtdPedidoProduto()));
			
			System.out.println("ELSE");
			System.out.println("ID: "+ pedidoExistente.get().getId() +" | Valor a pagar: "+ pedidoExistente.get().getValorTotal());
			
			//pedidoExistente.get().setQtdProduto(1);
			
			produtoRepository.save(produtoExistente.get());
			pedidoRepository.save(pedidoExistente.get());
			pedidoRepository.save(pedidoExistente.get()).getValorTotal();
			
			return produtoRepository.save(produtoExistente.get());
			
		}
		
		//return null;
	}

}
