/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     RestScanAuthzDAO class.
 *     
 */
package ca.sciencestudio.model.session.dao.rest;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.InflaterInputStream;

import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestClientException;

import ca.sciencestudio.model.session.Scan;
import ca.sciencestudio.model.session.dao.ScanAuthzDAO;
import ca.sciencestudio.model.session.dao.rest.support.RestScan;
import ca.sciencestudio.model.dao.Data;
import ca.sciencestudio.model.dao.SimpleData;
import ca.sciencestudio.model.dao.rest.AbstractRestModelAuthzDAO;
import ca.sciencestudio.util.exceptions.ModelAccessException;
import ca.sciencestudio.util.http.EncodingType;
import ca.sciencestudio.util.io.TempFileInputStream;
import ca.sciencestudio.util.io.TempFileOutputStream;

/**
 * @author maxweld
 *
 */
public class RestScanAuthzDAO extends AbstractRestModelAuthzDAO<Scan> implements ScanAuthzDAO {
	
	public static final String SCAN_MODEL_PATH = "/model/scans";
	
	public static final String HTTP_HEADER_ACCEPT_ENCODING = "Accept-Encoding";
	
	public static final String HTTP_HEADER_CONTENT_ENCODING = "Content-Encoding";
	
	public static final String DEFAULT_HEADER_ACCEPT_ENCODING = "gzip,deflate";
	
	private static final DataRequestCallback DATA_REQUEST_CALLBACK = new DataRequestCallback();
	
	private static final DataResponseExtractor DATA_RESPONSE_EXTRACTOR = new DataResponseExtractor();
	
	private static final String TEMP_DATA_FILE_PREFIX = "SSDATA";
		
	private static final String TEMP_DATA_FILE_SUFFIX = "";
	
	private static final int BUFFER_SIZE = 10485760;
	
	private static final int BUCKET_SIZE = 4096;
	
	
	
	@Override
	public Data<List<Scan>> getAllByExperimentGid(String user, String experimentGid) {
		List<Scan> scans;
		try {
			scans = Arrays.asList(getRestTemplate().getForObject(getModelUrl("", "user={user}", "experiment={experimentGid}"), getModelArrayClass(), user, experimentGid));
		}
		catch(RestClientException e) {
			logger.warn("Rest Client exception while getting Model list: " + e.getMessage());
			return new SimpleData<List<Scan>>(new ModelAccessException(e));
		}
		
		if(logger.isDebugEnabled()) {
			logger.debug("Get all Scans by Experiment GID: " + experimentGid + ", size: " + scans.size());
		}
		return new SimpleData<List<Scan>>(Collections.unmodifiableList(scans));
	}

	@Override
	public Data<InputStream> getData(String user, String gid, String path) {
		InputStream dataInputStream;
		try {
			dataInputStream = getRestTemplate().execute(getModelUrl("/{gid}/data/" + path, "user={user}"), HttpMethod.GET, DATA_REQUEST_CALLBACK, DATA_RESPONSE_EXTRACTOR, gid, user);
		}
		catch(RestClientException e) {
			logger.warn("Rest Client exception while getting data: " + e.getMessage());
			return new SimpleData<InputStream>(new ModelAccessException(e));
		}
		
		if(logger.isDebugEnabled()) {
			logger.debug("Get data in Scan with GID: " + gid + ", path: " + path);
		}
		return new SimpleData<InputStream>(dataInputStream);
	}
	
	@Override
	protected RestScan toRestModel(Scan scan) {
		RestScan restScan = new RestScan();
		restScan.setName(scan.getName());
		restScan.setExperimentGid(scan.getExperimentGid());
		restScan.setDataUrl(scan.getDataUrl());
		restScan.setParameters(scan.getParameters());
		restScan.setStartDate(scan.getStartDate());
		restScan.setEndDate(scan.getEndDate());
		return restScan;
	}

	@Override
	protected String getModelPath() {
		return SCAN_MODEL_PATH;
	}

	@Override
	protected Class<Scan> getModelClass() {
		return Scan.class;
	}

	@Override
	protected Class<Scan[]> getModelArrayClass() {
		return Scan[].class;
	}
	
	private static class DataRequestCallback implements RequestCallback {

		@Override
		public void doWithRequest(ClientHttpRequest request) throws IOException {
			request.getHeaders().add(HTTP_HEADER_ACCEPT_ENCODING, DEFAULT_HEADER_ACCEPT_ENCODING);
		}
	}
	
	private static class DataResponseExtractor implements ResponseExtractor<InputStream> {

		@Override
		public InputStream extractData(ClientHttpResponse response) throws IOException {
	
			InputStream body = new BufferedInputStream(response.getBody());
			
			ByteArrayOutputStream buffer = new ByteArrayOutputStream();
						
			byte[] bucket = new byte[BUCKET_SIZE];
			int nBuckets = BUFFER_SIZE / BUCKET_SIZE;	
			
			for(int i = 0; i < nBuckets; i++) {
				int n = body.read(bucket);
				if(n < 0) {
					InputStream data = new ByteArrayInputStream(buffer.toByteArray());
					return contentDecoder(response.getHeaders(), data);
				}
				buffer.write(bucket, 0, n);
			}
			
			TempFileOutputStream tempOutputStream = new TempFileOutputStream(TEMP_DATA_FILE_PREFIX, TEMP_DATA_FILE_SUFFIX);
			
			tempOutputStream.write(buffer.toByteArray());
				
			IOUtils.copyLarge(body, tempOutputStream);
			
			InputStream data = new TempFileInputStream(tempOutputStream, true);
			return contentDecoder(response.getHeaders(), data); 
		}
		
		protected InputStream contentDecoder(HttpHeaders headers, InputStream inputStream) throws IOException {
			String contentEncoding = headers.getFirst(HTTP_HEADER_CONTENT_ENCODING);
			if(contentEncoding != null) {
				if(EncodingType.GZIP.getType().equalsIgnoreCase(contentEncoding)) {
					return new GZIPInputStream(inputStream);
				}
				else if(EncodingType.DEFLATE.getType().equalsIgnoreCase(contentEncoding)) {
					return new InflaterInputStream(inputStream);				
				}
				else {
					throw new IOException("Content Encoding not supported: " + contentEncoding);
				}	
			}
			return inputStream;
		}
	}
}
