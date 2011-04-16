/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     CDFEventRecord class.
 *     
 */
package ca.sciencestudio.data.cdf;

import java.util.Vector;
import java.util.Collection;

import ca.sciencestudio.data.cdf.CDFTypesUtility;
import ca.sciencestudio.data.record.ObjectArrayRecord;
import ca.sciencestudio.data.support.TypesException;
import ca.sciencestudio.data.support.RecordFormatException;

/**
 * @author maxweld
 *
 */
public class CDFRecord extends ObjectArrayRecord {
	
	private CDFEvent event;
	
	public CDFRecord(CDFEvent event) {
		super(event.getNumberOfVariables());
		this.event = event;
	}
	
	public CDFRecord(CDFEvent event, int numberOfVariables) {
		super(numberOfVariables);
		this.event = event;
	}
	
	public CDFRecord(CDFEvent event, Collection<Object> values) {
		super(values);
		this.event = event;
	}
	
	public CDFRecord(CDFEvent event, Object[] values) {
		super(values);
		this.event = event;
	}
	
	public CDFEvent getEvent() {
		return event;
	}
	
	// Set value by CDF variable name and ID. //
	
	public void setValueByName(String name, Object value) throws RecordFormatException {
		setValue(event.getVariableIndexByName(name), value);
	}
	
	public void setValueByCDFVarID(long id, Object value) throws RecordFormatException {
		setValue(event.getVariableIndexByID(id), value);
	}
	
	// Get value by CDF variable name. //
	
	public Object getValueByName(String name) throws RecordFormatException {
		return getValue(event.getVariableIndexByName(name));
	}
	
	public String getStringByName(String name) throws RecordFormatException {
		return getString(event.getVariableIndexByName(name));
	}
	
	public String[] getStringArrayByName(String name) throws RecordFormatException {
		return getStringArray(event.getVariableIndexByName(name));
	}
	
	public Object getStringValueByName(String name) throws RecordFormatException {
		return getStringValue(event.getVariableIndexByName(name));
	}
	
	public byte getByteByName(String name) throws RecordFormatException {
		return getByte(event.getVariableIndexByName(name));
	}
	
	public byte[] getByteArrayByName(String name) throws RecordFormatException {
		return getByteArray(event.getVariableIndexByName(name));
	}
	
	public Object getByteValueByName(String name) throws RecordFormatException {
		return getByteValue(event.getVariableIndexByName(name));
	}
	
	public short getShortByName(String name) throws RecordFormatException {
		return getShort(event.getVariableIndexByName(name));
	}
	
	public short[] getShortArrayByName(String name) throws RecordFormatException {
		return getShortArray(event.getVariableIndexByName(name));
	}
	
	public Object getShortValueByName(String name) throws RecordFormatException {
		return getShortValue(event.getVariableIndexByName(name));
	}
	
	public int getIntByName(String name) throws RecordFormatException {
		return getInt(event.getVariableIndexByName(name));
	}
	
	public int[] getIntArrayByName(String name) throws RecordFormatException {
		return getIntArray(event.getVariableIndexByName(name));
	}
	
	public Object getIntValueByName(String name) throws RecordFormatException {
		return getIntValue(event.getVariableIndexByName(name));
	}
	
	public long getLongByName(String name) throws RecordFormatException {
		return getLong(event.getVariableIndexByName(name));
	}
	
	public long[] getLongArrayByName(String name) throws RecordFormatException {
		return getLongArray(event.getVariableIndexByName(name));
	}
	
	public Object getLongValueByName(String name) throws RecordFormatException {
		return getLongValue(event.getVariableIndexByName(name));
	}
	
	public float getFloatByName(String name) throws RecordFormatException {
		return getFloat(event.getVariableIndexByName(name));
	}
	
	public float[] getFloatArrayByName(String name) throws RecordFormatException {
		return getFloatArray(event.getVariableIndexByName(name));
	}
	
	public Object getFloatValueByName(String name) throws RecordFormatException {
		return getFloatValue(event.getVariableIndexByName(name));
	}
	
	public double getDoubleByName(String name) throws RecordFormatException {
		return getDouble(event.getVariableIndexByName(name));
	}
	
