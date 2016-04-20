package org.haw.vs.praktikum.gwln.yellowpages;

@SuppressWarnings("serial")
public class YellowPagesNotAvailableException extends Exception {
	private static final String DEFAULT_MESSAGE = "Yellow-Pages nicht erreichbar";
	
	public YellowPagesNotAvailableException() {
		this(DEFAULT_MESSAGE);
	}
	
	public YellowPagesNotAvailableException(String message) {
		super(message);
	}
	
	public YellowPagesNotAvailableException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public YellowPagesNotAvailableException(Throwable cause) {
		this(DEFAULT_MESSAGE, cause);
	}
}
