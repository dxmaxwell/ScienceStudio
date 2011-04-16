/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     IbatisSampleDAO class.
 *     
 */

package ca.sciencestudio.model.sample.dao.ibatis;

import java.util.List;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import ca.sciencestudio.model.sample.Sample;
import ca.sciencestudio.model.sample.dao.SampleDAO;
import ca.sciencestudio.model.sample.ibatis.IbatisSample;

/**
 * @author maxweld
 *  
 */
public class IbatisSampleDAO extends SqlMapClientDaoSupport implements SampleDAO {
	
	public Sample createSample() {
		return new IbatisSample();
	}
	
	public int addSample(Sample sample){
		int key = (Integer) getSqlMapClientTemplate().insert("addSample", sample);
		logger.info("Added new sample with id: " + key);
		return key;
	}
	
	public void editSample(Sample sample){
		int count = getSqlMapClientTemplate().update("editSample", sample);
		logger.info("Edit sample with id: " + sample.getId() + ", rows affected: " + count);
	}
	
	public void removeSample(int sampleId){
		int count = getSqlMapClientTemplate().delete("removeSample", sampleId);
		logger.info("Remove sample with id: " + sampleId + ", rows affected: " + count);
	}
	
	public Sample getSampleById(int sampleId){
		Sample sample = (Sample) getSqlMapClientTemplate().queryForObject("getSampleById", sampleId);
		logger.debug("Get sample with id: " + sampleId);
		return sample;
	}
	
	@SuppressWarnings("unchecked")
	public List<Sample> getSampleListByProjectId(int projectId) {
		List<Sample> samples = getSqlMapClientTemplate().queryForList("getSampleListByProjectId", projectId);
		logger.debug("Get sample list with project id: " + projectId + ", size: " + samples.size());
		return samples;
	}
}
