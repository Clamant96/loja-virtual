package br.com.helpconnect.LojaVirtual.service;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.helpconnect.LojaVirtual.model.Cliente;
import br.com.helpconnect.LojaVirtual.model.Produto;
import br.com.helpconnect.LojaVirtual.repository.ClienteRepository;
import br.com.helpconnect.LojaVirtual.repository.ProdutoRepository;

@Service
public class ProdutoService {

	@Autowired
	private ProdutoRepository produtoRepository;

	@Autowired
	private ClienteRepository clienteRepository;
	
	double a = 0;
	int posicao = 0; // aramazena a posicao do item dentro do array de lista de desejos
	
	/*public Produto compraProduto(long idProduto, long idCliente) {
			
		Optional<Produto> produtoExistente = produtoRepository.findById(idProduto);
		Optional<Cliente> clienteExistente = clienteRepository.findById(idCliente);
		
		if(produtoExistente.isPresent() && clienteExistente.isPresent() && produtoExistente.get().getEstoque() == 0) {
			// ACRESCENTA MAIS 50 PRODUTOS AO ESTOQUE 
			produtoExistente.get().setEstoque(50);
			
		}
		
		// SE O 'PRODUTO' E 'PEDIDO' EXISTIREM, E SE O 'ESTOQUE' CONTEM PRODUTOS DISPONIVEIS ENTRA NA CONDICAO
		if(produtoExistente.isPresent() && clienteExistente.isPresent() && produtoExistente.get().getEstoque() >= 0 && !(clienteExistente.get().getPedidos().isEmpty())) {
			
			// ADICIONA O PRODUTO AO CARRINHO DO USUARIO 
			produtoExistente.get().getPedidos().add(clienteExistente.get());
			
			System.out.println("Retorno: "+ clienteExistente.get().getPedidos().contains(produtoExistente.get()));
			
			System.out.println("QTD produtos "+ clienteExistente.get().getPedidos().size());
			
			// ARMAZENA A QTD DE PRODUTOS
			int contador = 0;
			
			// ARMAZENA OS IDs DOS PRODUTOS LISTADOS DENTRO DO CARRINHO DO USUARIO
			long[] vetor = new long[clienteExistente.get().getPedidos().size()];
			
			for(int i = 0; i < clienteExistente.get().getPedidos().size(); i++) {
				
				vetor[i] = clienteExistente.get().getPedidos().get(i).getId();
				
				System.out.println("Posicao do vetor ["+ i +"] = "+ vetor[i]);
				System.out.println("Produto ID: "+ produtoExistente.get().getId());
				
				if(vetor[i] == produtoExistente.get().getId()) {
					contador++;
					
				}
				
			}
			
			System.out.println("Valor Total a Pagar ATUAL "+ clienteExistente.get().getValorTotal());
			
			// RETIRA O VALOR EXISTENTE DO CARRINHO PARA PODER SER RECALCULADO 
			clienteExistente.get().setValorTotal(clienteExistente.get().getValorTotal() - (produtoExistente.get().getPreco() * contador));
			
			// COMPENSA ACRESCENTANDO O NOVO PRODUTO AO CARRINHO ==> O ID INFORMADO 
			contador++;
			System.out.println("QTD de produto: "+ contador);
			
			// INSERE O VALOR DO CONTADOR == QTD DE PRODUTOS POR ID 
			produtoExistente.get().setQtdPedidoProduto(contador);
			// DEBITA O ESTOQUE SEMPRE QUE E INSERIDO UM PRODUTO A UM CARRINHO 
			produtoExistente.get().setEstoque(produtoExistente.get().getEstoque() - 1);
			
			System.out.println("Valor Total a Pagar ZERADO "+ clienteExistente.get().getValorTotal());
			
			// AJUSTA O VALOR DO CARRINHO DE UM USUARIO ESPECIFICO 
			clienteExistente.get().setValorTotal(clienteExistente.get().getValorTotal() + (produtoExistente.get().getPreco() * produtoExistente.get().getQtdPedidoProduto()));
			
			System.out.println("Valor Total a Pagar ATUALIZADO "+ clienteExistente.get().getValorTotal());
			
			System.out.println("Contador: "+ contador);
			System.out.println("QTD Produtos Comprados: "+ produtoExistente.get().getQtdPedidoProduto());
			System.out.println("Valor Produto: "+ produtoExistente.get().getPreco());
			System.out.println("Valor Carrinho: "+ clienteExistente.get().getValorTotal());
			
			System.out.println("IF");
			System.out.println("ID: "+ clienteExistente.get().getId() +" | Valor a pagar: "+ clienteExistente.get().getValorTotal());
			
			produtoRepository.save(produtoExistente.get());
			clienteRepository.save(clienteExistente.get());
			clienteRepository.save(clienteExistente.get()).getValorTotal();
			
			return produtoRepository.save(produtoExistente.get());
			
		}else if(produtoExistente.isPresent() && clienteExistente.isPresent()) {
			// ADICIONA O PRODUTO AO CARRINHO DO USUARIO
			produtoExistente.get().getPedidos().add(clienteExistente.get());
			
			// ADICIONA UM PRODUTO AO QTD PRODUTOS DENTRO DE PRODUTO 
			produtoExistente.get().setQtdPedidoProduto(1);
			// GERENCIA O ESTOQUE DEBITNADO UM PRODUTO DO ESTOQUE 
			produtoExistente.get().setEstoque(produtoExistente.get().getEstoque() - 1);
			
			// ATUALIZA O VALOR DO CARRINHO DO USUARIO 
			clienteExistente.get().setValorTotal(clienteExistente.get().getValorTotal() + (produtoExistente.get().getPreco() * produtoExistente.get().getQtdPedidoProduto()));
			
			System.out.println("ELSE");
			System.out.println("ID: "+ clienteExistente.get().getId() +" | Valor a pagar: "+ clienteExistente.get().getValorTotal());
			
			produtoRepository.save(produtoExistente.get());
			clienteRepository.save(clienteExistente.get());
			clienteRepository.save(clienteExistente.get()).getValorTotal();
			
			return produtoRepository.save(produtoExistente.get());
			
		}
		
		return null;
	}*/

