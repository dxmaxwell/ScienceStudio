/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     IbatisExperiment class.
 *     
 */
package ca.sciencestudio.model.session.ibatis;

import ca.sciencestudio.model.session.Experiment;

/**
 * @author maxweld
 *
 */
public class IbatisExperiment implements Cloneable, Experiment {

private static final long serialVersionUID = 1L;
	
	private int id;
	private String name;
	private String description;
	private int sessionId;
	private int sampleId;
	private int instrumentTechniqueId;
	
	@Override
	public int getId() {
		return id;
	}
	@Override
	public void setId(int id) {
		this.id = id;
	}
	
	@Override
	public String getName() {
		return name;
	}
	@Override
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String getDescription() {
		return description;
	}
	@Override
	public void setDescription(String description) {
		this.description = description;
	}
	
	@Override
	public int getSessionId() {
		return sessionId;
	}
	@Override
	public void setSessionId(int sessionId) {
		this.sessionId = sessionId;
	}
	
	@Override
	public int getSampleId() {
		return sampleId;
	}
	@Override
	public void setSampleId(int sampleId) {
		this.sampleId = sampleId;
	}
	
	@Override
	public int getInstrumentTechniqueId() {
		return instrumentTechniqueId;
	}
	@Override
	public void setInstrumentTechniqueId(int instrumentTechniqueId) {
		this.instrumentTechniqueId = instrumentTechniqueId;
	}
		
	@Override
	public Experiment clone() {
		try {
			return (Experiment) super.clone();
		}
		catch (CloneNotSupportedException e) {	
			return null;
		}
	}
}
