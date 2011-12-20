/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     JsonProperties class.
 *     
 */
package ca.sciencestudio.util.json;

import java.io.IOException;
import java.io.InputStreamReader;

import org.eclipse.jetty.util.ajax.JSON;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.resource.Resource;

public class JsonProperties { 
	
	public static Object loadMap(String fileName) throws IOException {
		if (fileName == null)
			return null;
		Resource json = Resource.newClassPathResource(fileName);
		
		if (Log.isDebugEnabled()) 
			Log.debug("Load properties from " + fileName);
		
		InputStreamReader jsonReader = new InputStreamReader(json.getInputStream());
		
		return JSON.parse(jsonReader , true); 
		
	}

}
