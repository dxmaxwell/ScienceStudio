/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     AbstractScanConverterFactory class.
 *     
 */
package ca.sciencestudio.data.converter.factory;

import ca.sciencestudio.data.converter.ConverterMap;
import ca.sciencestudio.data.support.ConverterFactoryException;
import ca.sciencestudio.model.facility.Facility;
import ca.sciencestudio.model.facility.Instrument;
import ca.sciencestudio.model.facility.Laboratory;
import ca.sciencestudio.model.facility.Technique;
import ca.sciencestudio.model.project.Project;
import ca.sciencestudio.model.sample.Sample;
import ca.sciencestudio.model.session.Experiment;
import ca.sciencestudio.model.session.Session;

/**
 * @author maxweld
 *
 */
public abstract class AbstractModelConverterFactory extends AbstractScanConverterFactory {
	
	protected static final String DEFAULT_FACILITY_NAME = "Unknown";
	protected static final String DEFAULT_FACILITY_LONG_NAME = "UNKNOWN";
	
	protected static final String DEFAULT_LABORATORY_NAME = "Unknown";
	protected static final String DEFAULT_LABORATORY_LONG_NAME = "UNKNOWN";
	
	protected static final String DEFAULT_INSTRUMENT_NAME = "UNKNOWN";
	protected static final String DEFAULT_TECHNIQUE_NAME = "UNKNOWN";
	
