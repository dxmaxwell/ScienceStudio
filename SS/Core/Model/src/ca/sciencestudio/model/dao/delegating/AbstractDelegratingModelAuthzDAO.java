/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     AbstractDelegratingModelDAO abstract class.
 *     
 */
package ca.sciencestudio.model.dao.delegating;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ca.sciencestudio.model.Model;
import ca.sciencestudio.model.dao.AbstractModelDAO;
import ca.sciencestudio.model.dao.ModelAuthzDAO;
import ca.sciencestudio.util.exceptions.ModelAccessException;

/**
 * @author maxweld
 *
 *
 */
public abstract class AbstractDelegratingModelAuthzDAO<T extends Model, D extends ModelAuthzDAO<T>> extends AbstractModelDAO<T> implements ModelAuthzDAO<T> {

	private Collection<D> modelDAOs;
	
	@Override
	public boolean add(String personGid, T t) throws ModelAccessException {
		return add(personGid, t, null);
	}

	@Override
	public boolean add(String personGid, T t, String facility) {
		boolean modelAccessException = false;
		for(D d : modelDAOs) {
			try {
				boolean success;
				if(facility == null) {
					success = d.add(personGid, t);
				} else {
					success = d.add(personGid, t, facility);
				}
				
				if(success) {
					return true;
				}
			}
			catch(ModelAccessException e) {
				logger.warn("Delegate threw Model Access exception: " + e.getMessage());
				modelAccessException = true;
			}
		}
		if(modelAccessException) {
			throw new ModelAccessException("Model Access exception while delegates adding Model.");
		}
		return false;
	}

	@Override
	public boolean edit(String personGid, T t) {
		boolean modelAccessException = false;
		for(D d : modelDAOs) {
			try {
				boolean success = d.edit(personGid, t);
				if(success) {
					return true;
				}
			}
			catch(ModelAccessException e) {
				logger.warn("Delegate threw Model Access exception: " + e.getMessage());
				modelAccessException = false;
			}
		}
		if(modelAccessException) {
			throw new ModelAccessException("Model Access exception while delegates editing Model.");
		}
		return false;
	}

	@Override
	public boolean remove(String personGid, String gid) {
		boolean modelAccessException = false;
		for(D d : modelDAOs) {
			try {
				boolean success = d.remove(personGid, gid);
				if(success) {
					return true;
				}
			}
			catch(ModelAccessException e) {
				logger.warn("Delegate threw Model Access exception: " + e.getMessage());
				modelAccessException = false;
			}
		}
		if(modelAccessException) {
			throw new ModelAccessException("Model Access exception while delegates removing Model.");
		}
		return false;
	}

	@Override
	public T get(String personGid, String gid) {
		boolean modelAccessException = false;
		for(D d : modelDAOs) {
			try {
				T t = d.get(personGid, gid);
				if(t != null) {
					return t;
				}
			}
			catch(ModelAccessException e) {
				logger.warn("Delegate threw Model Access exception: " + e.getMessage());
				modelAccessException = false;
			}
		}
		if(modelAccessException) {
			throw new ModelAccessException("Model Access exception while delegates getting Model.");
		}
		return null;
	}

	@Override
	public List<T> getAll(String personGid, boolean admin) {
		boolean modelAccessException = true;
		List<T> models = new ArrayList<T>();
		for(D d : modelDAOs) {
			try {
				models.addAll(d.getAll(personGid, admin));
				modelAccessException = false;
			}
			catch(ModelAccessException e) {
				logger.warn("Delegate threw Model Access exception: " + e.getMessage());
			}
		}
		if(modelAccessException) {
			throw new ModelAccessException("Model Access exception while delegates getting all Models.");
		}
		return models;
	}

	public Collection<D> getModelDAOs() {
		return modelDAOs;
	}
	public void setModelDAOs(Collection<D> modelDAOs) {
		this.modelDAOs = modelDAOs;
	}
}
