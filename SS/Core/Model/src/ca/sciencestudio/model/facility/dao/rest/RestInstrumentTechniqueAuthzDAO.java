/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     InstrumentTechniqueAuthzDAO class.
 *     
 */
package ca.sciencestudio.model.facility.dao.rest;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.web.client.RestClientException;

import ca.sciencestudio.model.dao.Data;
import ca.sciencestudio.model.dao.SimpleData;
import ca.sciencestudio.model.dao.rest.AbstractRestModelAuthzDAO;
import ca.sciencestudio.model.facility.InstrumentTechnique;
import ca.sciencestudio.model.facility.dao.InstrumentTechniqueAuthzDAO;
import ca.sciencestudio.model.facility.dao.rest.support.RestInstrumentTechnique;
import ca.sciencestudio.util.exceptions.ModelAccessException;

public class RestInstrumentTechniqueAuthzDAO extends AbstractRestModelAuthzDAO<InstrumentTechnique> implements InstrumentTechniqueAuthzDAO {

	public static final String INSTRUMENT_TECHNIQUE_MODEL_PATH = "/instrument/techniques";
	
	@Override
	public Data<List<InstrumentTechnique>> getAllByLaboratoryGid(String laboratoryGid) {
		List<InstrumentTechnique> instrumentTechniques;
		try {
			instrumentTechniques = Arrays.asList(getRestTemplate().getForObject(getRestUrl("", "laboratory={laboratory}"), getModelArrayClass(), laboratoryGid));
		}
		catch(RestClientException e) {
			logger.warn("Rest Client exception while getting Model list: " + e.getMessage());
			return new SimpleData<List<InstrumentTechnique>>(new ModelAccessException(e));
		}
		
		if(logger.isDebugEnabled()) {
			logger.debug("Get all InstrumentTechniques by Laboratory GID: " + laboratoryGid + ", size: " + instrumentTechniques.size());
		}
		return new SimpleData<List<InstrumentTechnique>>(Collections.unmodifiableList(instrumentTechniques));
	}

	@Override
	protected Object toRestModel(InstrumentTechnique instrumentTechnique) {
		RestInstrumentTechnique restInstrumentTechnique = new RestInstrumentTechnique();
		restInstrumentTechnique.setInstrumentGid(instrumentTechnique.getInstrumentGid());
		restInstrumentTechnique.setTechniqueGid(instrumentTechnique.getTechniqueGid());
		return restInstrumentTechnique;
	}

	@Override
	protected String getModelPath() {
		return INSTRUMENT_TECHNIQUE_MODEL_PATH;
	}

	@Override
	protected Class<InstrumentTechnique> getModelClass() {
		return InstrumentTechnique.class;
	}

	@Override
	protected Class<InstrumentTechnique[]> getModelArrayClass() {
		return InstrumentTechnique[].class;
	}
}
