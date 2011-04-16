/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 * 		SampleCamera class.
 * 		Implementation based on FirewireCamera provided by BCM, but with no dependence on SWT.
 *     
 */
package ca.sciencestudio.vespers.bcm.device.model;



import java.util.Timer;
import java.util.TimerTask;
import java.io.IOException;
import java.io.Serializable;
import java.io.ByteArrayOutputStream;
import java.util.Map;
import java.util.HashMap;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

import javax.imageio.ImageIO;

import ca.sciencestudio.device.control.component.DeviceComponent;
import ca.sciencestudio.device.control.composite.DeviceComposite;
import ca.sciencestudio.device.control.DeviceType;
import ca.sciencestudio.device.control.event.DeviceEvent;
import ca.sciencestudio.device.control.event.DeviceEventListener;
import ca.sciencestudio.device.control.event.DeviceEventType;

import ca.sciencestudio.vespers.bcm.device.model.SimpleStageHV;
import ca.sciencestudio.vespers.bcm.daq.AbstractScanDevice;
import ca.sciencestudio.vespers.bcm.daq.AbstractScanDevice.ScanDeviceState;

/**
 * @author maxweld
 *
 */
public class SampleCamera extends DeviceComposite<DeviceComponent> implements DeviceEventListener {

	protected static final long PUBLISH_VALUE_TASK_DELAY_MILLISECONDS = 1000L;
	protected static final String SAMPLE_CAMERA_IMAGE_FORMAT = "png";
	protected static final double DEFAULT_SAMPLE_CAMERA_IMAGE_SCALE = 0.4;
	
	protected static final String COMPONENT_KEY_DATA = "data";
	protected static final String COMPONENT_KEY_BPP = "bpp";
	protected static final String COMPONENT_KEY_WIDTH = "width";
	protected static final String COMPONENT_KEY_HEIGHT = "height";
	
	protected static final String VALUE_KEY_IMAGE = "image";
	protected static final String VALUE_KEY_BPP = "bpp";
	protected static final String VALUE_KEY_WIDTH = "width";
	protected static final String VALUE_KEY_HEIGHT = "height";
	protected static final String VALUE_KEY_SCALE = "scale";
	protected static final String VALUE_KEY_POSITION_H = "positionH";
	protected static final String VALUE_KEY_POSITION_V = "positionV";
	
	private Timer publishValueTimer = new Timer(true);
	private PublishValueTask publishValueTask = new PublishValueTask();
	
	private SimpleStageHV sampleStage;
	private AbstractScanDevice scanDevice;
	
	private double scale = DEFAULT_SAMPLE_CAMERA_IMAGE_SCALE;
	
	public SampleCamera(String id, Map<String,DeviceComponent> components, SimpleStageHV sampleStage, AbstractScanDevice scanDevice) {
		super(id, components);
		deviceType = DeviceType.Camera;
		this.sampleStage = sampleStage;
		this.scanDevice = scanDevice;
		sampleStage.addEventListener(this);
	}
	
