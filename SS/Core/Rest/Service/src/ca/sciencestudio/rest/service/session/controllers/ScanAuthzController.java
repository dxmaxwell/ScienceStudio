/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     ScanAuthzController class.
 *     
 */
package ca.sciencestudio.rest.service.session.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.GZIPOutputStream;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.ServletContextAware;

import ca.sciencestudio.model.dao.ModelBasicDAO;
import ca.sciencestudio.model.session.Experiment;
import ca.sciencestudio.model.session.Scan;
import ca.sciencestudio.model.session.Session;
import ca.sciencestudio.model.session.dao.ExperimentBasicDAO;
import ca.sciencestudio.model.session.dao.ScanBasicDAO;
import ca.sciencestudio.model.session.dao.SessionBasicDAO;
import ca.sciencestudio.model.session.validators.ScanValidator;
import ca.sciencestudio.model.validators.ModelValidator;
import ca.sciencestudio.rest.service.controllers.AbstractSessionAuthzController;
import ca.sciencestudio.util.authz.Authorities;
import ca.sciencestudio.util.exceptions.ModelAccessException;
import ca.sciencestudio.util.http.EncodingType;
import ca.sciencestudio.util.rest.AddResult;
import ca.sciencestudio.util.rest.EditResult;
import ca.sciencestudio.util.rest.FileProps;
import ca.sciencestudio.util.rest.RemoveResult;

/**
 * @author maxweld
 *
 */
@Controller
public class ScanAuthzController extends AbstractSessionAuthzController<Scan> implements ServletContextAware {

	private static final String SCAN_MODEL_PATH = "/scans";
	
	private static final String URI_SCHEME_FILE = "file:";
	
	private static final String URI_SEPERATOR = "/";
	
	private static final String HTTP_HEADER_ACCEPT_ENCODING = "Accept-Encoding";
	
	private static final String HTTP_HEADER_CONTENT_ENCODING = "Content-Encoding";
	
	private static final String DEFAULT_RELATIVE_PATH = "";
	
	private static final String DEFAULT_CONTENT_TYPE = null;
	
	private static final List<EncodingType> SUPPORTED_ENCODING_TYPES = Arrays.asList(EncodingType.GZIP, EncodingType.DEFLATE, EncodingType.IDENTITY);	
	
	private static final String FILE_TYPE_ANY = "any";
	
	private static final String FILE_TYPE_FILE = "file";
	
	private static final String FILE_TYPE_DIRECTORY = "directory";
	
	private ScanBasicDAO scanBasicDAO;
	
	private SessionBasicDAO sessionBasicDAO;
	
	private ExperimentBasicDAO experimentBasicDAO;
	
	private ScanValidator scanValidator;
	
	private ServletContext servletContext;
	
	@ResponseBody 
	@RequestMapping(value = SCAN_MODEL_PATH + "*", method = RequestMethod.POST)
	public AddResult add(@RequestBody Scan scan, @RequestParam String user, HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		String experimentGid = scan.getExperimentGid();
		
		Experiment experiment;
		try {
			experiment = experimentBasicDAO.get(experimentGid);
		}
		catch(ModelAccessException e) {
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return new AddResult(e.getMessage());
		}
		
		if(experiment == null) {
			response.setStatus(HttpStatus.NOT_FOUND.value());
			return new AddResult("Experiment (" + experimentGid + ") not found.");
		}
		
		Authorities authorities;
		try {
			authorities = sessionAuthorityAccessor.getAuthorities(user, experiment.getSessionGid());
		}
		catch(ModelAccessException e) {
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return new AddResult(e.getMessage());
		}
		
		if(authorities.containsNone(SESSION_EXPERIMENTER, FACILITY_ADMIN_SESSIONS)) {
			response.setStatus(HttpStatus.FORBIDDEN.value());
			return new AddResult("Required authorities not found.");
		}
		
		return doAdd(scan, request, response);
	}
	
