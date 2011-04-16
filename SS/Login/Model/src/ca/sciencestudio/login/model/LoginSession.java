/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     LoginSession implementation.
 *     
 */
package ca.sciencestudio.login.model;

import java.io.Serializable;
import java.util.Date;

/**
 * @author maxweld
 *  
 */
public class LoginSession implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String id;
	private Object session;
	private Date timestamp;

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public Object getSession() {
		return session;
	}
	public void setSession(Object session) {
		this.session = session;
	}

	public Date getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
}
