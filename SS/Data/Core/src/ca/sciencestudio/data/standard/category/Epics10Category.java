/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *    Epics10Category class.
 *     
 */
package ca.sciencestudio.data.standard.category;

import ca.sciencestudio.data.standard.category.AbstractCategory;

/**
 * @author maxweld
 *
 */
public class Epics10Category<T extends Epics10UniqueCategory<?>> extends AbstractCategory<T> {
	
	public static final Epics10Category<Epics10UniqueCategory<?>> INSTANCE =
									new Epics10Category<Epics10UniqueCategory<?>>(); 

	private final int cmajor = 1;
	private final int cminor = 0;
	private final String cname = "Epics";
	private final String PV = join(getName(), "PV");

	@SuppressWarnings("unchecked")
	public T getUniqueCategory(String uid) {
		return (T) new Epics10UniqueCategory<Epics10Category<Epics10UniqueCategory<?>>>(uid);
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

	public String PV() {
		return PV;
	}
	public String PV(String uid) {
		return join(uid, PV());
	}
}
