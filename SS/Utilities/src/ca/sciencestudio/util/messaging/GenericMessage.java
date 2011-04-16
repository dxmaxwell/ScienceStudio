/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 * 		GenericMessage class.
 *     
 */
package ca.sciencestudio.util.messaging;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

/**
 * @author maxweld
 *
 */
public class GenericMessage<T extends Serializable> implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private T value;
	
	private String id = UUID.randomUUID().toString();
	private Date timestamp = new Date();
	
	public T getValue() {
		return value;
	}
	public void setValue(T value) {
		this.value = value;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Date getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
}
