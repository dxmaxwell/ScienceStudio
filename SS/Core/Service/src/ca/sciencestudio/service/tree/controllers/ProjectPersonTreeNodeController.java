/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *  	ProjectPersonTreeNodeController class.
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
import ca.sciencestudio.model.project.ProjectPerson;
import ca.sciencestudio.model.project.dao.ProjectPersonAuthzDAO;
import ca.sciencestudio.security.util.SecurityUtil;
import ca.sciencestudio.service.utilities.ModelPathUtils;

/**
 * @author maxweld
 *
 */
@Controller
public class ProjectPersonTreeNodeController extends AbstractTreeNodeController {

	private static final String PERSON_NAME_UNKNOWN = "Unknown Person";
	
	private PersonAuthzDAO personAuthzDAO;
	
	private ProjectPersonAuthzDAO projectPersonAuthzDAO;
	
	@ResponseBody
	@RequestMapping(value = ModelPathUtils.PROJECT_PERSON_PATH + "*", method = RequestMethod.GET)
	public TreeNodeList projectPersons(@RequestParam("project") String projectGid) {

		String user = SecurityUtil.getPersonGid();

		TreeNodeList treeNodes = new TreeNodeList();
				
		List<ProjectPerson> projectPersonList = projectPersonAuthzDAO.getAllByProjectGid(user, projectGid).get();
			
		for(ProjectPerson projectPerson : projectPersonList) {
			TreeNodeMap treeNode = new TreeNodeMap();
			treeNode.put(TREE_NODE_ID, "PROJECT_PERSON_" + projectPerson.getGid());
			treeNode.put(TREE_NODE_VIEW_URL, ModelPathUtils.getModelProjectPersonPath("/", projectPerson.getGid(), ".html"));
			treeNode.put(TREE_NODE_TEXT, buildTreeNodeText(user, projectPerson.getPersonGid()));
			treeNode.put(TREE_NODE_ICON_CLASS, "ss-project-person-tree-node-icon");
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
	
	public void setPersonAuthzDAO(PersonAuthzDAO personAuthzDAO) {
		this.personAuthzDAO = personAuthzDAO;
	}
	public void setProjectPersonAuthzDAO(ProjectPersonAuthzDAO projectPersonAuthzDAO) {
		this.projectPersonAuthzDAO = projectPersonAuthzDAO;
	}
	
	public ProjectPersonAuthzDAO getProjectPersonAuthzDAO() {
		return projectPersonAuthzDAO;
	}
	public PersonAuthzDAO getPersonAuthzDAO() {
		return personAuthzDAO;
	}
}
