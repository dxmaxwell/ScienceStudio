/**	Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     Model path utilities.
 *     
 */

var ModelPathUtils = {

	MODEL_BASE_PATH: '/ss/model',
	
	PERSON:"/person",
	
	SCAN:"/scan",
	SCANS:"/scans",
	
	PROJECT: "/project",
    PROJECTS: "/projects",
	
    PROJECT_PERSON:"/project/person",
    PROJECT_PERSONS:"/persons",
	
	SAMPLE:"/sample",
    SAMPLES:"/samples",
	
    SESSION:"/session",
    SESSIONS:"/sessions",
	
    EXPERIMENT:"/experiment",
	EXPERIMENTS:"/experiments",
	
	getProjectsPath: function(gid, ext) {
		var projectsPath = ModelPathUtils.MODEL_BASE_PATH + ModelPathUtils.PROJECTS;
		if(gid && ext) {
			projectsPath += '/' + gid + ext;	
		}
		else if(gid) {
			projectsPath += gid;
		}
		else if(ext) {
			projectsPath += ext;
		}
		return projectsPath;
	},
	
	getProjectPersonsPath: function(projectId, ext) {
		var projectPersonsPath = ModelPathUtils.getProjectPath(projectId) + ModelPathUtils.PROJECT_PERSONS;
		if(ext) {
			projectPersonsPath += ext;
		}
		return projectPersonsPath;
	},
	
	getProjectPersonPath: function(projectPersonId, ext) {
		var projectPersonPath = ModelPathUtils.MODEL_BASE_PATH + ModelPathUtils.PROJECT_PERSON + "/" + projectPersonId;
		if(ext) {
			projectPersonPath += ext;
		}
		return projectPersonPath;
	},
	
	getSamplesPath: function(projectId, ext) {
		var samplesPath =  ModelPathUtils.getProjectPath(projectId) + ModelPathUtils.SAMPLES;
		if(ext) {
			samplesPath += ext;
		}
		return samplesPath;
	},
	
	getSamplePath: function(sampleId, ext) {
		var samplePath = ModelPathUtils.MODEL_BASE_PATH + ModelPathUtils.SAMPLE + "/" + sampleId;
		if(ext) {
			samplePath += ext;
		}
		return samplePath;
	},
	
	getSessionsPath: function(projectId, ext) {
		var sessionsPath =  ModelPathUtils.getProjectPath(projectId) + ModelPathUtils.SESSIONS;
		if(ext) {
			sessionsPath += ext;
		}
		return sessionsPath;
	},
	
	getSessionPath: function(sessionId, ext) {
		var sessionPath = ModelPathUtils.MODEL_BASE_PATH + ModelPathUtils.SESSION + "/" + sessionId;
		if(ext) {
			sessionPath += ext;
		}
		return sessionPath;
	},
	
	getExperimentsPath: function(sessionId, ext) {
		var experimentsPath = ModelPathUtils.MODEL_BASE_PATH + ModelPathUtils.SESSION + "/" + sessionId + ModelPathUtils.EXPERIMENTS;
		if(ext) {
			experimentsPath += ext;
		}
		return experimentsPath;
	},
	
	getExperimentPath: function(experimentId, ext) {
		var experimentPath = ModelPathUtils.MODEL_BASE_PATH + ModelPathUtils.EXPERIMENT + "/" + experimentId;
		if(ext) {
			experimentPath += ext;
		}
		return experimentPath;
	},
	
	getScansPath: function(experimentId, ext) {
		var scanPath =  ModelPathUtils.getExperimentPath(experimentId, ModelPathUtils.SCANS);
		if(ext) {
			scanPath += ext;
		}
		return scanPath;
	},
	
	getScanPath: function(scanId, ext) {
		var scanPath = ModelPathUtils.MODEL_BASE_PATH + ModelPathUtils.SCAN + "/" + scanId;
		if(ext) {
			scanPath += ext;
		}
		return scanPath;
	},
	
	getPersonPath: function(personId) {
		var personPath = ModelPathUtils.MODEL_BASE_PATH + ModelPathUtils.PERSON;
		if(personId) {
			personPath += "/" + personId;
		}
		return personPath;	
	}
};


