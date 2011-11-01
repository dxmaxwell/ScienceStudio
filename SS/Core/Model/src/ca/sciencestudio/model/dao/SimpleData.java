/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *    SimpleData class.
 *     
 */
package ca.sciencestudio.model.dao;

/**
 * @author maxweld
 *
 *
 */
public class SimpleData<D> implements Data<D> {

	private D data;
	private RuntimeException exception;
	
	public SimpleData(D data) {
		this.data = data;
		this.exception = null;
	}
	
	public SimpleData(RuntimeException exception) {
		this.data = null;
		this.exception = exception;
	}
	
	@Override
	public boolean available() {
		return true;
	}

	@Override
	public D get() {
		if(exception != null) {
			throw exception;
		}
		return data;
	}
}
