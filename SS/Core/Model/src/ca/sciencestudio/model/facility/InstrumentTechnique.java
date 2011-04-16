/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     InstrumentTechnique interface.
 *     
 */
package ca.sciencestudio.model.facility;

import java.io.Serializable;

/**
 * @author maxweld
 */
public interface InstrumentTechnique extends Serializable {

	public int getId();
	public void setId(int id);
	
	public int getInstrumentId();
	public void setInstrumentId(int instrumentId);
	
	public int getTechniqueId();
	public void setTechniqueId(int techniqueId);
	
	public InstrumentTechnique clone();
}
