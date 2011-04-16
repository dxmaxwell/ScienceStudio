/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 * 		SimulationSampleCamera class.
 *     
 */
package ca.sciencestudio.vespers.bcm.simulation.model;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import java.awt.Image;
import java.awt.image.Raster;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

import ca.sciencestudio.device.control.simulation.SimulationDevice;
import ca.sciencestudio.device.control.simulation.SimulationEvent;
import ca.sciencestudio.device.control.simulation.SimulationEventListener;
import ca.sciencestudio.device.control.simulation.AbstractSimulationDevice;

/**
 * @author maxweld
*
*/
public class SimulationSampleCamera extends AbstractSimulationDevice implements SimulationEventListener {
	
	public static final String VALUE_KEY_DATA = "data";
	public static final String VALUE_KEY_BPP = "bpp";
	public static final String VALUE_KEY_WIDTH = "width";
	public static final String VALUE_KEY_HEIGHT = "height";
	public static final String VALUE_KEY_POSITION = "position";
	
	private BufferedImage sampleImage;
	private double sampleImageScale = 1.0; // per pixel //
	private double fieldOfViewWidth = 100.0;
	private double fieldOfViewHeight = 100.0;
	
	private byte[] data;
	private int bpp = 3;
	private int width = 640;
	private int height = 480;
	private double zoom = 1.0;
	
	private SimulationDevice motorX;
	private SimulationDevice motorY;
	
	private double positionX = 0.0;
	private double positionY = 0.0;
	
	private double updatePositionX = Double.NaN;
	private double updatePositionY = Double.NaN;
	
	public SimulationSampleCamera(String id, File sampleImageFile, SimulationDevice motorX, SimulationDevice motorY) {
		super(id);
		this.motorX = motorX;
		this.motorY = motorY;
		initSampleImage(sampleImageFile);
		this.motorX.addEventListener(this);
		this.motorY.addEventListener(this);
		update();
	}
	
	protected void initSampleImage(File sampleImageFile) {
		try {
			sampleImage = ImageIO.read(sampleImageFile);
		}
		catch(IOException e) {
			log.info("Sample image cannot be read! <" + sampleImageFile.getAbsolutePath() + ">");
			sampleImage = new BufferedImage(0, 0, BufferedImage.TYPE_CUSTOM);
		}
	}
	
	public Object getValue() {
		Map<String, Object> value = new HashMap<String,Object>();
		value.put(VALUE_KEY_DATA, getData());
		value.put(VALUE_KEY_BPP, getBpp());
		value.put(VALUE_KEY_WIDTH, getWidth());
		value.put(VALUE_KEY_HEIGHT, getHeight());
		return value;
	}

	public void setValue(Object value) {
		// TODO Auto-generated method stub
	}
	
	public byte[] getData() {
		updateDataWithCenterCoordinates(positionX, positionY, false);
		return data;
	}
	
	public int getBpp() {
		return bpp;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
		update();
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
		update();
	}

	public double getZoom() {
		return zoom;
	}

	public void setZoom(double zoom) {
		this.zoom = zoom;
		update();
	}
	
	public double getSampleImageScale() {
		return sampleImageScale;
	}

	public void setSampleImageScale(double sampleImageScale) {
		this.sampleImageScale = sampleImageScale;
		update();
	}

	public double getFieldOfViewWidth() {
		return fieldOfViewWidth;
	}

	public void setFieldOfViewWidth(double fieldOfViewWidth) {
		this.fieldOfViewWidth = fieldOfViewWidth;
		update();
	}

	public double getFieldOfViewHeight() {
		return fieldOfViewHeight;
	}

	public void setFieldOfViewHeight(double fieldOfViewHeight) {
		this.fieldOfViewHeight = fieldOfViewHeight;
		update();
	}
	
	protected void update() {
		Map<String,Object> valueMapX = getMapFromObject(motorX.getValue());
		Map<String,Object> valueMapY = getMapFromObject(motorY.getValue());
		updateDataWithValueMaps(valueMapX, valueMapY, true);
	} 
	
