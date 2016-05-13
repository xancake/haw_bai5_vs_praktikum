package org.haw.vs.praktikum.gwln.bank.rest;

import java.util.Collection;
import org.haw.vs.praktikum.gwln.bank.Transfer;
import org.json.JSONArray;
import org.json.JSONObject;

public class TransferJsonMarshaller {
	public String marshall(Collection<Transfer> transfers) {
		JSONArray array = new JSONArray();
		for(Transfer transfer : transfers) {
			array.put(marshallJSONObject(transfer));
		}
		return array.toString();
	}
	
	public String marshall(Transfer object) {
		return marshallJSONObject(object).toString();
	}
	
	private JSONObject marshallJSONObject(Transfer object) {
		JSONObject json = new JSONObject();
		json.put("from", object.getFrom().getUri());
		json.put("to", object.getTo().getUri());
		json.put("amount", object.getAmount());
		json.put("reason", object.getReason());
		return json;
	}
}
