package org.haw.vs.praktikum.gwln.praktikum2.b.bank;

import java.util.HashMap;
import java.util.Map;

public class BankManager {
	private Map<String, Bank> _gameIdBankMapping;
	
	public BankManager() {
		_gameIdBankMapping = new HashMap<String, Bank>();
	}
	
	public void createKonto(String gameId, String player, int saldo) {
		Bank bank = getBankLazily(gameId);
		bank.erzeugeKonto(player, saldo);
	}
	
	public Konto getKonto(String gameId, String accountId) {
		Bank bank = getBankLazily(gameId);
		return bank.getKonto(accountId);
	}
	
	public void transferTo(String gameId, Konto to, int amount, String reason) {
		Bank bank = getBankLazily(gameId);
		bank.transferTo(to, amount);
	}
	
	public void transferFrom(String gameId, Konto from, int amount) throws InsufficientFondsException {
		Bank bank = getBankLazily(gameId);
		bank.transferFrom(from, amount);
	}
	
	public void transferFromTo(String gameId, Konto from, Konto to, int amount) throws InsufficientFondsException {
		Bank bank = getBankLazily(gameId);
		bank.transferFromTo(from, to, amount);
	}
	
	private Bank getBankLazily(String gameId) {
		Bank bank = _gameIdBankMapping.get(gameId);
		if(bank == null) {
			bank = new Bank();
			_gameIdBankMapping.put(gameId, bank);
		}
		return bank;
	}
}
