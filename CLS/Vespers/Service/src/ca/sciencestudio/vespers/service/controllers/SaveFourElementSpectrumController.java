/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     SaveFourElementSpectrumController class.
 *     
 */
package ca.sciencestudio.vespers.service.controllers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
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
public class SaveFourElementSpectrumController extends AbstractBeamlineAuthzController {
	
	private static final String ERROR_VIEW = "page/error";
	
	private static final String VALUE_KEY_SPECTRUM_ALL = "spectrumAll";
	private static final String VALUE_KEY_MAX_ENERGY_ALL = "maxEnergyAll";
	
	private static final String MODEL_KEY_SPECTRUM = "spectrum";
	private static final String MODEL_KEY_MAX_ENERGY = "maxEnergy";
	
	private static final String FORMAT_VALUE_CDFML = "cdfml";
	private static final String FORMAT_VALUE_PNG = "png";
	private static final String FORMAT_VALUE_TXT = "txt";
	
	private static final int[] DEFAULT_VALUE_SPECTRUM_ALL = new int[0];
	private static final double DEFAULT_VALUE_MAX_ENERGY_ALL = 1.0;
	
	private StateMap fourElementDetectorProxy;
	
	protected Log logger = LogFactory.getLog(getClass());
	
	@RequestMapping(value = "/detector/fed/save/spectrum.{format}", method = RequestMethod.GET)
	public String handleRequest(@PathVariable String format, HttpServletResponse response, ModelMap model) {
		
		if(!canReadBeamline()) {
			model.put("error", "Not permitted to save detector data.");
			return ERROR_VIEW;
		}
		
		Object value = fourElementDetectorProxy.get(VALUE_KEY_SPECTRUM_ALL);
		int[] spectrumAll;
		if(value instanceof int[]) {
			spectrumAll = (int[]) value;
		} else {
			spectrumAll = DEFAULT_VALUE_SPECTRUM_ALL;
		}
		
		value = fourElementDetectorProxy.get(VALUE_KEY_MAX_ENERGY_ALL);
		double maxEnergyAll;
		if(value instanceof Double) {
			maxEnergyAll = (Double) value;
		} else {
			maxEnergyAll = DEFAULT_VALUE_MAX_ENERGY_ALL;
		}

		String responseView = "spectrum-" + format;
		
		if(FORMAT_VALUE_CDFML.equals(format)) {
			response.setHeader("Content-disposition","attachment; filename=spectrum.cdfml");
			model.put(MODEL_KEY_SPECTRUM, spectrumAll);
			model.put(MODEL_KEY_MAX_ENERGY, maxEnergyAll);
			model.put("now", new Date());
			return responseView;
		}
		
		if(FORMAT_VALUE_PNG.equals(format)) {
			response.setContentType("image/png");
			response.setHeader("Content-disposition","attachment; filename=spectrum.png");
			
			XYPlotter plotter = new XYPlotter(spectrumAll, maxEnergyAll, "Energy", "Count");
			
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
			model.put(MODEL_KEY_SPECTRUM, spectrumAll);
			model.put(MODEL_KEY_MAX_ENERGY, maxEnergyAll);
			return responseView;
		}

		model.put("error", "Spectrum format '" + format + "' not supported.");
		return ERROR_VIEW;
	}

	public StateMap getFourElementDetectorProxy() {
		return fourElementDetectorProxy;
	}
	public void setFourElementDetectorProxy(StateMap fourElementDetectorProxy) {
		this.fourElementDetectorProxy = fourElementDetectorProxy;
	}
}
