/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *      AbstractSessionController class.	     
 */
package ca.sciencestudio.nanofab.service.controllers;

import java.util.Map;

import ca.sciencestudio.util.net.TunnelManager;
import ca.sciencestudio.nanofab.state.NanofabSessionStateMap;

/**
 * @author maxweld
 *
 */
public abstract class AbstractSessionController {
	
	protected static final String DEFAULT_VNC_PASSWORD = "";
	protected static final int DEFAULT_VNC_WIDTH = 800;
	protected static final int DEFAULT_VNC_HEIGHT = 600;
	
	protected static final String MODEL_KEY_VNC_PORT = "vncPort";
	protected static final String MODEL_KEY_VNC_PASSWORD = "vncPassword";
	protected static final String MODEL_KEY_VNC_WIDTH = "vncWidth";
	protected static final String MODEL_KEY_VNC_HEIGHT = "vncHeight";
	
	protected static final String MODEL_KEY_ERROR = "error";
	protected static final String MODEL_KEY_ERROR_MESSAGE = "errorMessage";
	
	protected static final String SUCCESS_VIEW = "page/vnc";
	protected static final String FAILURE_VIEW = "page/error";
	
	protected static final int VNC_EXTRA_WIDTH = 0;
	protected static final int VNC_EXTRA_HEIGHT = 25;
	
	protected TunnelManager tunnelManager;
	protected NanofabSessionStateMap nanofabSessionStateMap;
	
	private String vncPassword = DEFAULT_VNC_PASSWORD;
	private int vncWidth = DEFAULT_VNC_WIDTH;
	private int vncHeight = DEFAULT_VNC_HEIGHT;
	
	protected String onSuccess(int localPort, Map<String,Object> model) {		
		model.put(MODEL_KEY_VNC_PORT, localPort);
		model.put(MODEL_KEY_VNC_PASSWORD, vncPassword);
		model.put(MODEL_KEY_VNC_WIDTH, vncWidth + VNC_EXTRA_WIDTH);
		model.put(MODEL_KEY_VNC_HEIGHT, vncHeight + VNC_EXTRA_HEIGHT);
		return SUCCESS_VIEW;
	}
	
	protected String onFailure(String error, String errorMessage, Map<String,Object> model) {
		model.put(MODEL_KEY_ERROR, error);
		model.put(MODEL_KEY_ERROR_MESSAGE, errorMessage);
		return FAILURE_VIEW;
	}
	
	public TunnelManager getTunnelManager() {
		return tunnelManager;
	}
	public void setTunnelManager(TunnelManager tunnelManager) {
		this.tunnelManager = tunnelManager;
	}
	
	public NanofabSessionStateMap getNanofabSessionStateMap() {
		return nanofabSessionStateMap;
	}
	public void setNanofabSessionStateMap(NanofabSessionStateMap nanofabSessionStateMap) {
		this.nanofabSessionStateMap = nanofabSessionStateMap;
	}
	
	public String getVncPassword() {
		return vncPassword;
	}
	public void setVncPassword(String vncPassword) {
		this.vncPassword = vncPassword;
	}

	public int getVncWidth() {
		return vncWidth;
	}
	public void setVncWidth(int vncWidth) {
		this.vncWidth = vncWidth;
	}

	public int getVncHeight() {
		return vncHeight;
	}
	public void setVncHeight(int vncHeight) {
		this.vncHeight = vncHeight;
	}	
}
