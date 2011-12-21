/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *	Description:
 *		CCDFocusImageController class.
 *
 */
package ca.sciencestudio.vespers.service.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.client.ContentExchange;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.HttpExchange;
import org.eclipse.jetty.http.HttpHeaders;
import org.eclipse.jetty.http.HttpSchemes;
import org.eclipse.jetty.http.HttpStatus;
import org.eclipse.jetty.io.Buffer;
import org.eclipse.jetty.util.URIUtil;
import org.eclipse.jetty.util.log.Log;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ca.sciencestudio.util.web.FormResponseMap;

/**
 * The controller to get CCD focus image. This controller acts as a proxy to get
 * the image from a service binded to inside organisation domain name. The image
 * name is from the PV "IOC1607-003:det1:FullFileName_RBV". That is
 * ccdFile/fullfilenameRBV in the heartbeat XML.
 * 
 * This controller is developed based on jetty client, and its structure
 * imitates the ProxyServlet in Jetty. The reason to use Jetty client other than
 * Apache HTTPClient is the later does not provide explicit interface for
 * response handling as the former does.
 * 
 * Continuation is not supported by Tomcat and Spring. Since the handleRequest
 * has to return a ModelAndView object, the client needs to work in a sync way.
 * 
 * @author Dong Liu
 * 
 */

@Controller
public class CCDFocusImageController extends AbstractBeamlineAuthzController {

	private static final String PARAM_TYPE = "type";
	private static final String PARAM_FILE = "file";
	private static final String PARAM_DOWN = "download";

	private String imageServiceURL;

	private HttpClient _client;

	@PostConstruct
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

	@PreDestroy
	public void destroy() throws Exception {
		_client.stop();
	}

	@ResponseBody
	@RequestMapping(value = "/ccdFocusImage", method = RequestMethod.GET)
	public FormResponseMap handleRequest(@RequestParam(PARAM_TYPE) String typeParam, @RequestParam(PARAM_FILE) String fileParam, @RequestParam(value = PARAM_DOWN, required = false) String downloadParam, HttpServletRequest request,
			final HttpServletResponse response) throws Exception {

		if (!canReadBeamline()) {
			response.setStatus(HttpStatus.UNAUTHORIZED_401);
			return new FormResponseMap(false, "Not permitted to setup CCD.");
		}

		if (downloadParam != null) {
			response.addHeader("Content-Disposition", "attachment; filename=" + fileParam + "." + typeParam);
		}

		final InputStream in = request.getInputStream();
		final OutputStream out = response.getOutputStream();
		String url = URIUtil.addPaths(imageServiceURL, fileParam + "." + typeParam);

		// use a content exchange here
		ContentExchange getImage = new ContentExchange(false) {
			protected void onResponseHeader(Buffer name, Buffer value) throws IOException {
				// there might be some headers not wanted
				response.addHeader(name.toString(), value.toString());
				super.onResponseHeader(name, value);

			}

		};
		getImage.setScheme(HttpSchemes.HTTP_BUFFER);
		getImage.setMethod(request.getMethod());
		getImage.setURL(url);
		getImage.setVersion(request.getProtocol());

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
				getImage.setRequestHeader(HttpHeaders.CONTENT_LENGTH, Long.toString(contentLength));
				if (contentLength > 0)
					hasContent = true;
			} else if ("x-forwarded-for".equals(lhdr))
				xForwardedFor = true;

			Enumeration<?> vals = request.getHeaders(hdr);
			while (vals.hasMoreElements()) {
				String val = (String) vals.nextElement();
				if (val != null) {
					getImage.setRequestHeader(hdr, val);
				}
			}
		}
		getImage.setRequestHeader("Via", "CCDFocusImageController");
		if (!xForwardedFor)
			getImage.addRequestHeader("X-Forwarded-For", request.getRemoteAddr());

		if (hasContent)
			getImage.setRequestContentSource(in);

		_client.send(getImage);

		int exchangeStatus = getImage.waitForDone();
		int responseStatus = getImage.getResponseStatus();

		if (exchangeStatus != HttpExchange.STATUS_COMPLETED) {
			Log.warn(getImage.toString() + " cannot complete with a status " + exchangeStatus);
			response.setStatus(HttpStatus.BAD_GATEWAY_502);
			out.close();
			return new FormResponseMap(false, "Proxy failed.");
		}

		if (responseStatus != HttpStatus.OK_200) {
			Log.warn(getImage.toString() + " status code " + responseStatus);
		}

		response.setStatus(responseStatus);

		out.write(getImage.getResponseContentBytes());

		return null;

	}

	public void setImageServiceURL(String imageServiceURL) {
		this.imageServiceURL = imageServiceURL;
	}

}
