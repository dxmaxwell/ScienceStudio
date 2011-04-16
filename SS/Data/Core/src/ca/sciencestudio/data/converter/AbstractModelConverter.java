/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     AbstractModelConverter class.
 *     
 */
package ca.sciencestudio.data.converter;

/**
 * @author maxweld
 *
 */
public abstract class AbstractModelConverter extends AbstractScanConverter {

	private String techniqueName = null;
	private String instrumentName = null;
	private String facilityName = null;
	private String laboratoryName = null;
	
	private String sampleName = null;
	private String projectName = null;
	private String sessionName = null;
	private String experimentName = null;
	
	public AbstractModelConverter(String fromFormat, String toFormat, boolean forceUpdate) {
		super(fromFormat, toFormat, forceUpdate);
	}
	
	public boolean hasSSModel10() {
		return (sampleName != null) && (projectName != null) && (sessionName != null) &&
				(experimentName != null) && (techniqueName != null) && (instrumentName != null) &&	
				(facilityName != null) && (laboratoryName != null) && (getScanName() != null);
	}
	
	public String getTechniqueName() {
		return techniqueName;
	}
	public void setTechniqueName(String techniqueName) {
		this.techniqueName = techniqueName;
	}
	
	public String getInstrumentName() {
		return instrumentName;
	}
	public void setInstrumentName(String instrumentName) {
		this.instrumentName = instrumentName;
	}
	
	public String getFacilityName() {
		return facilityName;
	}
	public void setFacilityName(String facilityName) {
		this.facilityName = facilityName;
	}
	
	public String getLaboratoryName() {
		return laboratoryName;
	}
	public void setLaboratoryName(String laboratoryName) {
		this.laboratoryName = laboratoryName;
	}
	
	public String getSampleName() {
		return sampleName;
	}
	public void setSampleName(String sampleName) {
		this.sampleName = sampleName;
	}
	
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	
	public String getSessionName() {
		return sessionName;
	}
	public void setSessionName(String sessionName) {
		this.sessionName = sessionName;
	}
	
	public String getExperimentName() {
		return experimentName;
	}
	public void setExperimentName(String experimentName) {
		this.experimentName = experimentName;
	}
}
