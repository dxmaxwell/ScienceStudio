/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     ScanFileListController class.
 *     
 */
package ca.sciencestudio.data.service.controllers;

import java.io.File;
import java.util.List;
import java.util.ArrayList;
import java.util.Queue;
import java.util.LinkedList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ca.sciencestudio.model.project.Project;
import ca.sciencestudio.model.project.dao.ProjectDAO;
import ca.sciencestudio.model.session.Scan;
import ca.sciencestudio.model.session.dao.ScanDAO;
import ca.sciencestudio.security.util.AuthorityUtil;
import ca.sciencestudio.security.util.SecurityUtil;
import ca.sciencestudio.util.io.FileProperties;
import ca.sciencestudio.util.web.BindAndValidateUtils;

/**
 * @author maxweld
 *
 */
@Controller
@RequestMapping("/scan/{scanId}/file")
public class ScanFileListController {
	
	private static final String FILE_TYPE_ANY = "any";
	private static final String FILE_TYPE_FILE = "file";
	private static final String FILE_TYPE_DIRECTORY = "directory";
	
	@Autowired
	private ScanDAO scanDAO;
	
	@Autowired
	private ProjectDAO projectDAO;
	
	@RequestMapping("/list.{format}")
	public String getFiles(@RequestParam(defaultValue = "") String dir,
								@RequestParam(defaultValue = "0") int depth, 
									@RequestParam(defaultValue = "any") String type,
										@PathVariable int scanId, @PathVariable String format, ModelMap model) {
				
		BindException errors = BindAndValidateUtils.buildBindException();
		model.put("errors", errors);
		
		String responseView = "response-" + format;
		
		Project project = projectDAO.getProjectByScanId(scanId);
		if(project == null) {
			errors.reject("project.notfound", "Project not found.");
			return responseView;
		}
		
		Object admin = AuthorityUtil.ROLE_ADMIN_DATA;
		Object group = AuthorityUtil.buildProjectGroupAuthority(project.getId());
		if(!SecurityUtil.hasAnyAuthority(group, admin)) {
			errors.reject("permission.denied", "Not permitted to list files.");
			return responseView;
		}
		
		Scan scan = scanDAO.getScanById(scanId);
		if(scan == null) {
			errors.reject("scan.notfound", "Scan not found.");
			return responseView;
		}

		File scanDataDirectory = new File(scan.getDataUrl());
		
		// Remove trailing 'separators' from directory. //
		while(dir.endsWith(File.separator)) {
			dir = dir.substring(0, dir.length() - File.separator.length());
		}
		
		File fileListDirectory = new File(scanDataDirectory, dir);
		if(!fileListDirectory.isDirectory()) {
			errors.reject("file.notdirectory", "Directory does not exist.");
			return responseView;
		}
		
		type = type.toLowerCase();
		boolean typeAny = FILE_TYPE_ANY.startsWith(type);
		boolean typeFile = typeAny || FILE_TYPE_FILE.startsWith(type);
		boolean typeDirectory = typeAny || FILE_TYPE_DIRECTORY.startsWith(type);
		
		Queue<File> directories = new LinkedList<File>();
		directories.offer(fileListDirectory);
		
		List<FileProperties> fileProperties = new ArrayList<FileProperties>();
		for(int level = 0; directories.peek() != null; level++) {
			File pwd = directories.poll();
			if(typeDirectory) {
				fileProperties.add(new FileProperties(pwd, scanDataDirectory));
			}
			
			for(File file : pwd.listFiles()) {
				if(typeFile && file.isFile()) {
					fileProperties.add(new FileProperties(file, scanDataDirectory));
				}
				else if((level < depth) && file.isDirectory()) {
					directories.offer(file);
				}
			}
		}
		
		model.put("response", fileProperties);
		return "response-json";
	}
}
