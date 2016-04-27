package org.haw.vs.praktikum.gwln.praktikum2.b.bank;

import java.util.HashMap;
import java.util.Map;

public class BankManager {
	private Map<String, Bank> _gameIdBankMapping;
	
	public BankManager() {
		_gameIdBankMapping = new HashMap<String, Bank>();
	}
	
	public void createAccount(String gameId, Account account) {
		Bank bank = getBankLazily(gameId);
		bank.addAccount(account);
	}
	
	public Account getAccount(String gameId, String accountId) {
		Bank bank = getBankLazily(gameId);
		return bank.getAccount(accountId);
	}
	
	public void transferTo(String gameId, Account to, int amount, String reason) {
		Bank bank = getBankLazily(gameId);
		bank.transferTo(to, amount);
	}
	
	public void transferFrom(String gameId, Account from, int amount) throws InsufficientFondsException {
		Bank bank = getBankLazily(gameId);
		bank.transferFrom(from, amount);
	}
	
	public void transferFromTo(String gameId, Account from, Account to, int amount) throws InsufficientFondsException {
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