	@Override
	public Object getValue() {
		Map<String,Object> value = new HashMap<String,Object>();
		value.put(VALUE_KEY_IMAGE, getImage());
		value.put(VALUE_KEY_BPP, getBytesPerPixel());
		value.put(VALUE_KEY_HEIGHT, getHeight());
		value.put(VALUE_KEY_WIDTH, getWidth());
		value.put(VALUE_KEY_SCALE, getScale());
		value.put(VALUE_KEY_POSITION_H, getPositionH());
		value.put(VALUE_KEY_POSITION_V, getPositionV());
		return value;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public void setValue(Object value) {
		try {
			Map<String,Object> valueMap = (Map<String,Object>) value;
			Map<String,Object> eventMap = new HashMap<String,Object>();
			if(valueMap.containsKey(VALUE_KEY_SCALE)) {
				try {
					setScale((Double)valueMap.get(VALUE_KEY_SCALE));
					eventMap.put(VALUE_KEY_SCALE, getScale());
				}
				catch(ClassCastException e) {
					log.warn("Set value of " + VALUE_KEY_SCALE + " wrong class, expecting Double.", e);
				}
			}
			
			if(!eventMap.isEmpty()) {
				publishEvent(new DeviceEvent(DeviceEventType.VALUE_CHANGE, id, (Serializable) eventMap, status, alarm));
			}
		}
		catch(ClassCastException e) {
			log.warn("Set value wrong class, expecting Map<String,Object>.");
		}
	}
	
	public Object getImage() {
		DeviceComponent deviceComponent = components.get(COMPONENT_KEY_DATA);
		Object value = deviceComponent.getValue();
		return getImage(value);
	}
	
	public int getBytesPerPixel() {
		DeviceComponent deviceComponent = components.get(COMPONENT_KEY_BPP);
		Object value = deviceComponent.getValue();
		return getBytesPerPixel(value);
	}

	public int getWidth() {
		DeviceComponent deviceComponent = components.get(COMPONENT_KEY_WIDTH);
		Object value = deviceComponent.getValue();
		return getWidth(value);
	}
	
	public int getHeight() {
		DeviceComponent deviceComponent = components.get(COMPONENT_KEY_HEIGHT);
		Object value = deviceComponent.getValue();
		return getHeight(value);
	}
	
	public double getScale() {
		return scale;
	}
	
	public void setScale(double scale) {
		this.scale = scale;
	}
	
	public double getPositionH() {
		return sampleStage.getPositionH();
	}
	
	public double getPositionV() {
		return sampleStage.getPositionV();
	}
	
	protected byte[] getImage(Object value) {
		try {
			switch (getBytesPerPixel()) {
				case 3:
					return convert3ByteRGBtoImageFormat(getWidth(), getHeight(), (byte[])value);
					
				default:
					log.warn("Unsupported raw image format. Invalid value for Bytes-Per-Pixel.");
					return new byte[0];
			}
		}
		catch(ClassCastException e) {
			log.warn("Value for '" + VALUE_KEY_IMAGE + "', is not expected class (byte[]).", e);
			return new byte[0];
		}
		catch(NullPointerException e) {
			log.warn("Value for '" + VALUE_KEY_IMAGE + "', is null (Disconnected EpicsDevice?).", e);
			return new byte[0];
		}
	}
	
	protected int getBytesPerPixel(Object value) {
		try {
			double[] bpp = (double[])value;
			return (int) bpp[0];
		}
		catch(ClassCastException e) {
			log.warn("Value for '" + VALUE_KEY_BPP + "', is not expected class (double[]).", e);
			return 0;
		}
		catch(NullPointerException e) {
			log.warn("Value for '" + VALUE_KEY_BPP + "', is null (Disconnected EpicsDevice?).", e);
			return 0;
		}
	}
	
	protected int getWidth(Object value) {
		try {
			double[] width = (double[]) value;
			return (int) width[0];
		}
		catch(ClassCastException e) {
			log.warn("Value for '" + VALUE_KEY_WIDTH + "', is not expected class (double[]).", e);
			return 0;
		}
		catch(NullPointerException e) {
			log.warn("Value for '" + VALUE_KEY_WIDTH + "', is null (Disconnected EpicsDevice?).", e);
			return 0;
		}
	}
	
	protected int getHeight(Object value) {
		try {
			double[] height = (double[]) value;
			return (int) height[0];
		}
		catch(ClassCastException e) {
			log.warn("Value for '" + VALUE_KEY_HEIGHT + "', is not expected class (double[]).", e);
			return 0;
		}
		catch(NullPointerException e) {
			log.warn("Value for '" + VALUE_KEY_HEIGHT + "', is null (Disconnected EpicsDevice?).", e);
			return 0;
		}
	}
	
	protected byte[] convert3ByteRGBtoImageFormat(int width, int height, byte[] threeByteRGB) {
		
		int size = width * height;
		int nBytes = size * 3;
		
		if(threeByteRGB.length != nBytes) {
			log.warn("Cannot convert 3 Byte RGB raw image. Size does not match array length.");
			return new byte[0];
		}
		
		BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		WritableRaster wraster = bufferedImage.getRaster();
		int[] pixel = new int[3];
		
		for(int n=0; n<size; n++) {
			int idx = n * 3;
			pixel[0] = threeByteRGB[idx];
			pixel[1] = threeByteRGB[idx+1];
			pixel[2] = threeByteRGB[idx+2];
			wraster.setPixel(n % width, n / width, pixel);
		}
		
		ByteArrayOutputStream image = new ByteArrayOutputStream(nBytes);
		
		try {
			ImageIO.write(bufferedImage, SAMPLE_CAMERA_IMAGE_FORMAT, image);
		} catch (IOException e) {
			log.warn("Cannot convert 3 Byte RGB raw image. Failed to write image in " + SAMPLE_CAMERA_IMAGE_FORMAT + " format.", e);
			return new byte[0];
		}
		
		return image.toByteArray();
	}
	
//	protected byte[] convertToImage(int width, int height, int bytesPerPixel, byte[] rawBytes) {
//		//int bytesPerPixel = getBytesPerPixel();
//		//int width = getWidth();
//		//int height = getHeight();
//		int[] data = new int[rawBytes.length / bytesPerPixel];
//		int index = 0;
//		BufferedImage buf = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
//		ByteArrayOutputStream baos = new ByteArrayOutputStream();
//		
//		for(int i = 0; i < rawBytes.length; i += bytesPerPixel) {
//			byte b = rawBytes[i];
//			int red = (b & 0xFE) | (b & 0xFF);
//			b = rawBytes[i + 1];
//			int green = (b & 0xFE) | (b & 0xFF);
//			b = rawBytes[i + 2];
//			int blue = (b & 0xFE) | (b & 0xFF);
//			data[index++] = (red << 16) | (green << 8) | blue;
//		}
//		buf.setRGB(0, 0, width, height, data, 0, width);
//		try {
//			ImageIO.write(buf, SAMPLE_CAMERA_IMAGE_FORMAT, baos);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return baos.toByteArray();
//	}
	
	protected String getComponentKeyByDeviceId(String deviceId) {
		for(Map.Entry<String,DeviceComponent> entry : components.entrySet()) {
			if(entry.getValue().getId().equals(deviceId)) {
				return entry.getKey();
			}	
		}
		return new String();
	}
	
	@SuppressWarnings("unchecked")
	public void handleEvent(DeviceEvent event) {
		
		String deviceId = event.getDeviceId();
		
		DeviceEventType deviceEventType = event.getDeviceEventType();
		
		switch(deviceEventType) {
		
			case VALUE_CHANGE:
				
				HashMap<String,Object> newValueMap = new HashMap<String,Object>();
				
				if(sampleStage.getId().equals(deviceId)) {
					try {						
						Map<String,Object> valueMap = (Map<String,Object>) event.getValue();
						if(valueMap.containsKey(SimpleStageHV.VALUE_KEY_MOVING)) {
							try {
								Boolean moving = (Boolean) valueMap.get(SimpleStageHV.VALUE_KEY_MOVING);
								if(moving) {
									publishValueTask.cancel();
								}
								else if(scanDevice.getScanState() == ScanDeviceState.STOPPED ||
											scanDevice.getScanState() == ScanDeviceState.COMPLETE) {
										
									publishValueTask.cancel();
									publishValueTask = new PublishValueTask();
									publishValueTimer.schedule(publishValueTask, 1000L);
								}
							}
							catch(ClassCastException e) {
								log.warn("Value for '" + SimpleStageHV.VALUE_KEY_MOVING + "', is not expected class (Boolean).");
							}
						}
					}
					catch(ClassCastException e) {
						log.warn("Device event value is not expected class (Map<String,Object>).");
					}	
				}
				
				if(!newValueMap.isEmpty()) {
					publishEvent(new DeviceEvent(DeviceEventType.VALUE_CHANGE, id, newValueMap, status, alarm));
				}
				break;
				
			case ALARM_CHANGE:
			case CONNECTIVITY_CHANGE:
				break;
				
			default:
				log.warn("DeviceEventType, " + deviceEventType.toString() + ", not supported.");
				break;
		}
		
	}
		
	private class PublishValueTask extends TimerTask {
		
		@Override
		public void run() {
			publishValue();
		}
	}
}
