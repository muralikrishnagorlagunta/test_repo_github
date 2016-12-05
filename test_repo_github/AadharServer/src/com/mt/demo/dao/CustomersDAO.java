package com.mt.demo.dao;

import java.util.Map;
import java.util.TreeMap;

import com.mt.demo.model.Customer;

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
		Customer cust = new Customer("98237834", "Murali", "ABCDgjxh123tq",
				"800100301");
		customers.put(cust.getCustomerId(), cust);
		cust = new Customer("92237834", "Krishna", "EFGHgjxh123tq", "800100302");
		customers.put(cust.getCustomerId(), cust);
		cust = new Customer("93237834", "Madhu", "IJKLgjxh123tq", "800100303");
		customers.put(cust.getCustomerId(), cust);
		cust = new Customer("94237834", "Srinivas", "MNOPgjxh123tq",
				"800100304");
		customers.put(cust.getCustomerId(), cust);
		cust = new Customer("95237834", "krish", "QRSTgjxh123tq", "800100305");
		customers.put(cust.getCustomerId(), cust);
	}

	public Customer getCustomer(String customerId) {
		return customers.get(customerId);
	}

	public int getCount() {
		return 1;
	}
}
