/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     Sample interface.
 *     
 */
package ca.sciencestudio.model.sample;

import java.io.Serializable;
import java.util.Set;


/**
 * @author maxweld
 *
 */
public interface Sample extends Serializable {
	
	public int getId();
	public void setId(int id);
	
	public int getProjectId();
	public void setProjectId(int projectId);
	
	public String getName();
	public void setName(String name);
	
	public String getDescription();
	public void setDescription(String description);
	
	public String getQuantity();
	public void setQuantity(String quantity);
	
	public String getCasNumber();
	public void setCasNumber(String casNumber);
	
	public SampleState getState();
	public void setState(SampleState state);
	
	public Set<SampleHazard> getHazards();
	public void setHazards(Set<SampleHazard> hazards);
	
	public String getOtherHazards();
	public void setOtherHazards(String otherHazards);
}
