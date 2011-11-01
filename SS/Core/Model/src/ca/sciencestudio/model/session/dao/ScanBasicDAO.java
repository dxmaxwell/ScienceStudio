/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *  	ScanBasicDAO interface.
 *     
 */
package ca.sciencestudio.model.session.dao;

import java.util.List;

import ca.sciencestudio.model.session.Scan;
import ca.sciencestudio.model.dao.ModelBasicDAO;

/**
 * @author maxweld
 *
 */
public interface ScanBasicDAO extends ModelBasicDAO<Scan> {
	
	public List<Scan> getAllByExperimentGid(String experimentGid);
}
