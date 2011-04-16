/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     IbatisAccount class.
 *     
 */
package ca.sciencestudio.login.model.ibatis;

import java.util.Date;

import ca.sciencestudio.login.model.Account;

/**
 * @author maxweld
 *
 */
public class IbatisAccount implements Account {

	private int id;
	private String username;
	private String password;
	private String personUid;
	private Date creationDate;
	private Status status;
	
	public String getStatusString() {
		return status.name();
	}
	public void setStatusString(String status) {
		try {
			this.status = Status.valueOf(status.toUpperCase());
		}
		catch(Exception e) {
			this.status = Status.UNKNOWN;
		}
	}
	
	@Override
	public int getId() {
		return id;
	}
	@Override
	public void setId(int id) {
		this.id = id;
	}
	
	@Override
	public String getUsername() {
		return username;
	}
	@Override
	public void setUsername(String username) {
		this.username = username;
	}
	
	@Override
	public String getPassword() {
		return password;
	}
	@Override
	public void setPassword(String password) {
		this.password = password;
	}
	
	@Override
	public String getPersonUid() {
		return personUid;
	}
	@Override
	public void setPersonUid(String personUid) {
		this.personUid = personUid;
	}
	
	@Override
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	
	@Override
	public Status getStatus() {
		return status;
	}
	@Override
	public void setStatus(Status status) {
		this.status = status;
	}
}
