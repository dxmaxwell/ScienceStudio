/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     FormResponseMapConfigBean class.
 *     
 */
package ca.sciencestudio.util.web;

import org.springframework.context.MessageSource;

/**
 * @author maxweld
 *
 */
public class FormResponseMapConfigBean {

	public void setMessageSource(MessageSource messageSource) {
		FormResponseMap.setMessageSource(messageSource);
	}
}
