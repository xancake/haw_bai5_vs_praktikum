package org.haw.vs.praktikum.gwln.praktikum2.b.bank;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Bank {
	private Map<String, Konto> _playerKontoMapping;
	private List<Transaktion> _transaktionen;
	
	public Bank() {
		_playerKontoMapping = new HashMap<String, Konto>();
		_transaktionen = new ArrayList<Transaktion>();
	}
	
	public void erzeugeKonto(String player, int saldo) {
		if(!_playerKontoMapping.containsKey(player)) {
			_playerKontoMapping.put(player, new Konto(player, saldo));
		}
	}
	
	public Konto getKonto(String player) {
		return _playerKontoMapping.get(player);
	}
	
	public void transferTo(Konto to, int amount) {
		if(amount < 0) {
			throw new IllegalArgumentException("Der zu überweisende Betrag darf nicht negativ sein!");
		}
		to.alterKontostand(amount);
		_transaktionen.add(new Transaktion(to, amount));
	}
	
	public void transferFrom(Konto from, int amount) throws InsufficientFondsException {
		if(amount < 0) {
			throw new IllegalArgumentException("Der abzuhebende Betrag darf nicht negativ sein!");
		}
		if(from.getKontostand() - amount < 0) {
			throw new InsufficientFondsException("Nicht genug Saldo auf dem Konto!");
		}
		from.alterKontostand(-amount);
		_transaktionen.add(new Transaktion(from, -amount));
	}
	
	public void transferFromTo(Konto from, Konto to, int amount) throws InsufficientFondsException {
		transferFrom(from, amount);
		transferTo(to, amount);
	}
}
