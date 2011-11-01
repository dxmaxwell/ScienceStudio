/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     SampleBasicDAO interface.
 *     
 */
package ca.sciencestudio.model.sample.dao;

import ca.sciencestudio.model.dao.ModelBasicDAO;
import ca.sciencestudio.model.sample.Sample;

/**
 * @author maxweld
 * 
 */
public interface SampleBasicDAO extends ModelBasicDAO<Sample> {
	
	//public List<Sample> getAllByProjectGid(Object projectGid);
}
