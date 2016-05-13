package org.haw.vs.praktikum.gwln.bank;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BankManager {
	private Map<Integer, Bank> _banks;
	
	public BankManager() {
		_banks = new HashMap<>();
	}
	
	public List<Bank> getBanks() {
		return new ArrayList<Bank>(_banks.values());
	}
	
	public void addBank(Bank bank) {
		_banks.put(bank.getId(), bank);
	}
	
	public Bank getBank(int id) {
		return _banks.get(id);
	}
}
