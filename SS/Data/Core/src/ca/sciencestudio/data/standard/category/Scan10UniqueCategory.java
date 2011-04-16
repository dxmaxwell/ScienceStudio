/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     Scan10UniqueCategory class.
 *     
 */
package ca.sciencestudio.data.standard.category;

/**
 * @author maxweld
 *
 */
public class Scan10UniqueCategory<T extends Scan10Category<?>> extends AbstractUniqueCategory<T> {
	
	public Scan10UniqueCategory(String uid) {
		super(uid);
	}

	@SuppressWarnings("unchecked")
	public T getCategory() {
		return (T) Scan10Category.INSTANCE;
	}
	
	public String EndTime() {
		return getCategory().EndTime(getUID());
	}

	public String StartTime() {
		return getCategory().StartTime(getUID());
	}
}
