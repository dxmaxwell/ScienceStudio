/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     SS10Category class.
 *     
 */
package ca.sciencestudio.data.standard.category;

import ca.sciencestudio.data.standard.category.AbstractCategory;

/**
 * @author maxweld
 *
 */
public class SS10Category<T extends SS10UniqueCategory<?>> extends AbstractCategory<T> {

	public static final SS10Category<SS10UniqueCategory<?>> INSTANCE =
										new SS10Category<SS10UniqueCategory<?>>();
	
	private final int cmajor = 1;
	private final int cminor = 0;
	private final String cname = "SS";
	private final String Source = join(getName(), "Source");
	private final String Created = join(getName(), "Created");
	private final String CreatedBy = join(getName(), "CreatedBy");

	@SuppressWarnings("unchecked")
	public T getUniqueCategory(String uid) {
		return (T) new SS10UniqueCategory<SS10Category<SS10UniqueCategory<?>>>(uid);
	}

	public int getMajor() {
		return cmajor;
	}

	public int getMinor() {
		return cminor;
	}

	public String getName() {
		return cname;
	}

	public String Source() {
		return Source;
	}
	public String Source(String uid) {
		return join(uid, Source());
	}
	
	public String Created() {
		return Created;
	}
	public String Created(String uid) {
		return join(uid, Created());
	}

	public String CreatedBy() {
		return CreatedBy;
	}
	public String CreatedBy(String uid) {
		return join(uid, CreatedBy());
	}
}
