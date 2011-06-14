/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     GeneralResponseConfigBean class.
 *     
 */
package ca.sciencestudio.util.web;

import org.springframework.context.MessageSource;

/**
 * @author maxweld
 *
 */
public class GeneralResponseConfigBean {

	public void setMessageSource(MessageSource messageSource) {
		GeneralResponse.setMessageSource(messageSource);
	}
}