	public Produto compraProduto(long idProduto, long idCliente) {
			
		Optional<Produto> produtoExistente = produtoRepository.findById(idProduto);
		Optional<Cliente> clienteExistente = clienteRepository.findById(idCliente);
		
		// SE O 'PRODUTO' E 'PEDIDO' EXISTIREM, E SE O 'ESTOQUE' CONTEM PRODUTOS DISPONIVEIS ENTRA NA CONDICAO
		if(produtoExistente.isPresent() && clienteExistente.isPresent() && produtoExistente.get().getEstoque() >= 0 && !(clienteExistente.get().getPedidos().isEmpty())) {

			// ADICIONA O PRODUTO AO CARRINHO DO USUARIO 
			produtoExistente.get().getPedidos().add(clienteExistente.get());

			// DEBITA O ESTOQUE SEMPRE QUE E INSERIDO UM PRODUTO A UM CARRINHO 
			produtoExistente.get().setEstoque(produtoExistente.get().getEstoque() - 1);

			int contadorProduto = 0;
			
			// NAVEGA NO ARRAY DE PRODUTO DO USUARIO, E CASO ELE SEJA REPEDIDO, E INCREMENTADO O VALOR NA QTDPRODUTO E ADICIONADO AO ARRAY
			// CASO ELE NAO EXISTA AINDA NO ARRAY
			for (Produto produto : clienteExistente.get().getPedidos()) {

				for (Produto produtoRepeticao : clienteExistente.get().getPedidos()) {

					// VERIFICA SE EXISTE REPETICAO DO PRODUTO, PARA REALIZAR O INCREMENTO DO VALOR AO 'QTDPRODUTO'
					if(produto.getId() == produtoRepeticao.getId()) {
							
						contadorProduto = contadorProduto + 1;
						
						produto.setQtdPedidoProduto(contadorProduto); // INCREMENTA O VALOR NO CONTADOR

						// GERENCIA O ESTOQUE DEBITNADO UM PRODUTO DO ESTOQUE 
						produtoExistente.get().setQtdPedidoProduto(contadorProduto);

					}

				}

			}

			// RETIRA O VALOR EXISTENTE DO CARRINHO PARA PODER SER RECALCULADO 
			clienteExistente.get().setValorTotal(clienteExistente.get().getValorTotal() - (produtoExistente.get().getPreco() * contadorProduto));

			// COMPENSA ACRESCENTANDO O NOVO PRODUTO AO CARRINHO ==> O ID INFORMADO 
			contadorProduto = contadorProduto + 1;
		
			// AJUSTA O VALOR DO CARRINHO DE UM USUARIO ESPECIFICO 
			clienteExistente.get().setValorTotal(clienteExistente.get().getValorTotal() + (produtoExistente.get().getPreco() * contadorProduto));

			produtoRepository.save(produtoExistente.get());
			clienteRepository.save(clienteExistente.get());
			clienteRepository.save(clienteExistente.get()).getValorTotal();
			
			return produtoRepository.save(produtoExistente.get());
			
		}else if(produtoExistente.isPresent() && clienteExistente.isPresent()) {

			// ADICIONA O PRODUTO AO CARRINHO DO USUARIO
			produtoExistente.get().getPedidos().add(clienteExistente.get());
			
			// GERENCIA O ESTOQUE DEBITNADO UM PRODUTO DO ESTOQUE 
			produtoExistente.get().setQtdPedidoProduto(1);

			// GERENCIA O ESTOQUE DEBITNADO UM PRODUTO DO ESTOQUE 
			produtoExistente.get().setEstoque(produtoExistente.get().getEstoque() - 1);
			
			// ATUALIZA O VALOR DO CARRINHO DO USUARIO 
			double total = clienteExistente.get().getValorTotal() + (produtoExistente.get().getPreco() * produtoExistente.get().getQtdPedidoProduto());
		
			NumberFormat formatter = new DecimalFormat("#0.00");
			clienteExistente.get().setValorTotal(Double.parseDouble(formatter.format(total).replace(",", ".")));
			
			produtoRepository.save(produtoExistente.get());
			clienteRepository.save(clienteExistente.get());
			clienteRepository.save(clienteExistente.get()).getValorTotal();
			
			return produtoRepository.save(produtoExistente.get());
			
		}
		
		return null;
	}

