/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     Marshaller interface. Simplified marshaller interface.
 *     
 */
package ca.sciencestudio.util.marshal;

/**
 * @author maxweld
 *
 */
public interface Marshaller {

	public static enum Type {
		XML, JSON, UNKNOWN;
	}
	
	public Type getType();
	
	public String marshal(Object object);
}
