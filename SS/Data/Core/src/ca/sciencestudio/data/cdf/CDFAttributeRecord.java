/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     CDFAttributeRecord class.
 *     
 */
package ca.sciencestudio.data.cdf;

import java.util.List;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import gsfc.nssdc.cdf.Entry;
import gsfc.nssdc.cdf.Attribute;
import gsfc.nssdc.cdf.CDFException;

import ca.sciencestudio.data.cdf.CDFTypesUtility;
import ca.sciencestudio.data.record.AbstractRecord;
import ca.sciencestudio.data.support.RecordFormatException;
import ca.sciencestudio.data.support.TypesException;

/**
 * @author maxweld
 *
 */
public class CDFAttributeRecord extends AbstractRecord {

	private Attribute attribute;
	
	public CDFAttributeRecord(Attribute attribute) {
		this.attribute = attribute;
	}
	
	public Attribute getAttribute() {
		return attribute;
	}

	public int getNumberOfEntries() {
		return attribute.getEntries().size();
	}
	
	@SuppressWarnings("unchecked")
	public List<Entry> getEntries() {
		return Collections.unmodifiableList(attribute.getEntries());
	}
	
	public Entry getEntry(int idx) {
		try {
			return getEntries().get(idx);
		}
		catch(IndexOutOfBoundsException e) {
			return null;
		}
	}
	
	public int  getEntryIndexByName(String name) {
		String entryNamePattern = String.format("\\Q%s\\E[(\\d+)]", attribute.getName());
		Matcher m = Pattern.compile(entryNamePattern).matcher(name);
		if(m.matches()) {
			long id = Long.parseLong(m.group(1));
			return getEntryIndexByID(id);
		}
		else {
			return -1;
		}
	}
	
	public Entry getEntryByName(String name) {
		try {
			return getEntries().get(getEntryIndexByName(name));
		}
		catch(IndexOutOfBoundsException e) {
			return null;
		}
	}
	
	public int getEntryIndexByID(long id) {
		int entryIdx = 0;
		for(Entry entry : getEntries()) {
			if(entry.getID() == id) {
				return entryIdx;
			}
			entryIdx++;
		}
		return -1;
	}
	
	public Entry getEntryByID(long id) {
		try {
			return getEntries().get(getEntryIndexByID(id));
		}
		catch(IndexOutOfBoundsException e) {
			return null;
		}
	}
	
	public int getNumberOfValues() {
		return getNumberOfEntries();
	}

	public Object[] getValues() {
		int n = getNumberOfValues();
		Object[] values = new Object[n];
		for(int idx = 0; idx < n; idx++) {
			try {
				values[idx] = getValue(idx);
			}
			catch (RecordFormatException e) {
				values[idx] = "Not Available";
			}
		}
		return values;
	}
	
	public Object getValue(int idx) throws RecordFormatException {
		try {
			return getEntry(idx).getData();
		}
		catch(NullPointerException e) {
			throw new RecordFormatException("No CDF entry in record at index: " + idx, e);
		}
	}
	
	public void setValue(int idx, Object value) throws RecordFormatException {
		try {
			Entry entry = getEntry(idx);
			long cdfType = entry.getDataType();
			Object cdfValue = CDFTypesUtility.getCDFValue(cdfType, value);
			entry.putData(cdfType, cdfValue);
		}
		catch(CDFException e) {
			throw new RecordFormatException("CDF exception while putting entry value.", e);
		}
		catch(TypesException e) {
			throw new RecordFormatException("Cannot get value as CDF entry data type.", e);
		}
		catch(NullPointerException e) {
			throw new RecordFormatException("No CDF entry in record at index: " + idx, e);
		}
	}
	
	// Get value by CDF entry 'name'. //
	
	public Object getValueByName(String name) throws RecordFormatException {
		return getValue(getEntryIndexByName(name));
	}
	
	public String getStringByName(String name) throws RecordFormatException {
		return getString(getEntryIndexByName(name));
	}
	
	public String[] getStringArrayByName(String name) throws RecordFormatException {
		return getStringArray(getEntryIndexByName(name));
	}
	
	public Object getStringValueByName(String name) throws RecordFormatException {
		return getStringValue(getEntryIndexByName(name));
	}
	
	public byte getByteByName(String name) throws RecordFormatException {
		return getByte(getEntryIndexByName(name));
	}
	
	public byte[] getByteArrayByName(String name) throws RecordFormatException {
		return getByteArray(getEntryIndexByName(name));
	}
	
	public Object getByteValueByName(String name) throws RecordFormatException {
		return getByteValue(getEntryIndexByName(name));
	}
	
	public short getShortByName(String name) throws RecordFormatException {
		return getShort(getEntryIndexByName(name));
	}
	
