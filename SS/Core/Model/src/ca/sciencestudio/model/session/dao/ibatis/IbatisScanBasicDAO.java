/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     IbatisScanBasicDAO class.
 *     
 */
package ca.sciencestudio.model.session.dao.ibatis;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;

import ca.sciencestudio.model.session.Experiment;
import ca.sciencestudio.model.session.Scan;
import ca.sciencestudio.model.session.dao.ScanBasicDAO;
import ca.sciencestudio.model.session.dao.ibatis.support.IbatisScan;
import ca.sciencestudio.model.dao.ibatis.AbstractIbatisModelBasicDAO;
import ca.sciencestudio.model.utilities.GID;

import ca.sciencestudio.util.Parameters;
import ca.sciencestudio.util.exceptions.ModelAccessException;
import ca.sciencestudio.util.http.ByteArrayHttpInputMessage;
import ca.sciencestudio.util.http.ByteArrayHttpOutputMessage;

/**
 * @author maxweld
 *
 */
public class IbatisScanBasicDAO extends AbstractIbatisModelBasicDAO<Scan> implements ScanBasicDAO {

	private MediaType parametersType = MediaType.APPLICATION_JSON;  
	
	private Collection<HttpMessageConverter<Object>> messageConverters = Collections.emptyList();
	
	@Override
	public String getGidType() {
		return Scan.GID_TYPE;
	}
	
	@Override
	public List<Scan> getAllByExperimentGid(String experimentGid) {
		GID gid = parseAndCheckGid(experimentGid, getGidFacility(), Experiment.GID_TYPE);
		if(gid == null) {
			return Collections.emptyList();
		}
		
		List<Scan> scans;
		try {
			scans = toModelList(getSqlMapClientTemplate().queryForList(getStatementName("get", "ListByExperimentId"), gid.getId()));
		}
		catch(DataAccessException e) {
			logger.warn("Data Access exception while getting Model list: " + e.getMessage());
			throw new ModelAccessException(e);
		}
		
		if(logger.isDebugEnabled()) {
			logger.debug("Get all Scans by Experiment GID: " + experimentGid + ", size: " + scans.size());
		}
		return Collections.unmodifiableList(scans);
	}	
	
	@Override
	protected IbatisScan toIbatisModel(Scan scan) {
		if(scan == null) {
			return null;
		}
		IbatisScan ibatisScan = new IbatisScan();
		GID gid = GID.parse(scan.getGid());
		if((gid != null) && gid.isFacilityAndType(getGidFacility(), getGidType(), true, true)) {
			ibatisScan.setId(gid.getId());
		}
		GID experimentGid = GID.parse(scan.getExperimentGid());
		if((experimentGid != null) && experimentGid.isFacilityAndType(getGidFacility(), Experiment.GID_TYPE, true, true)) {
			ibatisScan.setExperimentId(experimentGid.getId());
		}
		ibatisScan.setName(scan.getName());
		ibatisScan.setDataUrl(scan.getDataUrl());
		String parameters = convertFromParameters(scan.getParameters(), parametersType);
		ibatisScan.setParametersType(parametersType.toString());
		ibatisScan.setParameters(parameters);
		ibatisScan.setStartDate(scan.getStartDate());
		ibatisScan.setEndDate(scan.getEndDate());
		return ibatisScan;
	}
	
	@Override
	protected Scan toModel(Object obj) {
		if(!(obj instanceof IbatisScan)) {
			return null;
		}
		IbatisScan ibatisScan = (IbatisScan)obj;
		Scan scan = new Scan();
		scan.setGid(GID.format(getGidFacility(), ibatisScan.getId(), getGidType()));
		scan.setExperimentGid(GID.format(getGidFacility(), ibatisScan.getExperimentId(), Experiment.GID_TYPE));
		scan.setName(ibatisScan.getName());
		scan.setDataUrl(ibatisScan.getDataUrl());
		scan.setParameters(convertToParameters(ibatisScan.getParameters(), ibatisScan.getParametersType()));
		scan.setStartDate(ibatisScan.getStartDate());
		scan.setEndDate(ibatisScan.getEndDate());
		return scan;
	}
	
	@Override
	protected String getStatementName(String prefix, String suffix) {
		return prefix + "Scan" + suffix;
	}

	protected String convertFromParameters(Parameters parameters, MediaType paramsMediaType) {
		for(HttpMessageConverter<Object> mc : messageConverters) {
			if(mc.canWrite(Parameters.class, paramsMediaType)) {
				try {
					ByteArrayHttpOutputMessage httpOutputMessage = new ByteArrayHttpOutputMessage();
					mc.write(parameters, paramsMediaType, httpOutputMessage);
					return new String(httpOutputMessage.toByteArray());
				}
				catch(IOException e) {
					continue;
				}	
			}
		}
		throw new IllegalArgumentException("No message converter found to write type: " + paramsMediaType);
	}
	
	protected Parameters convertToParameters(String parameters, String parametersType) {
		MediaType paramsMediaType = MediaType.parseMediaType(parametersType);
		for(HttpMessageConverter<Object> mc : messageConverters) {
			if(mc.canRead(Parameters.class, paramsMediaType)) {
				try {
					ByteArrayHttpInputMessage httpInputMessage = new ByteArrayHttpInputMessage(parameters.getBytes());
					return (Parameters) mc.read(Parameters.class, httpInputMessage);
				}
				catch(IOException e) {
					continue;
				}	
			}
		}
		throw new IllegalArgumentException("No message converter found to read type: " + paramsMediaType);
	}
	
	public MediaType getParametersType() {
		return parametersType;
	}
	public void setParametersType(MediaType parametersType) {
		this.parametersType = parametersType;
	}

	public Collection<HttpMessageConverter<Object>> getMessageConverters() {
		return messageConverters;
	}
	public void setMessageConverters(Collection<HttpMessageConverter<Object>> messageConverters) {
		this.messageConverters = messageConverters;
	}
}
