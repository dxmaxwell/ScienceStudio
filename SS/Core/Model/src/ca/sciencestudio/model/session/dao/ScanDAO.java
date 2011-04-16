/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *  	ScanDAO interface.
 *     
 */
package ca.sciencestudio.model.session.dao;

import java.util.List;

import ca.sciencestudio.model.session.Scan;

/**
 * @author maxweld
 *
 */
public interface ScanDAO {
	public Scan createScan();
	
	public int addScan(Scan scan);
	public void editScan(Scan scan);
	public void editScanInfo(Scan scan);
	public void removeScan(int scanId);
	
	public Scan getScanById(int scanId);
	
	public List<Scan> getScanListByExperimentId(int experimentId);
	
	public List<Scan> getScanList();
}
