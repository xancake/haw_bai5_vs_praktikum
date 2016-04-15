package org.haw.vs.praktikum.gwln.datatypes.bounded;

import java.text.MessageFormat;
import org.haw.vs.praktikum.gwln.datatypes.range.MutableRange;
import org.haw.vs.praktikum.gwln.datatypes.range.Range;

/**
 * Ein Datentyp der einen durch ein Minimum und ein Maximum beschränkten Integer-Wert repräsentiert.
 */
public class MutableBoundedInteger implements BoundedInteger {
	private Range _range;
	private int _value;
	
	/**
	 * Instanziiert einen beschränkten Integer, dessen Minimum mit {@code 0} definiert ist
	 * und den übergebenen Wert sowohl als Wert und auch als Maximum verwendet.
	 * @param value Der Wert und das Maximum
	 * @throws IllegalArgumentException Wenn der übergebene Wert kleiner oder gleich 0 ist
	 */
	public MutableBoundedInteger(int value) {
		this(value, value);
	}
	
	/**
	 * Instanziiert einen beschränkten Integer, dessen Minimum mit {@code 0} definiert ist.
	 * @param maximum Das Maximum
	 * @param value Der Wert
	 * @throws IllegalArgumentException Wenn folgende Eigenschaft verletzt ist: {@code 0 <= value <= maximum}
	 */
	public MutableBoundedInteger(int maximum, int value) {
		this(0, maximum, value);
	}
	
	/**
	 * Instanziiert einen durch ein Minimum und Maximum beschränkten Integer.
	 * @param minimum Das Minimum
	 * @param maximum Das Maximum
	 * @param value Der Wert
	 * @throws IllegalArgumentException Wenn folgende Eigenschaft verletzt ist: {@code minimum <= value <= maximum}
	 */
	public MutableBoundedInteger(int minimum, int maximum, int value) {
		_range = new MutableRange(minimum, maximum);
		if(!_range.contains(value)) {
			throw new IllegalArgumentException(MessageFormat.format("The value ({0}) must be between the minimum ({1}) and maximum ({2}) of the bounds!", value, minimum, maximum));
		}
		_value = value;
	}
	
	@Override
	public int getMinimum() {
		return _range.getMinimum();
	}
	
	@Override
	public int getMaximum() {
		return _range.getMaximum();
	}
	
	@Override
	public int getValue() {
		return _value;
	}
	
	@Override
	public double getPercentage() {
		int effectiveMax = getMaximum()-getMinimum();
		int effectiveCur = getValue()-getMinimum();
		double percentage = effectiveCur * effectiveMax / 100D;
		return percentage / 100D; // Umwandlung von Prozentwert in Faktor (z.B. 24% = 0.24)
	}
	
	@Override
	public boolean isValueAtMinimum() {
		return _value == getMinimum();
	}
	
	@Override
	public boolean isValueAtMaximum() {
		return _value == getMaximum();
	}
	
	/**
	 * Setzt den aktuellen Wert auf den übergebenen Wert.
	 * Der neue Wert darf nicht außerhalb der durch das Minimum und Maximum definierten
	 * Schranke liegen.
	 * @param value Der neue Wert
	 * @throws IllegalArgumentException Wenn der neue Wert nicht zwischen dem Minimum und Maximum liegt
	 * @see #getMinimum()
	 * @see #getMaximum()
	 */
	public void setValue(int value) {
		if(!_range.contains(value)) {
			throw new IllegalArgumentException(MessageFormat.format("The value ({0}) must be between the minimum ({1}) and maximum ({2}) of the bounds!", value, getMinimum(), getMaximum()));
		}
		_value = value;
	}
	
	/**
	 * Verändert den aktuellen Wert um die übergebene Menge. Dabei wird sichergestellt,
	 * dass der daraus resultierende Wert immer in der durch das Minimum und Maximum
	 * definierten Schranke liegt.
	 * <p>Um den Wert zu reduzieren reicht es aus, einen negativen Wert zu übergeben.
	 * @param amount Die Menge
	 * @see #getMinimum()
	 * @see #getMaximum()
	 */
	public void alterValue(int amount) {
		if(_value+amount < getMinimum()) {
			_value = getMinimum();
		} else if(_value+amount > getMaximum()) {
			_value = getMaximum();
		} else {
			_value += amount;
		}
	}
	
	/**
	 * Setzt den aktuellen Wert auf das Minimum.
	 * @see #getMinimum()
	 */
	public void setValueToMinimum() {
		_value = getMinimum();
	}
	
	/**
	 * Setzt den aktuellen Wert auf das Maximum.
	 * @see #getMaximum()
	 */
	public void setValueToMaximum() {
		_value = getMaximum();
	}
	
	@Override
	public String toString() {
		return _value + " " + _range;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((_range == null) ? 0 : _range.hashCode());
		result = prime * result + _value;
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
		if (!(obj instanceof MutableBoundedInteger)) {
			return false;
		}
		MutableBoundedInteger other = (MutableBoundedInteger) obj;
		if (_range == null) {
			if (other._range != null) {
				return false;
			}
		} else if (!_range.equals(other._range)) {
			return false;
		}
		if (_value != other._value) {
			return false;
		}
		return true;
	}
}
