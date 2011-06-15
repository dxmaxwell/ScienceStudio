/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     AbstractModelDAO abstract class.
 *     
 */
package ca.sciencestudio.model.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ca.sciencestudio.model.Model;

/**
 * @author maxweld
 *
 *
 */
public abstract class AbstractModelDAO<T extends Model> implements ModelDAO<T> {
		
	protected Log logger = LogFactory.getLog(getClass());
}
