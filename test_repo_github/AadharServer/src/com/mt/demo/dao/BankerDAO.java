package com.mt.demo.dao;

import java.util.Map;
import java.util.TreeMap;

import com.mt.demo.model.Banker;
import com.mt.demo.model.Customer;

public class BankerDAO {

	private static BankerDAO instance = null;

	public static BankerDAO getInstance() {
		if (instance == null) {
			instance = new BankerDAO();
		}
		return instance;
	}

	private Map<String, Banker> bankers = new TreeMap<String, Banker>();
	
	private Map<String, Customer> transactingCustomers = new TreeMap<String, Customer>();
	
	private BankerDAO() {
		Banker bank = new Banker("98237834", "ABCDgjxh123tq");
		bankers.put(bank.getBankerId(), bank);
	}

	public Banker getBanker(String bankerId) {
		return bankers.get(bankerId);
	}

	public int getCount() {
		return 1;
	}
	
	public void registerTransaction(String transaction, Customer customer) {
		transactingCustomers.put(transaction, customer);
	}
	
	public String validateTransaction(String posid, String transaction){
		String retValue = "ERROR:INVALID TRANSACTION";
		Customer customer = transactingCustomers.get(transaction);
		if ( customer != null){
			retValue = "ERROR: TRANSACTION TIME OUT";
			if ( customer.isTokenValid(posid, transaction) ) {
				retValue = "" + customer.getAccoutNumber() + ":" + customer.getAmount() + "";
			}
		}
		return retValue;
	}
}
