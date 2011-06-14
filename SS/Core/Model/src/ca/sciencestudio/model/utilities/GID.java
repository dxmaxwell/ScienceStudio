/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *   	GID (Global Identifier) class.
 *     
 */
package ca.sciencestudio.model.utilities;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @author maxweld
 *
 */
public class GID {
	
	private static final Pattern GID_ID_PATTERN = Pattern.compile("[0-9]+", Pattern.CASE_INSENSITIVE);
	private static final Pattern GID_TYPE_PATTERN = Pattern.compile("[A-Z]?", Pattern.CASE_INSENSITIVE);
	private static final Pattern GID_FACILITY_PATTERN = Pattern.compile("[A-Z]*", Pattern.CASE_INSENSITIVE);
	
	private static final Pattern GID_VALIDATE_PATTERN = Pattern.compile(String.format("(%s)(%s)(%s)", GID_FACILITY_PATTERN.pattern(),
															GID_ID_PATTERN.pattern(), GID_TYPE_PATTERN.pattern()), Pattern.CASE_INSENSITIVE);
	
	public static final int DEFAULT_ID = 0;
	public static final String DEFAULT_TYPE = "";
	public static final String DEFAULT_FACILITY = "";
	
	public static final String DEFAULT_GID = new GID().toString();
	
	private int id;
	private String type;
	private String facility;
	
	public static boolean isValid(String gid) {
		return (parse(gid) != null);
	}
	
	public static GID parse(String gid) {
		if(gid == null) {
			return null;
		}
		Matcher m = GID_VALIDATE_PATTERN.matcher(gid);
		if(!m.matches()) {
			return null;
		}
		return new GID(m.group(1), Integer.parseInt(m.group(2)), m.group(3));
	}
	
	public static String format(String facility, int id, String type) {
		return new GID(facility, id, type).toString();
	}
	
	public GID() {
		this.id = DEFAULT_ID;
		this.type = DEFAULT_TYPE;
		this.facility = DEFAULT_FACILITY;
	}
	
	public GID(String facility, int id, String type) {
		setId(id);
		setType(type);
		setFacility(facility);
	}
	
	public boolean isLocal() {
		return (facility.length() == 0);
	}
	
	public boolean isGlobal() {
		return (facility.length() != 0);
	}
	
	public boolean isTypeless() {
		return (type.length() == 0);
	}
	
	public boolean isId(int id) {
		return this.id == id;
	}
	
	public boolean isType(String type) {
		return this.type.equalsIgnoreCase(type);
	}
	
	public boolean isFacility(String facility) {
		return this.facility.equalsIgnoreCase(facility);
	}
	
	public boolean isFacilityAndType(String facility, String type) {
		return isFacilityAndType(facility, type, false, false);
	}
	
	public boolean isFacilityAndType(String facility, String type, boolean allowLocal, boolean allowTypeless) {
		return ((allowLocal && isLocal()) || isFacility(facility)) && ((allowTypeless && isTypeless()) || isType(type));
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof GID)) {
			return false;
		}
		GID gid = (GID) obj;
		if(!isFacility(gid.getFacility())) {
			return false;
		}
		else if(!isType(gid.getType())) {
			return false;
		}
		else if(!isId(gid.getId())) {
			return false;
		}
		else {
			return true;
		}
	}
	
	@Override
	public String toString() {
		return String.format("%s%d%s", facility, id, type);
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		if(id >= 0) {
			this.id = id;
		} else {
			this.id = DEFAULT_ID;
		}
	}

	public String getType() {
		return type;
	}
	public void setType(String type) {
		if((type != null) && GID_TYPE_PATTERN.matcher(type).matches()) {
			this.type = type;
		} else {
			this.type = DEFAULT_TYPE;
		}
	}

	public String getFacility() {
		return facility;
	}
	public void setFacility(String facility) {
		if((facility != null) && GID_FACILITY_PATTERN.matcher(facility).matches()) {
			this.facility = facility;
		} else {
			this.facility = DEFAULT_FACILITY;
		}
	}
}
