/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *	Description:
 *		CCDImageController class.
 *
 */
package ca.sciencestudio.vespers.service.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.HttpExchange;
import org.eclipse.jetty.http.HttpHeaders;
import org.eclipse.jetty.http.HttpSchemes;
import org.eclipse.jetty.io.Buffer;
import org.eclipse.jetty.io.EofException;
import org.eclipse.jetty.util.URIUtil;
import org.eclipse.jetty.util.log.Log;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import ca.sciencestudio.security.util.AuthorityUtil;
import ca.sciencestudio.security.util.SecurityUtil;
import ca.sciencestudio.util.state.StateMap;

/**
 * The controller to get CCD image. This controller acts as a proxy to get
 * the image from an inner service.
 *
 * @author Dong Liu
 *
 */
public class CCDImageController implements Controller {

	private static final String PARAM_TYPE = "type";
	private static final String PARAM_FILE = "file";
	private static final String PARAM_NUMBER = "number";
	private static final String PARAM_DOWN = "download";
	private static final String VALUE_KEY_PROJECT_ID = "projectId";
	private static final String VALUE_KEY_SCAN_ID = "scanId";

	private StateMap beamlineSessionStateMap;
	private String imageServiceURL;
	private String fileTemplate;



	private HttpClient _client;

	public void init() throws ServletException {
		_client = new HttpClient();
		_client.setConnectorType(HttpClient.CONNECTOR_SELECT_CHANNEL);
		try {
			_client.start();
		} catch (Exception e) {
			Log.warn("cannot start client");
			throw new ServletException(e);
		}

	}

	public void destroy() throws Exception {
		_client.stop();
	}

	public ModelAndView handleRequest(HttpServletRequest request,
			final HttpServletResponse response) throws Exception {

		Map<String, Object> model = new HashMap<String, Object>();

		if (!request.getMethod().equalsIgnoreCase("GET")) {
			model.put("errors", "<error>Only GET is supported.</error>");
			model.put("success", "false");
			return new ModelAndView("response", model);
		}

		Integer projectId = (Integer) beamlineSessionStateMap.get(VALUE_KEY_PROJECT_ID);
		if (projectId == null) {
			projectId = new Integer(0);
		}

		if (!(SecurityUtil.hasAuthority(AuthorityUtil.buildProjectGroupAuthority(projectId)))) {
			model.put("errors", "<error>Not permitted.</error>");
			model.put("success", "false");
			return new ModelAndView("response", model);
		}

		String typeParam = request.getParameter(PARAM_TYPE);
		String fileParam = request.getParameter(PARAM_FILE);
		String numberParam = request.getParameter(PARAM_NUMBER);
		String downloadParam = request.getParameter(PARAM_DOWN);

		if ((typeParam == null) || (fileParam == null) || (numberParam == null)) {
			model.put("errors", "<error>Parameters missed.</error>");
			model.put("success", "false");
			return new ModelAndView("response", model);
		}

		// construct the file name
		String fileName = fileTemplate.replaceFirst("%s", "");
		fileName = fileName.replaceFirst(".\\w{3}$", "");
		fileName = fileName.replaceFirst("%s", fileParam);
		fileName = fileName.replaceFirst("%d", numberParam);


		if (downloadParam != null){
			response.addHeader("Content-Disposition", "attachment; filename=" + fileName + "." + typeParam);
		}
		final InputStream in = request.getInputStream();
		final OutputStream out = response.getOutputStream();

		Integer scanId = (Integer) beamlineSessionStateMap.get(VALUE_KEY_SCAN_ID);


		String url = URIUtil.addPaths(imageServiceURL, scanId + URIUtil.SLASH + fileName + "." + typeParam);

		HttpExchange exchange = new HttpExchange() {
			/*protected void onRequestCommitted() throws IOException {
			}

			protected void onRequestComplete() throws IOException {
			}*/

			protected void onResponseComplete() throws IOException {
				response.flushBuffer();
			}

			protected void onResponseContent(Buffer content) throws IOException {
				content.writeTo(out);
			}

			/*protected void onResponseHeaderComplete() throws IOException {
			}*/

			protected void onResponseStatus(Buffer version, int status,
					Buffer reason) throws IOException {

				if (reason != null && reason.length() > 0)
					response.sendError(status, reason.toString());
				else
					response.setStatus(status);
			}

			protected void onResponseHeader(Buffer name, Buffer value)
					throws IOException {
				//String s = name.toString().toLowerCase();
				response.addHeader(name.toString(), value.toString());
			}

			protected void onConnectionFailed(Throwable ex) {
				onException(ex);
			}

			protected void onException(Throwable ex) {
				if (ex instanceof EofException) {
					Log.ignore(ex);
					return;
				}
				Log.warn(ex.toString());
				if (!response.isCommitted())
					response
							.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			}

			protected void onExpire() {
				if (!response.isCommitted())
					response
							.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			}
		};

		exchange.setScheme(HttpSchemes.HTTP_BUFFER);
		exchange.setMethod(request.getMethod());
		exchange.setURL(url);
		exchange.setVersion(request.getProtocol());

		// copy headers
		boolean xForwardedFor = false;
		boolean hasContent = false;
		long contentLength = -1;
		Enumeration<?> enm = request.getHeaderNames();
		while (enm.hasMoreElements()) {
			String hdr = (String) enm.nextElement();
			String lhdr = hdr.toLowerCase();

			if ("content-type".equals(lhdr))
				hasContent = true;
			else if ("content-length".equals(lhdr)) {
				contentLength = request.getContentLength();
				exchange.setRequestHeader(HttpHeaders.CONTENT_LENGTH, Long
						.toString(contentLength));
				if (contentLength > 0)
					hasContent = true;
			} else if ("x-forwarded-for".equals(lhdr))
				xForwardedFor = true;

			Enumeration<?> vals = request.getHeaders(hdr);
			while (vals.hasMoreElements()) {
				String val = (String) vals.nextElement();
				if (val != null) {
					exchange.setRequestHeader(hdr, val);
				}
			}
		}

		exchange.setRequestHeader("Via", "CCDImageController");
		if (!xForwardedFor)
			exchange.addRequestHeader("X-Forwarded-For", request
					.getRemoteAddr());

		if (hasContent)
			exchange.setRequestContentSource(in);

		_client.send(exchange);

		// have to wait here, then return null. Not sure what is the best to return.
		/*int exchangeState = */ exchange.waitForDone();

		return null;

	}

	public void setBeamlineSessionStateMap(StateMap beamlineSessionStateMap) {
		this.beamlineSessionStateMap = beamlineSessionStateMap;
	}

	public void setImageServiceURL(String imageServiceURL) {
		this.imageServiceURL = imageServiceURL;
	}

	public void setFileTemplate(String fileTemplate) {
		this.fileTemplate = fileTemplate;
	}

}
