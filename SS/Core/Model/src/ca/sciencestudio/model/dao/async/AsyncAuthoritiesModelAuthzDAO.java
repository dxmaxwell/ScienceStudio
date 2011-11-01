/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *    AsyncAuthoritiesModelAuthzDAO class.
 *     
 */
package ca.sciencestudio.model.dao.async;

import java.util.concurrent.Callable;

import ca.sciencestudio.model.Model;
import ca.sciencestudio.model.dao.Data;
import ca.sciencestudio.model.dao.AuthoritiesDAO;
import ca.sciencestudio.util.authz.Authorities;

public abstract class AsyncAuthoritiesModelAuthzDAO<T extends Model> extends AbstractAsyncModelAuthzDAO<T> implements AuthoritiesDAO {

	@Override
	public Data<Authorities> getAuthorities(String user, String gid) {
		return execute(new DataFutureTask<Authorities>(new AuthoritiesCallable(user, gid)));
	}
	
	protected abstract AuthoritiesDAO getAuthoritiesDAO();
	
	private class AuthoritiesCallable implements Callable<Authorities> {
		
		private String user;
		private String gid;
		
		public AuthoritiesCallable(String user, String gid) {
			this.user = user;
			this.gid = gid;
		}

		@Override
		public Authorities call() throws Exception {
			return getAuthoritiesDAO().getAuthorities(user, gid).get();
		}
	}
}
