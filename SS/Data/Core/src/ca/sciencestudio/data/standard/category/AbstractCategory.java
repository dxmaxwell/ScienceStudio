/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     AbstractCategory class.
 *     
 */
package ca.sciencestudio.data.standard.category;

/**
 * @author maxweld
 *
 */
public abstract class AbstractCategory<T extends AbstractUniqueCategory<?>> implements Category<T> {
	
	protected final String version = join(getMajor(), getMinor());
	protected final String identity = join(getName(), getVersion());
	
	protected static String join(String str1, int num2) {
		return join(str1, String.valueOf(num2), "");
	}
	
	protected static String join(int num1, int num2) {
		return join(String.valueOf(num1), String.valueOf(num2), ".");
	}
	
	protected static String join(String str1, String str2) {
		return join(str1, str2, ":");
	}
	
	protected static String join(String str1, String str2, String sep) {
		if((str1 == null) || (str1.length() == 0)) {
			return str2;
		}
			
		if((str2 == null) || (str2.length() == 0)) {
			return str1;
		}
		
		if(sep == null) {
			return (str1 + str2);
		} else {
			return (str1 + sep + str2);
		}
	}

	protected AbstractCategory() {
		// Restrict constructor access. //
	}
	
	public String getVersion() {
		return version;
	}
	
	public String getIdentity() {
		return identity;
	}
	public String getIdentity(String uid) {
		return join(uid, getIdentity());
	}

	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Category)) {
			return false;
		}
		
		Category<?> category = (Category<?>) obj;
		
		if(!getName().equals(category.getName())) {
			return false;
		}
		
		if(getMajor() != category.getMajor()) {
			return false;
		}
		
		if(getMinor() != category.getMinor()) {
			return false;
		}
		
		return true;
	}

	@Override
	public String toString() {
		return getIdentity();
	}
}

