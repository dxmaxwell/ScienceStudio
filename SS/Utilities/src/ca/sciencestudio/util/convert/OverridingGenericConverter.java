/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     OverridingGenericConverter class.
 *     
 */
package ca.sciencestudio.util.convert;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.GenericConverter;

/**
 * @author maxweld
 * 
 *
 */
public class OverridingGenericConverter implements GenericConverter {

	private Collection<Class<?>> overriddenClasses;
	
	public OverridingGenericConverter() {
		super();
	}
	
	public OverridingGenericConverter(Collection<Class<?>> overriddenClassses) {
		setOverriddenClasses(overriddenClassses);
	}
	
	@Override
	public Set<ConvertiblePair> getConvertibleTypes() {
		
		if((overriddenClasses == null) || overriddenClasses.isEmpty()) {
			return Collections.emptySet();
		}
		
		Set<Class<?>> overriddenClassSet;
		if(overriddenClasses instanceof Set) {
			overriddenClassSet = (Set<Class<?>>) overriddenClasses;
		} else {
			overriddenClassSet = new HashSet<Class<?>>(overriddenClasses);
		}
		
		Set<ConvertiblePair> convertibleTypes = new HashSet<ConvertiblePair>();
		for(Class<?> clazz : overriddenClassSet) {
			convertibleTypes.add(new ConvertiblePair(clazz, clazz));
		}
		return convertibleTypes;
	}

	@Override
	public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
		return source;
	}

	public Collection<Class<?>> getOverriddenClasses() {
		return overriddenClasses;
	}
	public void setOverriddenClasses(Collection<Class<?>> overriddenClasses) {
		this.overriddenClasses = overriddenClasses;
	}
}
