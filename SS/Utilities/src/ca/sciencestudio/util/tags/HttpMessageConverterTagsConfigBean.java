/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     HttpMessageConverterTagsConfigBean class.
 *     
 */
package ca.sciencestudio.util.tags;

import java.util.Collection;

import org.springframework.http.converter.HttpMessageConverter;

import ca.sciencestudio.util.tags.HttpMessageConverterWriteTag;

/**
 * @author maxweld
 *
 */
public class HttpMessageConverterTagsConfigBean {

	public void setDefaultType(String defaultType) {
		HttpMessageConverterWriteTag.setDefaultType(defaultType);
	}

	public void setMessageConverters(Collection<HttpMessageConverter<Object>> messageConverters) {
		HttpMessageConverterWriteTag.setMessageConverters(messageConverters);
	}
}
