/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 * 		GstreamerData class.
 *     
 */
package ca.sciencestudio.vespers.bcm.device.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import ca.sciencestudio.device.control.DeviceType;
import ca.sciencestudio.device.control.component.DeviceComponent;
import ca.sciencestudio.device.control.component.impl.DoubleArrayDevice;
import ca.sciencestudio.util.process.ExpandingProcessFactory;
import ca.sciencestudio.util.process.ProcessFactory;

/**
 * @author maxweld
 * 
 *
 */
public class GstreamerData extends DeviceComponent {
	
	private static final String ENV_VAR_LOCATION = "LOCATION"; 
	private static final String ENV_VAR_HEIGHT = "HEIGHT";
	private static final String ENV_VAR_WIDTH = "WIDTH";
	private static final String ENV_VAR_BPP = "BPP";
		
	private List<String> gstreamerCommandList;
	
	private DoubleArrayDevice height;
	private DoubleArrayDevice width;
	private DoubleArrayDevice bpp;
	
	private File location;
	
	public GstreamerData(String id) throws IOException {
		super(id);
		deviceType = DeviceType.Simple;
		location = File.createTempFile("GstreamerData", ".tmp");
		Runtime.getRuntime().addShutdownHook(new DeleteLocationHook());
	}
	
	@Override
	public synchronized Object getValue() {
		
		int height = (int) this.height.getRawValue()[0];
		int width = (int) this.width.getRawValue()[0];
		int bpp = (int) this.bpp.getRawValue()[0];
		
		byte[] data = new byte[height * width * bpp];
		
		Map<String,String> environment = new HashMap<String,String>();
		environment.put(ENV_VAR_LOCATION, location.getAbsolutePath());
		environment.put(ENV_VAR_HEIGHT, String.valueOf(height));
		environment.put(ENV_VAR_WIDTH, String.valueOf(width));
		environment.put(ENV_VAR_BPP, String.valueOf(bpp));
		
		ProcessFactory gstreamerProcessFactory = new ExpandingProcessFactory();
		if(gstreamerCommandList.size() > 1) {
			gstreamerProcessFactory.setCommand(gstreamerCommandList);
		} else {
			gstreamerProcessFactory.setCommand(gstreamerCommandList.get(0));
		}
		
		Process gstreamerProcess;
		try {
			gstreamerProcess = gstreamerProcessFactory.start(environment);
		}
		catch(IOException e) {
			log.warn("Gstreamer command (" + gstreamerProcessFactory + ") failed to start: " + e.getMessage());
			return data;
		}
		
		int gstreamerStatus;
		try {
			gstreamerStatus = gstreamerProcess.waitFor();
		}
		catch(InterruptedException e) {
			log.warn("Gstreamer command interrupted before completion: " + e.getMessage());
			return data;
		}
		
		if(gstreamerStatus != 0) {
			log.warn("Gstreamer command (" + gstreamerProcessFactory + ") exit with non-zero status (" + gstreamerStatus + ")");
			return data;
		}
		
		if(location.length() != data.length) {
			log.warn("Location (" + location.getAbsolutePath() + ") length not equal to data buffer length");
			return data;
		}
		
		InputStream sampleImageInputStream;
		try {
			sampleImageInputStream = new FileInputStream(location);
		}
		catch(IOException e) {
			log.warn("Location (" + location.getAbsolutePath() + ") open failure: " + e.getMessage());
			return data;
		}
		
		int sampleImageRead;
		try {
			sampleImageRead = sampleImageInputStream.read(data);
		}
		catch(IOException e) {
			log.warn("Location (" + location.getAbsolutePath() + ") read failure: " + e.getMessage());
			return data;
		}
		
		if(sampleImageRead != data.length) {
			log.warn("Location read incomplete (" + sampleImageRead + " of " + data.length + ")");
			return data;
		}
	
		return data;
	}
	
	public void setValue(Object value) {
		// ignore //
	}
	
	private class DeleteLocationHook extends Thread {

		public DeleteLocationHook() {
			setDaemon(true);
		}
		
		@Override
		public void run() {
			location.delete();
		}
	}
	
	public String getGstreamerCommand() {
		return StringUtils.join(gstreamerCommandList, " ");
	}
	public void setGstreamerCommand(String gstreamerCommand) {
		setGstreamerCommandList(Collections.singletonList(gstreamerCommand));
	}
	
	public List<String> getGstreamerCommandList() {
		return gstreamerCommandList;
	}
	public void setGstreamerCommandList(List<String> gstreamerCommandList) {
		this.gstreamerCommandList = gstreamerCommandList;
	}
	
	public DoubleArrayDevice getHeight() {
		return height;
	}
	public void setHeight(DoubleArrayDevice height) {
		this.height = height;
	}

	public DoubleArrayDevice getWidth() {
		return width;
	}
	public void setWidth(DoubleArrayDevice width) {
		this.width = width;
	}

	public DoubleArrayDevice getBpp() {
		return bpp;
	}
	public void setBpp(DoubleArrayDevice bpp) {
		this.bpp = bpp;
	}
}
