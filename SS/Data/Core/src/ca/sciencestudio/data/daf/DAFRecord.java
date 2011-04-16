/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     AcqDatEventRecord class.
 *     
 */
package ca.sciencestudio.data.daf;

import java.util.Collection;

import ca.sciencestudio.data.daf.DAFEvent;
import ca.sciencestudio.data.record.ObjectArrayRecord;
import ca.sciencestudio.data.support.RecordFormatException;

/**
 * @author maxweld
 *
 */
public class DAFRecord extends ObjectArrayRecord {
	
	private DAFEvent event;
	
	public DAFRecord(DAFEvent event) {
		super(event.getNumberOfElements());
		this.event = event;
	}
	
	public DAFRecord(DAFEvent event, int numberOfValues) {
		super(numberOfValues);
		this.event = event;
	}
	
	public DAFRecord(DAFEvent event, Collection<?> values) {
		super(values);
		this.event = event;
	}
	
	public DAFRecord(DAFEvent event, Object[] values) {
		super(values);
		this.event = event;
	}
	
	public DAFEvent getEvent() {
		return event;
	}
	
	// Set value by event element name and description. //
	
	public void setValueByName(String name, Object value) throws RecordFormatException {
		setValue(event.getElementIndexByName(name), value);		
	}
	
	public void setValueByDescription(String description, Object value) throws RecordFormatException {
		setValue(event.getElementIndexByDescription(description), value);		
	}
	
	// Get value by event element name. //
	
	public Object getValueByName(String name) throws RecordFormatException {
		return getValue(event.getElementIndexByName(name));
	}
	
	public String getStringByName(String name) throws RecordFormatException {
		return getString(event.getElementIndexByName(name));
	}
	
	public String[] getStringArrayByName(String name) throws RecordFormatException {
		return getStringArray(event.getElementIndexByName(name));
	}
	
	public Object getStringValueByName(String name) throws RecordFormatException {
		return getStringValue(event.getElementIndexByName(name));
	}
	
	public byte getByteByName(String name) throws RecordFormatException {
		return getByte(event.getElementIndexByName(name));
	}
	
	public byte[] getByteArrayByName(String name) throws RecordFormatException {
		return getByteArray(event.getElementIndexByName(name));
	}
	
	public Object getByteValueByName(String name) throws RecordFormatException {
		return getByteValue(event.getElementIndexByName(name));
	}
	
	public short getShortByName(String name) throws RecordFormatException {
		return getShort(event.getElementIndexByName(name));
	}
	
	public int getIntByName(String name) throws RecordFormatException {
		return getInt(event.getElementIndexByName(name));
	}
	
	public int[] getIntArrayByName(String name) throws RecordFormatException {
		return getIntArray(event.getElementIndexByName(name));
	}
	
	public Object getIntValueByName(String name) throws RecordFormatException {
		return getIntValue(event.getElementIndexByName(name));
	}
	
	public long getLongByName(String name) throws RecordFormatException {
		return getLong(event.getElementIndexByName(name));
	}
	
	public long[] getLongArrayByName(String name) throws RecordFormatException {
		return getLongArray(event.getElementIndexByName(name));
	}
	
	public Object getLongValueByName(String name) throws RecordFormatException {
		return getLongValue(event.getElementIndexByName(name));
	}
	
	public float getFloatByName(String name) throws RecordFormatException {
		return getFloat(event.getElementIndexByName(name));
	}
	
	public float[] getFloatArrayByName(String name) throws RecordFormatException {
		return getFloatArray(event.getElementIndexByName(name));
	}
	
	public Object getFloatValueByName(String name) throws RecordFormatException {
		return getFloatValue(event.getElementIndexByName(name));
	}
	
	public double getDoubleByName(String name) throws RecordFormatException {
		return getDouble(event.getElementIndexByName(name));
	}
	
	public double[] getDoubleArrayByName(String name) throws RecordFormatException {
		return getDoubleArray(event.getElementIndexByName(name));
	}
	
	public Object getDoubleValueByName(String name) throws RecordFormatException {
		return getDoubleValue(event.getElementIndexByName(name));
	}
	
	// Get value by event element description. //
	
	public Object getValueByDescription(String description) throws RecordFormatException {
		return getValue(event.getElementIndexByDescription(description));
	}
	
	public String getStringByDescription(String description) throws RecordFormatException {
		return getString(event.getElementIndexByDescription(description));
	}
	
	public String[] getStringArrayByDescription(String description) throws RecordFormatException {
		return getStringArray(event.getElementIndexByDescription(description));
	}
	
	public Object getStringValueByDescription(String description) throws RecordFormatException {
		return getStringValue(event.getElementIndexByDescription(description));
	}
	
	public byte getByteByDescription(String description) throws RecordFormatException {
		return getByte(event.getElementIndexByDescription(description));
	}
	
	public byte[] getByteArrayByDescription(String description) throws RecordFormatException {
		return getByteArray(event.getElementIndexByDescription(description));
	}
	
	public Object getByteValueByDescription(String description) throws RecordFormatException {
		return getByteValue(event.getElementIndexByDescription(description));
	}
	
	public short getShortByDescription(String description) throws RecordFormatException {
		return getShort(event.getElementIndexByDescription(description));
	}
	
	public short[] getShortArrayByDescription(String description) throws RecordFormatException {
		return getShortArray(event.getElementIndexByDescription(description));
	}
	
	public Object getShortValueByDescription(String description) throws RecordFormatException {
		return getShortValue(event.getElementIndexByDescription(description));
	}
	
	public int getIntByDescription(String description) throws RecordFormatException {
		return getInt(event.getElementIndexByDescription(description));
	}
	
	public int[] getIntArrayByDescription(String description) throws RecordFormatException {
		return getIntArray(event.getElementIndexByDescription(description));
	}
	
	public Object getIntValueByDescription(String description) throws RecordFormatException {
		return getIntValue(event.getElementIndexByDescription(description));
	}
	
	public long getLongByDescription(String description) throws RecordFormatException {
		return getLong(event.getElementIndexByDescription(description));
	}
	
	public long[] getLongArrayByDescription(String description) throws RecordFormatException {
		return getLongArray(event.getElementIndexByDescription(description));
	}
	
	public Object getLongValueByDescription(String description) throws RecordFormatException {
		return getLongValue(event.getElementIndexByDescription(description));
	}
	
	public float getFloatByDescription(String description) throws RecordFormatException {
		return getFloat(event.getElementIndexByDescription(description));
	}
	
	public float[] getFloatArrayByDescription(String description) throws RecordFormatException {
		return getFloatArray(event.getElementIndexByDescription(description));
	}
	
	public Object getFloatValueByDescription(String description) throws RecordFormatException {
		return getFloatValue(event.getElementIndexByDescription(description));
	}
	
	public double getDoubleByDescription(String description) throws RecordFormatException {
		return getDouble(event.getElementIndexByDescription(description));
	}
	
	public double[] getDoubleArrayByDescription(String description) throws RecordFormatException {
		return getDoubleArray(event.getElementIndexByDescription(description));
	}
	
	public Object getDoubleValueByDescription(String description) throws RecordFormatException {
		return getDoubleValue(event.getElementIndexByDescription(description));
	}
}