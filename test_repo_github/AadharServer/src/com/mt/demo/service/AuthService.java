package com.mt.demo.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.json.JSONObject;

import com.mt.demo.dao.BankerDAO;
import com.mt.demo.dao.CustomersDAO;
import com.mt.demo.model.Customer;

@Path("/authService")
public class AuthService {

	CustomersDAO custDao = CustomersDAO.getInstance();
	BankerDAO banker = BankerDAO.getInstance();

	// This method is called if TEXT_PLAIN is request
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String sayPlainTextHello() {
		return "Hello There";
	}

	// This method is called if XML is request
	@GET
	@Produces(MediaType.TEXT_XML)
	public String sayXMLHello() {
		return "<?xml version=\"1.0\"?>" + "<hello> Hello There" + "</hello>";
	}

	// This method is called if HTML is request
	@GET
	@Produces(MediaType.TEXT_HTML)
	public String sayHtmlHello() {
		return "<html> " + "<title>" + "Hello There" + "</title>"
				+ "<body><h1>" + "Hello There" + "</body></h1>" + "</html> ";
	}

	@POST
	@Path("validatePOST")
	@Consumes(MediaType.APPLICATION_JSON)
	public String isValidCustomer(InputStream incomingData) {

		String retValue = "";
		try {
			JSONObject obj = readJSON(incomingData);
			String customerId = obj.getString("custid");
			String fingerPrint = obj.getString("fprint");
			String posid = obj.getString("posid");
			String amount = obj.getString("amount");
			retValue = "INVALID CUSTOMER";
			retValue = validateAndGetToken(retValue, customerId, fingerPrint,
					posid, amount);

		} catch (Exception e) {
			System.out.println("Error Parsing: - ");
			e.printStackTrace();
			retValue = "INVALID DATA";
		}

		return retValue;
	}

	@GET
	@Path("validate")
	@Produces(MediaType.TEXT_HTML)
	public String isValidCustomer(@QueryParam("posid") String posid,
			@QueryParam("custid") String customerId,
			@QueryParam("fprint") String fingerPrint,
			@QueryParam("amount") String amount) {
		String retValue = "INVALID CUSTOMER";
		System.out.println(" cust id " + customerId);
		System.out.println(" fprint " + fingerPrint);
		System.out.println(" pos-id " + posid);

		retValue = validateAndGetToken(retValue, customerId, fingerPrint,
				posid, amount);

		return retValue;
	}

	private String validateAndGetToken(String retValue, String customerId,
			String fingerPrint, String posid, String amount) {

		System.out.println(" cust id " + customerId);
		System.out.println(" fprint " + fingerPrint);
		System.out.println(" pos-id " + posid);
		
		Customer customer = custDao.getCustomer(customerId);
		if (customer != null) {
			System.out.println(" cust id " + customerId);
			retValue = customer.validate(posid, fingerPrint,
					Integer.parseInt(amount));
			System.out.println(" ret val " + retValue);
			if (retValue == null) {
				retValue = "INVALID TOKEN";
			} else {
				banker.registerTransaction(retValue, customer);
			}
			System.out.println("token = " + retValue);
		}
		return retValue;
	}

	private JSONObject readJSON(InputStream incomingData) throws IOException {
		StringBuilder data = new StringBuilder();
		BufferedReader in = new BufferedReader(new InputStreamReader(
				incomingData));
		String line = null;
		while ((line = in.readLine()) != null) {
			data.append(line).append("\n");
		}
		System.out.println("Data Received: " + data.toString());
		JSONObject obj = new JSONObject(data.toString());
		return obj;
	}

}
