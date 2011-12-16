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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ca.sciencestudio.util.io.FileProperties;
import ca.sciencestudio.util.web.FormResponseMap;

/**
 * @author maxweld
 *
 */
@Controller
public class ShareFileListController extends AbstractShareController {
	
	@ResponseBody
	@RequestMapping(value = "/share/files*")
	public FormResponseMap getFileList(@RequestParam String dir) {
	
		if(!canReadLaboratory()) {
			return new FormResponseMap(false, "Not permitted to view data files.");
		}
		
		// Remove trailing 'separators' from path. //
		while(dir.endsWith(File.separator)) {
			dir = dir.substring(0, dir.length() - File.separator.length());
		}
		
		File directory = new File(getShareDirectory(), dir);
		
		if(!directory.isDirectory()) {
			return new FormResponseMap(false, "Specified path is not a directory.");
		}
		
		if(!directory.getPath().startsWith(getShareDirectory().getPath())) {
			return new FormResponseMap(false, "Specified path is not in shared directory.");
		}
		
		List<FileProperties> filePropertiesList = new ArrayList<FileProperties>();
		
		for(File file : directory.listFiles()) {
			if(file.isFile()) {
				filePropertiesList.add(new FileProperties(file, getShareDirectory()));
			}
		}
		
		Collections.sort(filePropertiesList);
		
		FormResponseMap response = new FormResponseMap(true);
		response.put("files", filePropertiesList);
		return response;
	}
}
