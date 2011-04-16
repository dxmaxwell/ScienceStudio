/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *   ProjectPersonTreeNodeController class.
 *     
 */
package ca.sciencestudio.service.tree.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import ca.sciencestudio.model.person.Person;
import ca.sciencestudio.model.person.dao.PersonDAO;
import ca.sciencestudio.model.project.ProjectPerson;
import ca.sciencestudio.model.project.dao.ProjectPersonDAO;
import ca.sciencestudio.security.util.AuthorityUtil;
import ca.sciencestudio.security.util.SecurityUtil;
import ca.sciencestudio.service.utilities.ModelPathUtils;

/**
 * @author maxweld
 *
 */
@Controller
public class ProjectPersonTreeNodeController extends AbstractTreeNodeController {

	@Autowired
	private PersonDAO personDAO;
	
	@Autowired
	private ProjectPersonDAO projectPersonDAO;
	
	@RequestMapping(value = "/project/{projectId}/persons.{format}", method = RequestMethod.GET)
	public String projectPersons(@PathVariable int projectId, @PathVariable String format, HttpServletRequest request, ModelMap model) {
		
		TreeNodeList treeNodes = new TreeNodeList();
		
		Object admin = AuthorityUtil.ROLE_ADMIN_PROJECTS;
		Object group = AuthorityUtil.buildProjectGroupAuthority(projectId);
		
		if(SecurityUtil.hasAnyAuthority(group, admin)) {
			
			List<ProjectPerson> projectPersonList = projectPersonDAO.getProjectPersonListByProjectId(projectId);
			
			for(ProjectPerson projectPerson : projectPersonList) {
				TreeNodeMap treeNode = new TreeNodeMap();
				treeNode.put(TREE_NODE_ID, "PROJECT_PERSON_" + projectPerson.getId());
				treeNode.put(TREE_NODE_VIEW_URL, getModelPath(request) + ModelPathUtils.getProjectPersonPath(projectPerson.getId(), ".html"));
				treeNode.put(TREE_NODE_TEXT, buildTreeNodeText(projectPerson.getPersonUid()));
				treeNode.put(TREE_NODE_ICON_CLASS, "ss-project-person-tree-node-icon");
				treeNode.put(TREE_NODE_LEAF, true);
				treeNodes.add(treeNode);
			}
		}
		
		model.put("treeNodes", treeNodes);
		return getResponseView(format);
	}
	
	protected String buildTreeNodeText(String personUid) {
		Person person = personDAO.getPersonByUid(personUid);
		
		if(person == null) {
			return personUid;
		}
		
		return person.getFirstName() + "_" + person.getLastName();
	}

	public PersonDAO getPersonDAO() {
		return personDAO;
	}
	public void setPersonDAO(PersonDAO personDAO) {
		this.personDAO = personDAO;
	}

	public ProjectPersonDAO getProjectPersonDAO() {
		return projectPersonDAO;
	}
	public void setProjectPersonDAO(ProjectPersonDAO projectPersonDAO) {
		this.projectPersonDAO = projectPersonDAO;
	}
}
