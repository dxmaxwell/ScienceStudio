/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     RestScanAuthzDAO class.
 *     
 */
package ca.sciencestudio.model.session.dao.rest;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.InflaterInputStream;

import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;

import ca.sciencestudio.model.session.Scan;
import ca.sciencestudio.model.session.dao.ScanAuthzDAO;
import ca.sciencestudio.model.session.dao.rest.support.RestScan;
import ca.sciencestudio.model.dao.Data;
import ca.sciencestudio.model.dao.SimpleData;
import ca.sciencestudio.model.dao.rest.AbstractRestModelAuthzDAO;
import ca.sciencestudio.util.exceptions.ModelAccessException;
import ca.sciencestudio.util.http.EncodingType;
import ca.sciencestudio.util.rest.FileProps;

/**
 * @author maxweld
 *
 */
public class RestScanAuthzDAO extends AbstractRestModelAuthzDAO<Scan> implements ScanAuthzDAO {
	
	public static final String SCAN_MODEL_PATH = "/model/scans";
	
	public static final String HTTP_HEADER_ACCEPT_ENCODING = "Accept-Encoding";
	
	public static final String HTTP_HEADER_CONTENT_ENCODING = "Content-Encoding";
	
	public static final String DEFAULT_HEADER_ACCEPT_ENCODING = "gzip,deflate";
	
	@Override
	public Data<List<Scan>> getAllByExperimentGid(String user, String experimentGid) {
		List<Scan> scans;
		try {
			scans = Arrays.asList(getRestTemplate().getForObject(getModelUrl("", "user={user}", "experiment={experimentGid}"), getModelArrayClass(), user, experimentGid));
		}
		catch(RestClientException e) {
			logger.warn("Rest Client exception while getting Scan list: " + e.getMessage());
			return new SimpleData<List<Scan>>(new ModelAccessException(e));
		}
		
		if(logger.isDebugEnabled()) {
			logger.debug("Get all Scans by Experiment GID: " + experimentGid + ", size: " + scans.size());
		}
		return new SimpleData<List<Scan>>(Collections.unmodifiableList(scans));
	}

	@Override
	public Data<InputStream> getFileData(String user, String gid, String path) {
		URI fileDataUrl;
		try {
			fileDataUrl = URI.create(getModelUrl("/" + gid + "/file/data" + path, "user=" + user));
		}
		catch(IllegalArgumentException e) {
			logger.warn("Invalid file data URL: " + e.getMessage());
			return new SimpleData<InputStream>(new ModelAccessException(e));
		}
		
		ClientHttpRequest request;
		try {
			request = getRestTemplate().getRequestFactory().createRequest(fileDataUrl, HttpMethod.GET);
		}
		catch(IOException e) {
			logger.warn("Error creating file data request: " + e.getMessage());
			return new SimpleData<InputStream>(new ModelAccessException(e));
		}
		
		request.getHeaders().add(HTTP_HEADER_ACCEPT_ENCODING, DEFAULT_HEADER_ACCEPT_ENCODING);
		
		ClientHttpResponse response;
		try {
			response = request.execute();
		}
		catch(IOException e) {
			logger.warn("Error executing file data request: " + e.getMessage());
			return new SimpleData<InputStream>(new ModelAccessException(e));
		}
		
		try {
			if(getRestTemplate().getErrorHandler().hasError(response)) {
				getRestTemplate().getErrorHandler().handleError(response);
			}
		}
		catch(HttpClientErrorException e) {
			logger.debug("HTTP Client Error exception while getting file data: " + e.getMessage());
			return new SimpleData<InputStream>((InputStream)null);
		}
		catch(IOException e) {
			logger.warn("Rest Client exception while getting file data: " + e.getMessage());
			return new SimpleData<InputStream>(new ModelAccessException(e));
		}
		
		InputStream inputStream;
		try {
			inputStream = new ClientHttpResponseInputStream(response);
		}
		catch(IOException e) {
			logger.warn("Error creating input stream from response: " + e.getMessage());
			return new SimpleData<InputStream>(new ModelAccessException(e));
		}
		
		return new SimpleData<InputStream>(inputStream);
	}
	
