/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     ScanDataViewController class.
 *     
 */
package ca.sciencestudio.service.app.controllers;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author maxweld
 *
 */
@Controller
public class ScanDataViewController {
	
	private static final String URL_CHAR_ENCODING = "UTF-8";
	
	private String scanDataViewUrl;
	
	@RequestMapping(value = "/scan/data/view.html")
	public String viewScanData(@RequestParam("scan") String scanGid) {
		
		StringBuffer redirectUrl = new StringBuffer();
		redirectUrl.append("redirect:");
		redirectUrl.append(scanDataViewUrl);
		redirectUrl.append("?scan=");
		
		try {
			redirectUrl.append(URLEncoder.encode(scanGid, URL_CHAR_ENCODING));
		}
		catch(UnsupportedEncodingException e) {
			redirectUrl.append(scanGid);
		}
		
		return redirectUrl.toString();
	}

	public String getScanDataViewUrl() {
		return scanDataViewUrl;
	}
	public void setScanDataViewUrl(String scanDataViewUrl) {
		this.scanDataViewUrl = scanDataViewUrl;
	}
}
