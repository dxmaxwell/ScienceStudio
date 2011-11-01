/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *    FutureData interface.
 *     
 */
package ca.sciencestudio.model.dao;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author maxweld
 *
 *
 */
public interface FutureData<D> extends Data<D> {

	public D get(long timeout, TimeUnit unit) throws TimeoutException;
}
