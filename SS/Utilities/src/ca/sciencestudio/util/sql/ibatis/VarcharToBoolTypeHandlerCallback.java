/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     VarcharToBoolTypeHandlerCallback class.
 *     
 */
package ca.sciencestudio.util.sql.ibatis;

import java.sql.SQLException;

import com.ibatis.sqlmap.client.extensions.ParameterSetter;
import com.ibatis.sqlmap.client.extensions.ResultGetter;
import com.ibatis.sqlmap.client.extensions.TypeHandlerCallback;

/**
 * @author medrand
 *
 */
public class VarcharToBoolTypeHandlerCallback implements TypeHandlerCallback {

	private static final String TRUE = "true";
	private static final String FALSE = "false";
	
	public Object getResult(ResultGetter getter) throws SQLException {
		String s = getter.getString();
		if(TRUE.equalsIgnoreCase(s)) {
			return new Boolean(true);
		} else if(FALSE.equalsIgnoreCase(s)) {
			return new Boolean(false);
		} else {
			throw new SQLException("Unexpected value " + s + " found where "
					+ TRUE + " or " + FALSE + " was expected.");
		}
	}

	public void setParameter(ParameterSetter setter, Object parameter)
			throws SQLException {
		boolean b = ((Boolean)parameter).booleanValue();
		if(b) {
			setter.setString(TRUE);
		} else  {
			setter.setString(FALSE);
		}
	}

	public Object valueOf(String str) {
		Boolean b = null;
		if(TRUE.equalsIgnoreCase(str)) {
			b = new Boolean(true);
		} else if(FALSE.equalsIgnoreCase(str)) {
			b = new Boolean(false);
		}
		return b;
	}
	
}
