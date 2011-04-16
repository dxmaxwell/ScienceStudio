/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     ExperimentDAO interface.
 *     
 */
package ca.sciencestudio.model.session.dao;

import java.util.List;

import ca.sciencestudio.model.session.Experiment;

/**
 * @author medrand
 *
 */
public interface ExperimentDAO {
	public Experiment createExperiment();
	
	public int addExperiment(Experiment experiment);
	public void editExperiment(Experiment experiment);
	public void removeExperiment(int experimentId);
	
	public Experiment getExperimentById(int experimentId);
	
	public List<Experiment> getExperimentListBySessionId(int sessionId);
	public List<Experiment> getExperimentListBySampleId(int sampleId);
	public List<Experiment> getExperimentList();
}
