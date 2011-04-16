/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     Session interface.
 *     
 */
package ca.sciencestudio.model.session;

import java.io.Serializable;
import java.util.Date;

/**
 * @author maxweld
 *
 */
public interface Session extends Serializable {

	public int getId();
	public void setId(int id);
	
	public String getName();
	public void setName(String name);
	
	public String getDescription();
	public void setDescription(String description);
	
	public String getProposal();
	public void setProposal(String proposal);
	
	public Date getStartDate();
	public void setStartDate(Date startDate);
	
	public Date getEndDate();
	public void setEndDate(Date endDate);
	
	public int getProjectId();
	public void setProjectId(int projectId);
	
	public int getLaboratoryId();
	public void setLaboratoryId(int laboratoryId);
	
	public Session clone();
}
