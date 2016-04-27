package org.haw.vs.praktikum.gwln.praktikum2.b.bank;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Bank {
	private Map<String, Account> _playerAccountMapping;
	private List<Transaktion> _transaktionen;
	
	public Bank() {
		_playerAccountMapping = new HashMap<String, Account>();
		_transaktionen = new ArrayList<Transaktion>();
	}
	
	public void addAccount(Account account) {
		if(!_playerAccountMapping.containsKey(account.getId())) {
			_playerAccountMapping.put(account.getId(), account);
		}
	}
	
	public Account getAccount(String id) {
		return _playerAccountMapping.get(id);
	}
	
	public void transferTo(Account to, int amount) {
		if(amount < 0) {
			throw new IllegalArgumentException("Der zu überweisende Betrag darf nicht negativ sein!");
		}
		to.alterSaldo(amount);
		_transaktionen.add(new Transaktion(to, amount));
	}
	
	public void transferFrom(Account from, int amount) throws InsufficientFondsException {
		if(amount < 0) {
			throw new IllegalArgumentException("Der abzuhebende Betrag darf nicht negativ sein!");
		}
		if(from.getSaldo() - amount < 0) {
			throw new InsufficientFondsException("Nicht genug Saldo auf dem Konto!");
		}
		from.alterSaldo(-amount);
		_transaktionen.add(new Transaktion(from, -amount));
	}
	
	public void transferFromTo(Account from, Account to, int amount) throws InsufficientFondsException {
		transferFrom(from, amount);
		transferTo(to, amount);
	}
}
