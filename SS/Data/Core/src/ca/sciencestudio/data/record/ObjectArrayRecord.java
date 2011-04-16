/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     AbstractRecord class.
 *     
 */
package ca.sciencestudio.data.record;

import java.util.Arrays;
import java.util.Collection;

import ca.sciencestudio.data.support.RecordFormatException;

/**
 * @author maxweld
 *
 */
public class ObjectArrayRecord extends AbstractRecord {

	private Object[] values;
	
	public ObjectArrayRecord(int numberOfValues) {
		this.values = new Object[numberOfValues];
		Arrays.fill(this.values, "Not Initialized");
	}
	
	public ObjectArrayRecord(Collection<?> values) {
		this.values = values.toArray();
	}
		
	public ObjectArrayRecord(Object[] values) {
		this.values = values.clone();
	}
	
	public int getNumberOfValues() {
		return values.length;
	}
	
	public Object[] getValues() {
		return values;
	}
	
	public Object getValue(int idx) throws RecordFormatException {
		Object value;
		try {
			value = values[idx];	
		}
		catch(IndexOutOfBoundsException e) {
			throw new RecordFormatException("Cannot get value at index: " + idx, e);
		}
		
		if(value == null) {
			throw new RecordFormatException("Cannot get value at index: " + idx + ", will not return null value.");
		}
		
		return value;	
	}
	
	public void setValue(int idx, Object value) throws RecordFormatException {
		try {
			values[idx] = value;
		}
		catch(IndexOutOfBoundsException e) {
			throw new RecordFormatException("Cannot set value at index: " + idx, e);
		}
	}
}
