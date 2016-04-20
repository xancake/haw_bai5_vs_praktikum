package org.haw.vs.praktikum.gwln.yellowpages;

@SuppressWarnings("serial")
public class YellowPagesNotAvailableException extends Exception {
	public YellowPagesNotAvailableException() {
		super();
	}
	
	public YellowPagesNotAvailableException(String message) {
		super(message);
	}
	
	public YellowPagesNotAvailableException(String message, Throwable cause) {
		super(message, cause);
	}
}
