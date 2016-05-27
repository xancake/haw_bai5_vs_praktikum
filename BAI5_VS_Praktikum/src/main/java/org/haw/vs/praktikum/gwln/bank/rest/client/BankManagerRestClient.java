package org.haw.vs.praktikum.gwln.bank.rest.client;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jetty.http.HttpHeader;
import org.eclipse.jetty.http.HttpStatus;
import org.haw.vs.praktikum.gwln.bank.Account;
import org.haw.vs.praktikum.gwln.bank.Transfer;
import org.haw.vs.praktikum.gwln.bank.rest.AccountJsonMarshaller;
import org.haw.vs.praktikum.gwln.rest.client.AbstractRestClient;
import org.json.JSONObject;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class BankManagerRestClient extends AbstractRestClient {
	private static final AccountJsonMarshaller ACCOUNT_MARSHALLER = new AccountJsonMarshaller();
	
	public BankManagerRestClient(String url) throws MalformedURLException {
		super(url, "/banks");
	}
	
	public List<String> getBanks() throws UnirestException {
		return getBanks(getURL().toExternalForm());
	}
	
	public String postBank(String gameId) throws UnirestException {
		return postBank(getURL().toExternalForm(), gameId);
	}
	
	public JSONObject getBank(int bankId) throws UnirestException {
		return getBank(getURL().toExternalForm() + "/" + bankId);
	}
	
	public void putBank(int bankId, String accountsUri, String transfersUri) throws UnirestException {
		Unirest.put(getURL().toExternalForm() + "/" + bankId).asString();
		// TODO: Not yet implemented (completely)
	}
	
	public String postAccount(int bankId, Account account) throws UnirestException {
		return postAccount(getURL().toExternalForm() + "/" + bankId + "/accounts", account);
	}
	
	public Account getAccountById(int bankId, int accountId) throws UnirestException {
		return getAccount(getURL().toExternalForm() + "/" + bankId + "/accounts/" + accountId);
	}
	
	
	
	
	
//	public Account getAccount(String gameId, String playerId) throws UnirestException {
//		HttpResponse<String> response = Unirest.get(getURL().toExternalForm() + "/" + gameId + "/accounts/" + playerId).asString();
//		return ACCOUNT_MARSHALLER.unmarshall(response.getBody());
//	}
//	
//	public void postTransferTo(String gameId, String to, int amount) throws UnirestException {
//		Unirest.post(getURL().toExternalForm() + "/" + gameId + "/transfer/to/" + to + "/" + amount).asString();
//	}
//	
//	public void postTransferFrom(String gameId, String from, int amount) throws UnirestException {
//		Unirest.post(getURL().toExternalForm() + "/" + gameId + "/transfer/from/" + from + "/" + amount).asString();
//	}
//	
//	public void postTransferFromTo(String gameId, String from, String to, String amount) throws UnirestException {
//		Unirest.post(getURL().toExternalForm() + "/" + gameId + "/transfer/from/" + from + "/to/" + to + "/" + amount);
//	}
	
	
	
	
	public static List<String> getBanks(String url) throws UnirestException {
		HttpResponse<JsonNode> response = Unirest.get(url).asJson();
		List<String> banks = new ArrayList<>();
		response.getBody().getObject().getJSONArray("banks").forEach(object -> {
			String bankUri = (String)object;
			banks.add(bankUri);
		});
		return banks;
	}
	
	public static String postBank(String url, String gameId) throws UnirestException {
		JSONObject json = new JSONObject();
		json.put("game", gameId);
		
		HttpResponse<String> response = Unirest.post(url)
				.header(HttpHeader.CONTENT_TYPE.asString(), "application/json")
				.body(json)
				.asString();
		
		if(HttpStatus.CREATED_201 == response.getStatus()) {
			return response.getHeaders().getFirst(HttpHeader.LOCATION.asString());
		} else {
			throw new RuntimeException(response.getStatus() + ": " + response.getStatusText());
		}
	}
	
	public static JSONObject getBank(String url) throws UnirestException {
		HttpResponse<JsonNode> response = Unirest.get(url).asJson();
		return response.getBody().getObject();
	}
	
	public static String postAccount(String url, Account account) throws UnirestException {
		HttpResponse<String> response = Unirest.post(url)
				.header(HttpHeader.CONTENT_TYPE.asString(), "application/json")
				.body(ACCOUNT_MARSHALLER.marshall(account))
				.asString();
		
		if(HttpStatus.CREATED_201 == response.getStatus()) {
			return response.getHeaders().getFirst(HttpHeader.LOCATION.asString());
		} else {
			throw new RuntimeException(response.getStatus() + ": " + response.getStatusText());
		}
	}
	
	public static Account getAccount(String url) throws UnirestException {
		HttpResponse<String> response = Unirest.get(url).asString();
		return ACCOUNT_MARSHALLER.unmarshall(response.getBody());
	}
	
	public static List<String> getTransfers(String url) throws UnirestException {
		HttpResponse<JsonNode> response = Unirest.get(url).asJson();
		List<String> transferUrls = new ArrayList<String>();
		response.getBody().getObject().getJSONArray("transfers").forEach(object -> {
			transferUrls.add((String)object);
		});
		return transferUrls;
	}
	
	public static Transfer getTransfer(String url) throws UnirestException {
		// TODO: Not yet implemented
		return null;
	}
	
	public static String postTransaction(String url) throws UnirestException {
		// TODO: Not yet implemented
		return null;
	}
}
