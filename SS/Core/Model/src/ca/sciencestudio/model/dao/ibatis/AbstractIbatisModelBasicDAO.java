/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     AbstractIbatisModelBasicDAO abstract class.
 *     
 */
package ca.sciencestudio.model.dao.ibatis;

import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.JdbcUpdateAffectedIncorrectNumberOfRowsException;
import org.springframework.orm.ibatis.SqlMapClientTemplate;

import ca.sciencestudio.model.Model;
import ca.sciencestudio.model.dao.AbstractModelDAO;
import ca.sciencestudio.model.dao.ModelBasicDAO;
import ca.sciencestudio.util.exceptions.ModelAccessException;
import ca.sciencestudio.model.utilities.GID;

/**
 * @author maxweld
 *
 */
public abstract class AbstractIbatisModelBasicDAO<T extends Model> extends AbstractModelDAO<T> implements ModelBasicDAO<T> {

	private String gidFacility = GID.DEFAULT_FACILITY;
	
	private SqlMapClientTemplate sqlMapClientTemplate;
	
	@Override
	public void add(T t) {
		Integer id;
		try {
			id = (Integer) getSqlMapClientTemplate().insert(getStatementName("add"), toIbatisModel(t));
		}
		//// TODO: Catch less serious exceptions and just return 'false'. //
		catch(DataAccessException e) {
			logger.warn("Data Access exception while adding Model: " + e.getMessage());
			throw new ModelAccessException(e);
		}
		
		t.setGid(GID.format(getGidFacility(), id, getGidType()));
		if(logger.isDebugEnabled()) {
			logger.debug("Add Model with GID: " + t.getGid());
		}
	}
	
	@Override
	public boolean edit(T t) {
		GID gid = parseAndCheckGid(t.getGid());
		if(gid == null) {
			return false;
		}
		
		try {
			getSqlMapClientTemplate().update(getStatementName("edit"),  toIbatisModel(t), 1);
		}
		catch(JdbcUpdateAffectedIncorrectNumberOfRowsException e) {
			logger.debug("Update affected incorrect number of rows: " + e.getMessage());
			return false;
		}
		catch(DataAccessException e) {
			logger.warn("Data Access exception while editing Model: " +  e.getMessage());
			throw new ModelAccessException(e);
		}
		
		if(logger.isDebugEnabled()) {
			logger.debug("Edit Model with GID: " + t.getGid());
		}
		return true;
	}
	
	@Override
	public boolean remove(String modelGid){
		GID gid = parseAndCheckGid(modelGid);
		if(gid == null) {
			return false;
		}
		
		try {
			getSqlMapClientTemplate().delete(getStatementName("remove"), gid.getId(), 1);
		}
		catch(JdbcUpdateAffectedIncorrectNumberOfRowsException e) {
			logger.debug("Delete affected incorrect number of rows: " + e.getMessage());
			return false;
		}
		catch(DataAccessException e) {
			logger.warn("Data Access exception while removing Model: " + e.getMessage());
			throw new ModelAccessException(e);
		}
		
		if(logger.isDebugEnabled()) {
			logger.debug("Remove Model with GID: " + modelGid);
		}
		return true;
	}
	
	@Override
	public T get(String modelGid) {
		GID gid = parseAndCheckGid(modelGid);
		if(gid == null) {
			return null;
		}
		
		T t;
		try {
			t = toModel(getSqlMapClientTemplate().queryForObject(getStatementName("get", "ById"), gid.getId()));
		}
		catch(DataAccessException e) {
			logger.warn("Data Access exception while getting Model: " + e.getMessage());
			throw new ModelAccessException(e);
		}
		
		if(logger.isDebugEnabled()) {
			logger.debug("Get Model with GID: " + modelGid);
		}
		return t;
	}
	
	@Override
	public List<T> getAll() {
		List<T> ts;
		try {
			ts = toModelList(getSqlMapClientTemplate().queryForList(getStatementName("get", "List")));
		}
		catch(DataAccessException e) {
			logger.warn("Data Access exception while getting Model list: " + e.getMessage());
			throw new ModelAccessException(e);
		}
		
		if(logger.isDebugEnabled()) {
			logger.debug("Get all Models, size: " + ts.size());
		}
		return Collections.unmodifiableList(ts);
	}
	
	protected GID parseAndCheckGid(String modelGid) {
		return parseAndCheckGid(modelGid, getGidFacility(), getGidType());
	}
	
	protected GID parseAndCheckGid(String modelGid, String facility, String type) {
		if(modelGid == null) {
			logger.debug("GID format exception: Source object is null.");
			return null;
		}
		GID gid = GID.parse(modelGid);
		if(gid == null) {
			logger.debug("GID format exception: Source string is invalid.");
			return null;
		}
		if(!gid.isTypeless() && !gid.isType(type)) {
			if(logger.isDebugEnabled()) {
				logger.debug("Type is not supported. (" + gid.getType() + " != " + type + ")");
			}
			return null;
		}
		if(!gid.isLocal() && !gid.isFacility(facility)) {
			if(logger.isDebugEnabled()) {
				
				logger.debug("Facility is not supported. (" + gid.getFacility() + " != " + facility + ")");
			}
			return null;
		}
		return gid;
	}
	
	protected List<T> toModelList(Collection<?> objects) {
		List<T> modelList = new ArrayList<T>();
		for(Object obj : objects) {
			modelList.add(toModel(obj));
		}
		return modelList;
	}
	
	protected abstract T toModel(Object obj);
	
	protected String getStatementName(String prefix) {
		return getStatementName(prefix, "");
	}
	
	protected abstract String getStatementName(String prefix, String suffix);
	
	protected abstract Object toIbatisModel(T t);
	
	public abstract String getGidType();
	
	public String getGidFacility() {
		return gidFacility;
	}
	public void setGidFacility(String gidFacility) {
		this.gidFacility = gidFacility;
	}

	public SqlMapClientTemplate getSqlMapClientTemplate() {
		return sqlMapClientTemplate;
	}
	public void setSqlMapClientTemplate(SqlMapClientTemplate sqlMapClientTemplate) {
		this.sqlMapClientTemplate = sqlMapClientTemplate;
	}	
}
