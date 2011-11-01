/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     ScanController class.
 *     
 */
package ca.sciencestudio.rest.service.session.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import ca.sciencestudio.model.session.Scan;
import ca.sciencestudio.model.session.dao.ScanBasicDAO;
import ca.sciencestudio.model.session.validators.ScanValidator;
import ca.sciencestudio.rest.service.controllers.AbstractModelController;

/**
 * @author maxweld
 *
 */
@Controller
public class ScanController extends AbstractModelController<Scan, ScanBasicDAO, ScanValidator> {

	private static final String SCAN_MODEL_URL = "/scans";
	
	public ScanController() {
		setValidator(new ScanValidator());
	}
	
	@Override
	@ResponseBody 
	@RequestMapping(value = SCAN_MODEL_URL + "*", method = RequestMethod.POST)
	public List<String> add(@RequestBody Scan scan, HttpServletRequest request, HttpServletResponse response) throws Exception{
		return super.add(scan, request, response);
	}
	
	@Override
	@ResponseBody 
	@RequestMapping(value = SCAN_MODEL_URL + "/{facility}*", method = RequestMethod.POST)
	public List<String> add(@RequestBody Scan scan, @PathVariable String facility, HttpServletRequest request, HttpServletResponse response) throws Exception{
		return super.add(scan, facility, request, response);
	}
	
	@Override
	@RequestMapping(value = SCAN_MODEL_URL + "/{gid}*", method = RequestMethod.PUT)
	public void edit(@RequestBody Scan scan, @PathVariable String gid, HttpServletResponse response) throws Exception{
		super.edit(scan, gid, response);
	}
	
	@Override
	@RequestMapping(value = SCAN_MODEL_URL + "/{gid}*", method = RequestMethod.DELETE)
	public void remove(@PathVariable String gid, HttpServletResponse response) throws Exception{
		super.remove(gid, response);
	}
	
	@Override
	@ResponseBody 
	@RequestMapping(value = SCAN_MODEL_URL + "/{gid}*", method = RequestMethod.GET)
	public Object get(@PathVariable String gid, HttpServletResponse response) throws Exception {
		return super.get(gid, response);
	}
	
	@Override
	@ResponseBody 
	@RequestMapping(value = SCAN_MODEL_URL + "*", method = RequestMethod.GET)
	public List<Scan> getAll(HttpServletResponse response) {
		return super.getAll(response);
	}
	
	@Override
	protected String getModelUrl() {
		return SCAN_MODEL_URL;
	}

	public ScanBasicDAO getScanBasicDAO() {
		return getModelBasicDAO();
	}
	public void setScanBasicDAO(ScanBasicDAO scanBasicDAO) {
		setModelBasicDAO(scanBasicDAO);
	}
}
