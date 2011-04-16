/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 * 		SimulationEventListener interface.
 *     
 */
package ca.sciencestudio.device.control.simulation;

import ca.sciencestudio.device.control.simulation.SimulationEvent;

/**
 * @author maxweld
 *
 */
public interface SimulationEventListener {

	void handleEvent(SimulationEvent event);
}
