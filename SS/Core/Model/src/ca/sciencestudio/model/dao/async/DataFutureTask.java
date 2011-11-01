/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *    DataFutureTask class.
 *     
 */
package ca.sciencestudio.model.dao.async;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.ExecutionException;

import ca.sciencestudio.model.dao.FutureData;
import ca.sciencestudio.util.exceptions.ModelAccessException;

/**
 * @author maxweld
 *
 *
 */
public class DataFutureTask<D> extends FutureTask<D> implements FutureData<D> {
	
	public DataFutureTask(Callable<D> callable) {
		super(callable);
	};
	
	@Override
	public boolean available() {
		return isDone();
	}

	@Override
	public D get() {
		try {
			return super.get();
		}
		catch(ExecutionException e) {
			if(e.getCause() instanceof RuntimeException) {
				throw (RuntimeException) e.getCause();
			} else {
				throw new ModelAccessException(e);
			}
		}
		catch(InterruptedException e) {
			throw new ModelAccessException(e);
		}
	}
	
	@Override
	public D get(long timeout, TimeUnit unit) throws TimeoutException {
		try {
			return super.get(timeout, unit);
		}
		catch(ExecutionException e) {
			if(e.getCause() instanceof RuntimeException) {
				throw (RuntimeException) e.getCause();
			} else {
				throw new ModelAccessException(e);
			}
		}
		catch(InterruptedException e) {
			throw new ModelAccessException(e);
		}
	}
}
