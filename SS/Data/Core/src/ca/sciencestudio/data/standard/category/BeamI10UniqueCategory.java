/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     BeamI10UniqueCategory class.
 *     
 */
package ca.sciencestudio.data.standard.category;

import ca.sciencestudio.data.standard.category.BeamI10Category;

/**
 * @author maxweld
 *
 */
public class BeamI10UniqueCategory<T extends BeamI10Category<?>> extends AbstractUniqueCategory<T> {
	
	public BeamI10UniqueCategory(String uid) {
		super(uid);
	}

	@SuppressWarnings("unchecked")
	public T getCategory() {
		return (T) BeamI10Category.INSTANCE;
	}

	public String I0() {
		return getCategory().I0(getUID());
	}
	
	public String I1() {
		return getCategory().I1(getUID());
	}
	
	public String I2() {
		return getCategory().I2(getUID());
	}

	public String I3() {
		return getCategory().I3(getUID());
	}
	
	public String unit() {
		return getCategory().unit(getUID());
	}
}
