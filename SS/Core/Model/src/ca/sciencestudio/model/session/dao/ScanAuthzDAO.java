/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *  	ScanAuthzDAO interface.
 *     
 */
package ca.sciencestudio.model.session.dao;

import ca.sciencestudio.model.session.Scan;
import ca.sciencestudio.model.dao.ModelAuthzDAO;

/**
 * @author maxweld
 *
 */
public interface ScanAuthzDAO extends ModelAuthzDAO<Scan> {
	
	//public List<Scan> getScanListByExperimentId(int experimentId);
}
