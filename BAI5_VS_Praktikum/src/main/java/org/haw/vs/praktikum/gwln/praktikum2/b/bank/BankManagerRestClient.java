package org.haw.vs.praktikum.gwln.praktikum2.b.bank;

import org.haw.vs.praktikum.gwln.rest.client.AbstractRestClient;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class BankManagerRestClient extends AbstractRestClient {
	private AccountJsonMarshaller _marshaller;
	
	public BankManagerRestClient(String url) {
		super(url, "/banks");
		_marshaller = new AccountJsonMarshaller();
	}
	
	public void postAccount(String gameId, Account account) throws UnirestException {
		Unirest.post(getUrl() + "/" + gameId + "/accounts")
				.header("Content-Type", "application/json")
				.body(_marshaller.marshall(account))
				.asString();
	}
	
	public Account getAccount(String gameId, String playerId) throws UnirestException {
		HttpResponse<String> response = Unirest.get(getUrl() + "/" + gameId + "/accounts/" + playerId).asString();
		return _marshaller.unmarshall(response.getBody());
	}
	
	public void postTransferTo(String gameId, String to, int amount) throws UnirestException {
		Unirest.post(getUrl() + "/" + gameId + "/transfer/to/" + to + "/" + amount).asString();
	}
	
	public void postTransferFrom(String gameId, String from, int amount) throws UnirestException {
		Unirest.post(getUrl() + "/" + gameId + "/transfer/from/" + from + "/" + amount).asString();
	}
	
	public void postTransferFromTo(String gameId, String from, String to, String amount) throws UnirestException {
		Unirest.post(getUrl() + "/" + gameId + "/transfer/from/" + from + "/to/" + to + "/" + amount);
	}
}
