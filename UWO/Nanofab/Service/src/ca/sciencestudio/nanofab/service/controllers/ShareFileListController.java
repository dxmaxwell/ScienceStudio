/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *      ShareFileListController class.	     
 */
package ca.sciencestudio.nanofab.service.controllers;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ca.sciencestudio.util.io.FileProperties;
import ca.sciencestudio.util.web.BindAndValidateUtils;
import ca.sciencestudio.security.util.AuthorityUtil;
import ca.sciencestudio.security.util.SecurityUtil;

/**
 * @author maxweld
 *
 */
@Controller
public class ShareFileListController extends AbstractShareController {
	
	@RequestMapping(value = "/share/files.{format}")
	public String getFileList(@RequestParam String dir, @PathVariable String format, ModelMap model) {
	
		BindException errors = BindAndValidateUtils.buildBindException();
		model.put("errors", errors);
		
		String responseView = "response-" + format;
		
		int projectId = nanofabSessionStateMap.getProjectId();
		Object admin = AuthorityUtil.buildRoleAuthority("ADMIN_NANOFAB");
		Object group = AuthorityUtil.buildProjectGroupAuthority(projectId);
		
		if(!SecurityUtil.hasAnyAuthority(group, admin)) {
			errors.reject("permission.denied", "Not permitted to view data files.");
			return responseView;
		}
		
		// Remove trailing 'separators' from path. //
		while(dir.endsWith(File.separator)) {
			dir = dir.substring(0, dir.length() - File.separator.length());
		}
		
		File directory = new File(getShareDirectory(), dir);
		
		if(!directory.isDirectory()) {
			errors.reject("file.notdirectory", "Specified path is not a directory.");
			return responseView;
		}
		
		if(!directory.getPath().startsWith(getShareDirectory().getPath())) {
			errors.reject("file.notchild", "Specified path is not in shared directory.");
			return responseView;
		}
		
		List<FileProperties> filePropertiesList = new ArrayList<FileProperties>();
		
		for(File file : directory.listFiles()) {
			if(file.isFile()) {
				filePropertiesList.add(new FileProperties(file, getShareDirectory()));
			}
		}
		
		Collections.sort(filePropertiesList);
		
		model.put("response", filePropertiesList);
		return responseView;
	}
}
