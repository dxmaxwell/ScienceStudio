/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     IbatisScanDAO class.
 *     
 */
package ca.sciencestudio.model.session.dao.ibatis;

import java.util.List;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import ca.sciencestudio.model.session.Scan;
import ca.sciencestudio.model.session.dao.ScanDAO;
import ca.sciencestudio.model.session.ibatis.IbatisScan;

/**
 * @author medrand
 *
 */
public class IbatisScanDAO extends SqlMapClientDaoSupport implements ScanDAO {

	@Override
	public Scan createScan() {
		return new IbatisScan();
	}
	
	public int addScan(Scan scan) {
		int id = (Integer) getSqlMapClientTemplate().insert("addScan", scan);
		return id;
	}

	public void editScan(Scan scan) {
		getSqlMapClientTemplate().update("editScan", scan);
	}
	
	public void editScanInfo(Scan scan) {
		getSqlMapClientTemplate().update("editScanInfo", scan);
	}

	public Scan getScanById(int scanId) {
		Scan s = (Scan) getSqlMapClientTemplate().queryForObject("getScanById", scanId);
		return s;
	}

	@SuppressWarnings("unchecked")
	public List<Scan> getScanList() {
		List<Scan> list = getSqlMapClientTemplate().queryForList("getScanList");
		return list;
	}

	@SuppressWarnings("unchecked")
	public List<Scan> getScanListByExperimentId(int experimentId) {
		List<Scan> list = getSqlMapClientTemplate().queryForList("getScanListByExperimentId", experimentId);
		return list;
	}

	public void removeScan(int scanId) {
		getSqlMapClientTemplate().delete("removeScan", scanId);
	}

}
