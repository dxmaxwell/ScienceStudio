/**	Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     Model path utilities.
 *     
 */

var ModelPathUtils = {

	PERSON_PATH:"/persons",
	
	PROJECT_PATH:'/projects',

    PROJECT_PERSON_PATH:'/project/persons',
	
    SESSION_PATH:'/sessions',
    
    SESSION_PERSON_PATH:'/session/persons',
    
    EXPERIMENT_PATH:"/experiments",
    
    SAMPLE_PATH:'/samples',
    
    SCAN_PATH:"/scans",
    
    MODEL_PATH:'/ss/model',
    
    getPersonPath: function(exts) {
    	return ModelPathUtils.specialJoin(ModelPathUtils.MODEL_PATH + ModelPathUtils.PERSON_PATH, exts);	
	},
    
	getModelProjectPath: function(exts) {
		return ModelPathUtils.specialJoin(ModelPathUtils.MODEL_PATH + ModelPathUtils.PROJECT_PATH, exts);
	},
	
	getModelProjectPersonPath: function(exts) {
		return ModelPathUtils.specialJoin(ModelPathUtils.MODEL_PATH + ModelPathUtils.PROJECT_PERSON_PATH, exts);
	},
	
	getModelSessionPath: function(exts) {
		return ModelPathUtils.specialJoin(ModelPathUtils.MODEL_PATH + ModelPathUtils.SESSION_PATH, exts);
	},
	
	getModelSessionPersonPath: function(exts) {
		return ModelPathUtils.specialJoin(ModelPathUtils.MODEL_PATH + ModelPathUtils.SESSION_PERSON_PATH, exts);
	},
	
	getModelExperimentPath: function(exts) {
		return ModelPathUtils.specialJoin(ModelPathUtils.MODEL_PATH + ModelPathUtils.EXPERIMENT_PATH, exts);
	},
	
	getModelSamplePath: function(exts) {
		return ModelPathUtils.specialJoin(ModelPathUtils.MODEL_PATH + ModelPathUtils.SAMPLE_PATH, exts);
	},
	
	getModelScanPath: function(exts) {
		return ModelPathUtils.specialJoin(ModelPathUtils.MODEL_PATH + ModelPathUtils.SCAN_PATH, exts);
	},
	
	specialJoin: function(prefix, exts) {
		var path = prefix;
		if(exts) {
			if(exts.join) { 
				path += exts.join('');
			} else { 
				path += exts;
			}
		}
		return path;
	}
};