	@ResponseBody
	@RequestMapping(value = SCAN_MODEL_PATH + "/{gid}*", method = RequestMethod.PUT)
	public EditResult edit(@RequestBody Scan scan, @RequestParam String user, @PathVariable String gid, HttpServletResponse response) throws Exception{
		
		Scan current;
		try {
			current = scanBasicDAO.get(gid); 
		}
		catch(ModelAccessException e) {
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return new EditResult(e.getMessage());
		}
		
		if(current == null) {
			response.setStatus(HttpStatus.NOT_FOUND.value());
			return new EditResult("Scan (" + gid + ") not found.");
		}
		
		Experiment experiment;
		try {
			experiment = experimentBasicDAO.get(current.getExperimentGid());
		}
		catch(ModelAccessException e) {
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return new EditResult(e.getMessage());
		}
		
		if(experiment == null) {
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return new EditResult("Experiment (" + current.getExperimentGid() + ") not found.");
		}
		
		Authorities authorities;
		try {
			authorities = sessionAuthorityAccessor.getAuthorities(user, experiment.getSessionGid());
		}
		catch(ModelAccessException e) {
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return new EditResult(e.getMessage());
		}
		
		if(authorities.containsNone(SESSION_EXPERIMENTER, FACILITY_ADMIN_SESSIONS)) {
			response.setStatus(HttpStatus.FORBIDDEN.value());
			return new EditResult("Required authorities not found.");
		}
		
		scan.setGid(current.getGid());
		scan.setExperimentGid(current.getExperimentGid());
		
		return doEdit(scan, response);
	}
	
	@RequestMapping(value = SCAN_MODEL_PATH + "/{gid}*", method = RequestMethod.DELETE)
	public RemoveResult remove(@RequestParam String user, @PathVariable String gid, HttpServletResponse response) throws Exception{
		
		Scan scan;
		try {
			scan = scanBasicDAO.get(gid); 
		}
		catch(ModelAccessException e) {
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return new RemoveResult(e.getMessage());
		}
		
		if(scan == null) {
			response.setStatus(HttpStatus.NOT_FOUND.value());
			return new RemoveResult("Scan (" + gid + ") not found.");
		}
		
		// At least for now, removing scans is never allowed. //
		response.setStatus(HttpStatus.FORBIDDEN.value());
		return new RemoveResult("Removing Scans not allowed."); 
	}
	
	@ResponseBody 
	@RequestMapping(value = SCAN_MODEL_PATH + "/{gid}*", method = RequestMethod.GET)
	public Object get(@RequestParam String user, @PathVariable String gid, HttpServletResponse response) throws Exception {
		
		Scan scan;
		try {
			scan = scanBasicDAO.get(gid); 
		}
		catch(ModelAccessException e) {
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return Collections.emptyMap();
		}
		
		if(scan == null) {
			response.setStatus(HttpStatus.NOT_FOUND.value());
			return Collections.emptyMap();
		}
		
		Experiment experiment;
		try {
			experiment = experimentBasicDAO.get(scan.getExperimentGid());
		}
		catch(ModelAccessException e) {
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return Collections.emptyMap();
		}
		
		if(experiment == null) {
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return Collections.emptyMap();
		}
		
		Authorities authorities;
		try {
			authorities = sessionAuthorityAccessor.getAuthorities(user, experiment.getSessionGid());
		}
		catch(ModelAccessException e) {
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return Collections.emptyMap();
		}
		
		if(!authorities.containsSessionAuthority() && authorities.containsNone(FACILITY_ADMIN_SESSIONS)) {
			
			Session session;
			try {
				session = sessionBasicDAO.get(experiment.getSessionGid());
			}
			catch(ModelAccessException e) {
				response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
				return Collections.emptyMap();
			}
			
			if(session == null) {
				response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
				return Collections.emptyMap();
			}
			
			try {
				authorities = projectAuthzDAO.getAuthorities(user, session.getProjectGid()).get();
			}
			catch(ModelAccessException e) {
				response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
				return Collections.emptyMap();
			}
			
			if(!authorities.containsProjectAuthority() && authorities.containsNone(FACILITY_ADMIN_PROJECTS)) {
				response.setStatus(HttpStatus.FORBIDDEN.value());
				return Collections.emptyMap();
			}
		}
		
		return scan;
	}
	