	public double[] getDoubleArrayByName(String name) throws RecordFormatException {
		return getDoubleArray(event.getVariableIndexByName(name));
	}
	
	public Object getDoubleValueByName(String name) throws RecordFormatException {
		return getDoubleValue(event.getVariableIndexByName(name));
	}
	
	// Get value by CDF variable ID. //
	
	public Object getValueByID(long id) throws RecordFormatException {
		return getValue(event.getVariableIndexByID(id));
	}
	
	public String getStringByID(long id) throws RecordFormatException {
		return getString(event.getVariableIndexByID(id));
	}
	
	public String[] getStringArrayByID(long id) throws RecordFormatException {
		return getStringArray(event.getVariableIndexByID(id));
	}
	
	public Object getStringValueByID(long id) throws RecordFormatException {
		return getStringValue(event.getVariableIndexByID(id));
	}
	
	public byte getByteByID(long id) throws RecordFormatException {
		return getByte(event.getVariableIndexByID(id));
	}
	
	public byte[] getByteArrayByID(long id) throws RecordFormatException {
		return getByteArray(event.getVariableIndexByID(id));
	}
	
	public Object getByteValueByID(long id) throws RecordFormatException {
		return getByteValue(event.getVariableIndexByID(id));
	}
	
	public short getShortByID(long id) throws RecordFormatException {
		return getShort(event.getVariableIndexByID(id));
	}
	
	public short[] getShortArrayByID(long id) throws RecordFormatException {
		return getShortArray(event.getVariableIndexByID(id));
	}
	
	public Object getShortValueByID(long id) throws RecordFormatException {
		return getShortValue(event.getVariableIndexByID(id));
	}
	
	public int getIntByID(long id) throws RecordFormatException {
		return getInt(event.getVariableIndexByID(id));
	}
	
	public int[] getIntArrayByID(long id) throws RecordFormatException {
		return getIntArray(event.getVariableIndexByID(id));
	}
	
	public Object getIntValueByID(long id) throws RecordFormatException {
		return getIntValue(event.getVariableIndexByID(id));
	}
	
	public long getLongByID(long id) throws RecordFormatException {
		return getLong(event.getVariableIndexByID(id));
	}
	
	public long[] getLongArrayByID(long id) throws RecordFormatException {
		return getLongArray(event.getVariableIndexByID(id));
	}
	
	public Object getLongValueByID(long id) throws RecordFormatException {
		return getLongValue(event.getVariableIndexByID(id));
	}
	
	public float getFloatByID(long id) throws RecordFormatException {
		return getFloat(event.getVariableIndexByID(id));
	}
	
	public float[] getFloatArrayByID(long id) throws RecordFormatException {
		return getFloatArray(event.getVariableIndexByID(id));
	}
	
	public Object getFloatValueByID(long id) throws RecordFormatException {
		return getFloatValue(event.getVariableIndexByID(id));
	}
	
	public double getDoubleByID(long id) throws RecordFormatException {
		return getDouble(event.getVariableIndexByID(id));
	}
	
	public double[] getDoubleArrayByID(long id) throws RecordFormatException {
		return getDoubleArray(event.getVariableIndexByID(id));
	}
	
	public Object getDoubleValueByID(long id) throws RecordFormatException {
		return getDoubleValue(event.getVariableIndexByID(id));
	}
	
	// Get values with CDF types. //
	
	public Object getCDFValue(int idx) throws RecordFormatException {
		long cdfType;
		try {
			cdfType = event.getVariable(idx).getDataType();
		}
		catch(NullPointerException e) {
			throw new RecordFormatException("Cannot get CDF data type at index: " + idx, e);
		}
		Object value = getValue(idx);
		try {
			return CDFTypesUtility.getCDFValue(cdfType, value);
		}
		catch(TypesException e) {
			throw new RecordFormatException("Cannot get CDF value at index: " + idx, e);
		}
	}
	
	public Vector<Object> getCDFValues() throws RecordFormatException {		
		int n = getNumberOfValues();
		Vector<Object> values = new Vector<Object>(n); 
		for(int idx = 0; idx < n; idx++) {
			values.add(idx, getCDFValue(idx));
		}
		return values;
	}
}
