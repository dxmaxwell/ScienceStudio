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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import ca.sciencestudio.model.session.Scan;
import ca.sciencestudio.security.util.SecurityUtil;
import ca.sciencestudio.util.Parameters;

/**
 * @author maxweld
 *
 */
@Controller
public class ScanFileSampleController extends AbstractScanFileController {
	
	@RequestMapping(value = "/scan/{scanGid}/file/sample.{format}")
	public void getSampleImage(@PathVariable String scanGid, @PathVariable String format, HttpServletResponse response) {
		
		String user = SecurityUtil.getPersonGid();
		
		Scan scan = scanAuthzDAO.get(user, scanGid).get();
		if(scan == null) {
			response.setStatus(HttpStatus.NOT_FOUND.value());
			return;
		}
			
		Parameters sp = scan.getParameters();
		
		String sampleImageFileName  = sp.get(PARAM_KEY_SAMPLE_IMAGE_FILE);
		if((sampleImageFileName == null) || (sampleImageFileName.length() == 0)) {
			response.setStatus(HttpStatus.NOT_FOUND.value());
			return;
		}

		String dataUrl = scan.getDataUrl();
		if((dataUrl == null) || (dataUrl.length() == 0)) {
			response.setStatus(HttpStatus.NOT_FOUND.value());
			return;
		}
		
		File sampleImageFile = new File(dataUrl, sampleImageFileName);
		if(!sampleImageFile.isFile()) {
			response.setStatus(HttpStatus.NOT_FOUND.value());
			return;
		}
		
		int sampleImageFileExtIndex = sampleImageFile.getName().lastIndexOf(".");
		if(sampleImageFileExtIndex < 0) {
			logger.warn("Sample image file name extension not found.");
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return;
		}
		
		String sampleImageFileNameExt = sampleImageFile.getName().substring(sampleImageFileExtIndex + 1);
		
		OutputStream responseOutputStream;
		try {
			responseOutputStream = new BufferedOutputStream(response.getOutputStream());
		}
		catch(IOException e) {
			logger.warn("Exception while getting response output stream.", e);
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return;
		}
		
		InputStream sampleImageInputStream;
		try {
			sampleImageInputStream = new BufferedInputStream(new FileInputStream(sampleImageFile));
		}
		catch(IOException e) {
			logger.warn("Exception while openning sample image input stream.", e);
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return;
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
				response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
				return;
			}
			
			if(sampleImageBuffer == null) {
				logger.warn("Sample image input format (" + sampleImageFileNameExt + ") not supported. ");
				response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
				return;
			}
			
			ByteArrayOutputStream sampleImageOutputStream = 
					new ByteArrayOutputStream((int)sampleImageFile.length());
			
			boolean imageWriterFound;
			try {
				imageWriterFound = ImageIO.write(sampleImageBuffer, format, sampleImageOutputStream);
			}
			catch(IOException e) {
				logger.warn("Exception while writing sample image to response buffer.", e);
				response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
				return;
			}
			
			if(!imageWriterFound) {
				logger.warn("Sample image output format (" + format + ") not supported. ");
				response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
				return;
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
		
		return;
	}
}
