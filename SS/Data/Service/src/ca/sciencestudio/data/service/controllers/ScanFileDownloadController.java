/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     ScanFileDownloadController class.
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
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ca.sciencestudio.model.session.Scan;
import ca.sciencestudio.util.text.SpecialCharacterUtils;

/**
 * @author maxweld
 *
 */
@Controller
@RequestMapping("/scan/{scanId}/file")
public class ScanFileDownloadController extends AbstractScanFileController {
	
	@RequestMapping(value = "/download")
	public String download(@RequestParam(required = false) String[] paths, @PathVariable int scanId, HttpServletResponse response) {
		 
		Scan scan = getScanWithSecurityCheck(scanId, response);
		if(response.isCommitted()) {
			return null;
		}
		
		if((paths == null) || (paths.length == 0)) {
			logger.debug("No data file paths specified for download.");
			sendError(response, HttpServletResponse.SC_NO_CONTENT);
			return null;
		}
		
		File scanDataDirectory = new File(scan.getDataUrl());
		if(!scanDataDirectory.isDirectory()) {
			logger.warn("Scan data directory not found for path: " + scanDataDirectory);
			sendError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return null;
		}
		
		if(paths.length == 1) {
			serveSingleFile(response, scan, scanDataDirectory, paths[0]);
		} else {
			serveZipArchive(response, scan, scanDataDirectory, paths);
		}
		
		return null;
	}
	
	protected void serveSingleFile(HttpServletResponse response, Scan scan, File scanDataDirectory, String path) {
		
		File file = new File(scanDataDirectory, path);
		if(!file.isFile()) {
			logger.warn("Specified path is not a regular file: " + file);
			sendError(response, HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		
		OutputStream responseOutputStream;
		try {
			responseOutputStream = new BufferedOutputStream(response.getOutputStream());
		}
		catch(IOException e) {
			logger.warn("Error constructing buffered output stream from resposne.", e);
			sendError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return;
		}
		
		InputStream fileInputStream;
		try {
			fileInputStream = new BufferedInputStream(new FileInputStream(file));
		}
		catch(IOException e) {
			logger.warn("Error constructing buffered input stream from file: " + file, e);
			sendError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return;
		}
		
		String filename = SpecialCharacterUtils.replaceSpecial(file.getName(), true);
		
		try {
			response.setContentLength((int)file.length());
			response.setContentType(getContentType(file));
			response.setHeader("Content-disposition","attachment; filename="+filename);
			IOUtils.copy(fileInputStream, responseOutputStream);
		}
		catch(IOException e) {
			logger.warn("Error copying data from file stream to response stream.", e);
			sendError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		
		try {
			fileInputStream.close();
		}
		catch(IOException e) {
			logger.warn("Error while closing file input stream: " + file, e);
		}
		
		try {
			responseOutputStream.close();
		}
		catch(IOException e) { 
			logger.warn("Error while closing response output stream.", e);
		}
		
		return;
	}
	
	protected void serveZipArchive(HttpServletResponse response, Scan scan, File scanDataDirectory, String[] paths) {
		
		String filename = SpecialCharacterUtils.replaceSpecial(scan.getName(), true);
		
		File zipEntryFile;
		String zipEntryPath;
		String zipEntryRoot = "/" + filename;
		
		Map<String,File> zipPathFileMap = new HashMap<String,File>();
		for(String path : paths) {
			zipEntryPath = zipEntryRoot + path;
			if(!zipPathFileMap.containsKey(zipEntryPath)) {
				zipEntryFile = new File(scanDataDirectory, path); 
				if(zipEntryFile.isFile()) {
					zipPathFileMap.put(zipEntryPath, zipEntryFile);
				} else {
					logger.warn("Specified path is not a regular file: " + zipEntryFile + ". Ignoring.");
				}
			}
		}
		
		if(zipPathFileMap.isEmpty()) {
			logger.warn("No valid data file paths specified for download.");
			sendError(response, HttpServletResponse.SC_NO_CONTENT);
			return;
		}
		
		ZipOutputStream zipResponseOutputStream;
		try {
			zipResponseOutputStream = new ZipOutputStream(new BufferedOutputStream(response.getOutputStream()));
		}
		catch(IOException e) {
			logger.warn("Error constructing buffered zip response stream.", e);
			sendError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return;
		}
		
		filename += ".zip";
		response.setContentType(getContentType(filename));
		response.setHeader("Content-disposition","attachment; filename="+filename);
		
		for(Map.Entry<String,File> entry : zipPathFileMap.entrySet()) {
			
			boolean escape = false;
			zipEntryPath = entry.getKey();
			zipEntryFile = entry.getValue();
			
			InputStream fileInputStream;
			try {
				fileInputStream = new BufferedInputStream(new FileInputStream(zipEntryFile));
			}
			catch(IOException e) {
				logger.warn("Error constructing buffered file input stream: " + zipEntryFile, e);
				continue;
			}
			
			ZipEntry zipEntry = new ZipEntry(zipEntryPath);
			zipEntry.setTime(zipEntryFile.lastModified());
			
			try {
				zipResponseOutputStream.putNextEntry(zipEntry);
			}
			catch(IOException e) {
				logger.warn("Error writing next zip entry for path: " + zipEntryPath, e);
				escape = true;
			}
			
			if(!escape) {
				try {
					IOUtils.copy(fileInputStream, zipResponseOutputStream);
				}
				catch(IOException e) {
					logger.warn("Error copying file input stream to response output stream.", e);
					escape = true;
				}
			
				try {
					zipResponseOutputStream.closeEntry();
				}
				catch(IOException e) {
					logger.warn("Error closing zip entry for path." + zipEntryPath, e);
					escape = true;
				}
			}
			
			try {
				fileInputStream.close();
			}
			catch(IOException e) {
				logger.warn("Error closing file input stream: "  + zipEntryFile, e);
				escape = true;
			}
			
			if(escape) {
				break;
			}	
		}
		
		try {
			zipResponseOutputStream.close();
		}
		catch(IOException e) {
			logger.warn("Error closing zip response output stream.", e);
		}
		
		return;
	}
}
