package br.com.helpconnect.LojaVirtual.service;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.helpconnect.LojaVirtual.model.Cliente;
import br.com.helpconnect.LojaVirtual.model.Compras;
import br.com.helpconnect.LojaVirtual.model.Produto;
import br.com.helpconnect.LojaVirtual.repository.CompraRepository;
import br.com.helpconnect.LojaVirtual.repository.ProdutoRepository;

@Service
public class CompraService {
	
	@Autowired
	private ProdutoRepository produtoRepository;
	
	@Autowired
	private CompraRepository compraRepository;
	
	@Autowired
	private ProdutoService produtoService;
	
	double a = 0;
	int posicao = 0; // aramazena a posicao do item dentro do array de lista de desejos

	public boolean compraProduto(long idProduto, long idCompra) {
			
		Optional<Produto> produtoExistente = produtoRepository.findById(idProduto);
		Optional<Compras> compraExistente = compraRepository.findById(idCompra);
		
		System.out.println("PRODUTO: "+ produtoExistente.isPresent());
		System.out.println("COMPRA: "+ compraExistente.isPresent());
		
		// SE O 'PRODUTO' E 'PEDIDO' EXISTIREM, E SE O 'ESTOQUE' CONTEM PRODUTOS DISPONIVEIS ENTRA NA CONDICAO
		if(produtoExistente.isPresent() && compraExistente.isPresent() && produtoExistente.get().getEstoque() >= 0 && !(compraExistente.get().getMeuPedido().isEmpty())) {

			// RETIRA O PRODUTO DO CARRINHO DO USUARIO
			produtoService.deletarProduto(idProduto, compraExistente.get().getCliente().getId());
						
			// ADICIONA O PRODUTO AO CARRINHO DO USUARIO 
			produtoExistente.get().getCompra().add(compraExistente.get());

			// DEBITA O ESTOQUE SEMPRE QUE E INSERIDO UM PRODUTO A UM CARRINHO 
			// produtoExistente.get().setEstoque(produtoExistente.get().getEstoque() - 1);

			int contadorProduto = 0;
			
			// NAVEGA NO ARRAY DE PRODUTO DO USUARIO, E CASO ELE SEJA REPEDIDO, E INCREMENTADO O VALOR NA QTDPRODUTO E ADICIONADO AO ARRAY
			// CASO ELE NAO EXISTA AINDA NO ARRAY
			for (Produto produto : compraExistente.get().getMeuPedido()) {

				for (Produto produtoRepeticao : compraExistente.get().getMeuPedido()) {

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
			compraExistente.get().setValorTotal(compraExistente.get().getValorTotal() - (produtoExistente.get().getPreco() * contadorProduto));

			// COMPENSA ACRESCENTANDO O NOVO PRODUTO AO CARRINHO ==> O ID INFORMADO 
			contadorProduto = contadorProduto + 1;
		
			// AJUSTA O VALOR DO CARRINHO DE UM USUARIO ESPECIFICO 
			compraExistente.get().setValorTotal(compraExistente.get().getValorTotal() + (produtoExistente.get().getPreco() * contadorProduto));

			produtoRepository.save(produtoExistente.get());
			compraRepository.save(compraExistente.get());
			compraRepository.save(compraExistente.get()).getValorTotal();
			
			return true;
			
		}else if(produtoExistente.isPresent() && compraExistente.isPresent()) {
			
			// RETIRA O PRODUTO DO CARRINHO DO USUARIO
			produtoService.deletarProduto(idProduto, compraExistente.get().getCliente().getId());

			// ADICIONA O PRODUTO AO CARRINHO DO USUARIO
			produtoExistente.get().getCompra().add(compraExistente.get());
			
			// GERENCIA O ESTOQUE DEBITNADO UM PRODUTO DO ESTOQUE 
			produtoExistente.get().setQtdPedidoProduto(1);

			// GERENCIA O ESTOQUE DEBITNADO UM PRODUTO DO ESTOQUE 
			produtoExistente.get().setEstoque(produtoExistente.get().getEstoque() - 1);
			
			// ATUALIZA O VALOR DO CARRINHO DO USUARIO 
			double total = compraExistente.get().getValorTotal() + (produtoExistente.get().getPreco() * produtoExistente.get().getQtdPedidoProduto());
		
			NumberFormat formatter = new DecimalFormat("#0.00");
			compraExistente.get().setValorTotal(Double.parseDouble(formatter.format(total).replace(",", ".")));
			
			produtoRepository.save(produtoExistente.get());
			compraRepository.save(compraExistente.get());
			compraRepository.save(compraExistente.get()).getValorTotal();
			
			return true;
			
		}
		
		return false;
	}
	
	public String geraNumeroCompra(Compras compra) {
		
		List<Compras> listaCompras = compraRepository.findAll();
		
		try {
			long numeroPedido;
			
			if(listaCompras.size() == 0) {
				numeroPedido = 00000001;
			
			}else {
				numeroPedido = Long.parseLong(listaCompras.get( (listaCompras.size() - 1) ).getNumeroPedido().replace("0", "")) + 1;
			
			}
			
			switch(String.valueOf(numeroPedido).length()) {
				case 1:
					compra.setNumeroPedido("0000000"+ String.valueOf(numeroPedido));
				break;
				case 2:
					compra.setNumeroPedido("000000"+ String.valueOf(numeroPedido));
				break;
				case 3:
					compra.setNumeroPedido("00000"+ String.valueOf(numeroPedido));
				break;
				case 4:
					compra.setNumeroPedido("0000"+ String.valueOf(numeroPedido));
				break;
				case 5:
					compra.setNumeroPedido("000"+ String.valueOf(numeroPedido));
				break;
				case 6:
					compra.setNumeroPedido("00"+ String.valueOf(numeroPedido));
				break;
				case 7:
					compra.setNumeroPedido("0"+ String.valueOf(numeroPedido));
				break;
				case 8:
					compra.setNumeroPedido(String.valueOf(numeroPedido));
				break;
				default:
					compra.setNumeroPedido(String.valueOf(numeroPedido));
				break;
			}
			
		}catch(Exception ex) {
			
		}
		
		return compra.getNumeroPedido();
	}
	
	public List<Produto> retirraDuplicidadeCarrinho(Optional<Compras> compra) {

		List<Produto> produtos = new ArrayList<>();

		int contadorProduto = 0;
		
		// NAVEGA NO ARRAY DE PRODUTO DO USUARIO, E CASO ELE SEJA REPEDIDO, E INCREMENTADO O VALOR NA QTDPRODUTO E ADICIONADO AO ARRAY
		// CASO ELE NAO EXISTA AINDA NO ARRAY
		for (Produto produto : compra.get().getMeuPedido()) {

			for (Produto produtoRepeticao : compra.get().getMeuPedido()) {

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

}
