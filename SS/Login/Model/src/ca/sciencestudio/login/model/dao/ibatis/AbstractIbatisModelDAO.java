/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     AbstractIbatisModelDAO class.
 *     
 */
package ca.sciencestudio.login.model.dao.ibatis;

import java.util.Collections;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.JdbcUpdateAffectedIncorrectNumberOfRowsException;
import org.springframework.orm.ibatis.SqlMapClientTemplate;

import ca.sciencestudio.login.model.Model;
import ca.sciencestudio.login.model.dao.ModelDAO;
import ca.sciencestudio.util.exceptions.ModelAccessException;

/**
 * @author maxweld
 *
 *
 * 
 */
public abstract class AbstractIbatisModelDAO<T extends Model> implements ModelDAO<T> {


	private SqlMapClientTemplate sqlMapClientTemplate;

	protected Log logger = LogFactory.getLog(getClass());
	
	@Override
	public boolean add(T t) {
		
		try {
			getSqlMapClientTemplate().insert(getStatementName("add"), t);
		}
		// TODO: Catch less serious exceptions and just return 'false'. //
		catch(DataAccessException e) {
			logger.warn("Data Access exception while adding Model: " + e.getMessage());
			throw new ModelAccessException(e);
		}
		
		if(logger.isDebugEnabled()) {
			logger.debug("Add Model with ID: " + t.getId());
		}
		return true;
	}

	@Override
	public boolean edit(T t) {
		
		try {
			getSqlMapClientTemplate().update(getStatementName("edit"),  t, 1);
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
			logger.debug("Edit Model with ID: " + t.getId());
		}
		return true;
	}


	@Override
	public boolean remove(int id) {
		try {
			getSqlMapClientTemplate().delete(getStatementName("remove"), id, 1);
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
			logger.debug("Remove Model with ID: " + id);
		}
		return true;
	}

	@Override
	@SuppressWarnings("unchecked")
	public T get(int id) {
		
		T t;
		try {
			t = (T)getSqlMapClientTemplate().queryForObject(getStatementName("get", "ById"), id);
		}
		catch(DataAccessException e) {
			logger.warn("Data Access exception while getting Model: " + e.getMessage());
			throw new ModelAccessException(e);
		}
		
		if(logger.isDebugEnabled()) {
			logger.debug("Get Model with ID: " + id);
		}
		return t;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<T> getAll() {
		
		List<T> ts;
		try {
			ts = getSqlMapClientTemplate().queryForList(getStatementName("get", "List"));
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

	protected String getStatementName(String prefix) {
		return getStatementName(prefix, "");
	}
	
	protected abstract String getStatementName(String prefix, String suffix);
	
	public SqlMapClientTemplate getSqlMapClientTemplate() {
		return sqlMapClientTemplate;
	}
	public void setSqlMapClientTemplate(SqlMapClientTemplate sqlMapClientTemplate) {
		this.sqlMapClientTemplate = sqlMapClientTemplate;
	}
}
