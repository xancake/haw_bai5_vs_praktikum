package org.haw.vs.praktikum.gwln.praktikum2.b.client.restclient.dice;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class DiceRestClient {
	private String _uri;
	
	public DiceRestClient(String uri){
		_uri = uri;
	}
	
	public String rollDice() throws UnirestException{
		return Unirest.get(_uri).asString().getBody();
	}
}
