/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     IbatisExperimentDAO class.
 *     
 */
package ca.sciencestudio.model.session.dao.ibatis;

import java.util.List;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import ca.sciencestudio.model.session.Experiment;
import ca.sciencestudio.model.session.dao.ExperimentDAO;
import ca.sciencestudio.model.session.ibatis.IbatisExperiment;

/**
 * @author medrand
 *
 */
public class IbatisExperimentDAO extends SqlMapClientDaoSupport implements ExperimentDAO {

	public Experiment createExperiment() {
		return new IbatisExperiment();
	}
	
	public int addExperiment(Experiment experiment) {
		int key = (Integer) getSqlMapClientTemplate().insert("addExperiment", experiment);
		logger.debug("Added new Experiment with key: " + key);
		return key;
	}

	public void editExperiment(Experiment experiment) {
		int count = getSqlMapClientTemplate().update("editExperiment", experiment);
		logger.debug("Edit Experiment with id: " + experiment.getId() + ", rows affected: " + count);
	}
	
	public void removeExperiment(int experimentId) {
		int count = getSqlMapClientTemplate().delete("removeExperiment", experimentId);
		logger.debug("Remove Experiment with id: " + experimentId + ", rows affected: " + count);
	}

	public Experiment getExperimentById(int experimentId) {
		Experiment experiment = (Experiment) getSqlMapClientTemplate().queryForObject("getExperimentById", experimentId);
		logger.debug("Get Experiment with id: " + experimentId);
		return experiment;
	}

	@SuppressWarnings("unchecked")
	public List<Experiment> getExperimentList() {
		List<Experiment> list = getSqlMapClientTemplate().queryForList("getExperimentList");
		logger.debug("Get Experiment list, size: " + list.size());
		return list;
	}

	@SuppressWarnings("unchecked")
	public List<Experiment> getExperimentListBySampleId(int sampleId) {
		List<Experiment> list = getSqlMapClientTemplate().queryForList("getExperimentListBySampleId", sampleId);
		logger.debug("Get Experiment list by sampleId: " + sampleId + ", size: " + list.size());
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public List<Experiment> getExperimentListBySessionId(int sessionId) {
		List<Experiment> list = getSqlMapClientTemplate().queryForList("getExperimentListBySessionId", sessionId);
		logger.debug("Get Experiment list by sessionId: " + sessionId + ", size: " + list.size());
		return list;
	}
}
