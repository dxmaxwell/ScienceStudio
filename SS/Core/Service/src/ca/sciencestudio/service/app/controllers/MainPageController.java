/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *  	MainPageController class.
 *     
 */
package ca.sciencestudio.service.app.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import ca.sciencestudio.security.util.SecurityUtil;

/**
 * @author maxweld
 *
 */
@Controller
public class MainPageController {
	
	@RequestMapping(value = "/main.html", method = RequestMethod.GET)
	public String getMainPage(ModelMap model) {
		return "page/main";
	}
	
	@ModelAttribute("username")
	public String getUsername() {
		return SecurityUtil.getPersonGid();
	}
}