	public List<Produto> retirraDuplicidadeCarrinho(Optional<Cliente> cliente) {

		List<Produto> produtos = new ArrayList<>();

		int contadorProduto = 0;
		
		// NAVEGA NO ARRAY DE PRODUTO DO USUARIO, E CASO ELE SEJA REPEDIDO, E INCREMENTADO O VALOR NA QTDPRODUTO E ADICIONADO AO ARRAY
		// CASO ELE NAO EXISTA AINDA NO ARRAY
		for (Produto produto : cliente.get().getPedidos()) {

			for (Produto produtoRepeticao : cliente.get().getPedidos()) {

				System.out.println(produtoRepeticao.getNome());

				// VERIFICA SE EXISTE REPETICAO DO PRODUTO, PARA REALIZAR O INCREMENTO DO VALOR AO 'QTDPRODUTO'
				if(produto.getId() == produtoRepeticao.getId()) {
						
					contadorProduto = contadorProduto + 1;
					
					produto.setQtdPedidoProduto(contadorProduto); // INCREMENTA O VALOR NO CONTADOR

				}

			}

			if(!produtos.contains(produto)) {
				
				// ADICIONA O PRODUTO AO CARRINHO DO USUARIO 
				produtos.add(produto);
				
			}
			
			contadorProduto = 0; // ZERA O CONTADOR PARA O PROXIMO PRODUTO
			
		}

		contadorProduto = 0; // ZERA O CONTADOR PARA O PROXIMO PRODUTO

		return produtos;
	}
	
	/* DELETAR OBJETOS DO PRODUTO */
	public Produto deletarProduto(long idProduto, long idCliente) {

		Optional<Produto> produtoExistente = produtoRepository.findById(idProduto);
		Optional<Cliente> clienteExistente = clienteRepository.findById(idCliente);
		
		if(clienteExistente.get().getPedidos().contains(produtoExistente.get())) {
			/* REMOVE O CARRINHO DO PRODUTO */
			produtoExistente.get().getPedidos().remove(clienteExistente.get());
			
			/* GERENCIA O ESTOQUE DEBITNADO UM PRODUTO DO ESTOQUE */
			produtoExistente.get().setEstoque(produtoExistente.get().getEstoque() + 1);
			
			int contador = 0;
			
			/* ARMAZENA OS IDs DOS PRODUTOS LISTADOS DENTRO DO CARRINHO DO USUARIO */
			long[] vetor = new long[clienteExistente.get().getPedidos().size()];
			
			for(int i = 0; i < clienteExistente.get().getPedidos().size(); i++) {
				
				vetor[i] = clienteExistente.get().getPedidos().get(i).getId();
				
				System.out.println("Posicao do vetor ["+ i +"] = "+ vetor[i]);
				System.out.println("Produto ID: "+ produtoExistente.get().getId());
				
				if(vetor[i] == produtoExistente.get().getId()) {
					contador++;
					
				}
				
			}
			
			produtoExistente.get().setQtdPedidoProduto(contador - 1);
			
			/* AJUSTA O VALOR DO CARRINHO DE UM USUARIO ESPECIFICO */
			clienteExistente.get().setValorTotal(clienteExistente.get().getValorTotal() - produtoExistente.get().getPreco());
			
			a = clienteExistente.get().getValorTotal();
			a = Math.floor(a * 100) / 100;
				clienteExistente.get().setValorTotal(a);
				
			if(clienteExistente.get().getValorTotal() < 0) {
				clienteExistente.get().setValorTotal(0);
			}
			
			produtoRepository.save(produtoExistente.get());
			clienteRepository.save(clienteExistente.get());
			clienteRepository.save(clienteExistente.get()).getValorTotal();
			
			return produtoRepository.save(produtoExistente.get());
		}
		
		return null;
	}
	
