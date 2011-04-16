/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     BeamI11UniqueCategory class.
 *     
 */
package ca.sciencestudio.data.standard.category;

/**
 * @author maxweld
 *
 */
public class BeamI11UniqueCategory<T extends BeamI11Category<BeamI11UniqueCategory<?>>> extends BeamI10UniqueCategory<T> {
	
	public BeamI11UniqueCategory(String uid) {
		super(uid);
	}

	@Override
	@SuppressWarnings("unchecked")
	public T getCategory() {
		return (T) BeamI11Category.INSTANCE;
	}
	
	public String Name() {
		return getCategory().Name(getUID());
	}
	
	public String Desc() {
		return getCategory().Desc(getUID());
	}
	
	public String I(int n) {
		return getCategory().I(getUID(), n);
	}
	
	public String I4() {
		return getCategory().I4(getUID());
	}
	
	public String I5() {
		return getCategory().I5(getUID());
	}

	public String I6() {
		return getCategory().I6(getUID());
	}

	public String I7() {
		return getCategory().I7(getUID());
	}

	public String I8() {
		return getCategory().I8(getUID());
	}

	public String I9() {
		return getCategory().I9(getUID());
	}
}
