/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     IbatisScanDAO class.
 *     
 */
package ca.sciencestudio.model.session.dao.ibatis;

import ca.sciencestudio.model.session.Experiment;
import ca.sciencestudio.model.session.Scan;
import ca.sciencestudio.model.session.dao.ScanBasicDAO;
import ca.sciencestudio.model.session.dao.ibatis.support.IbatisScan;
import ca.sciencestudio.model.dao.ibatis.AbstractIbatisModelBasicDAO;
import ca.sciencestudio.model.utilities.GID;

/**
 * @author maxweld
 *
 */
public class IbatisScanBasicDAO extends AbstractIbatisModelBasicDAO<Scan, IbatisScan> implements ScanBasicDAO {

	@Override
	public String getGidType() {
		return Scan.GID_TYPE;
	}
	
	//@SuppressWarnings("unchecked")
	//public List<Scan> getScanListByExperimentId(int experimentId) {
	//	List<Scan> list = getSqlMapClientTemplate().queryForList("getScanListByExperimentId", experimentId);
	//	return list;
	//}
	
	@Override
	protected IbatisScan toIbatisModel(Scan scan) {
		if(scan == null) {
			return null;
		}
		IbatisScan ibatisScan = new IbatisScan();
		GID gid = GID.parse(scan.getGid());
		if((gid != null) && gid.isFacilityAndType(getGidFacility(), getGidType(), true, true)) {
			ibatisScan.setId(gid.getId());
		}
		GID experimentGid = GID.parse(scan.getExperimentGid());
		if((experimentGid != null) && experimentGid.isFacilityAndType(getGidFacility(), getGidType(), true, true)) {
			ibatisScan.setExperimentId(experimentGid.getId());
		}
		ibatisScan.setName(scan.getName());
		ibatisScan.setDataUrl(scan.getDataUrl());
		ibatisScan.setParameters(scan.getParameters());
		ibatisScan.setStartDate(scan.getStartDate());
		ibatisScan.setEndDate(scan.getEndDate());
		return ibatisScan;
	}
	
	@Override
	protected Scan toModel(IbatisScan ibatisScan) {
		if(ibatisScan == null) {
			return null;
		}
		Scan scan = new Scan();
		scan.setGid(GID.format(getGidFacility(), ibatisScan.getId(), getGidType()));
		scan.setExperimentGid(GID.format(getGidFacility(), ibatisScan.getExperimentId(), Experiment.GID_TYPE));
		scan.setName(ibatisScan.getName());
		scan.setDataUrl(ibatisScan.getDataUrl());
		scan.setParameters(ibatisScan.getParameters());
		scan.setStartDate(ibatisScan.getStartDate());
		scan.setEndDate(ibatisScan.getEndDate());
		return scan;
	}

	@Override
	protected String getStatementName(String prefix, String suffix) {
		return prefix + "Scan" + suffix;
	}	
}
