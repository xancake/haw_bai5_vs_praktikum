package org.haw.vs.praktikum.gwln.bank.rest;

import java.util.List;
import org.haw.vs.praktikum.gwln.bank.Bank;
import org.json.JSONArray;
import org.json.JSONObject;

public class BankJsonMarshaller {
	public String marshall(String resourceUri, List<Bank> banks) {
		if(!resourceUri.endsWith("/"))
			resourceUri = resourceUri + "/";
		if(!resourceUri.startsWith("/"))
			resourceUri = "/" + resourceUri;
		
		JSONArray array = new JSONArray();
		for(Bank bank : banks) {
			array.put(resourceUri + bank.getId());
		}
		JSONObject json = new JSONObject();
		json.put("banks", array);
		return json.toString();
	}
}
