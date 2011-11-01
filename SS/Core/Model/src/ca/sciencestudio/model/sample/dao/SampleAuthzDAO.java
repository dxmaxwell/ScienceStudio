/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     SampleAuthzDAO interface.
 *     
 */
package ca.sciencestudio.model.sample.dao;

import java.util.List;

import ca.sciencestudio.model.sample.Sample;
import ca.sciencestudio.model.dao.Data;
import ca.sciencestudio.model.dao.ModelAuthzDAO;

/**
 * @author maxweld
 * 
 */
public interface SampleAuthzDAO extends ModelAuthzDAO<Sample> {
	
	public Data<List<Sample>> getAllByProjectGid(String user, String projectGid);
}
