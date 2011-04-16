/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     UniqueCategory interface.
 *     
 */
package ca.sciencestudio.data.standard.category;

import ca.sciencestudio.data.standard.category.Category;

public interface UniqueCategory<T extends Category<?>> {
	
	public int getMajor();
	public int getMinor();
	public String getName();
	public String getVersion();
	public String getIdentity();
	
	public String getUID();
	public T getCategory();
}
