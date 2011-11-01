/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     HttpMessageConvertersHolder class.
 *
 */
package ca.sciencestudio.util.http;

import java.util.Collection;
import java.util.Collections;

import org.springframework.http.converter.HttpMessageConverter;

/**
 *
 * @author maxweld
 *
 */
public class HttpMessageConvertersHolder {

	public static Collection<HttpMessageConverter<Object>> messageConverters = Collections.emptyList();

	public static Collection<HttpMessageConverter<Object>> getMessageConverters() {
		return messageConverters;
	}
	public static void setMessageConverters(Collection<HttpMessageConverter<Object>> messageConverters) {
		HttpMessageConvertersHolder.messageConverters = messageConverters;
	}
}
