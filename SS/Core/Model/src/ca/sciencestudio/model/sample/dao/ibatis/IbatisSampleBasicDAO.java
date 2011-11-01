/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     IbatisSampleBasicDAO class.
 *     
 */
package ca.sciencestudio.model.sample.dao.ibatis;

import java.util.Set;
import java.util.HashSet;

import ca.sciencestudio.model.dao.ibatis.AbstractIbatisModelBasicDAO;
import ca.sciencestudio.model.project.Project;
import ca.sciencestudio.model.sample.Sample;
import ca.sciencestudio.model.sample.dao.SampleBasicDAO;
import ca.sciencestudio.model.sample.dao.ibatis.support.IbatisSample;
import ca.sciencestudio.model.utilities.GID;

/**
 * @author maxweld
 *  
 */
public class IbatisSampleBasicDAO extends AbstractIbatisModelBasicDAO<Sample, IbatisSample> implements SampleBasicDAO {
	
	private static final String HAZARDS_SEPARATOR_REGEX = "\\s+";
	private static final String HAZARDS_SEPARATOR_STRING = " ";
	
	
	@Override
	public String getGidType() {
		return Sample.GID_TYPE;
	}
	
//	@SuppressWarnings("unchecked")
//	public List<Sample> getSampleListByProjectId(int projectId) {
//		List<Sample> samples = getSqlMapClientTemplate().queryForList("getSampleListByProjectId", projectId);
//		logger.debug("Get sample list with project id: " + projectId + ", size: " + samples.size());
//		return samples;
//	}
	
	@Override
	protected IbatisSample toIbatisModel(Sample sample) {
		if(sample == null) {
			return null;
		}
		IbatisSample ibatisSample = new IbatisSample();
		GID gid = GID.parse(sample.getGid());
		if((gid != null) && gid.isFacilityAndType(getGidFacility(), getGidType(), true, true)) {
			ibatisSample.setId(gid.getId());
		}
		GID projectGid = GID.parse(sample.getProjectGid());
		if((projectGid != null) && projectGid.isFacilityAndType(getGidFacility(), Project.GID_TYPE, true, true)) {
			ibatisSample.setProjectId(projectGid.getId());
		}
		ibatisSample.setName(sample.getName());
		ibatisSample.setDescription(sample.getDescription());
		ibatisSample.setCasNumber(sample.getCasNumber());
		ibatisSample.setState(sample.getState());
		ibatisSample.setQuantity(sample.getQuantity());
		StringBuffer hazards = new StringBuffer();
		if(sample.getHazards() != null) {
			for(String hazard : sample.getHazards()) {
				if(hazards.length() != 0) {
					hazards.append(HAZARDS_SEPARATOR_STRING);
				}
				hazards.append(hazard);
			}
		}
		ibatisSample.setHazards(hazards.toString());
		ibatisSample.setOtherHazards(sample.getOtherHazards());
		return ibatisSample;
	}
	
	@Override
	protected Sample toModel(IbatisSample ibatisSample) {
		if(ibatisSample == null) {
			return null;
		}
		Sample sample = new Sample();
		sample.setGid(GID.format(getGidFacility(), ibatisSample.getId(), getGidType()));
		sample.setProjectGid(GID.format(getGidFacility(), ibatisSample.getProjectId(), Project.GID_TYPE));
		sample.setName(ibatisSample.getName());
		sample.setDescription(ibatisSample.getDescription());
		sample.setCasNumber(ibatisSample.getCasNumber());
		sample.setState(ibatisSample.getState());
		sample.setQuantity(ibatisSample.getQuantity());
		Set<String> hazards = new HashSet<String>();
		if((ibatisSample.getHazards() != null) && (ibatisSample.getHazards().length() > 0)) {			
			for(String hazard : ibatisSample.getHazards().split(HAZARDS_SEPARATOR_REGEX)) {
				if(hazard.length() > 0) {
					hazards.add(hazard);
				}
			}
		}
		sample.setHazards(hazards);
		sample.setOtherHazards(ibatisSample.getOtherHazards());
		return sample;
	}

	@Override
	protected String getStatementName(String prefix, String suffix) {
		return prefix + "Sample" + suffix;
	}
}