	protected void updateDataWithValueMaps(Map<String,Object> valueMapX, Map<String,Object> valueMapY, boolean force) {
		try {
			if(valueMapX.containsKey(VALUE_KEY_POSITION) && valueMapY.containsKey(VALUE_KEY_POSITION)) {
				positionX = (Double)valueMapX.get(VALUE_KEY_POSITION);
				positionY = (Double)valueMapY.get(VALUE_KEY_POSITION);
				updateDataWithCenterCoordinates(positionX, positionY, force);
			}
			else {
				log.warn("Map does not contain key: " + VALUE_KEY_POSITION);
			}
		}
		catch(ClassCastException e) {
			log.warn("Value for key, " + VALUE_KEY_POSITION + ", is unexpected class.");
		}
	}
	
	protected void updateDataWithCenterCoordinates(double x, double y, boolean force) {
		
		if((!force) && (updatePositionX == x) && (updatePositionY == y)) {
			return;
		}
		
		updatePositionX = x;
		updatePositionY = y;
		
		BufferedImage subImage = getSubimageWithCenterCoordinates(x, y);
		Image scaledSubImage = subImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);

		BufferedImage bgrScaledSubImage = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
		bgrScaledSubImage.getGraphics().drawImage(scaledSubImage, 0, 0, null);
		
		Raster raster = bgrScaledSubImage.getData();
		DataBufferByte buffer = (DataBufferByte) raster.getDataBuffer();
		
		byte[] bgrData = buffer.getData(0);		
		byte[] rgbData = new byte[bgrData.length];
		
		// convert blue-green-red data to red-green-blue data //
		for(int idx=0; idx<bgrData.length; idx+=3) {
			rgbData[idx + 0] = bgrData[idx + 2]; // red   //
			rgbData[idx + 1] = bgrData[idx + 1]; // green //
			rgbData[idx + 2] = bgrData[idx + 0]; // blue  //
		}
		
		data = rgbData;
	}

	protected BufferedImage getSubimageWithCenterCoordinates(double centerX, double centerY) {
		
		int masterSampleImageWidthPixel = sampleImage.getWidth();
		int masterSampleImageHeightPixel = sampleImage.getHeight();
		
		double masterSampleImageWidth = masterSampleImageWidthPixel * sampleImageScale;
		double masterSampleImageHeight =  masterSampleImageHeightPixel * sampleImageScale;
		
		double fovWidth = fieldOfViewWidth / zoom;
		double fovHeight = fieldOfViewHeight / zoom;
		
		if(fovWidth > masterSampleImageWidth) { fovWidth = masterSampleImageWidth; }
		if(fovHeight > masterSampleImageHeight) { fovHeight = masterSampleImageHeight; }
		
		double topLeftX = centerX - (fovWidth / 2.0); 
		double topLeftY = centerY + (fovHeight / 2.0);
		
		int topLeftXPixel = (int)(((masterSampleImageWidth / 2.0) + topLeftX) / sampleImageScale);
		int topLeftYPixel = (int)(((masterSampleImageHeight / 2.0) - topLeftY) / sampleImageScale);
		
		int fovWidthPixel = (int)(fovWidth / sampleImageScale);
		int fovHeightPixel = (int)(fovHeight / sampleImageScale); 
		
		if(topLeftXPixel < 0) { topLeftXPixel = 0; }
		if(topLeftYPixel < 0) { topLeftYPixel = 0; }
		
		if((topLeftXPixel + fovWidthPixel) > masterSampleImageWidthPixel) {
			topLeftXPixel = masterSampleImageWidthPixel - fovWidthPixel;
		}
		if((topLeftYPixel + fovHeightPixel) > masterSampleImageHeightPixel) {
			topLeftYPixel = masterSampleImageHeightPixel - fovHeightPixel;
		}
				
		return sampleImage.getSubimage(topLeftXPixel, topLeftYPixel, fovWidthPixel, fovHeightPixel);
	}

	public void handleEvent(SimulationEvent event) {
		
		String deviceId = event.getDeviceId();
		
		if(motorX.getId().equals(deviceId)) {
			Map<String,Object> valueMapX = getMapFromObject(event.getValue());
			if(valueMapX.containsKey(VALUE_KEY_POSITION)) {
				positionX = (Double)valueMapX.get(VALUE_KEY_POSITION);
			}
		}
		else if(motorY.getId().equals(deviceId)) {
			Map<String,Object> valueMapY = getMapFromObject(event.getValue());
			if(valueMapY.containsKey(VALUE_KEY_POSITION)) {
				positionY = (Double)valueMapY.get(VALUE_KEY_POSITION);
			}
		}
	}	
}
