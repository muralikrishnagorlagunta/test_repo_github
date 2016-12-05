package com.mt.bank.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.json.JSONObject;

import com.mt.bank.dao.CustomersDAO;
import com.mt.demo.model.Transaction;

//Will map the resource to the URL authService
@Path("/transService")
public class TransService {

	CustomersDAO custDao = CustomersDAO.getInstance();

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

	@GET
	@Path("{customerid}")
	@Produces(MediaType.TEXT_PLAIN)
	public String getBalance(@PathParam("customerid") String customerId) {
		System.out.println (" customer " + customerId);
		int count = custDao.getCustomer(customerId).getAvailableAmount();
		return String.valueOf(count);
	}

	@POST
	@Path("validatePOST")
	@Consumes(MediaType.APPLICATION_JSON)
	public String isValidCustomer(InputStream incomingData) {

		String retValue = "TRANSACTION FAILED";
		try {

			// Receive
			JSONObject obj = readJSON(incomingData);
			String transid = obj.getString("transid");
			String posid = obj.getString("posid");
			Transaction trans = new Transaction();
			trans.setPosid(posid);
			trans.setTransId(transid);

			// validate with Aadhar Server
			JSONObject jsonObject = new JSONObject("{ }");
			jsonObject.accumulate("bankerid", trans.getBankerId());
			jsonObject.accumulate("fprint", trans.getBankerToken());
			jsonObject.accumulate("transid", trans.getTransId());
			jsonObject.accumulate("posid", trans.getPosid());

			String sUrl = "http://127.0.0.1:8080/AadharServer/rest/";
			String response = null;

			response = getResponse(sUrl + "bas/validatePOST", jsonObject);

			System.out.println(" response = " + response);

			if (response.indexOf("ERROR") == -1 && response.indexOf(":") != -1) {
				String custId = response.substring(0, response.indexOf(":"));
				String amount = response.substring(response.indexOf(":") + 1);
				System.out.println(" cust id = " + custId + " -- amount = "
						+ amount);
				custDao.getCustomer(custId).debit(Integer.parseInt(amount));
				retValue = "SUCCESS";
			} else {
				retValue = response;
			}
		} catch (Exception e) {
			System.out.println("Error Parsing: - ");
			e.printStackTrace();
			retValue = "INVALID DATA";
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

	private String getResponse(String sUrl, JSONObject jsonObject) {
		String retValue = null;
		try {
			URL url = new URL(sUrl);
			URLConnection connection = url.openConnection();
			connection.setDoOutput(true);
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setConnectTimeout(5000);
			connection.setReadTimeout(5000);
			OutputStreamWriter out = new OutputStreamWriter(
					connection.getOutputStream());
			out.write(jsonObject.toString());
			out.close();

			BufferedReader in = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));

			String line = "";
			while ((line = in.readLine()) != null) {
				retValue = line;
				System.out.println("<" + line + ">");
			}

			System.out.println(retValue);

			in.close();
		} catch (Exception e) {
			System.out.println("\nError while calling Crunchify REST Service");
			System.out.println(e);
		}
		return retValue;
	}
}
