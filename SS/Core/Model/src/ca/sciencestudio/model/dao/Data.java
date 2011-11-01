/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *    Data interface.
 *     
 */
package ca.sciencestudio.model.dao;

/**
 * @author maxweld
 *
 *
 */
public interface Data<D> {

	public D get();
	public boolean available();
}
