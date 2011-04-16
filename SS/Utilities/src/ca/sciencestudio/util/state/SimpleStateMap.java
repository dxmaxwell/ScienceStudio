/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *      SimpleStateMap class.
 *     
 */
package ca.sciencestudio.util.state;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ca.sciencestudio.util.state.StateMap;
import ca.sciencestudio.util.xml.ContainerToXML;

/**
 * @author maxweld
 *
 */
public class SimpleStateMap implements StateMap {

	public static final String VALUE_KEY_TIMESTAMP = "timestamp";
	
	protected Date timestamp = new Date();
	protected String name = getClass().getSimpleName() + UUID.randomUUID();
	protected Map<String,Serializable> stateMap = new LinkedHashMap<String,Serializable>();
	
	protected Log logger = LogFactory.getLog(getClass());
	
	public SimpleStateMap() {
		this(true);
	}
	
	public SimpleStateMap(boolean synch) {
		if(synch) {
			stateMap = Collections.synchronizedMap(stateMap);
		}
	}

	public SimpleStateMap(StateMap stateMap) {
		this(stateMap, true);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public SimpleStateMap(StateMap stateMap, boolean synch) {
		this((Map)stateMap, synch);
		setName(stateMap.getName());
		setTimestamp(stateMap.getTimestamp()); // <-- Must be after 'putAll'. //
	}
	
	public SimpleStateMap(Map<? extends String, ? extends Serializable> values) {
		this(values, true);
	}
		
	public SimpleStateMap(Map<? extends String, ? extends Serializable> values, boolean synch) {
		this(synch);
		putAll(values);
	}
	
	// StateMap Interface Methods //
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		if(name != null) {
			this.name = name;
		}
	}
	
	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		if(timestamp != null) {
			this.timestamp = timestamp;
		}
	}

	public String toXML() {
		Map<String,Serializable> map = new HashMap<String,Serializable>(stateMap);
		if(!map.containsKey(VALUE_KEY_TIMESTAMP)) {
			map.put(VALUE_KEY_TIMESTAMP, timestamp.getTime());
		}
		return ContainerToXML.mapToXML(map, name);
	}
	
	// Map Interface Methods //
	
	public boolean isEmpty() {
		return stateMap.isEmpty();
	}
	
	public int size() {
		return stateMap.size();
	}
	
	public Serializable get(Object key) {
		return stateMap.get(key);
	}

	public Serializable put(String key, Serializable value) {
		timestamp = new Date();
		return stateMap.put(key, value);
	}
	
	public void putAll(Map<? extends String, ? extends Serializable> map) {
		timestamp = new Date();
		stateMap.putAll(map);
	}
	
	public boolean containsKey(Object key) {
		return stateMap.containsKey(key);
	}

	public boolean containsValue(Object value) {
		return stateMap.containsValue(value);
	}
	
	public Set<String> keySet() {
		return stateMap.keySet();
	}

	public Collection<Serializable> values() {
		return stateMap.values();
	}

	public Set<java.util.Map.Entry<String, Serializable>> entrySet() {
		return stateMap.entrySet();
	}

	public Serializable remove(Object key) {
		return stateMap.remove(key);
	}
	
	public void clear() {
		stateMap.clear();
	}
}
