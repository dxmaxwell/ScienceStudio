/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     AbstractMapXYVespersConverter class.
 *     
 */
package ca.sciencestudio.vespers.data.converter;

public abstract class AbstractMapXYVespersConverter extends AbstractVespersConverter {

	public AbstractMapXYVespersConverter(String fromFormat, String toFormat, boolean forceUpdate) {
		super(fromFormat, toFormat, forceUpdate);
	}
}
