package org.haw.vs.praktikum.gwln.praktikum2.b.bank;

import org.json.JSONObject;

public class AccountJsonMarshaller {
	public String marshall(Account object) {
		JSONObject json = new JSONObject();
		json.put("player", object.getPlayer());
		json.put("saldo", object.getSaldo());
		return json.toString();
	}

	public Account unmarshall(String source) {
		JSONObject json = new JSONObject(source);
		String player = json.getString("player");
		int saldo = json.getInt("saldo");
		return new Account(player, saldo);
	}
}
