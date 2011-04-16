/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     MapXY11Category class.
 *     
 */
package ca.sciencestudio.data.standard.category;

import ca.sciencestudio.data.standard.category.MapXY10Category;

/**
 * @author maxweld
 *
 */
public class MapXY11Category<T extends MapXY11UniqueCategory<?>> extends MapXY10Category<T> {
	
	public static final MapXY11Category<MapXY11UniqueCategory<?>> INSTANCE =
										new MapXY11Category<MapXY11UniqueCategory<?>>(); 
	
	private final int cminor = 1;
	private final String Name = join(getName(), "Name");
	private final String Desc = join(getName(), "Desc");

	@Override
	@SuppressWarnings("unchecked")
	public T getUniqueCategory(String uid) {
		return (T) new MapXY11UniqueCategory<MapXY11Category<MapXY11UniqueCategory<?>>>(uid);
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
}
