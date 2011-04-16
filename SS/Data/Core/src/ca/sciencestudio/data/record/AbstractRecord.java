/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     AbstractRecord class.
 *     
 */
package ca.sciencestudio.data.record;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ca.sciencestudio.data.util.TypesUtility;
import ca.sciencestudio.data.support.TypesException;
import ca.sciencestudio.data.support.RecordFormatException;

/**
 * @author maxweld
 *
 */
public abstract class AbstractRecord implements Record {

	protected Log log = LogFactory.getLog(getClass());
	
	public abstract Object getValue(int idx) throws RecordFormatException;
	
	// String Value //
	
	public String getString(int idx) throws RecordFormatException {
		Object value = getValue(idx);
		try {
			return TypesUtility.getString(value);
		}
		catch(TypesException e) {
			throw new RecordFormatException("Cannot get String at index: " + idx, e);
		}
	}
	
	public Object getStringValue(int idx) throws RecordFormatException {
		Object value = getValue(idx);
		try {
			return TypesUtility.getStringValue(value);
		}
		catch(TypesException e) {
			throw new RecordFormatException("Cannot get String value at index: " + idx, e);
		}
	}
	
	public String[] getStringArray(int idx) throws RecordFormatException {
		Object value = getStringValue(idx);
		try {
			return (String[]) value;
		}
		catch(ClassCastException e) {
			throw new RecordFormatException("Value at index: " + idx + ", expected class: String[], actual class: " + value.getClass().getSimpleName(), e);
		}
	}
	
	// Byte Value // 
	
	public byte getByte(int idx) throws RecordFormatException {
		Object value = getValue(idx);
		try {
			return TypesUtility.getByte(value);
		}
		catch(TypesException e) {
			throw new RecordFormatException("Cannot get byte at index: " + idx, e);
		}
	}
	
	public Object getByteValue(int idx) throws RecordFormatException {
		Object value = getValue(idx);
		try {
			return TypesUtility.getByteValue(value);
		}
		catch(TypesException e) {
			throw new RecordFormatException("Cannot get byte value at index: " + idx, e);
		}
	}
	
	public byte[] getByteArray(int idx) throws RecordFormatException {
		Object value = getByteValue(idx);
		try {
			return (byte[]) value;
		}
		catch(ClassCastException e) {
			throw new RecordFormatException("Value at index: " + idx + ", expected class: byte[], actual class: " + value.getClass().getSimpleName(), e);
		}
	}
	
	// Short Value //
	
	public short getShort(int idx) throws RecordFormatException {
		Object value = getValue(idx);
		try {
			return TypesUtility.getShort(value);
		}
		catch(TypesException e) {
			throw new RecordFormatException("Cannot get short at index: " + idx, e);
		}
	}
	
	public Object getShortValue(int idx) throws RecordFormatException {
		Object value = getValue(idx);
		try {
			return TypesUtility.getShortValue(value);
		}
		catch(TypesException e) {
			throw new RecordFormatException("Cannot get short value at index: " + idx, e);
		}
	}
	
	public short[] getShortArray(int idx) throws RecordFormatException {
		Object value = getShortValue(idx);
		try {
			return (short[]) value;
		}
		catch(ClassCastException e) {
			throw new RecordFormatException("Value at index: " + idx + ", expected class: short[], actual class: " + value.getClass().getSimpleName(), e);
		}
	}
	
	// Int Value //
	
	public int getInt(int idx) throws RecordFormatException {
		Object value = getValue(idx);
		try {
			return TypesUtility.getInt(value);
		}
		catch(TypesException e) {
			throw new RecordFormatException("Cannot get int at index: " + idx, e);
		}
	}
	
	public Object getIntValue(int idx) throws RecordFormatException {
		Object value = getValue(idx);
		try {
			return TypesUtility.getIntValue(value);
		}
		catch(TypesException e) {
			throw new RecordFormatException("Cannot get int value at index: " + idx, e);
		}
	}
	
