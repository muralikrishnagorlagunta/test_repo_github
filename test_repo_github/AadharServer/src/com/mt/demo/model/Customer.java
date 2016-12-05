package com.mt.demo.model;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Customer implements Serializable {

	private static final int TIMEOUT = 60000;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String customerId;
	private String posid;
	private String customerIdentity;
	private String accoutNumber;
	private String token;
	private Date tokenGenTime;
	private String whoWantsToken;
	private int amount;

	public Customer() {
	}

	public Customer(String customerId, String posid, String customerIdentity,
			String accountNumber) {
		this.customerId = customerId;
		this.posid = posid;
		this.customerIdentity = customerIdentity;
		this.accoutNumber = accountNumber;
	}

	/**
	 * @return the customerId
	 */
	public String getCustomerId() {
		return customerId;
	}

	/**
	 * @param customerId
	 *            the customerId to set
	 */
	@XmlElement
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	/**
	 * @return the Point of Sale Id
	 */
	public String getPosid() {
		return posid;
	}

	/**
	 * @param posid
	 *            the posid to set
	 */
	@XmlElement
	public void setPosid(String posid) {
		this.posid = posid;
	}

	/**
	 * @return the customerIdentity
	 */
	public String getCustomerIdentity() {
		return customerIdentity;
	}

	/**
	 * @param customerIdentity
	 *            the customerIdentity to set
	 */
	@XmlElement
	public void setCustomerIdentity(String customerIdentity) {
		this.customerIdentity = customerIdentity;
	}

	/**
	 * @return the accoutNumber
	 */
	public String getAccoutNumber() {
		return accoutNumber;
	}

	/**
	 * @param accoutNumber
	 *            the accoutNumber to set
	 */
	public void setAccoutNumber(String accoutNumber) {
		this.accoutNumber = accoutNumber;
	}

	public String validate(String posid, String fingerPrint, int amount) {
		token = null;
		whoWantsToken = null;
		this.amount = 0;
		if (customerIdentity.equals(fingerPrint)) {
			this.token = UUID.randomUUID().toString().replaceAll("-", "");
			this.whoWantsToken = posid;
			this.amount = amount;
			tokenGenTime = Calendar.getInstance().getTime();
		}
		return token;
	}

	public boolean isTokenValid(String posid, String token) {
		boolean result = false;
		
		if (posid.equals(whoWantsToken) && token.equals(token)) {
			Date now = new Date();
			if ((now.getTime() - tokenGenTime.getTime()) < TIMEOUT) {
				result = true;
			}
		}
		
		return result;
	}

	public int getAmount() {
		return amount;
	}
}
