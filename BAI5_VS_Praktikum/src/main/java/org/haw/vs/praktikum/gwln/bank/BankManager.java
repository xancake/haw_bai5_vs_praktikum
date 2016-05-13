package org.haw.vs.praktikum.gwln.bank;

import java.util.HashMap;
import java.util.Map;

public class BankManager {
	private Map<String, Bank> _gameIdBankMapping;
	
	public BankManager() {
		_gameIdBankMapping = new HashMap<String, Bank>();
	}
	
	public Bank getBank(String gameId) {
		return getBankLazily(gameId);
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
