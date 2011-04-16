/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     Types class.
 *     
 */
package ca.sciencestudio.data.util;

import java.lang.reflect.Array;
import java.util.Arrays;

import ca.sciencestudio.data.support.TypesException;

/**
 * @author maxweld
 *
 */
public abstract class TypesUtility {

	public static String getString(Object value) throws TypesException {
		if(value instanceof String) {
			return (String)value;
		}
		else {
			try {
				return value.toString();
			}
			catch(NullPointerException e) {
				throw new TypesException("Cannot get string from null value.");
			}
		}
	}
	
	public static byte getByte(Object value) throws TypesException {
		if(value instanceof Number) {
			return ((Number)value).byteValue();
		}
		else {
			try {
				return Byte.parseByte(value.toString().trim());
			}
			catch(NumberFormatException e) {
				throw new TypesException("Cannot parse value, <" + value.toString().trim() + ">, to byte type.", e);
			}
			catch(NullPointerException e) {
				throw new TypesException("Cannot get byte type from null value.", e);
			}
		}
	}
	
	public static short getShort(Object value) throws TypesException {
		if(value instanceof Number) {
			return ((Number)value).shortValue();
		}
		else {
			try {
				return Short.parseShort(value.toString().trim());
			}
			catch(NumberFormatException e) {
				throw new TypesException("Cannot parse value, <" + value.toString().trim() +">, to short type.", e);
			}
			catch(NullPointerException e) {
				throw new TypesException("Cannot get short type from null value.", e);
			}
		}
	}
	
	public static int getInt(Object value) throws TypesException {
		if(value instanceof Number) {
			return ((Number)value).intValue();
		}
		else {
			try {
				return Integer.parseInt(value.toString().trim());
			}
			catch(NumberFormatException e) {
				throw new TypesException("Cannot parse value, <" + value.toString().trim() +">, to int type.", e);
			}
			catch(NullPointerException e) {
				throw new TypesException("Cannot get int type from null value.", e);
			}
		}
	}

	public static long getLong(Object value) throws TypesException {
		if(value instanceof Number) {
			return ((Number)value).longValue();
		}
		else {
			try {
				return Long.parseLong(value.toString().trim());
			}
			catch(NumberFormatException e) {
				throw new TypesException("Cannot parse value, <" + value.toString().trim() +">, to long type.", e);
			}
			catch(NullPointerException e) {
				throw new TypesException("Cannot get long type from null value.", e);
			}
		}
	}
	
	public static float getFloat(Object value) throws TypesException {
		if(value instanceof Number) {
			return ((Number)value).floatValue();
		}
		else {
			try {
				return Float.parseFloat(value.toString().trim());
			}
			catch(NumberFormatException e) {
				throw new TypesException("Cannot parse value, <" + value.toString().trim() +">, to float type.", e);
			}
			catch(NullPointerException e) {
				throw new TypesException("Cannot get float type from null value.", e);
			}
		}
		
	}
	
	public static double getDouble(Object value) throws TypesException {
		if(value instanceof Number) {
			return ((Number)value).doubleValue();
		}
		else {
			try {
				return Double.parseDouble(value.toString().trim());
			}
			catch(NumberFormatException e) {
				throw new TypesException("Cannot parse value, <" + value.toString().trim() +">, to double type.", e);
			}
			catch(NullPointerException e) {
				throw new TypesException("Cannot get double type from null value.", e);
			}
		}
	}
	
	public static Object getStringValue(Object value) throws TypesException {
		return getTypeValue(String.class, value);
	}
	
	public static Object getByteValue(Object value) throws TypesException {
		return getTypeValue(Byte.TYPE, value);
	}
	
	public static Object getShortValue(Object value) throws TypesException {
		return getTypeValue(Short.TYPE, value);
	}
	
	public static Object getIntValue(Object value) throws TypesException {
		return getTypeValue(Integer.TYPE, value);
	}
	
	public static Object getLongValue(Object value) throws TypesException {
		return getTypeValue(Long.TYPE, value);
	}
	
	public static Object getFloatValue(Object value) throws TypesException {
		return getTypeValue(Float.TYPE, value);
	}
	
	public static Object getDoubleValue(Object value) throws TypesException {
		return getTypeValue(Double.TYPE, value);
	}
	
	public static boolean isArray(Object value) throws TypesException {
		try {
			return value.getClass().isArray();
		}
		catch(NullPointerException e) {
			throw new TypesException("Cannot get array status from null value.", e);
		}
	}
	
	public static int getNumberOfDimensions(Object value) throws TypesException {
		try {
			int numberOfDimensions = 0;
			Class<?> valueClass = value.getClass();
			while(valueClass.isArray()) {
				valueClass = valueClass.getComponentType();
				numberOfDimensions++;
			}
			return numberOfDimensions;
		}
		catch(NullPointerException e) {
			throw new TypesException("Cannot get number of dimensions from null value.", e);
		}
	}
	
	protected static Object getTypeValue(Class<?> componentType, Object value) throws TypesException {
		
		int numberOfDimensions = getNumberOfDimensions(value);
		
		if(numberOfDimensions == 0) {
			if(Byte.TYPE.isAssignableFrom(componentType)) {
				return getByte(value);
			}	
			else if(Short.TYPE.isAssignableFrom(componentType)) {
				return getShort(value);
			}
			else if(Integer.TYPE.isAssignableFrom(componentType)) {
				return getInt(value);
			}
			else if(Long.TYPE.isAssignableFrom(componentType)) {
				return getLong(value);
			}
			else if(Float.TYPE.isAssignableFrom(componentType)) {
				return getFloat(value);
			}
			else if(Double.TYPE.isAssignableFrom(componentType)) {
				return getDouble(value);
			}
			else if(String.class.isAssignableFrom(componentType)) {
				return getString(value);
			}
			else {
				throw new TypesException("Component type not supported. (Should not happen!)");
			}	
		}
		
		try {
			int arrayLength = Array.getLength(value);
			Class<?> arrayComponentType = getArrayComponentType(componentType, numberOfDimensions-1);
			Object array = Array.newInstance(arrayComponentType, arrayLength);
			for(int i = 0; i < arrayLength; i++) {
				Array.set(array, i, getTypeValue(componentType, Array.get(value, i)));
			}
			return array;
		}
		catch(NullPointerException e) {
			throw new TypesException("The value is null. (Should not happen!)", e);
		}
		catch(IllegalArgumentException e) {
			throw new TypesException("The value is not an array or component type is Void.TYPE.", e);
		}
		catch(ArrayIndexOutOfBoundsException e) {
			throw new TypesException("The index is out of bounds. (Should not happen!)", e);
		}
	}
	
	protected static Class<?> getArrayComponentType(Class<?> componentType, int numDims) throws TypesException {
		
		if(componentType == null) {
			throw new TypesException("Cannot get array component type from null.");
		}
		
		if(numDims == 0) {
			return componentType;
		}
		
		try {
			int[] dimensions = new int[numDims]; 
			Arrays.fill(dimensions, 1);
			return Array.newInstance(componentType, dimensions).getClass();
		}
		catch(NullPointerException e) {
			throw new TypesException("Cannot get array instance from null component type.", e);
		}
		catch(IllegalArgumentException e) {
			throw new TypesException("Cannot get array instance with zero or less dimensions.", e);
		}
		catch(NegativeArraySizeException e) {
			throw new TypesException("Cannot get array instance with dimension size less than zero.", e);
		}
	}
}