	public int[] getIntArray(int idx) throws RecordFormatException {
		Object value =  getIntValue(idx);
		try {
			return (int[]) value;
		}
		catch(ClassCastException e) {
			throw new RecordFormatException("Value at index: " + idx + ", expected class: int[], actual class: " + value.getClass().getSimpleName(), e);
		}
	}
	
	// Long Value //
	
	public long getLong(int idx) throws RecordFormatException {
		Object value = getValue(idx);
		try {
			return TypesUtility.getLong(value);
		}
		catch(TypesException e) {
			throw new RecordFormatException("Cannot get long at index: " + idx, e);
		}
	}
	
	public Object getLongValue(int idx) throws RecordFormatException {
		Object value = getValue(idx);
		try {
			return TypesUtility.getLongValue(value);
		}
		catch(TypesException e) {
			throw new RecordFormatException("Cannot get long value at index: " + idx, e);
		}
	}
	
	public long[] getLongArray(int idx) throws RecordFormatException {
		Object value = getLongValue(idx);
		try {
			return (long[]) value;
		}
		catch(ClassCastException e) {
			throw new RecordFormatException("Value at index: " + idx + ", expected class: long[], actual class: " + value.getClass().getSimpleName(), e);
		}
	}
	
	// Float Value //
	
	public float getFloat(int idx) throws RecordFormatException {
		Object value = getValue(idx);
		try {
			return TypesUtility.getFloat(value);
		}
		catch(TypesException e) {
			throw new RecordFormatException("Cannot get float at index: " + idx, e);
		}
	}
	
	public Object getFloatValue(int idx) throws RecordFormatException {
		Object value = getValue(idx);
		try {
			return TypesUtility.getFloatValue(value);
		}
		catch(TypesException e) {
			throw new RecordFormatException("Cannot get float value at index: " + idx, e);
		}
	}
	
	public float[] getFloatArray(int idx) throws RecordFormatException {
		Object value = getFloatValue(idx);
		try {
			return (float[]) value;
		}
		catch(ClassCastException e) {
			throw new RecordFormatException("Value at index: " + idx + ", expected class: float[], actual class: " + value.getClass().getSimpleName(), e);
		}
	}
	
	// Double Value //
	
	public double getDouble(int idx) throws RecordFormatException {
		Object value = getValue(idx);
		try {
			return TypesUtility.getDouble(value);
		}
		catch(TypesException e) {
			throw new RecordFormatException("Cannot get double at index: " + idx, e);
		}
	}
	
	public Object getDoubleValue(int idx) throws RecordFormatException {
		Object value = getValue(idx);
		try {
			return TypesUtility.getDoubleValue(value);
		}
		catch(TypesException e) {
			throw new RecordFormatException("Cannot get double value at index: " + idx, e);
		}
	}
	
	public double[] getDoubleArray(int idx) throws RecordFormatException {
		Object value = getDoubleValue(idx);
		try {
			return (double[]) value;
		}
		catch(ClassCastException e) {
			throw new RecordFormatException("Value at index: " + idx + ", expected class: double[], actual class: " + value.getClass().getSimpleName(), e);
		}
	}
	
	// Array Utilities //
	
	public boolean isArray(int idx) throws RecordFormatException {
		try {
			return TypesUtility.isArray(getValue(idx));
		}
		catch(TypesException e) {
			throw new RecordFormatException("Type exception while checking for array.", e);
		}
	}
	
	public int getNumberOfDimensions(int idx) throws RecordFormatException {
		try {
			return TypesUtility.getNumberOfDimensions(getValue(idx));
		}
		catch(TypesException e) {
			throw new RecordFormatException("Type exception while checking number of dimensions.", e);
		}
	}
	
	public String toString() {
		List<Object> values = new ArrayList<Object>();
		for(int idx=0; idx<getNumberOfValues(); idx++) {
			try {
				values.add(getValue(idx));
			}
			catch(RecordFormatException e) {
				values.add("null");
			}
		}
		return values.toString();
	}
}
