/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     DelegatingScanDAO abstract class.
 *     
 */
package ca.sciencestudio.model.session.dao.delegating;

import java.util.Collection;

/**
 * @author maxweld
 *
 *
 */
import ca.sciencestudio.model.session.Scan;
import ca.sciencestudio.model.session.dao.ScanAuthzDAO;
import ca.sciencestudio.model.dao.delegating.AbstractDelegratingModelAuthzDAO;

public class DelegatingScanAuthzDAO extends AbstractDelegratingModelAuthzDAO<Scan, ScanAuthzDAO> implements ScanAuthzDAO {

	public Collection<ScanAuthzDAO> getScanDAOs() {
		return getModelDAOs();
	}
	public void setScanDAOs(Collection<ScanAuthzDAO> scanDAOs) {
		setModelDAOs(scanDAOs);
	}
}
