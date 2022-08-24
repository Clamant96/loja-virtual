package br.com.helpconnect.LojaVirtual.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/*import br.com.techzee.correios.ws.ConsultaCorreios;
import br.com.techzee.correios.ws.dto.CorreiosPrecoPrazo;
import br.com.techzee.correios.ws.enumeration.CorreiosTipoServico;
import br.com.techzee.correios.ws.enumeration.IndicadorSN;*/

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import br.com.helpconnect.LojaVirtual.model.Cliente;
import br.com.helpconnect.LojaVirtual.model.Compras;
import br.com.helpconnect.LojaVirtual.model.Correio;
import br.com.helpconnect.LojaVirtual.model.EnderecoEntrega;
import br.com.helpconnect.LojaVirtual.model.Produto;
import br.com.helpconnect.LojaVirtual.repository.CompraRepository;

@Service
public class CorreioService {
	
	@Autowired
	private CompraRepository compraRepository;

	// REFERENCIA
	// https://www.guj.com.br/t/webservice-cliente-calculo-de-frete-correios/196814
	public String CalculoFrete(Correio correio) {
		
		//URL do webservice correio para calculo de frete
		String urlString = "http://ws.correios.com.br/calculador/CalcPrecoPrazo.aspx";

		// os parametros a serem enviados
		Properties parameters = new Properties();

		parameters.setProperty("nCdEmpresa", correio.getnCdEmpresa());
		parameters.setProperty("sDsSenha", correio.getsDsSenha());
		parameters.setProperty("nCdServico", correio.getnCdServico());
		parameters.setProperty("sCepOrigem", correio.getsCepOrigem());
		parameters.setProperty("sCepDestino", correio.getsCepDestino());
		parameters.setProperty("nVlPeso", correio.getnVlPeso());
		parameters.setProperty("nCdFormato", correio.getnCdFormato());
		parameters.setProperty("nVlComprimento", correio.getnVlComprimento());
		parameters.setProperty("nVlAltura", correio.getnVlAltura());
		parameters.setProperty("nVlLargura", correio.getnVlLargura());
		parameters.setProperty("nVlDiametro", correio.getnVlDiametro());
		parameters.setProperty("sCdMaoPropria", correio.getsCdMaoPropria());
		parameters.setProperty("nVlValorDeclarado", correio.getnVlValorDeclarado());
		parameters.setProperty("sCdAvisoRecebimento", correio.getsCdAvisoRecebimento());
		parameters.setProperty("StrRetorno", correio.getStrRetorno());
		
		// o iterador, para criar a URL
		Iterator i = parameters.keySet().iterator();
		// o contador
		int counter = 0;

		// enquanto ainda existir parametros
		while (i.hasNext()) {

			// pega o nome
			String name = (String) i.next();
			// pega o valor
			String value = parameters.getProperty(name);

			// adiciona com um conector (? ou &)
			// o primeiro é ?, depois são &
			urlString += (++counter == 1 ? "?" : "&") + name + "=" + value;
			
		}

		try {
			// cria o objeto url
			URL url = new URL(urlString);
			
			// cria o objeto httpurlconnection
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();

			// seta o metodo
			connection.setRequestProperty("Request-Method", "GET");

			// seta a variavel para ler o resultado
			connection.setDoInput(true);
			connection.setDoOutput(false);

			// conecta com a url destino
			connection.connect();

			// abre a conexão pra input
			BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));

			// le ate o final
			StringBuffer newData = new StringBuffer();
			String s = "";
			
			while (null != ((s = br.readLine()))) {
				newData.append(s);
			}
			
			br.close();
			
			//Prepara o XML que está em string para executar leitura por nodes
			DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		    InputSource is = new InputSource();
		    is.setCharacterStream(new StringReader(newData.toString()));
		    Document doc = db.parse(is);
		    NodeList nodes = doc.getElementsByTagName("cServico");

		    //Faz a leitura dos nodes
		    for (int j = 0; j < nodes.getLength(); j++) {
		      Element element = (Element) nodes.item(j);

		      NodeList valor = element.getElementsByTagName("Valor");
		      Element line = (Element) valor.item(0);
		      
		      return getCharacterDataFromElement(line);
		    }	
		    
		    return null;

		} catch (Exception e) {
			e.printStackTrace();
			
			return null;
		}
	
	}
	
	public static String getCharacterDataFromElement(Element e) {
		
	    Node child = e.getFirstChild();
	    
	    if (child instanceof CharacterData) {
	      CharacterData cd = (CharacterData) child;
	      
	      return cd.getData();
	    }
	    
	    return "";
	}
	
	public EnderecoEntrega GetEnderecoPorCEP(String cep) {
		
		String uri = "https://viacep.com.br/ws/"+ cep +"/json/";
		
		RestTemplate restTemplate = new RestTemplate();
		EnderecoEntrega result = restTemplate.getForObject(uri, EnderecoEntrega.class);
		
		return result;
	}
	
	public double calculaFreteDeProdutos(List<Produto> produtos, String codProduto, String cep) {
		
		Correio correio = new Correio();
		
		double memoria = 0.0;
		
		for(Produto p : produtos) {
			correio.setnCdEmpresa("");
	        correio.setsDsSenha("");
	        correio.setnCdServico(codProduto);
	        correio.setsCepOrigem("06029000");
	        correio.setsCepDestino(cep);
	        correio.setnVlPeso(Double.toString((p.getPeso() * p.getQtdPedidoProduto())));
	        correio.setnCdFormato("1");
	        correio.setnVlComprimento(Double.toString((p.getComprimento() * p.getQtdPedidoProduto())));
	        correio.setnVlAltura(Double.toString((p.getAltura() * p.getQtdPedidoProduto())));
	        correio.setnVlLargura(Double.toString((p.getLargura() * p.getQtdPedidoProduto())));
	        correio.setnVlDiametro("0");
	        correio.setsCdMaoPropria("s");
	        correio.setnVlValorDeclarado(Double.toString((p.getPreco() * p.getQtdPedidoProduto())));
	        correio.setsCdAvisoRecebimento("s");
	        correio.setStrRetorno("xml");
	        
	        memoria = memoria + Double.parseDouble(CalculoFrete(correio).replace(",", "."));
	        
		}
		
		return memoria;
	}
	
	public boolean InsereFrete(long idCompra, double frete) {
		try {
			Optional<Compras> comprasExistente = compraRepository.findById(idCompra);
			
			comprasExistente.get().setValorTotal(comprasExistente.get().getValorTotal() + frete);
			
			compraRepository.save(comprasExistente.get());
			
			return true;
		}catch(Exception erro) {
			
			return false;
		}
		
	}
	
}
