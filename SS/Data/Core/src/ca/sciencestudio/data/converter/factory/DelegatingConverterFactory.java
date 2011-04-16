/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     DelegatingConverterFactory class.
 *     
 */
package ca.sciencestudio.data.converter.factory;

import java.util.Collection;
import java.util.LinkedHashSet;

import ca.sciencestudio.data.converter.Converter;
import ca.sciencestudio.data.converter.ConverterMap;
import ca.sciencestudio.data.converter.AbstractDelegatingConverter;
import ca.sciencestudio.data.support.DelegatingException;
import ca.sciencestudio.data.support.ConverterFactoryException;

/**
 * @author maxweld
 *
 */
public class DelegatingConverterFactory implements ConverterFactory {

	private Collection<ConverterFactory> converterFactories = new LinkedHashSet<ConverterFactory>();
	
	public Converter getConverter(ConverterMap requestConverterMap) throws ConverterFactoryException {
		
		DelegatingConverter converter = new DelegatingConverter();
		DelegatingException exception = new DelegatingException();
		
		for(ConverterFactory converterFactory : converterFactories) {
			try {
				converter.add(converterFactory.getConverter(requestConverterMap));
			}
			catch(ConverterFactoryException e) {
				exception.add(e);
			}
		}
		
		if(converter.getConverters().isEmpty()) {
			throw new ConverterFactoryException("Delegating converter factory unable to get converter for request.", exception);
		}
		
		return converter;
	}

	public void add(ConverterFactory converterFactory) {
		if(converterFactory != null) {
			converterFactories.add(converterFactory);
		}
	}
	
	public void remove(ConverterFactory converterFactory) {
		converterFactories.remove(converterFactory);
	}

	public Collection<ConverterFactory> getConverterFactories() {
		return converterFactories;
	}

	public void setConverterFactories(Collection<ConverterFactory> converterFactories) {
		if(converterFactories != null) {
			this.converterFactories = converterFactories;
		}
	}

	protected class DelegatingConverter extends AbstractDelegatingConverter {
		//  Expose this abstract class. //
	}
}
