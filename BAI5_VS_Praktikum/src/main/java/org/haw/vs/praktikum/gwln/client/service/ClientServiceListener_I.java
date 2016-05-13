package org.haw.vs.praktikum.gwln.client.service;

import org.haw.vs.praktikum.gwln.events.Event;

public interface ClientServiceListener_I {
	
	/**
	 * Benachrichtigung wenn der Spieler an der Reihe ist
	 */
	void onTurn();
	/**
	 * Benachrichtigung über ein neu eingetroffenes Event der Subscription
	 * @param e das neue Event
	 */
	void onEvent(Event e);

}
