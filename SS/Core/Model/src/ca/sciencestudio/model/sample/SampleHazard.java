/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     SampleHazard enum.
 *     
 */
package ca.sciencestudio.model.sample;

import java.io.Serializable;
import java.util.Collection;

import ca.sciencestudio.util.marshal.MarshallableEnum;
import ca.sciencestudio.util.marshal.MarshallableEnumValue;

/**
 * @author maxweld
 *
 */
public enum SampleHazard implements MarshallableEnum<SampleHazard>, Serializable {
	CORROSIVE, REACTIVE, FLAMMABLE, OXIDIZER, TOXIC, OTHER, UNKNOWN;

	public static Collection<MarshallableEnumValue<SampleHazard>> getMarshallableValues() {
		return MarshallableEnumValue.getMarshallableValues(SampleHazard.class);
	}

	public static Collection<MarshallableEnumValue<SampleHazard>> getMarshallableValues(SampleHazard... exclude) {
		return MarshallableEnumValue.getMarshallableValues(SampleHazard.class, exclude);
	}
	
	private final SampleHazardMarshallableEnumValue value = new SampleHazardMarshallableEnumValue(this);
	
	@Override
	public String getName() {
		return value.getName();
	}

	@Override
	public String getLongName() {
		return value.getLongName();
	}

	@Override
	public MarshallableEnumValue<SampleHazard> getMarshallableValue() {
		return value;
	}
	
	public class SampleHazardMarshallableEnumValue extends MarshallableEnumValue<SampleHazard> {

		private static final long serialVersionUID = 1L;

		public SampleHazardMarshallableEnumValue(SampleHazard nonMarshallableEnum) {
			super(nonMarshallableEnum);
		}

		@Override
		public SampleHazard getEnum() {
			return SampleHazard.this;
		}
	}
}
