/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     MarshallableEnum interface.
 *     
 */
package ca.sciencestudio.util.marshal;

import ca.sciencestudio.util.marshal.MarshallableEnumValue;

/**
 * @author maxweld
 *
 * @param <E>
 */
public interface MarshallableEnum<E extends Enum<E>> {

	public String getName();
	public String getLongName();
	public MarshallableEnumValue<E> getMarshallableValue();
}
