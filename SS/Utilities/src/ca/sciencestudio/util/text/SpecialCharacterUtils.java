/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     SpecialCharacterUtils class.
 *     
 */

package ca.sciencestudio.util.text;

import java.util.regex.Pattern;

/**
 * @author maxweld
 * 
 */
public abstract class SpecialCharacterUtils {

	public static final Pattern PATTERN_SPECIAL_FORBIDDEN = Pattern.compile("[" + Pattern.quote("`'\"~!&?%$@#^+*") + "]+");	
	public static final Pattern PATTERN_SPECIAL_UNDERSCORE = Pattern.compile("[" + Pattern.quote("=,:;") + "]+");
	public static final Pattern PATTERN_SPECIAL_HYPHEN = Pattern.compile("[" + Pattern.quote("/|\\") + "]+");
	public static final Pattern PATTERN_SPECIAL_RIGHT = Pattern.compile("[" + Pattern.quote(">}]") + "]+");
	public static final Pattern PATTERN_SPECIAL_LEFT = Pattern.compile("[" + Pattern.quote("[{<") + "]+");
	public static final Pattern PATTERN_WHITESPACE = Pattern.compile("\\s+");
	
	public static final String REPLACEMENT_SPECIAL_FORBIDDEN= "";
	public static final String REPLACEMENT_SPECIAL_UNDERSCORE = "_";
	public static final String REPLACEMENT_SPECIAL_HYPHEN = "-";
	public static final String REPLACEMENT_SPECIAL_RIGHT = ")";
	public static final String REPLACEMENT_SPECIAL_LEFT = "(";
	public static final String REPLACEMENT_WHITESPACE = "_";
	
	public static String replaceWhitespace(String target) {
		return replaceWhitespace(target, REPLACEMENT_WHITESPACE);
	}
	
	public static String replaceWhitespace(String target, boolean trim) {
		return replaceWhitespace(target, REPLACEMENT_WHITESPACE, trim);
	}
	
	public static String replaceWhitespace(String target, String replacement) {
		return replaceWhitespace(target, replacement, false);
	}
	
	public static String replaceWhitespace(String target, String replacement,  boolean trim) {
		return PATTERN_WHITESPACE.matcher(trim ? target.trim() : target).replaceAll(replacement);
	}
	
	public static String replaceSpecial(String target) {
		return replaceSpecial(target, false);
	}
	
	public static String replaceSpecial(String target, boolean trim) {
		if(trim) {
			target = target.trim();
		}
		target = PATTERN_SPECIAL_FORBIDDEN.matcher(target).replaceAll(REPLACEMENT_SPECIAL_FORBIDDEN);
		target = PATTERN_SPECIAL_UNDERSCORE.matcher(target).replaceAll(REPLACEMENT_SPECIAL_UNDERSCORE);
		target = PATTERN_SPECIAL_HYPHEN.matcher(target).replaceAll(REPLACEMENT_SPECIAL_HYPHEN);
		target = PATTERN_SPECIAL_RIGHT.matcher(target).replaceAll(REPLACEMENT_SPECIAL_RIGHT);
		target = PATTERN_SPECIAL_LEFT.matcher(target).replaceAll(REPLACEMENT_SPECIAL_LEFT);
		target = PATTERN_WHITESPACE.matcher(target).replaceAll(REPLACEMENT_WHITESPACE);
		return target;
	}
	
	public static boolean hasSpecial(String target) {
		if(PATTERN_SPECIAL_FORBIDDEN.matcher(target).matches()) {
			return true;
		}
		else if(PATTERN_SPECIAL_UNDERSCORE.matcher(target).matches()) {
			return true;
		}
		else if(PATTERN_SPECIAL_HYPHEN.matcher(target).matches()) {
			return true;
		}
		else if(PATTERN_SPECIAL_RIGHT.matcher(target).matches()) {
			return true;
		}
		else if(PATTERN_SPECIAL_LEFT.matcher(target).matches()) {
			return true;
		}
		else if(PATTERN_WHITESPACE.matcher(target).matches()) {
			return true;
		}
		else {
			return false;
		}
	}
}
