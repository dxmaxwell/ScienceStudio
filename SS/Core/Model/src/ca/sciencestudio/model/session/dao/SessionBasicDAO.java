/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     SessionBasicDAO interface.
 *     
 */
package ca.sciencestudio.model.session.dao;

import java.util.List;

import ca.sciencestudio.model.dao.ModelBasicDAO;
import ca.sciencestudio.model.session.Session;

/**
 * @author maxweld
 *
 */
public interface SessionBasicDAO extends ModelBasicDAO<Session> {
	
	public List<Session> getAllByPersonGid(String personGid);	
	public List<Session> getAllByProjectGid(String projectGid);
	//public List<Session> getAllByLaboratoryGid(String laboratoryGid);
	//public List<Session> getAllByLaboratoryNameAndFacilityName(String laboratoryName, String facilityName);
}
