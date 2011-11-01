/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     ExperimentAuthzDAO interface.
 *     
 */
package ca.sciencestudio.model.session.dao;

import java.util.List;

import ca.sciencestudio.model.dao.Data;
import ca.sciencestudio.model.dao.ModelAuthzDAO;
import ca.sciencestudio.model.session.Experiment;

/**
 * @author maxweld
 *
 */
public interface ExperimentAuthzDAO extends ModelAuthzDAO<Experiment> {
	
	public Data<List<Experiment>> getAllBySessionGid(String user, String sessionGid);
}
