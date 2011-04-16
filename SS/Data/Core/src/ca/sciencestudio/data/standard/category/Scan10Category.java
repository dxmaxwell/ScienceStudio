/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     Scan10Category class.
 *     
 */
package ca.sciencestudio.data.standard.category;

import ca.sciencestudio.data.standard.category.AbstractCategory;

/**
 * @author maxweld
 *
 */
public class Scan10Category<T extends Scan10UniqueCategory<?>> extends AbstractCategory<T> {

	public static Scan10Category<Scan10UniqueCategory<?>> INSTANCE =
								new Scan10Category<Scan10UniqueCategory<?>>(); 

	private final int cmajor = 1;
	private final int cminor = 0;
	private final String cname = "Scan";
	private final String EndTime = join(getName(), "EndTime");
	private final String StartTime = join(getName(), "StartTime");

	@SuppressWarnings("unchecked")
	public T getUniqueCategory(String uid) {
		return (T) new Scan10UniqueCategory<Scan10Category<Scan10UniqueCategory<?>>>(uid);
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

	public String EndTime() {
		return EndTime;
	}
	public String EndTime(String uid) {
		return join(uid, EndTime());
	}

	public String StartTime() {
		return StartTime;
	}
	public String StartTime(String uid) {
		return join(uid, StartTime());
	}
}
