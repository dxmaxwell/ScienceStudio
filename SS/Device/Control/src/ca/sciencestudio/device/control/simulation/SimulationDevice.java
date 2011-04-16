/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 * 		SimulationDevice interface.
 *     
 */
package ca.sciencestudio.device.control.simulation;

/**
 * @author maxweld
 *
 */
public interface SimulationDevice {

	public String getId();
	public void setId(String id);
	
	public String getName();
	public void setName(String name);
	
	public Object getValue();
	public void setValue(Object value);
	
	public void addEventListener(SimulationEventListener eventListener);
	public void removeEventListener(SimulationEventListener eventListener);
}
