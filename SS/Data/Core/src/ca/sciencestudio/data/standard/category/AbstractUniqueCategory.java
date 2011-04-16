/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     AbstractUniqueCategory class.
 *     
 */
package ca.sciencestudio.data.standard.category;

/**
 * @author maxweld
 *
 */
public abstract class AbstractUniqueCategory<T extends AbstractCategory<?>> implements UniqueCategory<T> {
		
	private String uid;
	
	public AbstractUniqueCategory(String uid) {
		this.uid = uid;
	}
	
	public String getUID() {
		return uid;
	}

	public int getMajor() {
		return getCategory().getMajor();
	}

	public int getMinor() {
		return getCategory().getMinor();
	}

	public String getName() {
		return getCategory().getName();
	}

	public String getVersion() {
		return getCategory().getVersion();
	}

	public String getIdentity() {
		return getCategory().getIdentity(uid);	
	}

	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof UniqueCategory)) {
			return false;
		}
		
		UniqueCategory<?> unqiueCategory = (UniqueCategory<?>) obj;
		
		if(!getCategory().equals(unqiueCategory.getCategory())) {
			return false;
		}
		
		if(!getUID().equals(unqiueCategory.getUID())) {
			return false;
		}
		
		return true;
	}
	
	@Override
	public String toString() {
		return getIdentity();
	}
}
