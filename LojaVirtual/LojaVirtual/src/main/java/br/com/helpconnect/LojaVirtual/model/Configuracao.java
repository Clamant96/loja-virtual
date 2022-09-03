package br.com.helpconnect.LojaVirtual.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "configuracao")
public class Configuracao {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@NotNull
	private String emailSMTP;
	
	@NotNull
	private String senhaSMTP;
	
	@NotNull
	private String hostSMTP;
	
	@NotNull
	private String postSMTP;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getEmailSMTP() {
		return emailSMTP;
	}

	public void setEmailSMTP(String emailSMTP) {
		this.emailSMTP = emailSMTP;
	}

	public String getSenhaSMTP() {
		return senhaSMTP;
	}

	public void setSenhaSMTP(String senhaSMTP) {
		this.senhaSMTP = senhaSMTP;
	}

	public String getHostSMTP() {
		return hostSMTP;
	}

	public void setHostSMTP(String hostSMTP) {
		this.hostSMTP = hostSMTP;
	}

	public String getPostSMTP() {
		return postSMTP;
	}

	public void setPostSMTP(String postSMTP) {
		this.postSMTP = postSMTP;
	}
	
	
	
}
