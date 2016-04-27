package org.haw.vs.praktikum.gwln.praktikum2.b.bank;

import java.util.Objects;

public class Transaktion {
	private Account _account;
	private int _amount;
	
	public Transaktion(Account account, int amount) {
		_account = Objects.requireNonNull(account);
		_amount = amount;
	}
	
	public Account getAccount() {
		return _account;
	}
	
	public int getAmount() {
		return _amount;
	}
}
