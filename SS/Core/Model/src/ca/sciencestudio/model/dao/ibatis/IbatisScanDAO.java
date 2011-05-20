/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     IbatisScanDAO class.
 *     
 */
package ca.sciencestudio.model.dao.ibatis;

import ca.sciencestudio.model.Scan;
import ca.sciencestudio.model.dao.ScanDAO;
import ca.sciencestudio.model.dao.ibatis.support.IbatisScan;
import ca.sciencestudio.model.dao.ibatis.support.AbstractIbatisModelDAO;
import ca.sciencestudio.model.utilities.GID;

/**
 * @author maxweld
 *
 */
public class IbatisScanDAO extends AbstractIbatisModelDAO<Scan, IbatisScan> implements ScanDAO {

	@Override
	public String getType() {
		return Scan.GID_TYPE;
	}
	
	@Override
	protected IbatisScan toIbatisModel(Scan scan) {
		IbatisScan ibatisScan = new IbatisScan();
		ibatisScan.setId(GID.parse(scan.getGid()).getId());
		ibatisScan.setName(scan.getName());
		ibatisScan.setDataUrl(scan.getDataUrl());
		ibatisScan.setParameters(scan.getParameters());
		ibatisScan.setStartDate(scan.getStartDate());
		ibatisScan.setEndDate(scan.getEndDate());
		ibatisScan.setExperimentId(scan.getExperimentId());
		return ibatisScan;
	}
	
	@Override
	protected Scan toModel(IbatisScan ibatisScan) {
		if(ibatisScan == null) {
			return null;
		}
		Scan scan = new Scan();
		scan.setGid(GID.format(getFacility(), ibatisScan.getId(), getType()));
		scan.setName(ibatisScan.getName());
		scan.setDataUrl(ibatisScan.getDataUrl());
		scan.setParameters(ibatisScan.getParameters());
		scan.setStartDate(ibatisScan.getStartDate());
		scan.setEndDate(ibatisScan.getEndDate());
		scan.setExperimentId(ibatisScan.getExperimentId());
		return scan;
	}

	@Override
	protected String getStatementName(String prefix, String suffix) {
		return prefix + "Scan" + suffix;
	}	
}
