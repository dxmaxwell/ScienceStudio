/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     ModelPathUtils class.
 *     
 */
package ca.sciencestudio.service.utilities;

/**
 * @author maxweld
 *
 */
public abstract class ModelPathUtils {
	
	public static final String PERSON_PATH = "/persons";
	public static final String PROJECT_PATH = "/projects";
	public static final String PROJECT_PERSON_PATH = "/project/persons";
	public static final String SESSION_PATH = "/sessions";
	public static final String SESSION_PERSON_PATH = "/session/persons";
	public static final String EXPERIMENT_PATH = "/experiments";
	public static final String SAMPLE_PATH = "/samples";
	public static final String SCAN_PATH = "/scans";
	
	public static String TREE_PATH = "/ss/tree";
	public static String MODEL_PATH = "/ss/model";
	
	public static void setTreePath(String treePath) {
		TREE_PATH = treePath;
	}
	public static void setModelPath(String modelPath) {
		MODEL_PATH = modelPath;
	}
	
	public static String getTreeProjectPath(String... exts) {
		return join(TREE_PATH, PROJECT_PATH, exts);
	}
	public static String getModelProjectPath(String... exts) {
		return join(MODEL_PATH, PROJECT_PATH, exts);
	}

	public static String getTreeProjectPersonPath(String... exts) {
		return join(TREE_PATH, PROJECT_PERSON_PATH, exts);
	}
	public static String getModelProjectPersonPath(String... exts) {
		return join(MODEL_PATH, PROJECT_PERSON_PATH, exts);
	}
	
	public static String getTreeSessionPath(String... exts) {
		return join(TREE_PATH, SESSION_PATH, exts);
	}
	public static String getModelSessionPath(String... exts) {
		return join(MODEL_PATH, SESSION_PATH, exts);
	}
	
	public static String getTreeSessionPersonPath(String... exts) {
		return join(TREE_PATH, SESSION_PERSON_PATH, exts);
	}
	public static String getModelSessionPersonPath(String... exts) {
		return join(MODEL_PATH, SESSION_PERSON_PATH, exts);
	}
	
	public static String getTreeExperimentPath(String... exts) {
		return join(TREE_PATH, EXPERIMENT_PATH, exts);
	}
	public static String getModelExperimentPath(String... exts) {
		return join(MODEL_PATH, EXPERIMENT_PATH, exts);
	}
	
	public static String getTreeSamplePath(String... exts) {
		return join(TREE_PATH, SAMPLE_PATH, exts);
	}
	public static String getModelSamplePath(String... exts) {
		return join(MODEL_PATH, SAMPLE_PATH, exts);
	}
	
	public static String getTreeScanPath(String... exts) {
		return join(TREE_PATH, SCAN_PATH, exts);
	}
	public static String getModelScanPath(String... exts) {
		return join(MODEL_PATH, SCAN_PATH, exts);
	}
	
	protected static String join(String prefix, String path, String... exts) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(prefix).append(path);
		for(String e : exts) {
			buffer.append(e);
		}
		return buffer.toString();
	}
}
