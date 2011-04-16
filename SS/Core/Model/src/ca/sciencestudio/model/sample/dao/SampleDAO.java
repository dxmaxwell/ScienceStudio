/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     SampleDAO interface.
 *     
 */

package ca.sciencestudio.model.sample.dao;

import java.util.List;

import ca.sciencestudio.model.sample.Sample;

/**
 * @author maxweld
 * 
 */
public interface SampleDAO {
	public Sample createSample();
	
	public int addSample(Sample sample);
	public void editSample(Sample sample);
	public void removeSample(int sampleId);
	
	public Sample getSampleById(int sampleId);
	
	public List<Sample> getSampleListByProjectId(int projectId);
}
