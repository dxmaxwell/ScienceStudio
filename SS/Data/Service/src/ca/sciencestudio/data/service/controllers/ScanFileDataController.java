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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import ca.sciencestudio.model.session.Scan;
import ca.sciencestudio.security.util.SecurityUtil;
import ca.sciencestudio.util.text.SpecialCharacterUtils;
import ca.sciencestudio.util.Parameters;

/**
 * @author maxweld
 *
 */
@Controller
public class ScanFileDataController extends AbstractScanFileController {
	
	@RequestMapping(value = "/scan/{scanGid}/file/data.{format}")
	public void getDataFile(@PathVariable String scanGid, @PathVariable String format, HttpServletResponse response, ModelMap model) {
		
		String user = SecurityUtil.getPersonGid();
		
		Scan scan = scanAuthzDAO.get(user, scanGid).get();
		if(scan == null) {
			response.setStatus(HttpStatus.NOT_FOUND.value());
			return;
		}
		
		Parameters sp = scan.getParameters();
		
		String dataFileBase  = sp.get(PARAM_KEY_DATA_FILE_BASE);
		if((dataFileBase == null) || (dataFileBase.length() == 0)) {
			response.setStatus(HttpStatus.NOT_FOUND.value());
			return;
		}

		String dataUrl = scan.getDataUrl();
		if((dataUrl == null) || (dataUrl.length() == 0)) {
			response.setStatus(HttpStatus.NOT_FOUND.value());
			return;
		}
		
		File dataFile = new File(dataUrl, dataFileBase + "." + format);
		if(!dataFile.isFile()) {
			response.setStatus(HttpStatus.NOT_FOUND.value());
			return;
		}
		
		InputStream dataFileInputStream;
		try {
			dataFileInputStream = new BufferedInputStream(new FileInputStream(dataFile));
		}
		catch(IOException e) {
			logger.warn("Exception while constructing input stream for data file: " + dataFile, e);
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return;
		}
		
		OutputStream responseOutputStream;
		try {
			responseOutputStream = new BufferedOutputStream(response.getOutputStream());
		}
		catch(IOException e) {
			logger.warn("Exception while constructing output stream for response", e);
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return;
		}
		
		String filename = SpecialCharacterUtils.replaceSpecial(scan.getName()) + "." + format;
		
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
		
		return;
	}
}