	protected static final String DEFAULT_SAMPLE_NAME = "Not Available";
	protected static final String DEFAULT_SESSION_NAME = "Not Available";
	protected static final String DEFAULT_PROJECT_NAME = "Not Available";
	protected static final String DEFAULT_EXPERIMENT_NAME = "Not Available";
	
	
	protected ConverterMap validateRequest(ConverterMap request) throws ConverterFactoryException {
		
		request = super.validateRequest(request);
		
		// Facility Name //
		Object facilityName = request.get(REQUEST_KEY_FACILITY_NAME);
		if(!isNotEmptyString(facilityName)) {
			Object facility = request.get(REQUEST_KEY_FACILITY);
			if(facility instanceof Facility) {
				facilityName = ((Facility)facility).getName();
				request.put(REQUEST_KEY_FACILITY_NAME, facilityName);
			}			
		}
		
		if(!isNotEmptyString(facilityName)) {
			facilityName = DEFAULT_FACILITY_NAME;
			request.put(REQUEST_KEY_FACILITY_NAME, facilityName);
		}
		
		// Facility Long Name //
		Object facilityLongName = request.get(REQUEST_KEY_FACILITY_LONG_NAME);
		if(!isNotEmptyString(facilityLongName)) {
			Object facility = request.get(REQUEST_KEY_FACILITY);
			if(facility instanceof Facility) {
				facilityLongName = ((Facility)facility).getLongName();
				request.put(REQUEST_KEY_FACILITY_LONG_NAME, facilityLongName);
			}			
		}
		
		if(!isNotEmptyString(facilityLongName)) {
			facilityLongName = DEFAULT_FACILITY_LONG_NAME;
			request.put(REQUEST_KEY_FACILITY_LONG_NAME, facilityLongName);
		}
		
		// Laboratory Name //
		Object laboratoryName = request.get(REQUEST_KEY_LABORATORY_NAME);
		if(!isNotEmptyString(laboratoryName)) {
			Object laboratory = request.get(REQUEST_KEY_LABORATORY);
			if(laboratory instanceof Laboratory) {
				laboratoryName = ((Laboratory)laboratory).getName();
				request.put(REQUEST_KEY_LABORATORY_NAME, laboratoryName);
			}
		}
		
		if(!isNotEmptyString(laboratoryName)) {
			laboratoryName = DEFAULT_LABORATORY_NAME;
			request.put(REQUEST_KEY_LABORATORY_NAME, laboratoryName);
		}
		
		// Laboratory Long Name //
		Object laboratoryLongName = request.get(REQUEST_KEY_LABORATORY_LONG_NAME);
		if(!isNotEmptyString(laboratoryLongName)) {
			Object laboratory = request.get(REQUEST_KEY_LABORATORY);
			if(laboratory instanceof Laboratory) {
				laboratoryLongName = ((Laboratory)laboratory).getLongName();
				request.put(REQUEST_KEY_LABORATORY_LONG_NAME, laboratoryLongName);
			}
		}
		
		if(!isNotEmptyString(laboratoryLongName)) {
			laboratoryLongName = DEFAULT_LABORATORY_LONG_NAME;
			request.put(REQUEST_KEY_LABORATORY_LONG_NAME, laboratoryLongName);
		}
		
		// Instrument Name //
		Object instrumentName = request.get(REQUEST_KEY_INSTRUMENT_NAME);
		if(!isNotEmptyString(instrumentName)) {
			Object instrument = request.get(REQUEST_KEY_INSTRUMENT);
			if(instrument instanceof Instrument) {
				instrumentName = ((Instrument)instrument).getName();
				request.put(REQUEST_KEY_INSTRUMENT_NAME, instrumentName);
			}
		}
		
		if(!isNotEmptyString(instrumentName)) {
			instrumentName = DEFAULT_INSTRUMENT_NAME;
			request.put(REQUEST_KEY_INSTRUMENT_NAME, instrumentName);
		}
		
		// Technique Name //
		Object techniqueName = request.get(REQUEST_KEY_TECHNIQUE_NAME);
		if(!isNotEmptyString(techniqueName)) {
			Object technique = request.get(REQUEST_KEY_TECHNIQUE);
			if(technique instanceof Technique) {
				techniqueName = ((Technique)technique).getName();
				request.put(REQUEST_KEY_TECHNIQUE_NAME, techniqueName);
			}
		}
		
		if(!isNotEmptyString(techniqueName)) {
			techniqueName = DEFAULT_TECHNIQUE_NAME;
			request.put(REQUEST_KEY_TECHNIQUE_NAME, techniqueName);
		}
		
		// Project Name //
		Object projectName =  request.get(REQUEST_KEY_PROJECT_NAME);
		if(!isNotEmptyString(projectName)) {
			Object project = request.get(REQUEST_KEY_PROJECT);
			if(project instanceof Project) {
				projectName = ((Project)project).getName();
				request.put(REQUEST_KEY_PROJECT_NAME, projectName);
			}
		}
		
		if(!isNotEmptyString(projectName)) {
			projectName = DEFAULT_PROJECT_NAME;
			request.put(REQUEST_KEY_PROJECT_NAME, projectName);
		}
		
		// Session Name //
		Object sessionName = request.get(REQUEST_KEY_SESSION_NAME);
		if(!isNotEmptyString(sessionName)) {
			Object session = request.get(REQUEST_KEY_SESSION);
			if(session instanceof Session) {
				sessionName = ((Session)session).getName();
				request.put(REQUEST_KEY_SESSION_NAME, sessionName);
			}
		}
		
		if(!isNotEmptyString(sessionName)) {
			sessionName = DEFAULT_SESSION_NAME;
			request.put(REQUEST_KEY_SESSION_NAME, sessionName);
		}
		
		// Experiment Name //
		Object experimentName = request.get(REQUEST_KEY_EXPERIMENT_NAME);
		if(!isNotEmptyString(experimentName)) {
			Object experiment = request.get(REQUEST_KEY_EXPERIMENT);
			if(experiment instanceof Experiment) {
				experimentName = ((Experiment)experiment).getName();
				request.put(REQUEST_KEY_EXPERIMENT_NAME, experimentName);
			}
		}
		
		if(!isNotEmptyString(experimentName)) {
			experimentName = DEFAULT_EXPERIMENT_NAME;
			request.put(REQUEST_KEY_EXPERIMENT_NAME, experimentName);
		}

		// Sample Name //
		Object sampleName = request.get(REQUEST_KEY_SAMPLE_NAME);
		if(!isNotEmptyString(sampleName)) {
			Object sample = request.get(REQUEST_KEY_SAMPLE);
			if(sample instanceof Sample) {
				sampleName = ((Sample)sample).getName();
				request.put(REQUEST_KEY_SAMPLE_NAME, sampleName);
			}
		}
		
		if(!isNotEmptyString(sampleName)) {
			sampleName = DEFAULT_SAMPLE_NAME;
			request.put(REQUEST_KEY_SAMPLE_NAME, sampleName);
		}
		
		return request;
	}
}
