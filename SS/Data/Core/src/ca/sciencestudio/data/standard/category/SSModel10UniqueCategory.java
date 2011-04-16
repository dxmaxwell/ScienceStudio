/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     SSModel10UniqueCategory class.
 *     
 */
package ca.sciencestudio.data.standard.category;

/**
 * @author maxweld
 *
 */
public class SSModel10UniqueCategory<T extends SSModel10Category<?>> extends AbstractUniqueCategory<T> {
	
	public SSModel10UniqueCategory(String uid) {
		super(uid);
	}

	@SuppressWarnings("unchecked")
	public T getCategory() {
		return (T) SSModel10Category.INSTANCE;
	}
	
	public String ScanName() {
		return getCategory().ScanName(getUID());
	}

	public String Facility() {
		return getCategory().Facility(getUID());
	}
	
	public String Technique() {
		return getCategory().Technique(getUID());
	}
	
	public String SampleName() {
		return getCategory().SampleName(getUID());
	}
	
	public String Laboratory() {
		return getCategory().Laboratory(getUID());
	}
	
	public String Instrument() {
		return getCategory().Instrument(getUID());
	}
	
	public String ProjectName() {
		return getCategory().ProjectName(getUID());
	}
	
	public String SessionName() {
		return getCategory().SessionName(getUID());
	}
	
	public String ExperimentName() {
		return getCategory().ExperimentName(getUID());
	}
}
