/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 * 		MainPageController class.
 *     
 */
package ca.sciencestudio.vespers.service.admin.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author maxweld
 *
 */
@Controller
public class MainPageController extends AbstractBeamlineAdminController {
	
	private static final String MODEL_KEY_HTML_HEADER_TITLE = "htmlHeaderTitle";
	private static final String MODEL_KEY_PAGE_HEADER_TITLE = "pageHeaderTitle";
	
	private static final String ERROR_VIEW = "page/error";
	private static final String ADMIN_VIEW = "page/admin";
	
	private String htmlHeaderTitle;
	private String pageHeaderTitle;
	
	@RequestMapping(value = "/main.html", method = RequestMethod.GET)
	public String main(HttpServletRequest request, ModelMap model) {
		
		if(!canAdminBeamline()) {
			model.put("error", "Authorization Error");
			model.put("errorMessage", "Not authorized to administrate beamline.");
			return ERROR_VIEW;
		}
		
		model.put(MODEL_KEY_HTML_HEADER_TITLE, htmlHeaderTitle);
		model.put(MODEL_KEY_PAGE_HEADER_TITLE, pageHeaderTitle);
		return ADMIN_VIEW;
	}
	
	public String getHtmlHeaderTitle() {
		return htmlHeaderTitle;
	}
	public void setHtmlHeaderTitle(String htmlHeaderTitle) {
		this.htmlHeaderTitle = htmlHeaderTitle;
	}

	public String getPageHeaderTitle() {
		return pageHeaderTitle;
	}
	public void setPageHeaderTitle(String pageHeaderTitle) {
		this.pageHeaderTitle = pageHeaderTitle;
	}
}
