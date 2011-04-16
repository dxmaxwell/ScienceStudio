/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     TechniqueDAO interface.
 *     
 */
package ca.sciencestudio.model.facility.dao;

import java.util.List;

import ca.sciencestudio.model.facility.Technique;

/**
 * @author maxweld
 *
 */
public interface TechniqueDAO {
	public Technique createTechnique();
	
	public int addTechnique(Technique technique);
	public void editTechnique(Technique technique);
	public void removeTechnique(int techniqueId);
	
	public Technique getTechniqueById(int techniqueId);
	public Technique getTechniqueByNameAndInstrumentId(String techniqueName, int instrumentId);
	
	public List<Technique> getTechniqueList();
	public List<Technique> getTechniqueListByName(String techniqueName);
	public List<Technique> getTechniqueListByInstrumentId(int instrumentId);
	public List<Technique> getTechniqueListByLaboratoryId(int laboratoryId);
	public List<Technique> getTechniqueListByNameAndInstrumentId(String techniqueName, int instrumentId);
}
