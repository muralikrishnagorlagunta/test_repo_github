package com.mt.demo.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Banker implements Serializable {

	private static final long serialVersionUID = 1L;

	private String bankerId;
	private String token;

	public Banker() {
	}

	public Banker(String bankerId, String bankerIdentity) {
		this.bankerId = bankerId;
		this.token = bankerIdentity;
	}

	/**
	 * @return the customerId
	 */
	public String getBankerId() {
		return bankerId;
	}

	/**
	 * @param customerId
	 *            the customerId to set
	 */
	@XmlElement
	public void setBankerId(String customerId) {
		this.bankerId = customerId;
	}

	
	/**
	 * @return the customerIdentity
	 */
	public String getBankerIdentity() {
		return token;
	}

	/**
	 * @param customerIdentity
	 *            the customerIdentity to set
	 */
	@XmlElement
	public void setBankerIdentity(String bankerIdentity) {
		this.token = bankerIdentity;
	}


	public boolean validate(String identity) {
		return token.equals(identity);
	}

}
