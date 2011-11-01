/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     ScanFormBacker class.
 *     
 */
package ca.sciencestudio.service.session.backers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Map;

import ca.sciencestudio.model.session.Scan;
import ca.sciencestudio.util.Parameters;
import ca.sciencestudio.util.rest.ValidationError;
import ca.sciencestudio.util.rest.ValidationResult;

/**
 * @author maxweld
 *
 */
public class ScanFormBacker extends Scan {

	private static final long serialVersionUID = 1L;
	
	private static final String DEFAULT_PARAM_VALUE = "";
	
	private static final String PARAMETERS_FIELD = "parameters";
	
	private static final String PARAMS_TEXT_FIELD = "paramsText";
	
	public static ValidationResult transformResult(ValidationResult result) {
		Map<String,ValidationError> fieldErrors = result.getFieldErrors();
		if(fieldErrors.containsKey(PARAMETERS_FIELD)) {
			fieldErrors.put(PARAMS_TEXT_FIELD, fieldErrors.get(PARAMETERS_FIELD));
			fieldErrors.remove(PARAMETERS_FIELD);
		}
		return result;
	}
	
	public ScanFormBacker() {
		super();
	}
	
	public ScanFormBacker(Scan scan) {
		super(scan);
	}
	
	public String getParamsText() {
		StringBuffer buffer = new StringBuffer();
		Parameters params = getParameters();
		if(params != null) {
			for(Map.Entry<String,String> param : params.entrySet()) {
				buffer.append(param.getKey()).append(" = ").append(param.getValue()).append("\n");
			}
		}
		return buffer.toString();
	}

	public void setParamsText(String paramsText) {
		Parameters params = new Parameters();
		try {
			BufferedReader r = new BufferedReader(new StringReader(paramsText));
			for(String line = r.readLine(); line != null; line = r.readLine()) {
				if(line.length() > 0) {
					String[] kv = line.split("=", 2);
					if(kv.length > 0) {
						if((kv.length == 1) || (kv[2].length() == 0)) {
							params.put(kv[0], DEFAULT_PARAM_VALUE);
						} else {
							params.put(kv[0], kv[1]);
						}
					}
				}
			}
		}
		catch(IOException e) {
			return;
		}
		setParameters(params);
	}
}
