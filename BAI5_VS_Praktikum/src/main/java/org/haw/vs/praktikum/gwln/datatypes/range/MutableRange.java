package org.haw.vs.praktikum.gwln.datatypes.range;

import java.text.MessageFormat;

/**
 * Ein Datentyp der einen Wertebereich definiert. Der Wertebereich kann auch von außen
 * geändert werden.
 */
public class MutableRange implements Range {
	private int _minimum;
	private int _maximum;
	
	/**
	 * Instanziiert eine Range mit einem Minimum von {@code 0} dem übergebenen Maximum.
	 * @param maximum Das Maximum
	 */
	public MutableRange(int maximum) {
		this(0, maximum);
	}
	
	/**
	 * Instanziiert eine Range mit dem übergebenen Minimum und Maximum.
	 * @param minimum Das Minimum
	 * @param maximum Das Maximum
	 * @param current Der aktuelle Wert
	 * @throws IllegalArgumentException Wenn folgende Eigenschaft verletzt ist {@code minimum < maximum}
	 */
	public MutableRange(int minimum, int maximum) {
		if(minimum >= maximum) {
			throw new IllegalArgumentException(MessageFormat.format("The maximum ({0}) must be greater than the minimum ({1})!", minimum, maximum));
		}
		_minimum = minimum;
		_maximum = maximum;
	}
	
	@Override
	public int getMinimum() {
		return _minimum;
	}
	
	@Override
	public int getMaximum() {
		return _maximum;
	}
	
	@Override
	public boolean contains(int value) {
		return value >= _minimum && value <= _maximum;
	}
	
	/**
	 * Setzt das Minimum auf den übergebenen neuen Wert.
	 * @param minimum Das neue Minimum
	 * @throws IllegalArgumentException Wenn das neue Minimum größer oder gleich dem Maximum ist
	 * @see #getMaximum()
	 */
	public void setMinimum(int minimum) {
		if(minimum >= _maximum) {
			throw new IllegalArgumentException(MessageFormat.format("The minimum ({0}) must be less than the maximum ({1})!", minimum, _maximum));
		}
		_minimum = minimum;
	}
	
	/**
	 * Setzt das Maximum auf den übergebenen neuen Wert
	 * @param maximum Das neue Maximum
	 * @throws IllegalArgumentException Wenn das neue Maximum kleiner oder gleich dem Minimum ist
	 */
	public void setMaximum(int maximum) {
		if(maximum <= _minimum) {
			throw new IllegalArgumentException(MessageFormat.format("The maximum ({0}) must be greater than the minimum ({1})!", maximum, _minimum));
		}
		_maximum = maximum;
	}
	
	@Override
	public String toString() {
		return "[" + _minimum + " - " + _maximum + "]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + _maximum;
		result = prime * result + _minimum;
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof MutableRange)) {
			return false;
		}
		MutableRange other = (MutableRange) obj;
		if (_maximum != other._maximum) {
			return false;
		}
		if (_minimum != other._minimum) {
			return false;
		}
		return true;
	}
}
