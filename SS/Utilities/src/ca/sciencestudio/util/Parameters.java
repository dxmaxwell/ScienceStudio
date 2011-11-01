/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     Parameters class.
 *     
 */
package ca.sciencestudio.util;

import java.util.Map;
import java.util.LinkedHashMap;

/**
 * @author maxweld
 *
 */
public class Parameters extends LinkedHashMap<String,String> {
	
	private static final long serialVersionUID = 1L;
	
	public Parameters() {
		super();
	}

	public Parameters(Map<? extends String, ? extends String> m) {
		super(m);
	}

	public String get(String key, String dflt) {
		return containsKey(key) ? get(key) : dflt;
	}
}