	public short[] getShortArrayByName(String name) throws RecordFormatException {
		return getShortArray(getEntryIndexByName(name));
	}
	
	public Object getShortValueByName(String name) throws RecordFormatException {
		return getShortValue(getEntryIndexByName(name));
	}
	
	public int getIntByName(String name) throws RecordFormatException {
		return getInt(getEntryIndexByName(name));
	}
	
	public int[] getIntArrayByName(String name) throws RecordFormatException {
		return getIntArray(getEntryIndexByName(name));
	}
	
	public Object getIntValueByName(String name) throws RecordFormatException {
		return getIntValue(getEntryIndexByName(name));
	}
	
	public long getLongByName(String name) throws RecordFormatException {
		return getLong(getEntryIndexByName(name));
	}
	
	public long[] getLongArrayByName(String name) throws RecordFormatException {
		return getLongArray(getEntryIndexByName(name));
	}
	
	public Object getLongValueByName(String name) throws RecordFormatException {
		return getLongValue(getEntryIndexByName(name));
	}
	
	public float getFloatByName(String name) throws RecordFormatException {
		return getFloat(getEntryIndexByName(name));
	}
	
	public float[] getFloatArrayByName(String name) throws RecordFormatException {
		return getFloatArray(getEntryIndexByName(name));
	}
	
	public Object getFloatValueByName(String name) throws RecordFormatException {
		return getFloatValue(getEntryIndexByName(name));
	}
	
	public double getDoubleByName(String name) throws RecordFormatException {
		return getDouble(getEntryIndexByName(name));
	}
	
	public double[] getDoubleArrayByName(String name) throws RecordFormatException {
		return getDoubleArray(getEntryIndexByName(name));
	}
	
	public Object getDoubleValueByName(String name) throws RecordFormatException {
		return getDoubleValue(getEntryIndexByName(name));
	}
	
	// Get value by CDF entry id. //
	
	public Object getValueByID(long id) throws RecordFormatException {
		return getValue(getEntryIndexByID(id));
	}
	
	public String getStringByID(long id) throws RecordFormatException {
		return getString(getEntryIndexByID(id));
	}
	
	public String[] getStringArrayByID(long id) throws RecordFormatException {
		return getStringArray(getEntryIndexByID(id));
	}
	
	public Object getStringValueByID(long id) throws RecordFormatException {
		return getStringValue(getEntryIndexByID(id));
	}
	
	public byte getByteByID(long id) throws RecordFormatException {
		return getByte(getEntryIndexByID(id));
	}
	
	public byte[] getByteArrayByID(long id) throws RecordFormatException {
		return getByteArray(getEntryIndexByID(id));
	}
	
	public Object getByteValueByID(long id) throws RecordFormatException {
		return getByteValue(getEntryIndexByID(id));
	}
	
	public short getShortByID(long id) throws RecordFormatException {
		return getShort(getEntryIndexByID(id));
	}
	
	public short[] getShortArrayByID(long id) throws RecordFormatException {
		return getShortArray(getEntryIndexByID(id));
	}
	
	public Object getShortValueByID(long id) throws RecordFormatException {
		return getShortValue(getEntryIndexByID(id));
	}
	
	public int getIntByID(long id) throws RecordFormatException {
		return getInt(getEntryIndexByID(id));
	}
	
	public int[] getIntArrayByID(long id) throws RecordFormatException {
		return getIntArray(getEntryIndexByID(id));
	}
	
	public Object getIntValueByID(long id) throws RecordFormatException {
		return getIntValue(getEntryIndexByID(id));
	}
	
	public long getLongByID(long id) throws RecordFormatException {
		return getLong(getEntryIndexByID(id));
	}
	
	public long[] getLongArrayByID(long id) throws RecordFormatException {
		return getLongArray(getEntryIndexByID(id));
	}
	
	public Object getLongValueByID(long id) throws RecordFormatException {
		return getLongValue(getEntryIndexByID(id));
	}
	
	public float getFloatByID(long id) throws RecordFormatException {
		return getFloat(getEntryIndexByID(id));
	}
	
	public float[] getFloatArrayByID(long id) throws RecordFormatException {
		return getFloatArray(getEntryIndexByID(id));
	}
	
	public Object getFloatValueByID(long id) throws RecordFormatException {
		return getFloatValue(getEntryIndexByID(id));
	}
	
	public double getDoubleByID(long id) throws RecordFormatException {
		return getDouble(getEntryIndexByID(id));
	}
	
	public double[] getDoubleArrayByID(long id) throws RecordFormatException {
		return getDoubleArray(getEntryIndexByID(id));
	}
	
	public Object getDoubleValueByID(long id) throws RecordFormatException {
		return getDoubleValue(getEntryIndexByID(id));
	}
}
