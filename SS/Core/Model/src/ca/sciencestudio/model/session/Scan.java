/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     Scan interface.
 *     
 */
package ca.sciencestudio.model.session;

import java.io.Serializable;
import java.util.Date;

/**
 * @author maxweld
 *
 */
public interface Scan extends Serializable, Cloneable {
	
	public int getId();
	public void setId(int id);
	
	public String getName();
	public void setName(String name);
	
	public String getDataUrl();
	public void setDataUrl(String dataUrl);
	
	public String getParameters();
	public void setParameters(String parameters);
	
	public Date getStartDate();
	public void setStartDate(Date startDate);
	
	public Date getEndDate();
	public void setEndDate(Date endDate);
	
	public int getExperimentId();
	public void setExperimentId(int experimentId);
	
	public Scan clone();
}
