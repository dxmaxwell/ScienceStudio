/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     BeamI10Category class.
 *     
 */
package ca.sciencestudio.data.standard.category;

import ca.sciencestudio.data.standard.category.AbstractCategory;

/**
 * @author maxweld
 *
 */
public class BeamI10Category<T extends BeamI10UniqueCategory<?>> extends AbstractCategory<T> {

	public static BeamI10Category<BeamI10UniqueCategory<?>> INSTANCE = 
								new BeamI10Category<BeamI10UniqueCategory<?>>();
	
	private final int cmajor = 1;
	private final int cminor = 0;
	private final String cname = "BeamI";
	private final String SR = join(getName(), "SR");
	private final String I0 = join(getName(), "I0");
	private final String I1 = join(getName(), "I1");
	private final String I2 = join(getName(), "I2");
	private final String I3 = join(getName(), "I3");
	private final String Unit = join(getName(), "Unit");

	@SuppressWarnings("unchecked")
	public T getUniqueCategory(String uid) {
		return (T) new BeamI10UniqueCategory<BeamI10Category<BeamI10UniqueCategory<?>>>(uid);
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
	
	public String SR() {
		return SR;
	}
	public String SR(String uid) {
		return join(uid, SR());
	}
	
	public String I0() {
		return I0;
	}
	public String I0(String uid) {
		return join(uid, I0());
	}
	
	public String I1() {
		return I1;
	}
	public String I1(String uid) {
		return join(uid, I1());
	}
	
	public String I2() {
		return I2;
	}
	public String I2(String uid) {
		return join(uid, I2());
	}

	public String I3() {
		return I3;
	}
	public String I3(String uid) {
		return join(uid, I3());
	}
	
	public String Unit() {
		return Unit;
	}
	public String unit(String uid) {
		return join(uid, Unit());
	}
}
