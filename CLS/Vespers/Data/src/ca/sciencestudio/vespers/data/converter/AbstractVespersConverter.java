/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     AbstractVespersConverter class.
 *     
 */
package ca.sciencestudio.vespers.data.converter;

import ca.sciencestudio.data.converter.AbstractModelConverter;

/**
 * @author maxweld
 *
 */
public abstract class AbstractVespersConverter extends AbstractModelConverter {

	public AbstractVespersConverter(String fromFormat, String toFormat, boolean forceUpdate) {
		super(fromFormat, toFormat, forceUpdate);
	}
	
	// Reserved for future enhancements. //
}
