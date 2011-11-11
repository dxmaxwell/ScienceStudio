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
import ca.sciencestudio.util.rest.FileProps;

/**
 * @author maxweld
 *
 */
public interface ScanAuthzDAO extends ModelAuthzDAO<Scan> {
	
	public Data<List<Scan>> getAllByExperimentGid(String user, String experimentGid);
	
	public Data<InputStream> getFileData(String user, String gid, String path);
	public Data<List<FileProps>> getFileList(String user, String gid, String path);
	public Data<List<FileProps>> getFileList(String user, String gid, String path, String type);
	public Data<List<FileProps>> getFileList(String user, String gid, String path, String type, int depth);
}
