/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *  	ScanAuthzDAO interface.
 *     
 */
package ca.sciencestudio.model.session.dao;

import java.io.InputStream;
import java.util.List;

import ca.sciencestudio.model.session.Scan;
import ca.sciencestudio.model.dao.Data;
import ca.sciencestudio.model.dao.ModelAuthzDAO;

/**
 * @author maxweld
 *
 */
public interface ScanAuthzDAO extends ModelAuthzDAO<Scan> {
	
	public Data<List<Scan>> getAllByExperimentGid(String user, String experimentGid);
	
	public Data<InputStream> getData(String user, String gid, String path);
}
