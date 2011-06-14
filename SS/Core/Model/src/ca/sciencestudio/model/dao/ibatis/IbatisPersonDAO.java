/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     IbatisPersonDAO class.
 *     
 */
package ca.sciencestudio.model.dao.ibatis;

import ca.sciencestudio.model.Person;
import ca.sciencestudio.model.dao.PersonDAO;
import ca.sciencestudio.model.dao.ibatis.support.AbstractIbatisModelDAO;
import ca.sciencestudio.model.dao.ibatis.support.IbatisPerson;
import ca.sciencestudio.model.utilities.GID;

/**
 * @author maxweld
 * 
 *
 */
public class IbatisPersonDAO extends AbstractIbatisModelDAO<Person, IbatisPerson> implements PersonDAO {

	@Override
	public String getGidType() {
		return Person.GID_TYPE;
	}
	
	@Override
	protected IbatisPerson toIbatisModel(Person person) {
		IbatisPerson ibatisPerson = new IbatisPerson();
		GID gid = GID.parse(person.getGid());
		if(gid != null) { ibatisPerson.setId(gid.getId()); }
		ibatisPerson.setTitle(person.getTitle());
		ibatisPerson.setFirstName(person.getFirstName());
		ibatisPerson.setMiddleName(person.getMiddleName());
		ibatisPerson.setLastName(person.getLastName());
		ibatisPerson.setPhoneNumber(person.getPhoneNumber());
		ibatisPerson.setMobileNumber(person.getMobileNumber());
		ibatisPerson.setEmailAddress(person.getEmailAddress());
		ibatisPerson.setModificationDate(person.getModificationDate());
		return ibatisPerson;
	}
	
	@Override
	protected Person toModel(IbatisPerson ibatisPerson) {
		if(ibatisPerson == null) {
			return null;
		}
		Person person = new Person();
		person.setGid(GID.format(getGidFacility(), ibatisPerson.getId(), getGidType()));
		person.setTitle(ibatisPerson.getTitle());
		person.setFirstName(ibatisPerson.getFirstName());
		person.setMiddleName(ibatisPerson.getMiddleName());
		person.setLastName(ibatisPerson.getLastName());
		person.setPhoneNumber(ibatisPerson.getPhoneNumber());
		person.setMobileNumber(ibatisPerson.getMobileNumber());
		person.setEmailAddress(ibatisPerson.getEmailAddress());
		person.setModificationDate(ibatisPerson.getModificationDate());
		return person;
	}

	@Override
	protected String getStatementName(String prefix, String suffix) {
		return prefix + "Person" + suffix;
	}
}
