package org.haw.vs.praktikum.gwln.praktikum2.b.bank;

import org.json.JSONObject;

public class AccountJsonMarshaller {
	private AccountJsonMarshaller() {}
	
	public static JSONObject marshall(Account account) {
		JSONObject json = new JSONObject();
		json.put("player", account.getPlayer());
		json.put("saldo", account.getSaldo());
		return json;
	}
	
	public static Account unmarshall(JSONObject json) {
		String player = "/" + json.getString("player");
		int saldo = json.getInt("saldo");
		return new Account(player, saldo);
	}
}
