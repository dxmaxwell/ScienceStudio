/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     RestInstrumentAuthzDAO class.
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
import ca.sciencestudio.model.facility.Instrument;
import ca.sciencestudio.model.facility.dao.InstrumentAuthzDAO;
import ca.sciencestudio.model.facility.dao.rest.support.RestInstrument;
import ca.sciencestudio.util.exceptions.ModelAccessException;

/**
 * @author maxweld
 * 
 *
 */
public class RestInstrumentAuthzDAO extends AbstractRestModelAuthzDAO<Instrument> implements InstrumentAuthzDAO {

	public static final String INSTRUMENT_MODEL_PATH = "/model/instruments";
	
	@Override
	public Data<List<Instrument>> getAllByLaboratoryGid(String laboratoryGid) {
		List<Instrument> laboratories;
		try {
			laboratories = Arrays.asList(getRestTemplate().getForObject(getModelUrl("", "laboratory={laboratory}"), getModelArrayClass(), laboratoryGid));
		}
		catch(RestClientException e) {
			logger.warn("Rest Client exception while getting Model list: " + e.getMessage());
			return new SimpleData<List<Instrument>>(new ModelAccessException(e));
		}
		
		if(logger.isDebugEnabled()) {
			logger.debug("Get all Models, size: " + laboratories.size());
		}
		return new SimpleData<List<Instrument>>(Collections.unmodifiableList(laboratories));
	}

	@Override
	protected Object toRestModel(Instrument instrument) {
		RestInstrument restInstrument = new RestInstrument();
		restInstrument.setLaboratoryGid(instrument.getLaboratoryGid());
		restInstrument.setName(instrument.getName());
		restInstrument.setLongName(instrument.getLongName());
		restInstrument.setDescription(instrument.getDescription());
		return restInstrument;
	}

	@Override
	protected String getModelPath() {
		return INSTRUMENT_MODEL_PATH;
	}

	@Override
	protected Class<Instrument> getModelClass() {
		return Instrument.class;
	}

	@Override
	protected Class<Instrument[]> getModelArrayClass() {
		return Instrument[].class;
	}
}
