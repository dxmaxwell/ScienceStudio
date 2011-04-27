/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     MemoryProjectDAO class.
 *     
 */
package ca.sciencestudio.rest.model.project.dao;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import ca.sciencestudio.rest.model.project.Project;

/**
 * @author maxweld
 *
 */
public class MemoryProjectDAO {
	
	private String uidPrefix;
	
	private int count = 0;
	private List<Project> projects = new LinkedList<Project>();
	
	public MemoryProjectDAO(String uidPrefix) {
		this.uidPrefix = uidPrefix;
	}

	public MemoryProjectDAO(String uidPrefix, Collection<Project> projects) {
		this(uidPrefix);
		for(Project project : projects) {
			addProject(project);
		}
	}
	
	public synchronized String addProject(Project project) {
		count++;
		project.setUid(uidPrefix + 0 + count);
		projects.add(project);
		return project.getUid();
	}
	
	public synchronized Project getProjectByUid(String uid) {
		for(Project project : projects) {
			if(uid.equals(project.getUid())) {
				return project;
			}
		}
		return null;
	}
	
	public synchronized List<Project> getProjectList() {
		return Collections.unmodifiableList(new LinkedList<Project>(projects));
	}
}
