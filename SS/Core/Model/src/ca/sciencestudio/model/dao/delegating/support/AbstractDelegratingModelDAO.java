/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     AbstractDelegratingModelDAO abstract class.
 *     
 */
package ca.sciencestudio.model.dao.delegating.support;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ca.sciencestudio.model.Model;
import ca.sciencestudio.model.dao.ModelDAO;
import ca.sciencestudio.model.dao.support.AbstractModelDAO;
import ca.sciencestudio.model.dao.support.ModelAccessException;

/**
 * @author maxweld
 *
 *
 */
public abstract class AbstractDelegratingModelDAO<T extends Model, D extends ModelDAO<T>> extends AbstractModelDAO<T> {

	private Collection<D> modelDAOs;
	
	@Override
	public boolean add(T t) throws ModelAccessException {
		return add(t, null);
	}

	@Override
	public boolean add(T t, String facility) {
		boolean modelAccessException = false;
		for(D d : modelDAOs) {
			try {
				boolean success;
				if(facility == null) {
					success = d.add(t);
				} else {
					success = d.add(t, facility);
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
	public boolean edit(T t) {
		boolean modelAccessException = false;
		for(D d : modelDAOs) {
			try {
				boolean success = d.edit(t);
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
	public boolean remove(Object gid) {
		boolean modelAccessException = false;
		for(D d : modelDAOs) {
			try {
				boolean success = d.remove(gid);
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
	public T get(Object gid) {
		boolean modelAccessException = false;
		for(D d : modelDAOs) {
			try {
				T t = d.get(gid);
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
	public List<T> getAll() {
		boolean modelAccessException = true;
		List<T> models = new ArrayList<T>();
		for(D d : modelDAOs) {
			try {
				models.addAll(d.getAll());
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
