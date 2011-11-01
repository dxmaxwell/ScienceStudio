/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *  	SessionPersonTreeNodeController class.
 *     
 */
package ca.sciencestudio.service.tree.controllers;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


import ca.sciencestudio.model.person.Person;
import ca.sciencestudio.model.person.dao.PersonAuthzDAO;
import ca.sciencestudio.model.session.SessionPerson;
import ca.sciencestudio.model.session.dao.SessionPersonAuthzDAO;
import ca.sciencestudio.security.util.SecurityUtil;
import ca.sciencestudio.service.utilities.ModelPathUtils;

@Controller
public class SessionPersonTreeNodeController extends AbstractTreeNodeController {

	private static final String PERSON_NAME_UNKNOWN = "Unknown Person";
	
	private PersonAuthzDAO personAuthzDAO;
	
	private SessionPersonAuthzDAO sessionPersonAuthzDAO;
	
	@ResponseBody
	@RequestMapping(value = ModelPathUtils.SESSION_PERSON_PATH + "*", method = RequestMethod.GET)
	public TreeNodeList sessionPersons(@RequestParam("session") String sessionGid) {
		
		String user = SecurityUtil.getPersonGid();

		TreeNodeList treeNodes = new TreeNodeList();
		
		List<SessionPerson> sessionPersonList = sessionPersonAuthzDAO.getAllBySessionGid(user, sessionGid).get();
		
		for(SessionPerson sessionPerson : sessionPersonList) {
			TreeNodeMap treeNode = new TreeNodeMap();
			treeNode.put(TREE_NODE_ID, "PROJECT_PERSON_" + sessionPerson.getGid());
			treeNode.put(TREE_NODE_VIEW_URL, ModelPathUtils.getModelSessionPersonPath("/", sessionPerson.getGid(), ".html"));
			treeNode.put(TREE_NODE_TEXT, buildTreeNodeText(user, sessionPerson.getPersonGid()));
			treeNode.put(TREE_NODE_ICON_CLASS, "ss-session-person-tree-node-icon");
			treeNode.put(TREE_NODE_LEAF, true);
			treeNodes.add(treeNode);
		}
		
		return treeNodes;
	}
	
	protected String buildTreeNodeText(String user, String personGid) {
		Person person = personAuthzDAO.get(user, personGid).get();
		if(person == null) {
			return PERSON_NAME_UNKNOWN;
		}
		String fullName = Person.getFullName(person);
		return fullName.replaceAll("\\s", "_");
	}

	public PersonAuthzDAO getPersonAuthzDAO() {
		return personAuthzDAO;
	}
	public void setPersonAuthzDAO(PersonAuthzDAO personAuthzDAO) {
		this.personAuthzDAO = personAuthzDAO;
	}

	public SessionPersonAuthzDAO getSessionPersonAuthzDAO() {
		return sessionPersonAuthzDAO;
	}
	public void setSessionPersonAuthzDAO(SessionPersonAuthzDAO sessionPersonAuthzDAO) {
		this.sessionPersonAuthzDAO = sessionPersonAuthzDAO;
	}
}
