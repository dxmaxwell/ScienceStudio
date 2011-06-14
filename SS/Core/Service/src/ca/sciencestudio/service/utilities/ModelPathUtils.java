/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     AbstractModelController class.
 *     
 */
package ca.sciencestudio.service.utilities;

/**
 * @author maxweld
 *
 */
public abstract class ModelPathUtils {

	public static final String SCAN = "/scan";
	public static final String SCANS = "/scans";

	public static final String PROJECTS = "/projects";
	
	public static final String PROJECT_PERSON = "/project/person";
	public static final String PROJECT_PERSONS = "/persons";
	
	public static final String SAMPLE = "/sample";
	public static final String SAMPLES = "/samples";
	
	public static final String SESSION = "/session";
	public static final String SESSIONS = "/sessions";
	
	public static final String EXPERIMENT = "/experiment";
	public static final String EXPERIMENTS = "/experiments";
	
	public static String getProjectsPath(String ext) {
		return PROJECTS + ext;
	}
	
	public static String getProjectsPath(Object projectGid, String ext) {
		return getProjectsPath("/" + projectGid + ext);
	}
	
	public static String getProjectPersonsPath(String projectGid, String ext) {
		return getProjectsPath(projectGid, PROJECT_PERSONS + ext);
	}
	
	public static String getProjectPersonPath(int projectPersonId, String ext) {
		return PROJECT_PERSON + "/" + projectPersonId + ext;
	}
	
	public static String getSamplesPath(Object projectGid, String ext) {
		return getProjectsPath(projectGid, SAMPLES + ext);
	}
	
	public static String getSamplePath(int sampleId, String ext) {
		return SAMPLE + "/" + sampleId + ext;
	}
	
	public static String getSessionsPath(Object projectGid, String ext) {
		return getProjectsPath(projectGid, SESSIONS + ext);
	}
	
	public static String getSessionPath(int sessionId, String ext) {
		return SESSION + "/" + sessionId + ext;
	}
	
	public static String getExperimentsPath(int sessionId, String ext) {
		return getSessionPath(sessionId, EXPERIMENTS + ext);
	}
	
	public static String getExperimentPath(int experimentId, String ext) {
		return EXPERIMENT + "/" + experimentId + ext;
	}
	
	public static String getScansPath(int experimentId, String ext) {
		return getExperimentPath(experimentId, SCANS + ext);
	}
	
	public static String getScanPath(int scanId, String ext) {
		return SCAN + "/" + scanId + ext;
	}
}
