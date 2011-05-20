/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     AbstractIbatisModelDAO abstract class.
 *     
 */
package ca.sciencestudio.model.dao.ibatis.support;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.JdbcUpdateAffectedIncorrectNumberOfRowsException;
import org.springframework.orm.ibatis.SqlMapClientTemplate;

import ca.sciencestudio.model.Model;
import ca.sciencestudio.model.dao.support.AbstractModelDAO;
import ca.sciencestudio.model.dao.support.ModelAccessException;
import ca.sciencestudio.model.utilities.GID;

/**
 * @author maxweld
 *
 */
public abstract class AbstractIbatisModelDAO<T extends Model, I> extends AbstractModelDAO<T> {

	private String facility = GID.DEFAULT_FACILITY;
	
	private SqlMapClientTemplate sqlMapClientTemplate;

	@Override
	public boolean add(T t) {
		return add(t, getFacility());
	}
	
	@Override
	public boolean add(T t, String facility) {
		if((facility == null) || !facility.equalsIgnoreCase(getFacility())) {
			return false;
		}
		
		Integer id;
		try {
			id = (Integer) getSqlMapClientTemplate().insert(getStatementName("add"), toIbatisModel(t));
		}
		// TODO: Catch less serious exceptions and just return 'false'. //
		catch(DataAccessException e) {
			logger.warn("Data Access exception while adding Model: " + e.getMessage());
			throw new ModelAccessException(e);
		}
		
		t.setGid(GID.format(getFacility(), id, getType()));
		if(logger.isDebugEnabled()) {
			logger.debug("Add Model with GID: " + t.getGid());
		}
		return true;
	}
	
	@Override
	public boolean edit(T t) {
		GID gid = parseAndCheck(t.getGid());
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
	public boolean remove(Object modelGid){
		GID gid = parseAndCheck(modelGid);
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
	@SuppressWarnings("unchecked")
	public T get(Object modelGid) {
		GID gid = parseAndCheck(modelGid);
		if(gid == null) {
			return null;
		}
		
		T t;
		try {
			t = toModel((I)getSqlMapClientTemplate().queryForObject(getStatementName("get", "ById"), gid.getId()));
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
	@SuppressWarnings("unchecked")
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
	
	protected GID parseAndCheck(Object obj) {
		if(obj == null) {
			logger.debug("GID format exception: Source object is null.");
			return null;
		}
		GID gid = GID.parse(obj.toString());
		if(gid == null) {
			logger.debug("GID format exception: Source string is invalid.");
			return null;
		}
		if(!gid.isTypeless() && !gid.isType(getType())) {
			if(logger.isDebugEnabled()) {
				logger.debug("Type is not supported. (" + gid.getType() + " != " + getType() + ")");
			}
			return null;
		}
		if(!gid.isLocal() && !gid.isFacility(getFacility())) {
			if(logger.isDebugEnabled()) {
				
				logger.debug("Facility is not supported. (" + gid.getFacility() + " != " + getFacility() + ")");
			}
			return null;
		}
		return gid;
	}
	
	protected List<T> toModelList(List<I> iList) {
		List<T> modelList = new ArrayList<T>();
		for(I i : iList) {
			modelList.add(toModel(i));
		}
		return modelList;
	}
	
	protected abstract T toModel(I i);
	
	protected String getStatementName(String prefix) {
		return getStatementName(prefix, "");
	}
	
	protected abstract String getStatementName(String prefix, String suffix);
	
	protected abstract I toIbatisModel(T t);
	
	public abstract String getType();
	
	public String getFacility() {
		return facility;
	}
	public void setFacility(String facility) {
		this.facility = facility;
	}
	
	public SqlMapClientTemplate getSqlMapClientTemplate() {
		return sqlMapClientTemplate;
	}
	public void setSqlMapClientTemplate(SqlMapClientTemplate sqlMapClientTemplate) {
		this.sqlMapClientTemplate = sqlMapClientTemplate;
	}	
}
