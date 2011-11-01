/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     IbatisAccount class.
 *     
 */
package ca.sciencestudio.model.person.dao.ibatis.support;

import java.util.Date;

import ca.sciencestudio.model.person.Account;

public class IbatisAccount {

	public static final int DEFAULT_ID = 0;
	public static final int DEFAULT_PERSON_ID = 0;
	
	private int id = DEFAULT_ID;
	private String username = Account.DEFAULT_USERNAME;
	private String password = Account.DEFAULT_PASSWORD;
	private int personId = DEFAULT_PERSON_ID;
	private String status = Account.DEFAULT_STATUS.name();
	private Date creationDate = Account.DEFAULT_CREATION_DATE;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public int getPersonId() {
		return personId;
	}
	public void setPersonId(int personId) {
		this.personId = personId;
	}
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
}
