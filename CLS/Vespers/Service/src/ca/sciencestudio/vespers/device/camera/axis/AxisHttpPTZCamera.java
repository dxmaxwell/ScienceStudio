/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 * 		AxisHttpPTZCamera class.
 *     
 */
package ca.sciencestudio.vespers.device.camera.axis;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.imageio.ImageIO;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author maxweld
 *
 */
public class AxisHttpPTZCamera {

	private static final String URL_GET_IMAGE = "%s/axis-cgi/jpg/image.cgi";
	private static final String URL_GET_VIDEO = "%s/axis-cgi/mjpg/video.cgi?resolution=4CIF";
	private static final String URL_SET_FOCUS = "%s/axis-cgi/com/ptz.cgi?focus=%d";
	private static final String URL_SET_ZOOM = "%s/axis-cgi/com/ptz.cgi?zoom=%d";
	private static final String URL_SET_IRIS = "%s/axis-cgi/com/ptz.cgi?iris=%d";
	private static final String URL_SET_CENTER_RELATIVE = "%s/axis-cgi/com/ptz.cgi?center=%d,%d";
	private static final String URL_SET_FOCUS_RELATIVE = "%s/axis-cgi/com/ptz.cgi?rfocus=%d";
	private static final String URL_SET_ZOOM_RELATIVE = "%s/axis-cgi/com/ptz.cgi?rzoom=%d";
	private static final String URL_SET_IRIS_RELATIVE = "%s/axis-cgi/com/ptz.cgi?riris=%d";
	
	protected Log log = LogFactory.getLog(getClass());
	
	private String name;
	private String baseUrl;
	
	public BufferedImage getImage() {
		String url = String.format(URL_GET_IMAGE, baseUrl);
		try {
			final URL imageUrl = new URL(url);
			return ImageIO.read(imageUrl);
		}
		catch(MalformedURLException e) {
			logMalformedURLException(url);
			return null;
		}
		catch(IOException e) {
			logMalformedURLException(url);
			return null;
		}
	}
	
	public URLConnection getVideo()
	{
		String url = String.format(URL_GET_VIDEO, baseUrl);
		try {
			URLConnection urlConnection = new URL(url).openConnection();
			urlConnection.connect();
			return urlConnection;
		}
		catch(MalformedURLException e) {
			logMalformedURLException(url);
			return null;
		}
		catch(IOException e) {
			logIOException(url);
			return null;
		}
	}

	public void setCenterRelative(int x, int y) {
		String url = String.format(URL_SET_CENTER_RELATIVE, baseUrl, x, y);
		try {
			URLConnection urlConnection = new URL(url).openConnection();
			urlConnection.connect();
			urlConnection.getContent();
		}
		catch(MalformedURLException e) {
			logMalformedURLException(url);
		}
		catch(IOException e) {
			logIOException(url);
		}
	}
	
	public void setFocus(int focus) {
		String url = String.format(URL_SET_FOCUS, baseUrl, focus);
		try {
			URLConnection urlConnection = new URL(url).openConnection();
			urlConnection.connect();
			urlConnection.getContent();
		}
		catch(MalformedURLException e) {
			logMalformedURLException(url);
		}
		catch(IOException e) {
			logIOException(url);
		}
	}
	
	public void setFocusRelative(int focus) {
		String url = String.format(URL_SET_FOCUS_RELATIVE, baseUrl, focus);
		try {
			URLConnection urlConnection = new URL(url).openConnection();
			urlConnection.connect();
			urlConnection.getContent();
		}
		catch(MalformedURLException e) {
			logMalformedURLException(url);
		}
		catch(IOException e) {
			logIOException(url);
		}
	}

	public void setZoom(int zoom) {
		String url = String.format(URL_SET_ZOOM, baseUrl, zoom);
		try {
			URLConnection urlConnection = new URL(url).openConnection();
			urlConnection.connect();
			urlConnection.getContent();
		}
		catch(MalformedURLException e) {
			logMalformedURLException(url);
		}
		catch(IOException e) {
			logIOException(url);
		}
	}
	
	public void setZoomRelative(int zoom) {
		String url = String.format(URL_SET_ZOOM_RELATIVE, baseUrl, zoom);
		try {
			URLConnection urlConnection = new URL(url).openConnection();
			urlConnection.connect();
			urlConnection.getContent();
		}
		catch(MalformedURLException e) {
			logMalformedURLException(url);
		}
		catch(IOException e) {
			logIOException(url);
		}
	}
	
	public void setIris(int iris) {
		String url = String.format(URL_SET_IRIS, baseUrl, iris);
		try {
			URLConnection urlConnection = new URL(url).openConnection();
			urlConnection.connect();
			urlConnection.getContent();
		}
		catch(MalformedURLException e) {
			logMalformedURLException(url);
		}
		catch(IOException e) {
			logIOException(url);
		}
	}
	
	public void setIrisRelative(int iris) {
		String url = String.format(URL_SET_IRIS_RELATIVE, baseUrl, iris);
		try {
			URLConnection urlConnection = new URL(url).openConnection();
			urlConnection.connect();
			urlConnection.getContent();
		}
		catch(MalformedURLException e) {
			logMalformedURLException(url);
		}
		catch(IOException e) {
			logIOException(url);
		}
	}
	
	protected void logMalformedURLException(String url) {
		log.warn("Specified URL, " + url + ", is invalid.");
	}
	
	protected void logIOException(String url) {
		log.warn("Failed to connect to: " + url);
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getBaseUrl() {
		return baseUrl;
	}
	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}
}
