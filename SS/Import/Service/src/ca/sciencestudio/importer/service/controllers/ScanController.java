/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     ScanController class.
 *     
 */
package ca.sciencestudio.importer.service.controllers;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.client.ContentExchange;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.HttpExchange;
import org.eclipse.jetty.client.security.HashRealmResolver;
import org.eclipse.jetty.http.HttpSchemes;
import org.eclipse.jetty.http.HttpStatus;
import org.eclipse.jetty.io.Buffer;
import org.eclipse.jetty.util.URIUtil;
import org.eclipse.jetty.util.log.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import ca.sciencestudio.model.facility.dao.LaboratoryAuthzDAO;
import ca.sciencestudio.model.session.Scan;
import ca.sciencestudio.model.session.Session;
import ca.sciencestudio.model.session.dao.ScanAuthzDAO;
import ca.sciencestudio.model.session.dao.SessionAuthzDAO;
import ca.sciencestudio.model.utilities.GID;
import ca.sciencestudio.security.util.SecurityUtil;
import ca.sciencestudio.util.http.ClientRealmUtil;
import ca.sciencestudio.util.json.JsonProperties;
import ca.sciencestudio.util.rest.AddResult;

@Controller
public class ScanController {
	private static final String PARAM_ID = "id";
	
	private String realm;
	private String jsonFile;
	
	@Autowired
	private SessionAuthzDAO sessionAuthzDAO;

	@Autowired
	private LaboratoryAuthzDAO laboratoryAuthzDAO;
	
	@Autowired
	private ScanAuthzDAO scanAuthzDAO;
	
	private HttpClient _client;

	private Map dataSource; 

	@PostConstruct
	public void init() throws ServletException, IOException {
		_client = new HttpClient();
		_client.setConnectorType(HttpClient.CONNECTOR_SELECT_CHANNEL);
		try {
			_client.start();
		} catch (Exception e) {
			Log.warn("cannot start client");
			throw new ServletException(e);
		}
		HashRealmResolver realmResolver = new HashRealmResolver();
        try {
			ClientRealmUtil.addRealms(realmResolver, realm);
		} catch (IOException e) {
			e.printStackTrace();
		}
        _client.setRealmResolver(realmResolver);
        dataSource = (Map) JsonProperties.loadMap(jsonFile);

	}
	