	@ResponseBody 
	@RequestMapping(value = SCAN_MODEL_PATH + "*", method = RequestMethod.GET, params = "experiment")
	public Object getAll(@RequestParam String user, @RequestParam("experiment") String experimentGid, HttpServletResponse response) throws Exception {
		
		Experiment experiment;
		try {
			experiment = experimentBasicDAO.get(experimentGid);
		}
		catch(ModelAccessException e) {
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return Collections.emptyList();
		}
		
		if(experiment == null) {
			return Collections.emptyList();
		}
		
		Authorities authorities;
		try {
			authorities = sessionAuthorityAccessor.getAuthorities(user, experiment.getSessionGid());
		}
		catch(ModelAccessException e) {
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return Collections.emptyList();
		}
		

		if(!authorities.containsSessionAuthority() && authorities.containsNone(FACILITY_ADMIN_SESSIONS)) {
		
			Session session;
			try {
				session = sessionBasicDAO.get(experiment.getSessionGid());
			}
			catch(ModelAccessException e) {
				response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
				return Collections.emptyList();
			}
			
			if(session == null) {
				response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
				return Collections.emptyList();
			}
			
			try {
				authorities = projectAuthzDAO.getAuthorities(user, session.getProjectGid()).get();
			}
			catch(ModelAccessException e) {
				response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
				return Collections.emptyList();
			}
			
			if(!authorities.containsProjectAuthority() && authorities.containsNone(FACILITY_ADMIN_PROJECTS)) {
				return Collections.emptyList();
			}
		}
		
		try {
			return scanBasicDAO.getAllByExperimentGid(experimentGid);
		}
		catch(ModelAccessException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return Collections.emptyList();
		}
	}

