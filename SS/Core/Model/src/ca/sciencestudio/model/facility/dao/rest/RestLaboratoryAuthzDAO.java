/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     RestLaboratoryAuthzDAO interface.
 *     
 */
package ca.sciencestudio.model.facility.dao.rest;

import ca.sciencestudio.model.dao.rest.AbstractRestModelAuthzDAO;
import ca.sciencestudio.model.facility.Laboratory;
import ca.sciencestudio.model.facility.dao.LaboratoryAuthzDAO;

/**
 * @author maxweld
 * 
 *
 */
public class RestLaboratoryAuthzDAO extends AbstractRestModelAuthzDAO<Laboratory> implements LaboratoryAuthzDAO {

	public static final String LABORATORY_MODEL_PATH = "/laboratories";

	@Override
	protected Object toRestModel(Laboratory t) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getModelPath() {
		return LABORATORY_MODEL_PATH;
	}

	@Override
	protected Class<Laboratory> getModelClass() {
		return Laboratory.class;
	}

	@Override
	protected Class<Laboratory[]> getModelArrayClass() {
		return Laboratory[].class;
	}
}
