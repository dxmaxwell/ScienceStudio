/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     SampleState class.
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
public enum SampleState implements MarshallableEnum<SampleState>, Serializable {
	SOLID, LIQUID, GAS, MIXED, UNKNOWN;
	
	public static Collection<MarshallableEnumValue<SampleState>> getMarshallableValues() {
		return MarshallableEnumValue.getMarshallableValues(SampleState.class);
	}

	public static Collection<MarshallableEnumValue<SampleState>> getMarshallableValues(SampleState... exclude) {
		return MarshallableEnumValue.getMarshallableValues(SampleState.class, exclude);
	}
	
	public final SampleStateMarshallableEnumValue value = new SampleStateMarshallableEnumValue(this);

	@Override
	public String getName() {
		return value.getName();
	}

	@Override
	public String getLongName() {
		return value.getLongName();
	}

	@Override
	public MarshallableEnumValue<SampleState> getMarshallableValue() {
		return value;
	}

	public class SampleStateMarshallableEnumValue extends MarshallableEnumValue<SampleState> {

		private static final long serialVersionUID = 1L;

		public SampleStateMarshallableEnumValue(SampleState nonMarshallableEnum) {
			super(nonMarshallableEnum);
		}

		@Override
		public SampleState getEnum() {
			return SampleState.this;
		}
	}
}