	@ResponseBody
	@RequestMapping(value = SCAN_MODEL_PATH + "/{gid}/file/list/**", method = RequestMethod.GET)
	public Object getFileList(@RequestParam String user, @RequestParam(defaultValue = "any") String type, @RequestParam(defaultValue = "0") int depth,  @PathVariable String gid, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		Scan scan;
		try {
			scan = scanBasicDAO.get(gid);
		}
		catch(ModelAccessException e) {
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return Collections.emptyList();
		}
	
		if(scan == null) {
			response.setStatus(HttpStatus.NOT_FOUND.value());
			return Collections.emptyList();
		}
		
		// *** Check Authorities  *** //
		
		Experiment experiment;
		try {
			experiment = experimentBasicDAO.get(scan.getExperimentGid());
		}
		catch(ModelAccessException e) {
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return Collections.emptyList();
		}
		
		if(experiment == null) {
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return Collections.emptyList();
		}
		
		Authorities authorities;
		try {
			authorities = sessionAuthorityAccessor.getAuthorities(user, experiment.getSessionGid());
		}
		catch(ModelAccessException e) {
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return Collections.emptyList();
		}
		
		if(!authorities.containsSessionAuthority() && authorities.containsNone(FACILITY_ADMIN_SESSIONS)) {
		
			Session session;
			try {
				session = sessionBasicDAO.get(experiment.getSessionGid());
			}
			catch(ModelAccessException e) {
				response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
				return Collections.emptyList();
			}
			
			if(session == null) {
				response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
				return Collections.emptyList();
			}
			
			try {
				authorities = projectAuthzDAO.getAuthorities(user, session.getProjectGid()).get();
			}
			catch(ModelAccessException e) {
				response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
				return Collections.emptyList();
			}
			
			if(!authorities.containsProjectAuthority() && authorities.containsNone(FACILITY_ADMIN_PROJECTS)) {
				return Collections.emptyList();
			}
		}
		
		// ************************* //
		
		URI dataUrl;
		try {
			dataUrl = URI.create(scan.getDataUrl());
		}
		catch(Exception e) {
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return Collections.emptyList();
		}
		
		File dataDirectory = null;
		if(dataUrl.getScheme() == null) {
			dataDirectory = new File(dataUrl.getPath());
		} else if(dataUrl.getScheme().equals(URI_SCHEME_FILE)) {
			dataDirectory = new File(dataUrl);
		}
		System.out.println(dataDirectory);
		if((dataDirectory == null) || !dataDirectory.isDirectory()) {
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return Collections.emptyList();
		}
				
		String dataPath = getRelativePath(request.getRequestURI(), "/file/list/");
		
		File dataFile = new File(dataDirectory, dataPath);
		System.out.println(dataFile);
		
		if(!dataFile.exists()) {
			response.setStatus(HttpStatus.GONE.value());
			return Collections.emptyList();
		}
		
		if(!dataFile.isDirectory()) {
			return Collections.emptyList();
		}
		
		type = type.toLowerCase();
		boolean allowTypeAny = FILE_TYPE_ANY.startsWith(type);
		boolean allowTypeFile = allowTypeAny || FILE_TYPE_FILE.startsWith(type);
		boolean allowTypeDirectory = allowTypeAny || FILE_TYPE_DIRECTORY.startsWith(type);
		
		Queue<File> directories = new LinkedList<File>();
		directories.offer(dataFile);
		
		List<FileProps> filePropsList = new ArrayList<FileProps>();
		for(int level = 0; directories.peek() != null; level++) {
			File pwd = directories.poll();
			for(File file : pwd.listFiles()) {
				if(file.isDirectory()) {
					if(allowTypeDirectory) {
						filePropsList.add(new FileProps(file, dataDirectory));
					}
					if((depth < 0) || (level < depth)) {
						directories.offer(file);
					}
				}
				else if(file.isFile()) {
					if(allowTypeFile) {
						filePropsList.add(new FileProps(file, dataDirectory));
					}
				}
			}
		}
		
		Collections.sort(filePropsList);
		return filePropsList;
	}
	
	
	@RequestMapping(value = SCAN_MODEL_PATH + "/{gid}/file/data/**/*", method = RequestMethod.GET)
	public void getFileData(@RequestParam String user, @PathVariable String gid, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		Scan scan;
		try {
			scan = scanBasicDAO.get(gid);
		}
		catch(ModelAccessException e) {
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return;
		}
	
		if(scan == null) {
			response.setStatus(HttpStatus.NOT_FOUND.value());
			return;
		}
		
		// *** Check Authorities  *** //
		
		Experiment experiment;
		try {
			experiment = experimentBasicDAO.get(scan.getExperimentGid());
		}
		catch(ModelAccessException e) {
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return;
		}
		
		if(experiment == null) {
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return;
		}
		
		Authorities authorities;
		try {
			authorities = sessionAuthorityAccessor.getAuthorities(user, experiment.getSessionGid());
		}
		catch(ModelAccessException e) {
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return;
		}
		
		if(!authorities.containsSessionAuthority() && authorities.containsNone(FACILITY_ADMIN_SESSIONS)) {
		
			Session session;
			try {
				session = sessionBasicDAO.get(experiment.getSessionGid());
			}
			catch(ModelAccessException e) {
				response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
				return;
			}
			
			if(session == null) {
				response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
				return;
			}
			
			try {
				authorities = projectAuthzDAO.getAuthorities(user, session.getProjectGid()).get();
			}
			catch(ModelAccessException e) {
				response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
				return;
			}
			
			if(!authorities.containsProjectAuthority() && authorities.containsNone(FACILITY_ADMIN_PROJECTS)) {
				response.setStatus(HttpStatus.FORBIDDEN.value());
				return;
			}
		}
		
		// ************************* //
		
		URI dataUrl;
		try {
			dataUrl = URI.create(scan.getDataUrl());
		}
		catch(Exception e) {
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return;
		}
		
		File dataDirectory = null;
		if(dataUrl.getScheme() == null) {
			dataDirectory = new File(dataUrl.getPath());
		} else if(dataUrl.getScheme().equals(URI_SCHEME_FILE)) {
			dataDirectory = new File(dataUrl);
		}
		System.out.println(dataDirectory);
		if((dataDirectory == null) || !dataDirectory.isDirectory()) {
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return;
		}
				
		String dataPath = getRelativePath(request.getRequestURI(), "/file/data/");
		
		File dataFile = new File(dataDirectory, dataPath);
		System.out.println(dataFile);
		
		InputStream dataInputStream;
		try {
			dataInputStream = new FileInputStream(dataFile);
		}
		catch(FileNotFoundException e) {
			response.setStatus(HttpStatus.GONE.value());
			return;
		}
		
		EncodingType encodingType = null;
		
		String acceptEncoding = request.getHeader(HTTP_HEADER_ACCEPT_ENCODING);
		if(acceptEncoding == null) {
			encodingType = EncodingType.IDENTITY;
		}
		else {
			List<EncodingType> acceptedEncodingTypes = EncodingType.parseEncodingTypes(acceptEncoding);
			EncodingType.sortByQualityValue(acceptedEncodingTypes);
			for(EncodingType acceptedEncodingType : acceptedEncodingTypes) {
				if((acceptedEncodingType.getQualityValue() > 0.0) &&
						(acceptedEncodingType.isWildcardType() || SUPPORTED_ENCODING_TYPES.contains(acceptedEncodingType))) {				
					encodingType = acceptedEncodingType;
					break;
				}
			}
			
			if((encodingType != null) && encodingType.isWildcardType()) {
				for(EncodingType supportedEncodingType : SUPPORTED_ENCODING_TYPES) {
					int index = acceptedEncodingTypes.indexOf(supportedEncodingType);
					if(index >= 0) {
						EncodingType acceptedEncodingType = acceptedEncodingTypes.get(index);
						if(acceptedEncodingType.getQualityValue() == 0.0) {
							continue;
						}
					}
					encodingType = supportedEncodingType;
					break;
				}
				
				if(encodingType.isWildcardType()) {
					response.setStatus(HttpStatus.NOT_ACCEPTABLE.value());
					return;
				}
			}
			
			if(encodingType == null) {
				int index = acceptedEncodingTypes.indexOf(EncodingType.IDENTITY);
				if(index >= 0) {
					EncodingType acceptedEncodingType = acceptedEncodingTypes.get(index);
					if(acceptedEncodingType.getQualityValue() == 0.0) {
						response.setStatus(HttpStatus.NOT_ACCEPTABLE.value());
						return;
					}
				}
				encodingType = EncodingType.IDENTITY;
			}
		}
		
		OutputStream dataOutputStream = response.getOutputStream();
		
		if(encodingType.equals(EncodingType.GZIP)) {
			dataOutputStream = new GZIPOutputStream(dataOutputStream);
			response.addHeader(HTTP_HEADER_CONTENT_ENCODING, encodingType.getType());
		}
		else if(encodingType.equals(EncodingType.DEFLATE)) {
			dataOutputStream = new DeflaterOutputStream(dataOutputStream);
			response.addHeader(HTTP_HEADER_CONTENT_ENCODING, encodingType.getType());
		}
		
		String contentType = getContentType(dataFile.getPath());
		if(contentType != null) {
			response.setContentType(contentType);
		}

		try {
			System.out.println(IOUtils.copy(dataInputStream, dataOutputStream));
		}
		catch(IOException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}

		IOUtils.closeQuietly(dataInputStream);
		IOUtils.closeQuietly(dataOutputStream);
		return;
	}
	
