/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     SSModel10Category class.
 *     
 */
package ca.sciencestudio.data.standard.category;

import ca.sciencestudio.data.standard.category.AbstractCategory;

/**
 * @author maxweld
 *
 */
public class SSModel10Category<T extends SSModel10UniqueCategory<?>> extends AbstractCategory<T> {
	
	public static SSModel10Category<SSModel10UniqueCategory<?>> INSTANCE =
									new SSModel10Category<SSModel10UniqueCategory<?>>(); 

	private final int cmajor = 1;
	private final int cminor = 0;
	private final String cname = "SSModel";
	private final String ScanName = join(getName(), "ScanName");
	private final String Facility = join(getName(), "Facility");
	private final String Technique = join(getName(), "Technique");
	private final String SampleName = join(getName(), "SampleName");
	private final String Laboratory = join(getName(), "Laboratory");
	private final String Instrument = join(getName(), "Instrument");
	private final String ProjectName = join(getName(), "ProjectName");
	private final String SessionName = join(getName(), "SessionName");
	private final String ExperimentName = join(getName(), "ExperimentName");

	@SuppressWarnings("unchecked")
	public T getUniqueCategory(String uid) {
		return (T) new SSModel10UniqueCategory<SSModel10Category<SSModel10UniqueCategory<?>>>(uid);
	}

	public int getMajor() {
		return cmajor;
	}

	public int getMinor() {
		return cminor;
	}

	public String getName() {
		return cname;
	}

	public String ScanName() {
		return ScanName;
	}
	public String ScanName(String uid) {
		return join(uid, ScanName());
	}

	public String Facility() {
		return Facility;
	}
	public String Facility(String uid) {
		return join(uid, Facility());
	}
	
	public String Technique() {
		return Technique;
	}
	public String Technique(String uid) {
		return join(uid, Technique());
	}
	
	public String SampleName() {
		return SampleName;
	}
	public String SampleName(String uid) {
		return join(uid, SampleName());
	}
	
	public String Laboratory() {
		return Laboratory;
	}
	public String Laboratory(String uid) {
		return join(uid, Laboratory());
	}
	
	public String Instrument() {
		return Instrument;
	}
	public String Instrument(String uid) {
		return join(uid, Instrument());
	}
	
	public String ProjectName() {
		return ProjectName;
	}
	public String ProjectName(String uid) {
		return join(uid, ProjectName());
	}
	
	public String SessionName() {
		return SessionName;
	}
	public String SessionName(String uid) {
		return join(uid, SessionName());
	}
	
	public String ExperimentName() {
		return ExperimentName;
	}
	public String ExperimentName(String uid) {
		return join(uid, ExperimentName());
	}
}
