/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     Experiment interface.
 *     
 */
package ca.sciencestudio.model.session;

import java.io.Serializable;

/**
 * @author maxweld
 *
 */
public interface Experiment extends  Serializable {
	
	public int getId();
	public void setId(int id);
	
	public String getName();
	public void setName(String name);
	
	public String getDescription();
	public void setDescription(String description);
	
	public int getSessionId();
	public void setSessionId(int sessionId);
	
	public int getSampleId();
	public void setSampleId(int sampleId);
	
	public int getInstrumentTechniqueId();
	public void setInstrumentTechniqueId(int instrumentTechniqueId);
	
	public Experiment clone();
}
