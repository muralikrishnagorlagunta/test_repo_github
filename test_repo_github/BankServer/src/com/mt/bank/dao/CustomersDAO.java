package com.mt.bank.dao;

import java.util.Map;
import java.util.TreeMap;

import com.mt.bank.model.Customer;

public class CustomersDAO {

	private static CustomersDAO instance = null;

	public static CustomersDAO getInstance() {
		if (instance == null) {
			instance = new CustomersDAO();
		}
		return instance;
	}

	private Map<String, Customer> customers = new TreeMap<String, Customer>();

	private CustomersDAO() {
		Customer cust = new Customer("800100301");
		cust.credit(5000);
		customers.put(cust.getCustomerId(), cust);
		cust = new Customer("800100302");
		cust.credit(5000);
		customers.put(cust.getCustomerId(), cust);
		cust = new Customer("800100303");
		cust.credit(5000);
		customers.put(cust.getCustomerId(), cust);
		cust = new Customer("800100304");
		cust.credit(5000);
		customers.put(cust.getCustomerId(), cust);
		cust = new Customer("800100305");
		cust.credit(5000);
		customers.put(cust.getCustomerId(), cust);
	}

	public Customer getCustomer(String customerId) {
		return customers.get(customerId);
	}

	public int getCount() {
		return 1;
	}
}
