/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     Category interface.
 *     
 */
package ca.sciencestudio.data.standard.category;

import ca.sciencestudio.data.standard.category.UniqueCategory;

/**
 * @author maxweld
 *
 */
public interface Category<T extends UniqueCategory<?>> {
	
	public int getMajor();
	public int getMinor();
	public String getName();
	public String getVersion();
	public String getIdentity();
	
	public String getIdentity(String uid);
	public T getUniqueCategory(String uid);
}