	protected String getSystemPath(String path) {
		return path.replaceAll(URI_SEPERATOR, File.separator);
	}
	
	protected String getRelativePath(String uri, String separator) {
		int index = uri.indexOf(separator);
		if(index < 0) {
			return DEFAULT_RELATIVE_PATH;
		}
		String relativePath = uri.substring(index + separator.length());
		try {
			return URLDecoder.decode(relativePath, "UTF-8");
		}
		catch(UnsupportedEncodingException e) {
			return relativePath;
		}
	}
	
	protected String getContentType(String path) {
		int index = path.lastIndexOf(URI_SEPERATOR);
		if(index < 0) {
			return DEFAULT_CONTENT_TYPE;
		}
		if(servletContext == null) {
			return DEFAULT_CONTENT_TYPE;
		}
		return servletContext.getMimeType(path.substring(index + 1));
	}
	
	@Override
	public String getModelPath() {
		return SCAN_MODEL_PATH;
	}
	
	@Override
	public ModelBasicDAO<Scan> getModelBasicDAO() {
		return scanBasicDAO;
	}

	@Override
	public ModelValidator<Scan> getModelValidator() {
		return scanValidator;
	}

	public ExperimentBasicDAO getExperimentBasicDAO() {
		return experimentBasicDAO;
	}
	public void setExperimentBasicDAO(ExperimentBasicDAO experimentBasicDAO) {
		this.experimentBasicDAO = experimentBasicDAO;
	}

	public ScanBasicDAO getScanBasicDAO() {
		return scanBasicDAO;
	}
	public void setScanBasicDAO(ScanBasicDAO scanBasicDAO) {
		this.scanBasicDAO = scanBasicDAO;
	}

	public SessionBasicDAO getSessionBasicDAO() {
		return sessionBasicDAO;
	}
	public void setSessionBasicDAO(SessionBasicDAO sessionBasicDAO) {
		this.sessionBasicDAO = sessionBasicDAO;
	}

	public ScanValidator getScanValidator() {
		return scanValidator;
	}
	public void setScanValidator(ScanValidator scanValidator) {
		this.scanValidator = scanValidator;
	}
	
	public ServletContext getServletContext() {
		return servletContext;
	}
	@Override
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}
}
