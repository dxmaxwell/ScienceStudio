/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *  	HomePageController class.
 *     
 */
package ca.sciencestudio.service.app.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author maxweld
 *
 */
@Controller
public class HomePageController {

	@RequestMapping(value = "/home.html")
	public String getMainPage(ModelMap model) {
		return "frag/home";
	}
}
