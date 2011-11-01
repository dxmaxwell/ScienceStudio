/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *  	ScanBasicDAO interface.
 *     
 */
package ca.sciencestudio.model.session.dao;

import ca.sciencestudio.model.session.Scan;
import ca.sciencestudio.model.dao.ModelBasicDAO;

/**
 * @author maxweld
 *
 */
public interface ScanBasicDAO extends ModelBasicDAO<Scan> {
	
	//public List<Scan> getScanListByExperimentId(int experimentId);
}
