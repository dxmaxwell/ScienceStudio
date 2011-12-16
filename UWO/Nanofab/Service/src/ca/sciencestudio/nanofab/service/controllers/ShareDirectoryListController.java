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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import ca.sciencestudio.util.io.FileProperties;
import ca.sciencestudio.util.web.FormResponseMap;

/**
 * @author maxweld
 *
 */
@Controller
public class ShareDirectoryListController extends AbstractShareController {

	@ResponseBody
	@RequestMapping(value = "/share/directories*")
	public FormResponseMap getDirectoryList() {
		
		if(!canReadLaboratory()) {
			return new FormResponseMap(false, "Not permitted to view data directories.");
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
		
		FormResponseMap response = new FormResponseMap(true);
		response.put("directories", dirPropertiesList);
		return response;
	}
}
