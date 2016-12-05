package com.mt.demo;

import static org.hamcrest.core.Is.is;
import org.json.JSONObject;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import cucumber.annotation.en.And;
import cucumber.annotation.en.Given;
import cucumber.annotation.en.Then;
import cucumber.annotation.en.When;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.lang.*;

import org.json.JSONObject;

public class POSScenario {
	
	private String amount = "0";
	private String aadharNo = "";
	private String fprint = "";
	private String posid = "";
	
	private String token = "";
		
	@Given("^customer purchases for amount (\\d+)$")
	public void customer_purchases_for_amount(int amount) {
		this.amount = String.valueOf(amount);
	}
	
	@When("^provides his Aadhar details \"([^\"]*)\"$")
	public void provides_his_Aadhar_details_(String aadharNo) {
	    this.aadharNo = aadharNo;
	}

	@When("^provides his finger print details \"([^\"]*)\"$")
	public void provides_his_finger_print_details_(String fprint) {
	    this.fprint = fprint;
	}

	@When("^the POS requests Aadhar Server for verification with \"([^\"]*)\"$")
	public void the_POS_requests_Aadhar_Server_for_verification_with_(String posid) {
		
	    this.posid = posid;
	    
	    JSONObject jsonObject = new JSONObject("{ }");
		jsonObject.accumulate("posid", posid);
		jsonObject.accumulate("custid", aadharNo);
		jsonObject.accumulate("fprint", fprint);
		jsonObject.accumulate("amount", amount);

		String sUrl = "http://127.0.0.1:8080/AadharServer/rest/";

		this.token = getResponse(sUrl + "authService/validatePOST", jsonObject);
	}


	@When("^the Aadhar Server gives back the token validate$")
	public void the_Aadhar_Server_gives_back_the_token() {
		// validate 
		System.out.println(" token received :: " + token );
		assertFalse(token.indexOf("INVALID") != -1);
	}

	@And("^if the token is invlidated$")
	public void the_token_is_invalid() {
		// validate 
		System.out.println(" token received :: " + token );
		assertFalse(token.indexOf("INVALID") != -1);
		token = token + "junk";
	}


	@When("^the token is submitted to bank server$")
	public void the_token_is_submitted_to_bank_server() {
		JSONObject jsonObject2 = new JSONObject("{ }");
		jsonObject2.accumulate("posid", "345");
		jsonObject2.accumulate("transid", token);

		String sUrl = "http://127.0.0.1:8080/BankServer/rest/";
		token = getResponse(sUrl + "transService/validatePOST", jsonObject2);
	}

	@And("^wait for transaction to time-out$")
	public void wait_for_transaction_to_timeout() {
		// validate
		try{
			Thread.sleep(80000);
		} catch (Exception e) {
		}
	}
	
	@Then("^the transaction shall be successful$")
	public void the_transaction_shall_be_successful() {
		// validate
		System.out.println(" token received :: " + token );
		assertFalse(token.indexOf("SUCCESS") == -1);
	}

	@Then("^the transaction should fail$")
	public void the_transaction_should_fail() {
		// validate
		System.out.println(" token received :: " + token );
		assertFalse(token.indexOf("SUCCESS") != -1);
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
			}

			in.close();
		} catch (Exception e) {
			System.out.println("\nError while calling Crunchify REST Service");
			System.out.println(e);
		}
		return retValue;
	}
}
