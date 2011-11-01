/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     SampleAuthzDAO interface.
 *     
 */
package ca.sciencestudio.model.sample.dao;

import ca.sciencestudio.model.dao.ModelAuthzDAO;
import ca.sciencestudio.model.sample.Sample;

/**
 * @author maxweld
 * 
 */
public interface SampleAuthzDAO extends ModelAuthzDAO<Sample> {
	
	//public List<Sample> getAllByProjectGid(Object projectGid);
}
