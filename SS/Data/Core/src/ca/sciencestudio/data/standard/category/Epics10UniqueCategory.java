/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *    Epics10UniqueCategory class.
 *     
 */
package ca.sciencestudio.data.standard.category;

/**
 * @author maxweld
 *
 */
public class Epics10UniqueCategory<T extends Epics10Category<?>> extends AbstractUniqueCategory<T> {

	public Epics10UniqueCategory(String uid) {
		super(uid);
	}

	@SuppressWarnings("unchecked")
	public T getCategory() {
		return (T) Epics10Category.INSTANCE;
	}

	public String PV() {
		return  getCategory().PV(getUID());
	}
}
