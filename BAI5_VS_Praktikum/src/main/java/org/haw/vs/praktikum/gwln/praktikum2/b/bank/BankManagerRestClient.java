package org.haw.vs.praktikum.gwln.praktikum2.b.bank;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class BankManagerRestClient {
	private String _url;
	
	public BankManagerRestClient(String url) {
		_url = url;
	}
	
	public void postAccount(String gameId, Account account) throws UnirestException {
		Unirest.post(_url + "/banks/" + gameId + "/accounts")
				.header("Content-Type", "application/json")
				.body(AccountJsonMarshaller.marshall(account))
				.asString();
	}
	
	public Account getAccount(String gameId, String playerId) throws UnirestException {
		HttpResponse<JsonNode> response = Unirest.get(_url + "/banks/" + gameId + "/accounts/" + playerId).asJson();
		return AccountJsonMarshaller.unmarshall(response.getBody().getObject());
	}
	
	public void postTransferTo(String gameId, String to, int amount) throws UnirestException {
		Unirest.post(_url + "/banks/" + gameId + "/transfer/to/" + to + "/" + amount).asString();
	}
	
	public void postTransferFrom(String gameId, String from, int amount) throws UnirestException {
		Unirest.post(_url + "/banks/" + gameId + "/transfer/from/" + from + "/" + amount).asString();
	}
	
	public void postTransferFromTo(String gameId, String from, String to, String amount) throws UnirestException {
		Unirest.post(_url + "/banks/" + gameId + "/transfer/from/" + from + "/to/" + to + "/" + amount);
	}
}
