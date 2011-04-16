/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     SqlMapParameters class.
 *     
 */

package ca.sciencestudio.util.sql;

/**
 * @author medrand
 *
 */
public class SqlMapParameters {
	private Object param1;
	private Object param2;
	private Object param3;
	
	public SqlMapParameters() {
		this(null, null, null);
	}
	
	public SqlMapParameters(Object param1) {
		this(param1, null, null);
	}
	
	public SqlMapParameters(Object param1, Object param2) {
		this(param1, param2, null);
	}
	
	public SqlMapParameters(Object param1, Object param2, Object param3) {
		this.param1 = param1;
		this.param2 = param2;
		this.param3 = param3;
	}
		
	public Object getParam1() {
		return param1;
	}
	public void setParam1(Object param1) {
		this.param1 = param1;
	}
	public Object getParam2() {
		return param2;
	}
	public void setParam2(Object param2) {
		this.param2 = param2;
	}
	public Object getParam3() {
		return param3;
	}
	public void setParam3(Object param3) {
		this.param3 = param3;
	}
}
