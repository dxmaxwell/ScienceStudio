/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     DelegatingSampleDAO class.
 *     
 */
package ca.sciencestudio.model.sample.dao.delegating;

import java.util.Collection;

import ca.sciencestudio.model.dao.delegating.AbstractDelegratingModelAuthzDAO;
import ca.sciencestudio.model.sample.Sample;
import ca.sciencestudio.model.sample.dao.SampleAuthzDAO;

/**
 * @author maxweld
 *
 */
public class DelegatingSampleAuthzDAO extends AbstractDelegratingModelAuthzDAO<Sample, SampleAuthzDAO> {

	public Collection<SampleAuthzDAO> getSampleDAOs() {
		return getModelDAOs();
	}
	public void setSampleDAOs(Collection<SampleAuthzDAO> sampleDAOs) {
		setModelDAOs(sampleDAOs);
	}
}
