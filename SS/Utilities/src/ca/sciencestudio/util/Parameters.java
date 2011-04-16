/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     Parameters class.
 *     
 */
package ca.sciencestudio.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.InvalidPropertiesFormatException;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * @author maxweld
 *
 */
public class Parameters extends Properties {

	private static final long serialVersionUID = 1L;
	
	private boolean modified;
	
	public Parameters() {
		super();
		modified = false;
	}
	
	public Parameters(Properties defaults) {
		super(defaults);
		modified = false;
	}
	
	public Parameters(Parameters defaults) {
		super(defaults);
		modified = defaults.isModified();
	}
	
	public Parameters(Map<? extends String, ? extends String> defaults) {
		putAll(defaults);
		modified = false;
	}
	
	public boolean isModified() {
		return modified;
	}
	
	public void setModified(boolean modified) {
		this.modified = modified;
	}
	
	public String getParameter(String key) {
		return getProperty(key);
	}
	
	public String getParameter(String key, String defaultValue) {
		return getProperty(key, defaultValue);
	}
	
	public String setParameter(String key, String value) {
		Object obj = setProperty(key, value);
		if(obj != null) {
			return obj.toString();
		} else {
			return null;
		}
	}
	
	public Set<String> parameterNames() {
		return stringPropertyNames();
	}
	
	@Override
	@Deprecated
	public String getProperty(String key) {
		return super.getProperty(key);
	}

	@Override
	@Deprecated
	public String getProperty(String key, String defaultValue) {
		return super.getProperty(key, defaultValue);
	}
	
	@Override
	@Deprecated
	public synchronized Object setProperty(String key, String value) {
		Object obj = super.setProperty(key, value);
		modified = true;
		return obj;
	}

	@Override
	@Deprecated
	public Enumeration<?> propertyNames() {
		return super.propertyNames();
	}

	@Override
	@Deprecated
	public Set<String> stringPropertyNames() {
		return super.stringPropertyNames();
	}

	@Override
	@Deprecated
	public Object get(Object key) {
		return super.get(key);
	}
	
	@Override
	@Deprecated
	public Object put(Object key, Object value) {
		return super.put(key, value);
	}
	
	@Override
	public synchronized void load(InputStream inStream) throws IOException {
		super.load(inStream);
		modified = false;
	}

	@Override
	public synchronized void loadFromXML(InputStream in) throws IOException, InvalidPropertiesFormatException {
		super.loadFromXML(in);
		modified = false;
	}

	@Override
	@SuppressWarnings("deprecation")
	public synchronized void save(OutputStream out, String comments) {
		super.save(out, comments);
		modified = false;
	}

	@Override
	public synchronized void store(OutputStream out, String comments) throws IOException {
		super.store(out, comments);
		modified = false;
	}

	@Override
	public synchronized void storeToXML(OutputStream os, String comment) throws IOException {
		super.storeToXML(os, comment);
		modified = false;
	}

	@Override
	public synchronized void storeToXML(OutputStream os, String comment, String encoding) throws IOException {
		super.storeToXML(os, comment, encoding);
		modified = false;
	}
}
