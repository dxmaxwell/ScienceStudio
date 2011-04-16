/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     ProjectStatus enum.
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
public enum ProjectStatus implements MarshallableEnum<ProjectStatus>, Serializable {
	ACTIVE, INACTIVE, UNKNOWN;
	
	public static Collection<MarshallableEnumValue<ProjectStatus>> getMarshallableValues() {
		return MarshallableEnumValue.getMarshallableValues(ProjectStatus.class);
	}

	public static Collection<MarshallableEnumValue<ProjectStatus>> getMarshallableValues(ProjectStatus... exclude) {
		return MarshallableEnumValue.getMarshallableValues(ProjectStatus.class, exclude);
	}
	
	private final MarshallableEnumValue<ProjectStatus> value = new ProjectStatusMarshallableEnumValue(this);
	
	public String getName() {
		return value.getName();
	}
	
	public String getLongName() {
		return value.getLongName();
	}

	@Override
	public MarshallableEnumValue<ProjectStatus> getMarshallableValue() {
		return value;
	}
	
	public class ProjectStatusMarshallableEnumValue extends MarshallableEnumValue<ProjectStatus> {

		private static final long serialVersionUID = 1L;

		public ProjectStatusMarshallableEnumValue(ProjectStatus nonMarshallableEnum) {
			super(nonMarshallableEnum);
		}

		@Override
		public ProjectStatus getEnum() {
			return ProjectStatus.this;
		}
	}
}
