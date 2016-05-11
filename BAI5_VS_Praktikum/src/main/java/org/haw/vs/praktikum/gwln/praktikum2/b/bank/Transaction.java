package org.haw.vs.praktikum.gwln.praktikum2.b.bank;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Map.Entry;

public class Transaction {
	private int _id;
	private String _baseUri;
	private List<Transfer> _transfers;
	private State _state;
	
	public Transaction(int id, String baseUri) {
		_id = id;
		_baseUri = Objects.requireNonNull(baseUri);
		_transfers = new ArrayList<>();
		_state = State.READY;
	}
	
	public int getId() {
		return _id;
	}
	
	public String getBaseUri() {
		return _baseUri;
	}
	
	public String getUri() {
		return (_baseUri.endsWith("/") ? _baseUri : _baseUri + "/") + _id;
	}
	
	public List<Transfer> getTransfers() {
		return _transfers;
	}
	
	public State getState() {
		return _state;
	}
	
	public void addTransfer(Transfer transfer) {
		_transfers.add(transfer);
	}
	
	public void removeTransfer(Transfer transfer) {
		_transfers.remove(transfer);
	}
	
	public void setState(State state) {
		_state = state;
	}
	
	public void commit() throws InsufficientFondsException {
		validate();
		for(Transfer transfer : _transfers) {
			transfer.execute();
		}
		_state = State.COMMITTED;
	}
	
	private void validate() throws InsufficientFondsException {
		Map<Account, Integer> accountSaldoMapping = new HashMap<>();
		for(Transfer transfer : _transfers) {
			accountSaldoMapping.put(transfer.getFrom(), transfer.getFrom().getSaldo());
		}
		for(Transfer transfer : _transfers) {
			int vorherigerSaldo = accountSaldoMapping.get(transfer.getFrom());
			accountSaldoMapping.put(transfer.getFrom(), vorherigerSaldo - transfer.getAmount());
		}
		for(Entry<Account, Integer> accountSaldo : accountSaldoMapping.entrySet()) {
			if(accountSaldo.getValue() < 0) {
				throw new InsufficientFondsException("'" + accountSaldo.getKey().getId() + "' hat kein ausreichendes Saldo für diese Transaktion");
			}
		}
	}
	
	public enum State {
		READY,
		COMMITTED,
		DELETED;
	}
}
