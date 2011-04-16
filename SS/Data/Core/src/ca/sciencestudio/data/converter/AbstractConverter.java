/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     AbstractConverter class.
 *     
 */
package ca.sciencestudio.data.converter;

import ca.sciencestudio.util.CompareUtils;
import ca.sciencestudio.data.converter.Converter;

/**
 * @author maxweld
 *
 */
public abstract class AbstractConverter extends CompareUtils implements Converter {
	
	private ConverterMap response;
	
	public AbstractConverter(String fromFormat, String toFormat, boolean forceUpdate) {
		response = new LinkedHashConverterMap(fromFormat, toFormat, forceUpdate);
	}
	
	public String getToFormat() {
		return response.getToFormat();
	}
	
	public String getFromFormat() {
		return response.getFromFormat();
	}
	
	public boolean isForceUpdate() {
		return response.isForceUpdate();
	}
	
	protected ConverterMap getResponse() {
		return response;
	}
}
