/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     LoginGroup class.
 *     
 */
package ca.sciencestudio.login.model;

import ca.sciencestudio.login.model.Model;

/**
 * @author maxweld
 *
 */
public final class LoginGroup implements Model {

	private static final long serialVersionUID = 1L;
	
	public static final int DEFAULT_ID = 0;
	public static final String DEFAULT_NAME = "";
	
	private int id = DEFAULT_ID;
	private String name = DEFAULT_NAME;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
