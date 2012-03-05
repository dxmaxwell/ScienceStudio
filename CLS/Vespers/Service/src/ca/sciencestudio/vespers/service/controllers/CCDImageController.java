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
 * The controller to get CCD image. This controller acts as a proxy to get
 * the image from an inner service.
 *
 * @author Dong Liu
 *
 */

@Controller
public class CCDImageController extends AbstractBeamlineAuthzController{

	private static final String PARAM_TYPE = "type";
	private static final String PARAM_FILE = "file";
	private static final String PARAM_NUMBER = "number";
	private static final String PARAM_DOWN = "download";
	private static final String VALUE_KEY_SCAN_ID = "scanId";

	private String imageServiceURL;
	private String fileTemplate;



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
	@RequestMapping(value = "/ccdImage", method = RequestMethod.GET)
	public FormResponseMap handleRequest(
			@RequestParam(PARAM_TYPE) String typeParam,
			@RequestParam(PARAM_FILE) String fileParam,
			@RequestParam(PARAM_NUMBER) String numberParam,
			@RequestParam(value=PARAM_DOWN, required=false) String downloadParam,
			HttpServletRequest request,
			final HttpServletResponse response) throws IOException, InterruptedException {

		if (!canReadBeamline()) {
			response.setStatus(HttpStatus.UNAUTHORIZED_401);
			return new FormResponseMap(false, "Not permitted to setup CCD.");
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

		Integer scanId = (Integer) beamlineSessionProxy.get(VALUE_KEY_SCAN_ID);

		String url = URIUtil.addPaths(imageServiceURL, "scan"+ scanId + URIUtil.SLASH + fileName + "." + typeParam);

		// use a content exchange here
		ContentExchange getImage = new ContentExchange(false){
			protected void onResponseHeader(Buffer name, Buffer value)
			throws IOException {
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
				getImage.setRequestHeader(HttpHeaders.CONTENT_LENGTH, Long
						.toString(contentLength));
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
			getImage.addRequestHeader("X-Forwarded-For", request
					.getRemoteAddr());

		if (hasContent)
			getImage.setRequestContentSource(in);

		_client.send(getImage);

		int exchangeStatus = getImage.waitForDone();
        int responseStatus = getImage.getResponseStatus();

        if (exchangeStatus != HttpExchange.STATUS_COMPLETED) {
            Log.warn(getImage.toString() + " cannot complete with a status " + exchangeStatus);
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR_500);
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

	public void setFileTemplate(String fileTemplate) {
		this.fileTemplate = fileTemplate;
	}

}
