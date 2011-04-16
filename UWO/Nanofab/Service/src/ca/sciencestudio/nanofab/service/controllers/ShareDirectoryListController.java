/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *      ShareDirectoryListController class.	     
 */
package ca.sciencestudio.nanofab.service.controllers;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.Stack;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import ca.sciencestudio.util.io.FileProperties;
import ca.sciencestudio.util.web.BindAndValidateUtils;
import ca.sciencestudio.security.util.AuthorityUtil;
import ca.sciencestudio.security.util.SecurityUtil;

/**
 * @author maxweld
 *
 */
@Controller
public class ShareDirectoryListController extends AbstractShareController {

	@RequestMapping(value = "/share/directories.{format}")
	public String getDirectoryList(@PathVariable String format, ModelMap model) {
		
		BindException errors = BindAndValidateUtils.buildBindException();
		model.put("errors", errors);
		
		String responseView = "response-" + format;
		
		int projectId = nanofabSessionStateMap.getProjectId();
		Object admin = AuthorityUtil.buildRoleAuthority("ADMIN_NANOFAB");
		Object group = AuthorityUtil.buildProjectGroupAuthority(projectId);
		
		if(!SecurityUtil.hasAnyAuthority(group, admin)) {
			errors.reject("permission.denied", "Not permitted to view data directories.");
			return responseView;
		}
		
		List<FileProperties> dirPropertiesList = new ArrayList<FileProperties>();
		
		Stack<File> directoryStack = new Stack<File>();	
		directoryStack.push(getShareDirectory());
		
		while(!directoryStack.isEmpty()) {
			File directory = directoryStack.pop();
			for(File f : directory.listFiles()) {
				if(f.isDirectory()) {
					directoryStack.push(f);
				}
			}
			dirPropertiesList.add(new FileProperties(directory, getShareDirectory()));
		}
		
		Collections.sort(dirPropertiesList);
		
		model.put("response", dirPropertiesList);
		return responseView;
	}
}