	@PreDestroy
	public void destroy() throws Exception {
		_client.stop();
	}

	
	// TODO change the mapping url
	@RequestMapping(value = "/origin", method = RequestMethod.GET)
	public String requestHandler(
			@RequestParam String sessionGid, 
			@RequestParam(value=PARAM_ID, required=false) String idParam,
			HttpServletRequest request,
			final HttpServletResponse response,
			ModelMap model)
			throws Exception {


//		String responseView = "response-json"; 
		
		String user = SecurityUtil.getPersonGid();
		
		Session session = sessionAuthzDAO.get(user, sessionGid).get();
		if(session == null) {
			model.put("error", "Session not found.");
			return "frag/error";
		}
		
		String labName = laboratoryAuthzDAO.get(session.getLaboratoryGid()).get().getName();
		
		LabAuthz labAuthz = LabAuthz.createInstance(labName, sessionGid, sessionAuthzDAO);

		if (!labAuthz.canRead()) {
			response.setStatus(HttpStatus.UNAUTHORIZED_401);
			model.put("error", "Not permitted to view scan list.");
			return "page/error";
		}

		String url = null;
		String query = null;

		if (idParam == null || idParam.equalsIgnoreCase("_all")) {
			query = "_scans";
		} else {
			query = "_scans/" + idParam;
		}

		String import_url = (String) dataSource.get(labName.toLowerCase());
		
		if (import_url == null) {
			model.put("error", "Import server not known.");
			response.setStatus(HttpStatus.BAD_REQUEST_400);
			return "page/error";
		} else {
			url = URIUtil.addPaths(import_url, query);
		}

		ContentExchange content = new ContentExchange(false) {
			protected void onResponseHeader(Buffer name, Buffer value) throws IOException {
				// there might be some headers not wanted
				response.addHeader(name.toString(), value.toString());

			}
		};
		content.setScheme(HttpSchemes.HTTP_BUFFER);
		content.setMethod(request.getMethod());
		content.setURL(url);
		content.setVersion(request.getProtocol());
		_client.send(content);
		int exchangeStatus = content.waitForDone();
		int responseStatus = content.getResponseStatus();

		model.put("error", "proxy problem unknown.");

		if (exchangeStatus == HttpExchange.STATUS_COMPLETED) {

			if (responseStatus >= 200 && responseStatus < 300) {
				OutputStream out = response.getOutputStream();
				byte[] contentbytes = content.getResponseContentBytes();
				if (contentbytes != null) {
					out.write(content.getResponseContentBytes());
				}
				out.close();
				return null;
			} else if (responseStatus == HttpStatus.UNAUTHORIZED_401) {
				response.setStatus(HttpStatus.BAD_REQUEST_400);
				model.put("error", "proxy not authenrized.");
				return "page/error";
			} else {
				model.put("error", content.getResponseContent());
			}
			
		}

		if (exchangeStatus == HttpExchange.STATUS_EXPIRED) {
			model.put("error", "proxy expired.");
		}

		if (exchangeStatus == HttpExchange.STATUS_EXCEPTED) {
			model.put("error", "proxy exception.");
		}

		if (exchangeStatus == HttpExchange.STATUS_CANCELLED) {
			model.put("error", "proxy cancelled.");
		}

		response.setStatus(HttpStatus.BAD_REQUEST_400);
		return "page/error";
	}
	
	
	// TODO change the mapping url
	@RequestMapping(value = "/ss", method = RequestMethod.POST)
	public String ceateScanHandler(
			@RequestParam String sessionGid, 
			@RequestParam String experimentGid,
			@RequestParam String path,
			@RequestParam String scanName,
			HttpServletRequest request,
			final HttpServletResponse response,
			ModelMap model)
			throws Exception {
		
		String user = SecurityUtil.getPersonGid();
		
		Session session = sessionAuthzDAO.get(user, sessionGid).get();
		if(session == null) {
			response.setStatus(HttpStatus.BAD_REQUEST_400);
			model.put("error", "Session not found.");
			return "page/error";
		}
		
		String labName = laboratoryAuthzDAO.get(session.getLaboratoryGid()).get().getName();
		
		LabAuthz labAuthz = LabAuthz.createInstance(labName, sessionGid, sessionAuthzDAO);

		if (!labAuthz.canWrite()) {
			response.setStatus(HttpStatus.UNAUTHORIZED_401);
			model.put("error", "Not permitted to create scan.");
			return "page/error";
		}
		
		Date now = new Date();
		Scan scan = new Scan();
		scan.setName(scanName);
		scan.setDataUrl(path);
		scan.setExperimentGid(experimentGid);
		scan.setStartDate(now);
		scan.setEndDate(now);
		
		AddResult result = scanAuthzDAO.add(SecurityUtil.getPersonGid(), scan).get();
		if(result.hasErrors()) {
			response.setStatus(HttpStatus.BAD_REQUEST_400);
			model.put("error", "Error while adding new scan. (1)");
			return "page/error";
		}
		
		GID gid = GID.parse(scan.getGid());
		if(gid == null) {
			response.setStatus(HttpStatus.BAD_REQUEST_400);
			model.put("error", "Error while adding new scan. (2)");
			return "page/error";
		}
		
		model.put("response", scan);
		
		return "response-json";
	}
	
	public void setRealm(String realm) {
		this.realm = realm;
	}
	
	public String getJsonFile() {
		return jsonFile;
	}

	public void setJsonFile(String jsonFile) {
		this.jsonFile = jsonFile;
	}


}