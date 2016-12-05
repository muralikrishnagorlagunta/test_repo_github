package com.mt.bank.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Customer implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String customerId;
	private List<String> transactionLog = new ArrayList<String>();
	private int availableAmount;

	public Customer() {
	}

	public Customer(String customerId) {
		this.customerId = customerId;
		this.debit(0);
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
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	/**
	 * @return the transactionLog
	 */
	public List<String> getTransactionLog() {
		return transactionLog;
	}

	/**
	 * @param transactionLog
	 *            the transactionLog to set
	 */
	public void addTransactionLog(List<String> transactionLog) {
		this.transactionLog = transactionLog;
	}

	/**
	 * @return the availableAmount
	 */
	public int getAvailableAmount() {
		return availableAmount;
	}

	/**
	 * @param availableAmount
	 *            the availableAmount to set
	 */
	public void debit(int amount) {
		this.availableAmount -= amount;
		transactionLog.add("DEBIT: " + amount + " ON "
				+ Calendar.getInstance().getTime());
	}

	/**
	 * @param availableAmount
	 *            the availableAmount to set
	 */
	public void credit(int amount) {
		this.availableAmount += amount;
		transactionLog.add("CREDIT: " + amount + " ON "
				+ Calendar.getInstance().getTime());
	}

}
