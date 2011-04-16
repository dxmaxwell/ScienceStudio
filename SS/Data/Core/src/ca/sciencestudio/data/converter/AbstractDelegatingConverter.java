/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     DelegatingConverter class.
 *     
 */
package ca.sciencestudio.data.converter;

import java.util.Collection;
import java.util.LinkedHashSet;

import ca.sciencestudio.data.converter.Converter;
import ca.sciencestudio.data.converter.ConverterMap;
import ca.sciencestudio.data.support.ConverterException;
import ca.sciencestudio.data.support.DelegatingException;

/**
 * @author maxweld
 *
 */
public abstract class AbstractDelegatingConverter implements Converter {

	private Collection<Converter> converters = new LinkedHashSet<Converter>();
	
	public ConverterMap convert() throws ConverterException {
		
		DelegatingException exception = new DelegatingException();
		
		for(Converter converter : converters) {
			try {
				return converter.convert();
			}
			catch(ConverterException e) {
				exception.add(e);
			}
		}
		
		throw new ConverterException("Delegating converter unable to complete convert without exception.", exception);
	}
	
	public void add(Converter converter) {
		if(converter != null) {
			converters.add(converter);
		}
	}
	
	public void remove(Converter converter) {
		converters.remove(converter);
	}

	public Collection<Converter> getConverters() {
		return converters;
	}

	public void setConverters(Collection<Converter> converters) {
		if(converters != null) {
			this.converters = converters;
		}
	}	
}
