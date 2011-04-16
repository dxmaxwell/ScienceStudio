/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     ScanSampleImageController class.
 *
 */
package ca.sciencestudio.data.service.controllers;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import ca.sciencestudio.model.session.Scan;
import ca.sciencestudio.model.utilities.ScanParameters;

/**
 * @author maxweld
 *
 */
@Controller
public class ScanFileSampleController extends AbstractScanFileController {
	
	@RequestMapping(value = "/scan/{scanId}/file/sample.{format}", method = RequestMethod.GET)
	public String getSampleImage(@PathVariable int scanId, @PathVariable String format, HttpServletResponse response) {
		
		Scan scan = getScanWithSecurityCheck(scanId, response);
		if(scan == null) {
			return null;
		}
		
		ScanParameters sp = new ScanParameters(scan);
		
		String sampleImageFileName  = sp.getParameter(PARAM_KEY_SAMPLE_IMAGE_FILE);
		if((sampleImageFileName == null) || (sampleImageFileName.length() == 0)) {
			sendError(response, HttpServletResponse.SC_NOT_FOUND);
			return null;
		}

		String dataUrl = sp.getScan().getDataUrl();
		if((dataUrl == null) || (dataUrl.length() == 0)) {
			sendError(response, HttpServletResponse.SC_NOT_FOUND);
			return null;
		}
		
		File sampleImageFile = new File(dataUrl, sampleImageFileName);
		if(!sampleImageFile.isFile()) {
			sendError(response, HttpServletResponse.SC_NOT_FOUND);
			return null;
		}
		
		int sampleImageFileExtIndex = sampleImageFile.getName().lastIndexOf(".");
		if(sampleImageFileExtIndex < 0) {
			logger.warn("Sample image file name extension not found.");
			sendError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return null;
		}
		
		String sampleImageFileNameExt = sampleImageFile.getName().substring(sampleImageFileExtIndex + 1);
		
		OutputStream responseOutputStream;
		try {
			responseOutputStream = new BufferedOutputStream(response.getOutputStream());
		}
		catch(IOException e) {
			logger.warn("Exception while getting response output stream.", e);
			sendError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return null;
		}
		
		InputStream sampleImageInputStream;
		try {
			sampleImageInputStream = new BufferedInputStream(new FileInputStream(sampleImageFile));
		}
		catch(IOException e) {
			logger.warn("Exception while openning sample image input stream.", e);
			sendError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return null;
		}
		
		if(format.equalsIgnoreCase(sampleImageFileNameExt)) {
			// Shortcut: Sample image is already in requested format. //
			response.setContentLength((int)sampleImageFile.length());
			response.setContentType(getContentTypeWithExt(format));
		}
		else {
			// Convert sample image to requested format if possible. //
			BufferedImage sampleImageBuffer;
			try {
				sampleImageBuffer = ImageIO.read(sampleImageInputStream);
			}
			catch(IOException e) {
				logger.warn("Exception while reading sample image file: " + sampleImageFile + ". Format not supported?");
				sendError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				return null;
			}
			
			if(sampleImageBuffer == null) {
				logger.warn("Sample image input format (" + sampleImageFileNameExt + ") not supported. ");
				sendError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				return null;
			}
			
			ByteArrayOutputStream sampleImageOutputStream = 
					new ByteArrayOutputStream((int)sampleImageFile.length());
			
			boolean imageWriterFound;
			try {
				imageWriterFound = ImageIO.write(sampleImageBuffer, format, sampleImageOutputStream);
			}
			catch(IOException e) {
				logger.warn("Exception while writing sample image to response buffer.", e);
				sendError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				return null;
			}
			
			if(!imageWriterFound) {
				logger.warn("Sample image output format (" + format + ") not supported. ");
				sendError(response, HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE);
				return null;
			}
			
			byte[] sampleImageByteArray = sampleImageOutputStream.toByteArray();
			sampleImageInputStream = new ByteArrayInputStream(sampleImageByteArray);
			
			response.setContentLength(sampleImageByteArray.length);
			response.setContentType(getContentTypeWithExt(format));
		}
	
		try {
			IOUtils.copy(sampleImageInputStream, responseOutputStream);
		}
		catch(IOException e) {
			logger.warn("Exception while copying sample image data to response.", e);
		}
		
		try {
			sampleImageInputStream.close();
		}
		catch(IOException e) {
			logger.warn("Exception while closing sample image input stream.", e);
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
