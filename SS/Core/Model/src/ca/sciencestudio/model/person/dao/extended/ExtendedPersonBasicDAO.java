/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     ExtendedPersonBasicDAO class.
 *     
 */
package ca.sciencestudio.model.person.dao.extended;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ca.sciencestudio.model.person.Person;
import ca.sciencestudio.model.person.dao.PersonBasicDAO;
import ca.sciencestudio.model.utilities.GID;
import ca.sciencestudio.util.exceptions.ModelAccessException;

/**
 * @author maxweld
 * 
 *
 */
public class ExtendedPersonBasicDAO implements PersonBasicDAO {
	
	private int extension = 900000;
	private PersonBasicDAO standardPersonBasicDAO;
	private PersonBasicDAO extendedPersonBasicDAO;
	
	protected Log logger = LogFactory.getLog(getClass());
	
	@Override
	public String getGidType() {
		return standardPersonBasicDAO.getGidType();
	}
	
	@Override
	public String getGidFacility() {
		return standardPersonBasicDAO.getGidFacility();
	}
	
	@Override
	public void add(Person person) {
		extendedPersonBasicDAO.add(person);
		GID gid = GID.parse(person.getGid());
		if(gid == null) {
			ModelAccessException e = new ModelAccessException("Data Access exception while adding Person: Invalid GID!");
			logger.warn("Invalid Person GID unabled to extend.", e);
			throw e;
		}
		gid.setId(gid.getId() + extension);
		person.setGid(gid.toString());
	}
	
	@Override
	public boolean edit(Person person) {
		GID gid = GID.parse(person.getGid());
		if((gid != null) && (gid.getId() > extension)) {
			gid.setId(gid.getId() - extension);
			person.setGid(gid.toString());
			boolean rv = extendedPersonBasicDAO.edit(person);
			gid.setId(gid.getId() + extension);
			person.setGid(gid.toString());
			return rv;
		}
		return standardPersonBasicDAO.edit(person);
	}
	
	@Override
	public boolean remove(String gid) {
		GID g = GID.parse(gid);
		if((g != null) && (g.getId() > extension)) {
			g.setId(g.getId() - extension);
			return extendedPersonBasicDAO.remove(g.toString());
		}
		return standardPersonBasicDAO.remove(gid);
	}
	
	@Override
	public Person get(String gid) {
		GID g = GID.parse(gid);
		if((g != null) && (g.getId() > extension)) {
			g.setId(g.getId() - extension);
			Person person = extendedPersonBasicDAO.get(g.toString());
			if(person != null) {
				g.setId(g.getId() + extension);
				person.setGid(g.toString());
			}
			return person;
		}
		return standardPersonBasicDAO.get(gid);
	}
	
	@Override
	public Person getByUsername(String username, String facility) {
		Person person  = standardPersonBasicDAO.getByUsername(username, facility);
		if(person != null) {
			return person;
		}
		person = extendedPersonBasicDAO.getByUsername(username, facility);
		if(person != null) {
			GID gid = GID.parse(person.getGid());
			if(gid != null) {
				gid.setId(gid.getId() + extension);
				person.setGid(gid.toString());
				return person;
			}
		}		
		return null;
	}
	
	@Override
	public List<Person> getAll() {
		List<Person> stdPersons = standardPersonBasicDAO.getAll();
		List<Person> extPersons = extendedPersonBasicDAO.getAll();
		List<Person> persons = new ArrayList<Person>(stdPersons);
		for(Person person : extPersons) {
			GID gid = GID.parse(person.getGid());
			if(gid != null) {
				gid.setId(gid.getId() + extension);
				person.setGid(gid.toString());
				persons.add(person);
			} else {
				logger.warn("Invalid Person GID unabled to extend.");
			}
		}
		return persons;
	}
	
	@Override
	public List<Person> getAllByName(String name) {
		List<Person> stdPersons = standardPersonBasicDAO.getAllByName(name);
		List<Person> extPersons = extendedPersonBasicDAO.getAllByName(name);
		List<Person> persons = new ArrayList<Person>(stdPersons);
		for(Person person : extPersons) {
			GID gid = GID.parse(person.getGid());
			if(gid != null) {
				gid.setId(gid.getId() + extension);
				person.setGid(gid.toString());
				persons.add(person);
			} else {
				logger.warn("Invalid Person GID unabled to extend.");
			}
		}
		return persons;
	}

	public int getExtension() {
		return extension;
	}
	public void setExtension(int extension) {
		this.extension = extension;
	}

	public PersonBasicDAO getStandardPersonBasicDAO() {
		return standardPersonBasicDAO;
	}
	public void setStandardPersonBasicDAO(PersonBasicDAO standardPersonBasicDAO) {
		this.standardPersonBasicDAO = standardPersonBasicDAO;
	}

	public PersonBasicDAO getExtendedPersonBasicDAO() {
		return extendedPersonBasicDAO;
	}
	public void setExtendedPersonBasicDAO(PersonBasicDAO extendedPersonBasicDAO) {
		this.extendedPersonBasicDAO = extendedPersonBasicDAO;
	}
}
