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

//Will map the resource to the URL authService
@Path("/bas")
public class BAservice {

	CustomersDAO custDao = CustomersDAO.getInstance();
	BankerDAO bankDao = BankerDAO.getInstance();

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

			String bankerid = obj.getString("bankerid");
			String bankerIdentity = obj.getString("fprint");
			String transToken = obj.getString("transid");
			String posid = obj.getString("posid");

			retValue = validateAndGetCustomerDetails(retValue, bankerid,
					bankerIdentity, transToken, posid);

		} catch (Exception e) {
			System.out.println("Error Parsing: - ");
			e.printStackTrace();
			retValue = "INVALID DATA";
		}

		return retValue;
	}

	@GET
	@Path("validate")
	@Produces(MediaType.TEXT_PLAIN)
	public String validateToken(@QueryParam("bankerid") String bankerid,
			@QueryParam("fprint") String bankerIdentity,
			@QueryParam("transid") String transToken,
			@QueryParam("posid") String posid) {

		String retValue = "INVALID BANK";
		System.out.println(" banker id " + bankerid);
		System.out.println(" fprint " + bankerIdentity);
		System.out.println(" Trans token " + transToken);

		retValue = validateAndGetCustomerDetails(retValue, bankerid,
				bankerIdentity, transToken, posid);

		return retValue;
	}

	private String validateAndGetCustomerDetails(String retValue,
			String bankerid, String bankerIdentity, String transToken,
			String posid) {
		if (bankDao.getBanker(bankerid) != null) {
			System.out.println(" valid banker id ");
			retValue = "INVALID BANK IDENTITY";
			if (bankDao.getBanker(bankerid).validate(bankerIdentity)) {
				System.out.println(" valid banker token ");
				retValue = bankDao.validateTransaction(posid, transToken);
				System.out.println(" return " + retValue);
			}
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
