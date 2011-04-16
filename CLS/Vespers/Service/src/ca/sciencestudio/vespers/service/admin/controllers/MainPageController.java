/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 * 		AdminPageController class.
 *     
 */
package ca.sciencestudio.vespers.service.admin.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author maxweld
 *
 */
@Controller
public class MainPageController {
	
	private String htmlHeaderTitle = "Science Studio :: VESPERS Administration";
	private String pageHeaderTitle = "VESPERS Administration";
	
	@RequestMapping(value = "/main.html", method = RequestMethod.GET)
	public String main(HttpServletRequest request, ModelMap model) {
		return "page/admin";
	}

	@ModelAttribute("htmlHeaderTitle")
	public String getHtmlHeaderTitle() {
		return htmlHeaderTitle;
	}
	public void setHtmlHeaderTitle(String htmlHeaderTitle) {
		this.htmlHeaderTitle = htmlHeaderTitle;
	}

	@ModelAttribute("pageHeaderTitle")
	public String getPageHeaderTitle() {
		return pageHeaderTitle;
	}
	public void setPageHeaderTitle(String pageHeaderTitle) {
		this.pageHeaderTitle = pageHeaderTitle;
	}
}
