package org.haw.vs.praktikum.gwln.praktikum2.b.bank;

@SuppressWarnings("serial")
public class InsufficientFondsException extends Exception {
	public InsufficientFondsException() {
		super();
	}

	public InsufficientFondsException(String message) {
		super(message);
	}
}
