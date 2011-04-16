/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     AbstractModelController class.
 *     
 */
package ca.sciencestudio.service.controllers;

import javax.servlet.http.HttpServletRequest;

/**
 * @author maxweld
 *
 */
public abstract class AbstractModelController {

	public String getModelPath(HttpServletRequest request) {
		return request.getContextPath() + request.getServletPath();
	}
}
