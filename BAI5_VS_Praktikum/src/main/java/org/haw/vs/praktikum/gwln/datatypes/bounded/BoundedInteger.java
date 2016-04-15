package org.haw.vs.praktikum.gwln.datatypes.bounded;

/**
 * Ein Datentyp der einen durch ein Minimum und ein Maximum beschränkten Integer-Wert repräsentiert.
 */
public interface BoundedInteger {
	/**
	 * Der minimale Wert den der Integer annehmen kann.
	 * @return Das Minimum
	 * @see #getValue()
	 */
	int getMinimum();
	
	/**
	 * Der maximale Wert den der Integer annehmen kann.
	 * @return Das Maximum
	 * @see #getValue()
	 */
	int getMaximum();
	
	/**
	 * Der Wert des Integers. Für den Wert gilt immer folgende Eigenschaft:
	 * {@code minimum <= value <= maximum}
	 * @return Der Wert
	 */
	int getValue();
	
	/**
	 * Gibt den Wert als Prozentwert zurück. Der Prozentwert gibt an, wo sich der
	 * Wert prozentual innerhalb der Schranken befindet.
	 * @return Der Prozentwert, bewegt sich zwischen 0 bis 1
	 */
	double getPercentage();
	
	/**
	 * Prüft, ob der Integer das Minimum erreicht hat.
	 * @return {@code true} wenn {@code value == minimum}, ansonsten {@code false}
	 */
	boolean isValueAtMinimum();
	
	/**
	 * Prüft, ob der Integer das Maximum erreicht hat.
	 * @return {@code true} wenn {@code value == maximum}, ansonsten {@code false}
	 */
	boolean isValueAtMaximum();
}
