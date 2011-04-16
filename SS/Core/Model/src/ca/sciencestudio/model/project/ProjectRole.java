/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     ProjectRole interface.
 *     
 */
package ca.sciencestudio.model.project;

import java.io.Serializable;
import java.util.Collection;

import ca.sciencestudio.util.marshal.MarshallableEnum;
import ca.sciencestudio.util.marshal.MarshallableEnumValue;

/**
 * @author maxweld
 *
 */
public enum ProjectRole implements MarshallableEnum<ProjectRole>, Serializable {
	OBSERVER, EXPERIMENTER, UNKNOWN;
	
	public static Collection<MarshallableEnumValue<ProjectRole>> getMarshallableValues() {
		return MarshallableEnumValue.getMarshallableValues(ProjectRole.class);
	}

	public static Collection<MarshallableEnumValue<ProjectRole>> getMarshallableValues(ProjectRole... exclude) {
		return MarshallableEnumValue.getMarshallableValues(ProjectRole.class, exclude);
	} 
	
	private final MarshallableEnumValue<ProjectRole> value = new ProjectRoleMarshallableEnumValue(this);
	
	public String getName() {
		return value.getName();
	}
	
	public String getLongName() {
		return value.getLongName();
	}

	@Override
	public MarshallableEnumValue<ProjectRole> getMarshallableValue() {
		return value;
	}
	
	public class ProjectRoleMarshallableEnumValue extends MarshallableEnumValue<ProjectRole> {

		private static final long serialVersionUID = 1L;

		public ProjectRoleMarshallableEnumValue(ProjectRole nonMarshallableEnum) {
			super(nonMarshallableEnum);
		}

		@Override
		public ProjectRole getEnum() {
			return ProjectRole.this;
		}
	}
}
