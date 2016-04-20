package org.haw.vs.praktikum.gwln.praktikum2.b.bank;

import org.haw.vs.praktikum.gwln.datatypes.bounded.MutableBoundedInteger;

public class Konto {
	private String _accountId;
	private MutableBoundedInteger _kontostand;
	
	public Konto(String playerId, int saldo) {
		_accountId = playerId;
		_kontostand = new MutableBoundedInteger(0, Integer.MAX_VALUE, saldo);
	}
	
	public String getId() {
		return _accountId;
	}
	
	public int getKontostand() {
		return _kontostand.getValue();
	}
	
	public void setKontostand(int kontostand) {
		_kontostand.setValue(kontostand);
	}
	
	public void alterKontostand(int amount) {
		_kontostand.alterValue(amount);
	}
}
