/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     HttpMessageConverterHolderConfigBean class.
 *
 */
package ca.sciencestudio.util.bean;

import java.util.Collection;

import org.springframework.http.converter.HttpMessageConverter;

import ca.sciencestudio.util.http.HttpMessageConvertersHolder;

/**
 * @author maxweld
 * 
 *
 */
public class HttpMessageConvertersHolderConfigBean {

	public void setMessageConverters(Collection<HttpMessageConverter<Object>> messageConverters) {
		HttpMessageConvertersHolder.setMessageConverters(messageConverters);
	}
}
