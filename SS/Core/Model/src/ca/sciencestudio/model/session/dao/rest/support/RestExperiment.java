/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     RestExperiment class.
 *     
 */
package ca.sciencestudio.model.session.dao.rest.support;

/**
 * 
 * @author maxweld
 * 
 *
 */
public class RestExperiment {

	private String sessionGid;
	private String name;
	private String description;
	private String sourceGid;
	private String instrumentTechniqueGid;
	
	public String getSessionGid() {
		return sessionGid;
	}
	public void setSessionGid(String sessionGid) {
		this.sessionGid = sessionGid;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getSourceGid() {
		return sourceGid;
	}
	public void setSourceGid(String sourceGid) {
		this.sourceGid = sourceGid;
	}
	
	public String getInstrumentTechniqueGid() {
		return instrumentTechniqueGid;
	}
	public void setInstrumentTechniqueGid(String instrumentTechniqueGid) {
		this.instrumentTechniqueGid = instrumentTechniqueGid;
	}
}
