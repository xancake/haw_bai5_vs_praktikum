package org.haw.vs.praktikum.gwln.praktikum2.b.bank;

import java.util.Objects;

public class Transfer {
	private Account _from;
	private Account _to;
	private int _amount;
	private String _reason;
	
	public Transfer(Account from, Account to, int amount, String reason) throws InsufficientFondsException {
		if(amount < 0) {
			throw new IllegalArgumentException("Der zu überweisende Betrag darf nicht negativ sein!");
		}
		if(from.getSaldo() - amount < 0) {
			throw new InsufficientFondsException("Nicht genug Saldo auf dem Konto!");
		}
		_from = from;
		_to = to;
		_amount = amount;
		_reason = Objects.requireNonNull(reason);
	}
	
	public Account getFrom() {
		return _from;
	}

	public Account getTo() {
		return _to;
	}

	public int getAmount() {
		return _amount;
	}

	public String getReason() {
		return _reason;
	}

	public void setFrom(Account from) {
		_from = from;
	}

	public void setTo(Account to) {
		_to = to;
	}

	public void setAmount(int amount) {
		_amount = amount;
	}

	public void setReason(String reason) {
		_reason = reason;
	}
	
	public void execute() {
		_from.alterSaldo(-_amount);
		_to.alterSaldo(_amount);
	}

	@Override
	public String toString() {
		return "Transfer [from=" + _from + ", to=" + _to + ", amount=" + _amount + ", reason=" + _reason + "]";
	}
}
