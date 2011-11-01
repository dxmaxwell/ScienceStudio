/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     ConversionServiceHolderConfigBean class.
 *
 */
package ca.sciencestudio.util.bean;

import org.springframework.core.convert.ConversionService;

import ca.sciencestudio.util.convert.ConversionServiceHolder;

/**
 * @author maxweld
 * 
 *
 */
public class ConversionServiceHolderConfigBean {

	public void setConversionService(ConversionService conversionService) {
		ConversionServiceHolder.setConversionService(conversionService);
	}
}
