package com.mt.demo.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Transaction {

	private final String bankerId = "98237834";
	private final String bankerToken = "ABCDgjxh123tq";
	private String transId;
	private String posid;

	/**
	 * @return the transId
	 */
	public String getTransId() {
		return transId;
	}

	/**
	 * @param transId
	 *            the transId to set
	 */
	@XmlElement
	public void setTransId(String transId) {
		this.transId = transId;
	}

	/**
	 * @return the posid
	 */
	@XmlElement
	public String getPosid() {
		return posid;
	}

	/**
	 * @param posid
	 *            the posid to set
	 */
	public void setPosid(String posid) {
		this.posid = posid;
	}

	/**
	 * @return the bankerId
	 */
	public String getBankerId() {
		return bankerId;
	}

	/**
	 * @return the bankerToken
	 */
	public String getBankerToken() {
		return bankerToken;
	}
}
