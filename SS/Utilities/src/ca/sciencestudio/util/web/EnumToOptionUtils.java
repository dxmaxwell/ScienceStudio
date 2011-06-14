/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *   EnumToOptionUtils class.
 *     
 */
package ca.sciencestudio.util.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author maxweld
 *
 */
public abstract class EnumToOptionUtils {

	public static String DEFAULT_VALUE_KEY = "value";
	public static String DEFAULT_DISPLAY_KEY = "display";
	
	public static Map<String, String> toMap(Enum<?>... enums) {
		Map<String,String> options = new HashMap<String,String>();
		for(Enum<?> e : enums) {
			options.put(e.name(), e.toString());
		}
		return options;
	}
	
	public static List<Map<String,String>> toList(Enum<?>... enums) {
		return toList(DEFAULT_VALUE_KEY, DEFAULT_DISPLAY_KEY, enums);
	}
	
	public static List<Map<String,String>> toList(String value, String display, Enum<?>... enums) {
		List<Map<String,String>> options = new ArrayList<Map<String,String>>(enums.length);
		for(Enum<?> e : enums) {
			Map<String,String> option = new HashMap<String,String>();
			option.put(value, e.name());
			option.put(display, e.toString());
			options.add(option);
		}
		return options;
	}
}
