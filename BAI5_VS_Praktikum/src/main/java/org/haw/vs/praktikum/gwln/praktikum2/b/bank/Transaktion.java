package org.haw.vs.praktikum.gwln.praktikum2.b.bank;

import java.util.Objects;

public class Transaktion {
	private Konto _konto;
	private int _menge;
	
	public Transaktion(Konto konto, int menge) {
		_konto = Objects.requireNonNull(konto);
		_menge = menge;
	}
	
	public Konto getKonto() {
		return _konto;
	}
	
	public int getMenge() {
		return _menge;
	}
}
