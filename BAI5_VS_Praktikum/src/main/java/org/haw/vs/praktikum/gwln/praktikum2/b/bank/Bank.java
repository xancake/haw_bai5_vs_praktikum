package org.haw.vs.praktikum.gwln.praktikum2.b.bank;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Bank {
	private Account _bankAccount;
	private Map<String, Account> _playerAccountMapping;
	private List<Transfer> _transfers;
	private Map<Integer, Transaction> _transaktionen;
	
	public Bank() {
		_bankAccount = new BankAccount();
		_playerAccountMapping = new HashMap<String, Account>();
		addAccount(_bankAccount);
		_transfers = new ArrayList<>();
		_transaktionen = new HashMap<Integer, Transaction>();
	}
	
	public void addAccount(Account account) {
		if(_playerAccountMapping.containsKey(account.getId())) {
			throw new IllegalArgumentException("Das Konto '" + account.getId() + "' existiert bereits");
		}
		_playerAccountMapping.put(account.getId(), account);
	}
	
	public Account getAccount(String id) {
		return _playerAccountMapping.get(id);
	}
	
	public List<Transfer> getTransfers() {
		return Collections.unmodifiableList(_transfers);
	}
	
	public Transfer getTransfer(int id) {
		return _transfers.get(id);
	}
	
	public void createTransaction(Transaction transaction) {
		_transaktionen.put(transaction.getId(), transaction);
	}
	
	public Transaction getTransaction(int id) {
		return _transaktionen.get(id);
	}
	
	public void commitTransaction(Transaction transaktion) throws InsufficientFondsException {
		if(_transaktionen.containsKey(transaktion.getId())) {
			transaktion.commit();
			_transfers.addAll(transaktion.getTransfers());
			_transaktionen.remove(transaktion.getId());
		}
	}
	
	public void rollbackTransaction(Transaction transaktion) {
		if(_transaktionen.containsKey(transaktion.getId())) {
			_transaktionen.remove(transaktion.getId());
		}
	}
	
	public void transactionalTransfer(Transaction transaktion, Account from, Account to, int amount, String reason) throws InsufficientFondsException {
		transaktion.addTransfer(new Transfer(from, to, amount, reason));
	}
	
	public void transfer(Account from, Account to, int amount, String reason) throws InsufficientFondsException {
		Transfer transfer = new Transfer(from, to, amount, reason);
		transfer.execute();
		_transfers.add(transfer);
	}
	
	private class BankAccount extends Account {
		public BankAccount() {
			super("bank", Integer.MAX_VALUE);
		}
		
		@Override
		public String getPlayer() {
			return ""; // Das Bankkonto gehört keinem Spieler
		}
		
		@Override
		void setSaldo(int saldo) {
			// Do nothing, die Bank hat ein unendlich großes Saldo
		}
		
		@Override
		void alterSaldo(int amount) {
			// Do nothing, die Bank hat ein unendlich großes Saldo
		}
	}
}
