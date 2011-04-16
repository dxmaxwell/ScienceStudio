/** Copy	right (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     BeamI11Category class.
 *     
 */
package ca.sciencestudio.data.standard.category;

import ca.sciencestudio.data.standard.category.BeamI10Category;

/**
 * @author maxweld
 *
 */
public class BeamI11Category<T extends BeamI11UniqueCategory<?>> extends BeamI10Category<T> {
	
	public static final BeamI11Category<BeamI11UniqueCategory<?>> INSTANCE =
									new BeamI11Category<BeamI11UniqueCategory<?>>(); 

	private final int cminor = 1;
	private final String I = join(getName(), "I");
	private final String Name = join(getName(), "Name");
	private final String Desc = join(getName(), "Desc");
	private final String I4 = join(getName(), "I4");
	private final String I5 = join(getName(), "I5");
	private final String I6 = join(getName(), "I6");
	private final String I7 = join(getName(), "I7");
	private final String I8 = join(getName(), "I8");
	private final String I9 = join(getName(), "I9");
		
	@Override
	@SuppressWarnings("unchecked")
	public T getUniqueCategory(String uid) {
		return (T) new BeamI11UniqueCategory<BeamI11Category<BeamI11UniqueCategory<?>>>(uid);
	}
	
	public int getMinor() {
		return cminor;
	}
	
	public String Name() {
		return Name;
	}
	public String Name(String uid) {
		return join(uid, Name());
	}
	
	public String Desc() {
		return Desc;
	}
	public String Desc(String uid) {
		return join(uid, Desc());
	}
	
	public String I(int n) {
		return join(I, Math.abs(n));
	}
	public String I(String uid, int n) {
		return join(uid, I(n));
	}
	
	public String I4() {
		return I4;
	}
	public String I4(String uid) {
		return join(uid, I4());
	}
	
	public String I5() {
		return I5;
	}
	public String I5(String uid) {
		return join(uid, I5());
	}
	
	public String I6() {
		return I6;
	}
	public String I6(String uid) {
		return join(uid, I6());
	}
	
	public String I7() {
		return I7;
	}
	public String I7(String uid) {
		return join(uid, I7());
	}
	
	public String I8() {
		return I8;
	}
	public String I8(String uid) {
		return join(uid, I8());
	}
	
	public String I9() {
		return I9;
	}
	public String I9(String uid) {
		return join(uid, I9());
	}	
}
