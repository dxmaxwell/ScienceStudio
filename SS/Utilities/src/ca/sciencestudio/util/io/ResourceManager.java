/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     ResourceManager class.
 *     
 */
package ca.sciencestudio.util.io;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.springframework.core.io.Resource;

/**
 * @author maxweld
 *
 */
public class ResourceManager {
	
	private Resource baseResource;
	
	public ResourceManager() {
		baseResource = null;
	}
	
	public ResourceManager(Resource baseResource) throws IOException {
		setBaseResource(baseResource);
	}
	
	public Resource createRelativeResource(List<String> names) throws IOException {
		Resource rootResource = baseResource;
		for(String name : names) {
			rootResource = createRelativeResource(rootResource, name);
		}
		return rootResource;
	}
		
	public Resource createRelativeResource(String name) throws IOException {
		return createRelativeResource(baseResource, name);
	}
	
	protected Resource createRelativeResource(Resource rootResource, String name) throws IOException {
		try {
			Resource newResource = rootResource.createRelative(name);
			
			File newFile = newResource.getFile();
			if(newFile.exists()) {
				if(!newFile.isDirectory()) {
					throw new IOException("Resource exists and is not a directory.");
				}
			}
			else if(!newFile.mkdirs()) {
				throw new IOException("Failed to create relative directory resource.");
			}
			
			return newResource;
		}
		catch(IOException e) {
			throw new IOException("IOExeption while creating relative resource.", e);
		}
	}

	public Resource getBaseResource() {
		return baseResource;
	}

	public void setBaseResource(Resource baseResource) throws IOException {
		try {
			if(baseResource.getFile().isDirectory()) {
				this.baseResource = baseResource;
			} else {
				throw new IOException("Base resource is not a directory.");
			}
		}
		catch(IOException e) {
			throw new IOException("Base resource is not a file.", e);
		}
	}
}
