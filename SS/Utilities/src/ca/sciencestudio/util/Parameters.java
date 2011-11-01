/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     Parameters class.
 *     
 */
package ca.sciencestudio.util;

import java.util.Enumeration;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Properties;

/**
 * @author maxweld
 *
 */
public class Parameters extends LinkedHashMap<String,String> {
	
	private static final long serialVersionUID = 1L;
	
	public Parameters() {
		super();
	}

	public Parameters(Properties properties) {
		super();
		Enumeration<?> names = properties.propertyNames();
		while(names.hasMoreElements()) {
			String name = names.nextElement().toString();
			put(name, properties.getProperty(name));
		}
	}
	
	public Parameters(Map<? extends String, ? extends String> m) {
		super(m);
	}

	public String get(String key, String dflt) {
		return containsKey(key) ? get(key) : dflt;
	}
}
