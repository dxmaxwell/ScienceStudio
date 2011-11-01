/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     ConversionServiceHolder class.
 *
 */
package ca.sciencestudio.util.convert;

import org.springframework.core.convert.ConversionService;

/**
 * @author maxweld
 * 
 *
 */
public abstract class ConversionServiceHolder {

	public static ConversionService conversionService;

	public static ConversionService getConversionService() {
		return conversionService;
	}
	public static void setConversionService(ConversionService conversionService) {
		ConversionServiceHolder.conversionService = conversionService;
	}
}
