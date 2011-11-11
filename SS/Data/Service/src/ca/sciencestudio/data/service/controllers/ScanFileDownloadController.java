/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     ScanFileDownloadController class.
 *
 */
package ca.sciencestudio.data.service.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ca.sciencestudio.model.session.Scan;
import ca.sciencestudio.security.util.SecurityUtil;
import ca.sciencestudio.util.text.SpecialCharacterUtils;

/**
 * @author maxweld
 *
 */
@Controller
public class ScanFileDownloadController extends AbstractScanFileController {
	
	@RequestMapping(value = "/scan/{scanGid}/file/download")
	public void download(@RequestParam(required = false) String[] paths, @PathVariable String scanGid, HttpServletResponse response) {
		 
		String user = SecurityUtil.getPersonGid();
		
		if((paths == null) || (paths.length == 0)) {
			logger.debug("No data file paths specified for download.");
			response.setStatus(HttpStatus.NO_CONTENT.value());
			return;
		}
		
		if(paths.length == 1) {
			serveSingleFile(response, user, scanGid, paths[0]);
		} else {
			serveZipArchive(response, user, scanGid, paths);
		}
		
		return;
	}
	
	protected void serveSingleFile(HttpServletResponse response, String user, String gid, String path) {
		
		InputStream dataInputStream = scanAuthzDAO.getFileData(user, gid, path).get();
		if(dataInputStream == null) {
			response.setStatus(HttpStatus.NOT_FOUND.value());
			return;
		}
		
		int index = path.lastIndexOf("/");
		if(index >= 0) {
			String filename = path.substring(index+1);
			response.setContentType(getContentType(filename));
			response.setHeader("Content-disposition","attachment; filename="+filename);
		}
		else {
			response.setHeader("Content-disposition","attachment");
		}
		
		try {
			IOUtils.copy(dataInputStream, response.getOutputStream());
		}
		catch(IOException e) {
			logger.warn("Error copying data response stream.", e);
		}
		
		IOUtils.closeQuietly(dataInputStream);
		return;
	}
	
	
	protected void serveZipArchive(HttpServletResponse response, String user, String scanGid, String[] paths) {
		
		Scan scan = scanAuthzDAO.get(user, scanGid).get();
		if(scan == null) {
			response.setStatus(HttpStatus.NOT_FOUND.value());
			return;
		}
		
		String filename = SpecialCharacterUtils.replaceSpecial(scan.getName(), true);
	
		String zipEntryRoot = "/" + filename;
		
		ZipOutputStream zipResponseOutputStream;
		try {
			zipResponseOutputStream = new ZipOutputStream(response.getOutputStream());
		}
		catch(IOException e) {
			logger.warn("Error constructing buffered zip response stream.", e);
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return;
		}
		
		filename += ".zip";
		response.setContentType(getContentType(filename));
		response.setHeader("Content-disposition","attachment; filename="+filename);
		
		for(String path : paths) {
			
			if(path.length() == 0) {
				continue;
			}
			
			InputStream fileDataInputStream = scanAuthzDAO.getFileData(user, scanGid, path).get();
			if(fileDataInputStream == null) {
				continue;
			}
			
			boolean escape = false;
			
			ZipEntry zipEntry = new ZipEntry(zipEntryRoot + path);
			//zipEntry.setTime(zipEntryFile.lastModified());
			
			try {
				zipResponseOutputStream.putNextEntry(zipEntry);
			}
			catch(IOException e) {
				logger.warn("Error writing next zip entry for path: " + path, e);
				escape = true;
			}
			
			if(!escape) {
				try {
					IOUtils.copy(fileDataInputStream, zipResponseOutputStream);
				}
				catch(IOException e) {
					logger.warn("Error copying file input stream to response output stream.", e);
					escape = true;
				}
				finally {
					IOUtils.closeQuietly(fileDataInputStream);
				}
				
				try {
					zipResponseOutputStream.closeEntry();
				}
				catch(IOException e) {
					logger.warn("Error closing zip entry for path: " + path, e);
					escape = true;
				}
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
