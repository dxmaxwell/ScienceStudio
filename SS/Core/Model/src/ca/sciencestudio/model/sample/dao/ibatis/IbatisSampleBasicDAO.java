/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     IbatisSampleBasicDAO class.
 *     
 */
package ca.sciencestudio.model.sample.dao.ibatis;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

import org.springframework.dao.DataAccessException;

import ca.sciencestudio.model.dao.ibatis.AbstractIbatisModelBasicDAO;
import ca.sciencestudio.model.project.Project;
import ca.sciencestudio.model.sample.Sample;
import ca.sciencestudio.model.sample.Sample.Hazard;
import ca.sciencestudio.model.sample.Sample.State;
import ca.sciencestudio.model.sample.dao.SampleBasicDAO;
import ca.sciencestudio.model.sample.dao.ibatis.support.IbatisSample;
import ca.sciencestudio.model.utilities.GID;
import ca.sciencestudio.util.exceptions.ModelAccessException;

/**
 * @author maxweld
 *  
 */
public class IbatisSampleBasicDAO extends AbstractIbatisModelBasicDAO<Sample> implements SampleBasicDAO {
	
	private static final String HAZARDS_SEPARATOR_REGEX = "\\s+";
	private static final String HAZARDS_SEPARATOR_STRING = " ";
	
	
	@Override
	public String getGidType() {
		return Sample.GID_TYPE;
	}
	
	@Override
	public List<Sample> getAllByProjectGid(String projectGid) {
		GID gid = parseAndCheckGid(projectGid, getGidFacility(), Project.GID_TYPE);
		if(gid == null) {
			return Collections.emptyList();
		}
		
		List<Sample> samples;
		try {
			samples = toModelList(getSqlMapClientTemplate().queryForList(getStatementName("get", "ListByProjectId"), gid.getId()));
		}
		catch(DataAccessException e) {
			logger.warn("Data Access exception while getting Model list: " + e.getMessage());
			throw new ModelAccessException(e);
		}
		
		if(logger.isDebugEnabled()) {
			logger.debug("Get all Samples with Project GID: " + projectGid + ", size: " + samples.size());
		}
		return Collections.unmodifiableList(samples);
	}
	
	@Override
	public List<Sample> getAllByProjectMember(String personGid) {
		List<Sample> samples;
		try {
			samples = toModelList(getSqlMapClientTemplate().queryForList(getStatementName("get", "ListByProjectMember"), personGid));
		}
		catch(DataAccessException e) {
			logger.warn("Data Access exception while getting Model list: " + e.getMessage());
			throw new ModelAccessException(e);
		}
		
		if(logger.isDebugEnabled()) {
			logger.debug("Get all Samples by Project Member: " + personGid + ", size: " + samples.size());
		}
		return Collections.unmodifiableList(samples);
	}
	
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
		ibatisSample.setState(sample.getState().name());
		ibatisSample.setQuantity(sample.getQuantity());
		StringBuffer hazards = new StringBuffer();
		if(sample.getHazards() != null) {
			for(Hazard hazard : sample.getHazards()) {
				if(hazards.length() != 0) {
					hazards.append(HAZARDS_SEPARATOR_STRING);
				}
				hazards.append(hazard.name());
			}
		}
		ibatisSample.setHazards(hazards.toString());
		ibatisSample.setOtherHazards(sample.getOtherHazards());
		return ibatisSample;
	}
	
	@Override
	protected Sample toModel(Object obj) {
		if(!(obj instanceof IbatisSample)) {
			return null;
		}
		IbatisSample ibatisSample = (IbatisSample)obj;
		Sample sample = new Sample();
		sample.setGid(GID.format(getGidFacility(), ibatisSample.getId(), getGidType()));
		sample.setProjectGid(GID.format(getGidFacility(), ibatisSample.getProjectId(), Project.GID_TYPE));
		sample.setName(ibatisSample.getName());
		sample.setDescription(ibatisSample.getDescription());
		sample.setCasNumber(ibatisSample.getCasNumber());
		sample.setState(State.valueOf(ibatisSample.getState()));
		sample.setQuantity(ibatisSample.getQuantity());
		Set<Hazard> hazards = new HashSet<Hazard>();
		if((ibatisSample.getHazards() != null) && (ibatisSample.getHazards().length() > 0)) {			
			for(String hazard : ibatisSample.getHazards().split(HAZARDS_SEPARATOR_REGEX)) {
				if(hazard.length() > 0) {
					hazards.add(Hazard.valueOf(hazard));
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
