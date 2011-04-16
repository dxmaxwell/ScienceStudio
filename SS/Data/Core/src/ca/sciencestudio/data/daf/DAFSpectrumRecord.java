/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     AcqDatSpectrumRecord class.
 *     
 */
package ca.sciencestudio.data.daf;

import java.util.Collection;

import ca.sciencestudio.data.util.TypesUtility;
import ca.sciencestudio.data.record.ObjectArrayRecord;
import ca.sciencestudio.data.support.TypesException;
import ca.sciencestudio.data.support.RecordFormatException;

/**
 * @author maxweld
 *
 */
public class DAFSpectrumRecord extends ObjectArrayRecord {

	private long offset;
	
	public DAFSpectrumRecord(long offset, Collection<?> values) {
		super(values);
		this.offset = offset;
	}
	
	public DAFSpectrumRecord(long offset, Object[] values) {
		super(values);
		this.offset = offset;
	}
	
	public long getOffset() {
		return offset;
	}
	
	// String Value //
	
	public Object getStringValue() throws RecordFormatException {
		Object value = getValues();
		try {
			return TypesUtility.getStringValue(value);
		}
		catch(TypesException e) {
			throw new RecordFormatException("Cannot get String value from spectrum record", e);
		}
	}
	
	public String[] getStringArray() throws RecordFormatException {
		Object value = getStringValue();
		try {
			return (String[]) value;
		}
		catch(ClassCastException e) {
			throw new RecordFormatException("Cannot get String array from spectrum record, expected class: String[], actual class: " + value.getClass().getSimpleName(), e);
		}
	}
	
	// Byte Value //
	
	public Object getByteValue() throws RecordFormatException {
		Object value = getValues();
		try {
			return TypesUtility.getByteValue(value);
		}
		catch(TypesException e) {
			throw new RecordFormatException("Cannot get Byte value from spectrum record", e);
		}
	}
	
	public byte[] getByteArray() throws RecordFormatException {
		Object value = getByteValue();
		try {
			return (byte[]) value;
		}
		catch(ClassCastException e) {
			throw new RecordFormatException("Cannot get Byte array from spectrum record, expected class: byte[], actual class: " + value.getClass().getSimpleName(), e);
		}
	}
	
	// Short Value //
	
	public Object getShortValue() throws RecordFormatException {
		Object value = getValues();
		try {
			return TypesUtility.getShortValue(value);
		}
		catch(TypesException e) {
			throw new RecordFormatException("Cannot get Short value from spectrum record", e);
		}
	}
	
	public short[] getShortArray() throws RecordFormatException {
		Object value = getShortValue();
		try {
			return (short[]) value;
		}
		catch(ClassCastException e) {
			throw new RecordFormatException("Cannot get Short array from spectrum record, expected class: short[], actual class: " + value.getClass().getSimpleName(), e);
		}
	}
	
	// Int Value //
	
	public Object getIntValue() throws RecordFormatException {
		Object value = getValues();
		try {
			return TypesUtility.getIntValue(value);
		}
		catch(TypesException e) {
			throw new RecordFormatException("Cannot get Int value from spectrum record", e);
		}
	}
	
	public int[] getIntArray() throws RecordFormatException {
		Object value = getIntValue();
		try {
			return (int[]) value;
		}
		catch(ClassCastException e) {
			throw new RecordFormatException("Cannot get Int array from spectrum record, expected class: int[], actual class: " + value.getClass().getSimpleName(), e);
		}
	}
	
	// Long Value //
	
	public Object getLongValue() throws RecordFormatException {
		Object value = getValues();
		try {
			return TypesUtility.getLongValue(value);
		}
		catch(TypesException e) {
			throw new RecordFormatException("Cannot get Long value from spectrum record", e);
		}
	}
	
	public long[] getLongArray() throws RecordFormatException {
		Object value = getLongValue();
		try {
			return (long[]) value;
		}
		catch(ClassCastException e) {
			throw new RecordFormatException("Cannot get Long array from spectrum record, expected class: long[], actual class: " + value.getClass().getSimpleName(), e);
		}
	}
	
	// Float Value //
	
	public Object getFloatValue() throws RecordFormatException {
		Object value = getValues();
		try {
			return TypesUtility.getFloatValue(value);
		}
		catch(TypesException e) {
			throw new RecordFormatException("Cannot get Float value from spectrum record", e);
		}
	}
	
	public float[] getFloatArray() throws RecordFormatException {
		Object value = getFloatValue();
		try {
			return (float[]) value;
		}
		catch(ClassCastException e) {
			throw new RecordFormatException("Cannot get Float array from spectrum record, expected class: float[], actual class: " + value.getClass().getSimpleName(), e);
		}
	}
	
	// Double Value //
	
	public Object getDoubleValue() throws RecordFormatException {
		Object value = getValues();
		try {
			return TypesUtility.getDoubleValue(value);
		}
		catch(TypesException e) {
			throw new RecordFormatException("Cannot get Double value from spectrum record", e);
		}
	}
	
	public double[] getDoubleArray() throws RecordFormatException {
		Object value = getDoubleValue();
		try {
			return (double[]) value;
		}
		catch(ClassCastException e) {
			throw new RecordFormatException("Cannot get Double array from spectrum record, expected class: double[], actual class: " + value.getClass().getSimpleName(), e);
		}
	}
}
