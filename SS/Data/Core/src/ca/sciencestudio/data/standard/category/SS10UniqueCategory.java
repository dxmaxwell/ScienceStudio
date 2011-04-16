/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     SS10UniqueCategory class.
 *     
 */
package ca.sciencestudio.data.standard.category;

/**
 * @author maxweld
 *
 */
public class SS10UniqueCategory<T extends SS10Category<?>> extends AbstractUniqueCategory<T> {
		
	public SS10UniqueCategory(String uid) {
		super(uid);
	}

	@SuppressWarnings("unchecked")
	public T getCategory() {
		return (T) SS10Category.INSTANCE;
	}

	public String Source() {
		return getCategory().Source(getUID());
	}
	
	public String Created() {
		return getCategory().Created(getUID());
	}

	public String CreatedBy() {
		return getCategory().CreatedBy(getUID());
	}
}
