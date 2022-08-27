package br.com.helpconnect.LojaVirtual.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.stereotype.Service;

import br.com.helpconnect.LojaVirtual.model.Compras;
import br.com.helpconnect.LojaVirtual.model.Produto;

@Service
public class SendMailService {
	
	public Boolean sendMail(Compras compra) {

        // Recipient's email ID needs to be mentioned.
        String to = compra.getCliente().getEmail();

        // Sender's email ID needs to be mentioned
        String from = "kevin.helpconnect@gmail.com";

        // Assuming you are sending email from through gmails smtp
        String host = "smtp.gmail.com";

        // Get system properties
        Properties properties = System.getProperties();

        // Setup mail server
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");

        // Get the Session object.// and pass 
        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {	

            protected PasswordAuthentication getPasswordAuthentication() {

                return new PasswordAuthentication("kevin.helpconnect@gmail.com", "<senha-envio-email>");

            }

        });
        
        try {
        	
		  MimeMessage msg = new MimeMessage(session);
		  //set message headers
		  msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
		  msg.addHeader("format", "flowed");
		  msg.addHeader("Content-Transfer-Encoding", "8bit");
		
		  msg.setFrom(new InternetAddress(from, "Help Connect"));
		
		  msg.setReplyTo(InternetAddress.parse(to, false));
		
		  msg.setSubject("Dados cadastrais", "UTF-8");
		
		  msg.setContent(GerandoEmailStatusPedido(compra), "text/html");
		  
		  msg.setSentDate(new Date());
		
		  msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to, false));
		  
		  Transport.send(msg);  

		  return true;
        }
	    catch (Exception e) {
	      e.printStackTrace();
	      
	      return false;
	    }

    }
	
	public String GerandoEmailStatusPedido(Compras compra) {
		
		try {
			
			Path path = Paths.get("C:\\Users\\kevin\\Desktop\\templateEmail.html");
			Charset charset = StandardCharsets.UTF_8;
			
			LocalDateTime myDateObj = LocalDateTime.now();  
            DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");  
            
            String formattedDate = myDateObj.format(myFormatObj);

			String content = new String(Files.readAllBytes(path), charset);
				content = content.replaceAll("NUMPEDIDO", compra.getNumeroPedido());
				content = content.replaceAll("NOMEUSUARIO", compra.getCliente().getNome());
				content = content.replaceAll("DATAATUAL", formattedDate);
				content = content.replaceAll("STATUSPEDIDO", compra.getStatus());
				
			String memoriaItensCompra = "";
			
			for(Produto p : compra.getMeuPedido()) {
				memoriaItensCompra = memoriaItensCompra + p.getNome() +" x"+ p.getQtdPedidoProduto() +"<br/>";
				memoriaItensCompra = memoriaItensCompra + RenderizaImagemPedido(p);
				
			}
			
			content = content.replaceAll("PEDIDOCOMPRA", memoriaItensCompra);
			
			if(compra.getStatus().equals("Pedido realizado")) {
				content = content.replaceAll("REALIZARPAGAMENTO", "http://localhost:8080/compras/pagar-compra/"+ compra.getId());
				
			}else {
				content = content.replaceAll("<a href=\"REALIZARPAGAMENTO\" class=\"botao\" >\r\n"
						+ "                      Realizar pagamento\r\n"
						+ "                    </a>", "");
				
			}
			
			return content;
	        
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			return null;
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			return null;
		}
		
	}
	
	public String RenderizaImagemPedido(Produto produto) {
		String retorno = "<div class=\"img\">\r\n"
				+ "                        <img src="+ produto.getImg() +" alt="+ produto.getNome() +" />\r\n"
				+ "                      </div>";
		
		return retorno;
	}
	
}