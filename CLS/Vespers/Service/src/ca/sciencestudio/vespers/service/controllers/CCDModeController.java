/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *	Description:
 *		CCDModeController class.
 *
 */
package ca.sciencestudio.vespers.service.controllers;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ca.sciencestudio.util.state.StateMap;
import ca.sciencestudio.util.web.FormResponseMap;

/**
 * The controller to switch CCD mode
 * <p>
 * The file pv's are set to some default values when the mode is switched. The
 * user still has the freedom the modify them further though.
 * </p>
 * 
 * @author Dong Liu
 * 
 */
@Controller
public class CCDModeController extends AbstractBeamlineAuthzController {

	private static final String VALUE_KEY_MODE = "xrdMode";

	private StateMap ccdCollectionProxy;

	private StateMap ccdFileProxy;

	private String templateScan;
	private String focusTriggerMode;
	private String scanTriggerMode;

	private static final String VALUE_KEY_TRIGGERMODE = "triggerMode";
	private static final String VALUE_KEY_FILENAME = "fileName";
	private static final String VALUE_KEY_FILENUMBER = "fileNumber";
	private static final String VALUE_KEY_FILETEMPLATE = "fileTemplate";
	private static final String VALUE_KEY_AUTOINCREMENT = "autoIncrement";

	@ResponseBody
	@RequestMapping(value = "/ccdmode*", method = RequestMethod.POST)
	public FormResponseMap handleRequest(@RequestParam("mode") String mode, HttpServletResponse response) throws IOException {

		if (!canWriteBeamline()) {
			response.setStatus(HttpStatus.UNAUTHORIZED_401);
			return new FormResponseMap(false, "Not permitted to setup CCD.");
		}

		if (mode != null) {
			if (mode.equals("scan")) {
				beamlineSessionProxy.put(VALUE_KEY_MODE, mode);
				ccdCollectionProxy.put(VALUE_KEY_TRIGGERMODE, new Integer(scanTriggerMode)); // as
				// set some file pv's. The user can change them from the file form.
				Map<String, Serializable> values = new HashMap<String, Serializable>();
				values.put(VALUE_KEY_FILENAME, "scan");
				values.put(VALUE_KEY_FILENUMBER, new Integer(1));
				values.put(VALUE_KEY_FILETEMPLATE, templateScan);
				values.put(VALUE_KEY_AUTOINCREMENT, new Integer(1));
				ccdFileProxy.putAll(values);
			} else if (mode.equals("focus")) {
				beamlineSessionProxy.put(VALUE_KEY_MODE, mode);
				ccdCollectionProxy.put(VALUE_KEY_TRIGGERMODE, new Integer(focusTriggerMode)); // as
			} else {
				response.setStatus(HttpStatus.BAD_REQUEST_400);
				return new FormResponseMap(false, "Wrong parameter value.");
			}
		} else {
			response.setStatus(HttpStatus.BAD_REQUEST_400);
			return new FormResponseMap(false, "Parameter not found.");
		}

		return new FormResponseMap(true, "Set mode.");

	}

	public void setTemplateScan(String templateScan) {
		this.templateScan = templateScan;
	}

	public void setFocusTriggerMode(String focusTriggerMode) {
		this.focusTriggerMode = focusTriggerMode;
	}

	public void setScanTriggerMode(String scanTriggerMode) {
		this.scanTriggerMode = scanTriggerMode;
	}

	public void setCcdCollectionProxy(StateMap ccdCollectionProxy) {
		this.ccdCollectionProxy = ccdCollectionProxy;
	}

	public void setCcdFileProxy(StateMap ccdFileProxy) {
		this.ccdFileProxy = ccdFileProxy;
	}
}
