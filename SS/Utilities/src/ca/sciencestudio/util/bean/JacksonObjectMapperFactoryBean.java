/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     JacksonObjectMapperFactoryBean class.
 *
 */
package ca.sciencestudio.util.bean;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.springframework.beans.factory.FactoryBean;

/**
 * @author maxweld
 *
 */
public class JacksonObjectMapperFactoryBean implements FactoryBean<ObjectMapper> {

	private ObjectMapper objectMapper = null;
	
	private boolean writeDatesAsTimestamps = true;
	
	@Override
	public ObjectMapper getObject() throws Exception {
		if(objectMapper == null) {
			objectMapper = new ObjectMapper();
			objectMapper.configure(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS, writeDatesAsTimestamps);
		}
		return objectMapper;
	}

	@Override
	public Class<?> getObjectType() {
		return ObjectMapper.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	public boolean isWriteDatesAsTimestamps() {
		return writeDatesAsTimestamps;
	}
	public void setWriteDatesAsTimestamps(boolean writeDatesAsTimestamps) {
		this.writeDatesAsTimestamps = writeDatesAsTimestamps;
	}
}
