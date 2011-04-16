/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     Record interface.
 *     
 */
package ca.sciencestudio.data.record;

import ca.sciencestudio.data.support.RecordFormatException;

/**
 * @author maxweld
 *
 */
public interface Record {
	
	public int getNumberOfValues();
	public Object[] getValues();
	
	public Object getValue(int idx) throws RecordFormatException;
	public void setValue(int idx, Object value) throws RecordFormatException;
	
	public String getString(int idx) throws RecordFormatException;
	public String[] getStringArray(int idx) throws RecordFormatException;
	public Object getStringValue(int idx) throws RecordFormatException;
	
	public byte getByte(int idx) throws RecordFormatException;
	public byte[] getByteArray(int idx) throws RecordFormatException;
	public Object getByteValue(int idx) throws RecordFormatException;
	
	public short getShort(int idx) throws RecordFormatException;
	public short[] getShortArray(int idx) throws RecordFormatException;
	public Object getShortValue(int idx) throws RecordFormatException;
	
	public int getInt(int idx) throws RecordFormatException;
	public int[] getIntArray(int idx) throws RecordFormatException;
	public Object getIntValue(int idx) throws RecordFormatException;
	
	public long getLong(int idx) throws RecordFormatException;
	public long[] getLongArray(int idx) throws RecordFormatException;
	public Object getLongValue(int idx) throws RecordFormatException;
	
	public float getFloat(int idx) throws RecordFormatException;
	public float[] getFloatArray(int idx) throws RecordFormatException;
	public Object getFloatValue(int idx) throws RecordFormatException;
	
	public double getDouble(int idx) throws RecordFormatException;
	public double[] getDoubleArray(int idx) throws RecordFormatException;
	public Object getDoubleValue(int idx) throws RecordFormatException;
}