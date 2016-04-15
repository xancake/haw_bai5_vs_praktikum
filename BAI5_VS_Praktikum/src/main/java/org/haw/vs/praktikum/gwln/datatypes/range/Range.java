package org.haw.vs.praktikum.gwln.datatypes.range;

/**
 * Ein Datentyp der einen Wertebereich definiert.
 */
public interface Range {
	/**
	 * Gibt den minimalen Wert zurück.
	 * @return Der minimale Wert
	 */
	int getMinimum();
	
	/**
	 * Gibt den maximalen Wert zurück.
	 * @return Der maximale Wert
	 */
	int getMaximum();
	
	/**
	 * Prüft, ob der übergebene Wert in diesem Wertebereich liegt.
	 * @param value Der zu prüfende Wert
	 * @return {@code true}, wenn der Wert im Wertebereich liegt, ansonsten {@code false}
	 */
	boolean contains(int value);
}