	@Override
	public Data<List<FileProps>> getFileList(String user, String gid, String path) {
		List<FileProps> filePropsList;
		try {
			filePropsList = Arrays.asList(getRestTemplate().getForObject(getModelUrl("/{gid}/file/list" + path, "user={user}"), FileProps[].class, gid, user));
		}
		catch(RestClientException e) {
			logger.warn("Rest Client exception while getting data: " + e.getMessage());
			return new SimpleData<List<FileProps>>(new ModelAccessException(e));
		}
		
		if(logger.isDebugEnabled()) {
			logger.debug("Get data in Scan with GID: " + gid + ", path: " + path);
		}
		return new SimpleData<List<FileProps>>(filePropsList);
	}

	@Override
	public Data<List<FileProps>> getFileList(String user, String gid, String path, String type) {
		List<FileProps> filePropsList;
		try {
			filePropsList = Arrays.asList(getRestTemplate().getForObject(getModelUrl("/{gid}/file/list" + path, "user={user}", "type={type}"), FileProps[].class, gid, user, type));
		}
		catch(RestClientException e) {
			logger.warn("Rest Client exception while getting data: " + e.getMessage());
			return new SimpleData<List<FileProps>>(new ModelAccessException(e));
		}
		
		if(logger.isDebugEnabled()) {
			logger.debug("Get data in Scan with GID: " + gid + ", path: " + path);
		}
		return new SimpleData<List<FileProps>>(filePropsList);
	}

	@Override
	public Data<List<FileProps>> getFileList(String user, String gid, String path, String type, int depth) {
		List<FileProps> filePropsList;
		try {
			filePropsList = Arrays.asList(getRestTemplate().getForObject(getModelUrl("/{gid}/file/list" + path, "user={user}", "type={type}", "depth={depth}"), FileProps[].class, gid, user, type, depth));
		}
		catch(RestClientException e) {
			logger.warn("Rest Client exception while getting data: " + e.getMessage());
			return new SimpleData<List<FileProps>>(new ModelAccessException(e));
		}
		
		if(logger.isDebugEnabled()) {
			logger.debug("Get data in Scan with GID: " + gid + ", path: " + path);
		}
		return new SimpleData<List<FileProps>>(filePropsList);
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
	
	private static class ClientHttpResponseInputStream extends InputStream {
		
		private InputStream body;
		private ClientHttpResponse response;

		public ClientHttpResponseInputStream(ClientHttpResponse response) throws IOException {
			this.response = response;
			body = response.getBody();
			String contentEncodingHeader = response.getHeaders().getFirst(HTTP_HEADER_CONTENT_ENCODING);
			if((contentEncodingHeader != null) && (contentEncodingHeader.length() > 0)) {
				EncodingType contentEncoding = new EncodingType(contentEncodingHeader);
				if(EncodingType.GZIP.equals(contentEncoding)) {
					body = new GZIPInputStream(body);
				}
				else if(EncodingType.DEFLATE.equals(contentEncoding)) {
					body = new InflaterInputStream(body);
				}
				else if(EncodingType.IDENTITY.equals(contentEncoding)) {
					// body = body //
				}
				else {
					throw new IOException("Content Encoding not supported: " + contentEncoding);
				}
			}
		}

		@Override
		public int read() throws IOException {
			return body.read();
		}

		@Override
		public int read(byte[] b) throws IOException {
			return body.read(b);
		}

		@Override
		public int read(byte[] b, int off, int len) throws IOException {
			return body.read(b, off, len);
		}

		@Override
		public long skip(long n) throws IOException {
			return body.skip(n);
		}

		@Override
		public int available() throws IOException {
			return body.available();
		}

		@Override
		public void close() throws IOException {
			body.close();
			response.close();
		}

		@Override
		public synchronized void mark(int readlimit) {
			body.mark(readlimit);
		}

		@Override
		public synchronized void reset() throws IOException {
			body.reset();
		}

		@Override
		public boolean markSupported() {
			return body.markSupported();
		}
	}
}
