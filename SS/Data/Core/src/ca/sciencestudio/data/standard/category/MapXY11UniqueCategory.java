/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     MapXY11UniqueCategory class.
 *     
 */
package ca.sciencestudio.data.standard.category;

/**
 * @author maxweld
 *
 */
public class MapXY11UniqueCategory<T extends MapXY11Category<?>> extends MapXY10UniqueCategory<T> {
	
	public MapXY11UniqueCategory(String uid) {
		super(uid);
	}

	@Override
	@SuppressWarnings("unchecked")
	public T getCategory() {
		return (T) MapXY11Category.INSTANCE;
	}

	public String Name() {
		return getCategory().Name(getUID());
	}
	
	public String Desc() {
		return getCategory().Desc(getUID());
	}
}
