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
import org.springframework.orm.ibatis.SqlMapClientTemplate;
import org.springframework.validation.Errors;

import ca.sciencestudio.model.Model;
import ca.sciencestudio.model.dao.support.AbstractModelDAO;
import ca.sciencestudio.model.utilities.GID;
import ca.sciencestudio.model.validators.ModelValidator;

/**
 * @author maxweld
 *
 */
public abstract class AbstractIbatisModelDAO<T extends Model, I> extends AbstractModelDAO<T> {

	private String gidFacility = GID.DEFAULT_FACILITY;
	private SqlMapClientTemplate sqlMapClientTemplate;

	@Override
	public Errors add(T t) {
		Errors errors = getValidator().validate(t);
		if(errors.hasErrors()) {
			return errors;
		}
		
		Integer id;
		try {
			id = (Integer) getSqlMapClientTemplate().insert(getStatementName("add"), toIbatisModel(t));
		}
		catch(DataAccessException e) {
			logger.warn("Access error while adding Model", e);
			errors.reject(DataAccessException.class.getSimpleName(), e.getMessage());
			return errors;
		}
		
		t.setGid(GID.format(getGidFacility(), id, getGidType()));
		if(logger.isDebugEnabled()) {
			logger.debug("Add Model with GID: " + t.getGid());
		}
		return errors;
	}
	
	@Override
	public Errors edit(T t) {
		Errors errors = getValidator().validate(t);
		if(errors.hasErrors()) {
			return errors;
		}
		
		GID gid = GID.parse(t.getGid());
		if(!gid.isLocal() && !gid.isFacility(getGidFacility())) {
			errors.rejectValue("gid", ModelValidator.EC_FACILITY, "GID field is neither local or '" + getGidFacility() + "'.");
			return errors;
		}
		if(!gid.isTypeless() && !gid.isType(getGidType())) {
			errors.rejectValue("gid", ModelValidator.EC_TYPE, "GID field is neither typeless or '" + getGidType()+ "'.");
			return errors;
		}
		
		try {
			getSqlMapClientTemplate().update(getStatementName("edit"),  toIbatisModel(t), 1);
		}
		catch(DataAccessException e) {
			logger.warn("Access error while editing Model with GID: " + t.getGid(), e);
			errors.reject(DataAccessException.class.getSimpleName(), e.getMessage());
			return errors;
		}
		
		if(logger.isDebugEnabled()) {
			logger.debug("Edit Model with GID: " + t.getGid());
		}
		return errors;
	}
	
	@Override
	public boolean remove(Object modelGid){
		GID gid = GID.parse(modelGid.toString());
		if(!gid.isLocal() && !gid.isFacility(getGidFacility())) {
			return false;
		}
		if(!gid.isTypeless() && !gid.isType(getGidType())) {
			return false;
		}
		
		try {
			getSqlMapClientTemplate().delete(getStatementName("remove"), gid.getId(), 1);
		}
		catch(DataAccessException e) {
			logger.warn("Access error while removing Model with GID: " + modelGid, e);
			return false;
		}
		
		if(logger.isDebugEnabled()) {
			logger.debug("Remove Model with GID: " + modelGid);
		}
		return true;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public T get(Object modelGid) {
		GID gid = GID.parse(modelGid.toString());
		if(!gid.isLocal() && !gid.isFacility(getGidFacility())) {
			return null;
		}
		if(!gid.isTypeless() && !gid.isType(getGidType())) {
			return null;
		}
		
		T t;
		try {
			t = toModel((I)getSqlMapClientTemplate().queryForObject(getStatementName("get", "ById"), gid.getId()));
		}
		catch(DataAccessException e) {
			logger.warn("Access error while getting Model with GID: " + modelGid, e);
			return null;
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
			logger.warn("Access error while getting Model list", e);
			return Collections.emptyList();
		}
		
		if(logger.isDebugEnabled()) {
			logger.debug("Get all Models, size: " + ts.size());
		}
		return Collections.unmodifiableList(ts);
	} 
	
	
	protected List<T> toModelList(List<I> iList) {
		List<T> modelList = new ArrayList<T>();
		for(I i : iList) {
			modelList.add(toModel(i));
		}
		return modelList;
	}
	
	protected String getStatementName(String prefix) {
		return getStatementName(prefix, "");
	}
	
	protected abstract String getStatementName(String prefix, String suffix);
	
	protected abstract T toModel(I i);
	
	protected abstract I toIbatisModel(T t);
	
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
