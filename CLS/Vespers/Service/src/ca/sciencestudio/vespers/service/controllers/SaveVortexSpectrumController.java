/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     SaveVortexSpectrumController class.
 *     
 */
package ca.sciencestudio.vespers.service.controllers;

import java.io.IOException;
import java.io.ByteArrayOutputStream;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import ca.sciencestudio.util.plotting.XYPlotter;
import ca.sciencestudio.util.state.StateMap;

/**
 * @author maxweld
 *
 */
@Controller
public class SaveVortexSpectrumController extends AbstractBeamlineAuthzController {
	
	private static final String ERROR_VIEW = "page/error";
	
	private static final String VALUE_KEY_SPECTRUM = "spectrum";
	private static final String VALUE_KEY_MAX_ENERGY = "maxEnergy";
	
	private static final String MODEL_KEY_SPECTRUM = "spectrum";
	private static final String MODEL_KEY_MAX_ENERGY = "maxEnergy";
	
	private static final String FORMAT_VALUE_CDFML = "cdfml";
	private static final String FORMAT_VALUE_PNG = "png";
	private static final String FORMAT_VALUE_TXT = "txt";
	
	private static final int[] DEFAULT_VALUE_SPECTRUM = new int[0];
	private static final double DEFAULT_VALUE_MAX_ENERGY = 1.0;
	
	private StateMap vortexDetectorProxy;
	
	protected Log logger = LogFactory.getLog(getClass());
	
	@RequestMapping(value = "/detector/vtx/save/spectrum.{format}", method = RequestMethod.GET)
	public String handleRequest(@PathVariable String format, HttpServletResponse response, ModelMap model) {
		
		if(!canReadBeamline()) {
			model.put("error", "Not permitted to save detector data.");
			return ERROR_VIEW;
		}
		
		Object value = vortexDetectorProxy.get(VALUE_KEY_SPECTRUM);
		int[] spectrum;
		if(value instanceof int[]) {
			spectrum = (int[]) value;
		} else {
			spectrum = DEFAULT_VALUE_SPECTRUM;
		}
		
		value = vortexDetectorProxy.get(VALUE_KEY_MAX_ENERGY);
		double maxEnergy;
		if(value instanceof Number) {
			maxEnergy = ((Number)value).doubleValue();
		} else {
			maxEnergy = DEFAULT_VALUE_MAX_ENERGY;
		}
		
		String responseView = "spectrum-" + format;
		
		if(FORMAT_VALUE_CDFML.equals(format)) {
			response.setHeader("Content-disposition","attachment; filename=spectrum.cdfml");
			model.put(MODEL_KEY_SPECTRUM, spectrum);
			model.put(MODEL_KEY_MAX_ENERGY, maxEnergy);
			model.put("now", new Date());
			return responseView;
		}
		
		if(FORMAT_VALUE_PNG.equals(format)) {
			response.setContentType("image/png");
			response.setHeader("Content-disposition","attachment; filename=spectrum.png");
			
			XYPlotter plotter = new XYPlotter(spectrum, maxEnergy, "Energy", "Count");
			
			try {
				ByteArrayOutputStream buf = new ByteArrayOutputStream();
				ImageIO.write(plotter.getImage(), "png", buf);
				byte[] body = buf.toByteArray();
				response.setContentLength(body.length);
				response.getOutputStream().write(body);
			}
			catch(IOException e) {
				logger.warn("Exception while writing PNG to response stream.", e);
			}
			
			return null;
		}
		
		if(FORMAT_VALUE_TXT.equals(format)) {
			response.setHeader("Content-disposition","attachment; filename=spectrum.txt");
			model.put(MODEL_KEY_SPECTRUM, spectrum);
			model.put(MODEL_KEY_MAX_ENERGY, maxEnergy);
			return responseView;
		}
		
		model.put("error", "Spectrum format '" + format + "' not supported.");
		return ERROR_VIEW;
	}

	public StateMap getVortexDetectorProxy() {
		return vortexDetectorProxy;
	}
	public void setVortexDetectorProxy(StateMap vortexDetectorProxy) {
		this.vortexDetectorProxy = vortexDetectorProxy;
	}
}
