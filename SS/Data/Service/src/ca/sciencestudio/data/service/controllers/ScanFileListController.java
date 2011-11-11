/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     ScanFileListController class.
 *     
 */
package ca.sciencestudio.data.service.controllers;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ca.sciencestudio.model.session.Scan;
import ca.sciencestudio.security.util.SecurityUtil;
import ca.sciencestudio.util.rest.FileProps;
import ca.sciencestudio.util.web.FormResponseMap;

/**
 * @author maxweld
 *
 */
@Controller
public class ScanFileListController extends AbstractScanFileController {
	
	@ResponseBody
	@RequestMapping(value = "/scan/{scanGid}/file/list*")
	public FormResponseMap getFiles(@RequestParam(defaultValue = "") String dir, @RequestParam(defaultValue = "0") int depth, 
												@RequestParam(defaultValue = "any") String type, @PathVariable String scanGid) {
		
		String user = SecurityUtil.getPersonGid();
		
		Scan scan = scanAuthzDAO.get(user, scanGid).get();
		if(scan == null) {
			return new FormResponseMap(false, "Scan not found.");
		}

		List<FileProps> filePropsList = scanAuthzDAO.getFileList(user, scanGid, dir, type, depth).get();
		
		FormResponseMap response = new FormResponseMap(true);
		response.put("response", filePropsList);
		return response;
	}
}
