/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     DelegatingScanDAO abstract class.
 *     
 */
package ca.sciencestudio.model.dao.delegating;

import java.util.Collection;

/**
 * @author maxweld
 *
 *
 */
import ca.sciencestudio.model.Scan;
import ca.sciencestudio.model.dao.ScanDAO;
import ca.sciencestudio.model.dao.delegating.support.AbstractDelegratingModelDAO;

public class DelegatingScanDAO extends AbstractDelegratingModelDAO<Scan, ScanDAO> implements ScanDAO {

	public Collection<ScanDAO> getScanDAOs() {
		return getModelDAOs();
	}
	public void setScanDAOs(Collection<ScanDAO> scanDAOs) {
		setModelDAOs(scanDAOs);
	}
}
