/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *      PersonPageController class.
 *     
 */
package ca.sciencestudio.service.person.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import ca.sciencestudio.security.util.SecurityUtil;
import ca.sciencestudio.service.controllers.AbstractModelController;
import ca.sciencestudio.service.person.backers.PersonFormBacker;

/**
 * @author maxweld
 *
 */
@Controller
public class PersonPageController extends AbstractModelController {
	
	@RequestMapping(value = "/person/self.html", method = RequestMethod.GET)
	public String getPersonSelfPage(ModelMap model) {
		model.put("personFormBacker", new PersonFormBacker(SecurityUtil.getPerson()));		
		return "frag/person";
	}
}