	/* ADICIONA UM PRODUTO ESPECIFICO A LISTA DE DESEJOS DO USUARIO */
	public Produto adicionarProdutoListaDeDesejo(long idProduto, long idCiente) {
		Optional<Produto> produtoExistente = produtoRepository.findById(idProduto);
		Optional<Cliente> clienteExistente = clienteRepository.findById(idCiente);
		
		/* CASO OS ITENS EXISTAM NA BASE DE DADOS E O PRODUTO AINDA NAO ESTEJA INCLUSO DENTRO DA LSITA DE DESEJOS */
		if(produtoExistente.isPresent() && clienteExistente.isPresent() && !(produtoExistente.get().getListaDesejos().contains(clienteExistente.get()))) {
			// ADICIONA O PRODUTO A LISTA DE DESEJOS DO USUARIO
			
			System.out.println("Acessou o produto e lista por id");
			
			produtoExistente.get().getListaDesejos().add(clienteExistente.get());
			
			System.out.println("Adicionou o produto a lista");
			
			produtoRepository.save(produtoExistente.get());
			
			System.out.println("Salvou o produto com o novo dado");
			
			return produtoRepository.save(produtoExistente.get());
			
		}
		
		return null;
		
	}
	
	/* REMOVE UM PRODUTO ESPECIFICO DA LISTA DE DESEJOS DO USUARIO */
	public Produto removeProdutoListaDeDesejo(long idProduto, long idListaDeDesejo) {
		Optional<Produto> produtoExistente = produtoRepository.findById(idProduto);
		Optional<Cliente> clienteExistente = clienteRepository.findById(idListaDeDesejo);
		
		/* CASO OS ITENS EXISTAM NA BASE DE DADOS E O PRODUTO AINDA NAO ESTEJA INCLUSO DENTRO DA LSITA DE DESEJOS */
		if(produtoExistente.get().getListaDesejos().contains(clienteExistente.get())) {
			/* REMOVE O CARRINHO DO PRODUTO */
			produtoExistente.get().getListaDesejos().remove(clienteExistente.get());
			
			produtoRepository.save(produtoExistente.get());
			
			return produtoRepository.save(produtoExistente.get());
			
		}
		
		return null;
		
	}
	
	/* PESQUISANDO POR PRODUTO ESPECIFICO EM UMA LISTA DE DESEJOS DE PRODUTOS */
	public List<Produto> pesquisaPorIdDeProdutoNaListaDeDesejos(long idListaDeDesejo, String nome) {
		Optional<Cliente> clienteExistente = clienteRepository.findById(idListaDeDesejo);
		
		// ARMAZENA OS IDs DOS PRODUTOS LISTADOS DENTRO DO CARRINHO DO USUARIO
		long[] vetor = new long[clienteExistente.get().getPedidos().size()];
		
		for(int i = 0; i < vetor.length; i++) {
			
			if(clienteExistente.get().getPedidos().get(i).getNome().contains(nome)) {
				
				return produtoRepository.findAllByNomeContainingIgnoreCase(nome);	
			}
			
		}
		
		return null;
		
	}
	
	/* PESQUISANDO POR PRODUTO ESPECIFICO EM UMA LISTA DE DESEJOS DE PRODUTOS */
	public List<Produto> pesquisaPorProdutoNaListaDeDesejos(long idListaDeDesejo) {
		Optional<Cliente> clienteExistente = clienteRepository.findById(idListaDeDesejo);
		
		if(clienteExistente.isPresent()) {
			clienteExistente.get().getPedidos();
			
			return clienteRepository.save(clienteExistente.get()).getPedidos();
			
		}
		
		return null;
		
	}
	
	/* PESQUISANDO POR PRODUTO ESPECIFICO EM UMA LISTA DE DESEJOS DE PRODUTOS */
	public List<Produto> pesquisaPorProdutoNoCarrinho(long idCliente) {
		Optional<Cliente> clienteExistente = clienteRepository.findById(idCliente);
		
		if(clienteExistente.isPresent()) {
			clienteExistente.get().getPedidos();
			
			return clienteRepository.save(clienteExistente.get()).getPedidos();
			
		}
		
		return null;
		
	}

}
