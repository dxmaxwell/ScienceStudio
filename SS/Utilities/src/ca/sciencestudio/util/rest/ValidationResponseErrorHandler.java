/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     ValidationResponseErrorHandler class.
 *     
 */
package ca.sciencestudio.util.rest;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.DefaultResponseErrorHandler;

/**
 * @author maxweld
 * 
 *
 */
public class ValidationResponseErrorHandler extends DefaultResponseErrorHandler {

	@Override
	protected boolean hasError(HttpStatus statusCode) {		
		if(statusCode == HttpStatus.UNPROCESSABLE_ENTITY) {
			return false;
		}	
		return super.hasError(statusCode);
	}	
}
