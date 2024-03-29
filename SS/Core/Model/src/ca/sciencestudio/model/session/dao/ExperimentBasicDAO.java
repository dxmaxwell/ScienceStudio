/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     ExperimentBasicDAO interface.
 *     
 */
package ca.sciencestudio.model.session.dao;

import java.util.List;

import ca.sciencestudio.model.dao.ModelBasicDAO;
import ca.sciencestudio.model.session.Experiment;

/**
 * @author maxweld
 *
 */
public interface ExperimentBasicDAO extends ModelBasicDAO<Experiment> {
	
	public List<Experiment> getAllBySessionGid(String sessionGid);
	public List<Experiment> getAllBySourceGid(String sourceGid);
}
