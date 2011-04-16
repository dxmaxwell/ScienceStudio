/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     AbstractScanDataFileController class.
 *
 */
package ca.sciencestudio.data.service.controllers;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import ca.sciencestudio.model.session.Scan;
import ca.sciencestudio.model.utilities.ScanParameters;
import ca.sciencestudio.util.text.SpecialCharacterUtils;

/**
 * @author maxweld
 *
 */
@Controller
@RequestMapping("/scan/{scanId}/file")
public class ScanFileDataController extends AbstractScanFileController {
	
	@RequestMapping(value = "/data.{format}", method = RequestMethod.GET)
	public String getDataFile(@PathVariable int scanId, @PathVariable String format, HttpServletResponse response, ModelMap model) {
		
		Scan scan = getScanWithSecurityCheck(scanId, response);
		if(scan == null) {
			return null;
		}
		
		ScanParameters sp = new ScanParameters(scan);
		
		String dataFileBase  = sp.getParameter(PARAM_KEY_DATA_FILE_BASE);
		if((dataFileBase == null) || (dataFileBase.length() == 0)) {
			sendError(response, HttpServletResponse.SC_NOT_FOUND);
			return null;
		}

		String dataUrl = sp.getScan().getDataUrl();
		if((dataUrl == null) || (dataUrl.length() == 0)) {
			sendError(response, HttpServletResponse.SC_NOT_FOUND);
			return null;
		}
		
		File dataFile = new File(dataUrl, dataFileBase + "." + format);
		if(!dataFile.isFile()) {
			sendError(response, HttpServletResponse.SC_NOT_FOUND);
			return null;
		}
		
		InputStream dataFileInputStream;
		try {
			dataFileInputStream = new BufferedInputStream(new FileInputStream(dataFile));
		}
		catch(IOException e) {
			logger.warn("Exception while constructing input stream for data file: " + dataFile, e);
			sendError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return null;
		}
		
		OutputStream responseOutputStream;
		try {
			responseOutputStream = new BufferedOutputStream(response.getOutputStream());
		}
		catch(IOException e) {
			logger.warn("Exception while constructing output stream for response", e);
			sendError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return null;
		}
		
		String filename = SpecialCharacterUtils.replaceSpecial(sp.getScan().getName()) + "." + format;
		
		try {
			response.setContentLength((int)dataFile.length());
			response.setContentType(getContentTypeWithExt(format));
			response.setHeader("Content-disposition","attachment; filename=" + filename);
			IOUtils.copy(dataFileInputStream, responseOutputStream);
		}
		catch(IOException e) {
			logger.warn("Exception while writing data file to HTTP response.", e);
		}
		
		try {
			dataFileInputStream.close();
		}
		catch(IOException e) {
			logger.warn("Exception while closing data file input stream.", e);
		}
		
		try {
			responseOutputStream.close();
		}
		catch(IOException e) {
			logger.warn("Exception while closing response output stream.", e);
		}
		
		return null;
	}
}
