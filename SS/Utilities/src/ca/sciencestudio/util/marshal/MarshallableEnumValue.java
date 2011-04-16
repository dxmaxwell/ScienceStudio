/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     MarshallableEnumValue class.
 *     
 */
package ca.sciencestudio.util.marshal;

import java.io.Serializable;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

/**
 * @author maxweld
 *
 * @param <E>
 */
public abstract class MarshallableEnumValue<E extends Enum<E>> implements Serializable {
	
	private static final long serialVersionUID = 1L;  

	public static <E extends Enum<E>, T extends MarshallableEnum<E>> Collection<MarshallableEnumValue<E>> getMarshallableValues(Class<T> clazz) {
		return getMarshallableValues(Arrays.asList(clazz.getEnumConstants()));
	}

	public static <E extends Enum<E>, T extends MarshallableEnum<E>> Collection<MarshallableEnumValue<E>> getMarshallableValues(Class<T> clazz, E... exclude) {
		Collection<T> marshallableEnums = new LinkedList<T>(Arrays.asList(clazz.getEnumConstants()));
		marshallableEnums.removeAll(Arrays.asList(exclude));
		return getMarshallableValues(marshallableEnums);
	}
	
	public static <E extends Enum<E>, T extends MarshallableEnum<E>> Collection<MarshallableEnumValue<E>> getMarshallableValues(Collection<T> marshallableEnums) {
		Collection<MarshallableEnumValue<E>> values = new ArrayList<MarshallableEnumValue<E>>(marshallableEnums.size());
	
		for(T marshallableEnum : marshallableEnums) {
			values.add(marshallableEnum.getMarshallableValue());
		}
		
		return values;
	}
	
	private String name;
	private String longName;
	
	public MarshallableEnumValue(E nonMarshallableEnum) {
		name = nonMarshallableEnum.name();
		longName = nonMarshallableEnum.toString();
	}

	public String getName() {
		return name;
	}
	
	public String getLongName() {
		return longName;
	}
	
	public abstract E getEnum();
}
