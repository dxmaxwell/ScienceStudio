/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     SampleBasicDAO interface.
 *     
 */
package ca.sciencestudio.model.sample.dao;

import java.util.List;

import ca.sciencestudio.model.sample.Sample;
import ca.sciencestudio.model.dao.ModelBasicDAO;

/**
 * @author maxweld
 * 
 */
public interface SampleBasicDAO extends ModelBasicDAO<Sample> {
	
	public List<Sample> getAllByProjectGid(String projectGid);
	public List<Sample> getAllByProjectMember(String personGid);
}
