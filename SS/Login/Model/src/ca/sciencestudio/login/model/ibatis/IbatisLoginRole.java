/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     IbatisLoginRole class.
 *     
 */
package ca.sciencestudio.login.model.ibatis;

import ca.sciencestudio.login.model.LoginRole;

/**
 * @author maxweld
 *
 */
public class IbatisLoginRole implements LoginRole {

	private static final long serialVersionUID = 1L;
	
	private int id;
	private String name;
	
	@Override
	public int getId() {
		return id;
	}
	@Override
	public void setId(int id) {
		this.id = id;
	}
	
	@Override
	public String getName() {
		return name;
	}
	@Override
	public void setName(String name) {
		this.name = name;
	}
}
