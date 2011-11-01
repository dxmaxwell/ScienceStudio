/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 * 		AreaScanDevice class.
 *     
 */
package ca.sciencestudio.vespers.bcm.daq;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;

import org.springframework.core.io.Resource;

import ca.sciencestudio.device.control.event.DeviceEvent;
import ca.sciencestudio.device.control.event.DeviceEventType;
import ca.sciencestudio.util.io.ResourceManager;
import ca.sciencestudio.util.Parameters;

/**
 * @author maxweld
 *
 */
public class AreaScanDevice extends AbstractAcqLibCmdLineScanDevice {

	public static final String VALUE_KEY_START = "start";
	public static final String VALUE_KEY_STOP = "stop";
	public static final String VALUE_KEY_PAUSE = "pause";
	public static final String VALUE_KEY_COUNT_TOTAL = "scanCountTotal";
	public static final String VALUE_KEY_START_POSITION_X = "startPositionX";
	public static final String VALUE_KEY_START_POSITION_Y = "startPositionY";
	public static final String VALUE_KEY_END_POSITION_X = "endPositionX";
	public static final String VALUE_KEY_END_POSITION_Y = "endPositionY";
	public static final String VALUE_KEY_NUMBER_POINTS_X = "nPointsX";
	public static final String VALUE_KEY_NUMBER_POINTS_Y = "nPointsY";
	public static final String VALUE_KEY_STEP_SIZE_X = "stepSizeX";
	public static final String VALUE_KEY_STEP_SIZE_Y = "stepSizeY";
	public static final String VALUE_KEY_SCAN_ID = "scanId";
	public static final String VALUE_KEY_SCAN_DETAILS = "scanDetails";
	public static final String VALUE_KEY_SAMPLE_IMAGE = "sampleImage";
	public static final String VALUE_KEY_SAMPLE_IMAGE_SCALE = "sampleImageScale";
	public static final String VALUE_KEY_SAMPLE_IMAGE_POSITION_X = "sampleImagePositionX";
	public static final String VALUE_KEY_SAMPLE_IMAGE_POSITION_Y = "sampleImagePositionY";
	
	public static final String SCAN_DETAILS_KEY_PARAMETERS = "parameters";
	public static final String SCAN_DETAILS_KEY_START_DATE = "startDate";
	public static final String SCAN_DETAILS_KEY_END_DATE = "endDate";
	public static final String SCAN_DETAILS_KEY_DATA_URL = "dataUrl";
	
	public static final String SCAN_PARAM_COMMENT = "";
	public static final String SCAN_PARAM_DATA_FILE_BASE = "dataFileBase";
	public static final String SCAN_PARAM_DATA_FILE_VERSION = "dafFileVersion";
	public static final String SCAN_PARAM_START_POSITION_X = "startPositionX";
	public static final String SCAN_PARAM_START_POSITION_Y = "startPositionY";
	public static final String SCAN_PARAM_END_POSITION_X = "endPositionX";
	public static final String SCAN_PARAM_END_POSITION_Y = "endPositionY";
	public static final String SCAN_PARAM_NUMBER_POINTS_X = "nPointsX";
	public static final String SCAN_PARAM_NUMBER_POINTS_Y = "nPointsY";
	public static final String SCAN_PARAM_STEP_SIZE_X = "stepSizeX";
	public static final String SCAN_PARAM_STEP_SIZE_Y = "stepSizeY";
	public static final String SCAN_PARAM_SAMPLE_IMAGE_FILE = "sampleImageFile";
	public static final String SCAN_PARAM_SAMPLE_IMAGE_SCALE = "sampleImageScale";
	public static final String SCAN_PARAM_SAMPLE_IMAGE_POSITION_X = "sampleImagePosX";
	public static final String SCAN_PARAM_SAMPLE_IMAGE_POSITION_Y = "sampleImagePosY";
	
	public static final String DEFAULT_SCAN_NAME_X = "scanX";
	public static final String DEFAULT_SCAN_NAME_Y = "scanY";
	
	public static final String DEFAULT_SAMPLE_IMAGE_NAME = "sample.png";
	public static final String DEFAULT_SAMPLE_IMAGE_FORMAT = "png";
	
	private ScanOptions scanOptionsX;
	private ScanOptions scanOptionsY;
	
	private byte[] sampleImage = null;
	private double sampleImageScale = 1.0;
	private double sampleImagePositionX = 0.0;
	private double sampleImagePositionY = 0.0;
	private String sampleImageName = DEFAULT_SAMPLE_IMAGE_NAME;
	
	private int scanId = 0;
	private Map<String,Serializable> scanDetails = null;
	
	private int countTotal = 0;
	private int destFileSeqNo = 0;
	
	private ResourceManager resourceManager; 
	
	
	public AreaScanDevice(String id) {
		super(id);
		scanOptionsX = addScanOptions(DEFAULT_SCAN_NAME_X);
		scanOptionsX.setStartOptionEnabled(true);
		scanOptionsX.setStepOptionEnabled(true);
		scanOptionsX.setEndOptionEnabled(true);
		scanOptionsY = addScanOptions(DEFAULT_SCAN_NAME_Y);
		scanOptionsY.setStartOptionEnabled(true);
		scanOptionsY.setStepOptionEnabled(true);
		scanOptionsY.setEndOptionEnabled(true);
	}
	
	@SuppressWarnings("unchecked")
	public Object getValue() {
		Map<String,Object> valueMap = (Map<String,Object>)super.getValue();
		valueMap.put(VALUE_KEY_COUNT_TOTAL, getCountTotal());
		valueMap.put(VALUE_KEY_START_POSITION_X, getStartPositionX());
		valueMap.put(VALUE_KEY_START_POSITION_Y, getStartPositionY());
		valueMap.put(VALUE_KEY_END_POSITION_X, getEndPositionX());
		valueMap.put(VALUE_KEY_END_POSITION_Y, getEndPositionY());
		valueMap.put(VALUE_KEY_NUMBER_POINTS_X, getNPointsX());
		valueMap.put(VALUE_KEY_NUMBER_POINTS_Y, getNPointsY());
		valueMap.put(VALUE_KEY_STEP_SIZE_X, getStepSizeX());
		valueMap.put(VALUE_KEY_STEP_SIZE_Y, getStepSizeY());
		return valueMap;
	}
	
	@SuppressWarnings("unchecked")
	public void setValue(Object value) {
		
		try {
			Map<String,Object> valueMap = (Map<String,Object>)value;
			HashMap<String,Object> eventValueMap = new HashMap<String,Object>();
			
			if(valueMap.containsKey(VALUE_KEY_STEP_SIZE_X)) {
				try {
					setStepSizeX((Double)valueMap.get(VALUE_KEY_STEP_SIZE_X));
					eventValueMap.put(VALUE_KEY_STEP_SIZE_X, getStepSizeX());
					eventValueMap.put(VALUE_KEY_END_POSITION_X, getEndPositionX());
				}
				catch (ClassCastException e) {
					log.warn("Set value for '" + VALUE_KEY_STEP_SIZE_X + "', is not expected class (Double).");
				}
			}
			
			if(valueMap.containsKey(VALUE_KEY_STEP_SIZE_Y)) {
				try {
					setStepSizeY((Double)valueMap.get(VALUE_KEY_STEP_SIZE_Y));
					eventValueMap.put(VALUE_KEY_STEP_SIZE_Y, getStepSizeY());
					eventValueMap.put(VALUE_KEY_END_POSITION_Y, getEndPositionY());
				}
				catch (ClassCastException e) {
					log.warn("Set value for '" + VALUE_KEY_STEP_SIZE_Y + "', is not expected class (Double).");
				}
			}
			
			if(valueMap.containsKey(VALUE_KEY_START_POSITION_X)) {
				try {
					setStartPositionX((Double)valueMap.get(VALUE_KEY_START_POSITION_X));
					eventValueMap.put(VALUE_KEY_START_POSITION_X, getStartPositionX());
					eventValueMap.put(VALUE_KEY_END_POSITION_X, getEndPositionX());
				}
				catch (ClassCastException e) {
					log.warn("Set value for '" + VALUE_KEY_START_POSITION_X + "', is not expected class (Double).");
				}
			}
			
			if(valueMap.containsKey(VALUE_KEY_START_POSITION_Y)) {
				try {
					setStartPositionY((Double)valueMap.get(VALUE_KEY_START_POSITION_Y));
					eventValueMap.put(VALUE_KEY_START_POSITION_Y, getStartPositionY());
					eventValueMap.put(VALUE_KEY_END_POSITION_Y, getEndPositionY());
				}
				catch (ClassCastException e) {
					log.warn("Set value for '" + VALUE_KEY_START_POSITION_Y + "', is not expected class (Double).");
				}
			}
			
			if(valueMap.containsKey(VALUE_KEY_END_POSITION_X)) {
				try {
					setEndPositionX((Double)valueMap.get(VALUE_KEY_END_POSITION_X));
					eventValueMap.put(VALUE_KEY_END_POSITION_X, getEndPositionX());
					eventValueMap.put(VALUE_KEY_NUMBER_POINTS_X, getNPointsX());
				}
				catch (ClassCastException e) {
					log.warn("Set value for '" + VALUE_KEY_END_POSITION_X + "', is not expected class (Double).");
				}
			}
			
			if(valueMap.containsKey(VALUE_KEY_END_POSITION_Y)) {
				try {
					setEndPositionY((Double)valueMap.get(VALUE_KEY_END_POSITION_Y));
					eventValueMap.put(VALUE_KEY_END_POSITION_Y, getEndPositionY());
					eventValueMap.put(VALUE_KEY_NUMBER_POINTS_Y, getNPointsY());
				}
				catch (ClassCastException e) {
					log.warn("Set value for '" + VALUE_KEY_END_POSITION_Y + "', is not expected class (Double).");
				}
			}
			
			if(valueMap.containsKey(VALUE_KEY_NUMBER_POINTS_X)) {
				try {
					setNPointsX((Integer)valueMap.get(VALUE_KEY_NUMBER_POINTS_X));
					eventValueMap.put(VALUE_KEY_NUMBER_POINTS_X, getNPointsX());
					eventValueMap.put(VALUE_KEY_END_POSITION_X, getEndPositionX());
				}
				catch (ClassCastException e) {
					log.warn("Set value for '" + VALUE_KEY_NUMBER_POINTS_X + "', is not expected class (Integer).");
				}
			}
			
			if(valueMap.containsKey(VALUE_KEY_NUMBER_POINTS_Y)) {
				try {
					setNPointsY((Integer)valueMap.get(VALUE_KEY_NUMBER_POINTS_Y));
					eventValueMap.put(VALUE_KEY_NUMBER_POINTS_Y, getNPointsY());
					eventValueMap.put(VALUE_KEY_END_POSITION_Y, getEndPositionY());
				}
				catch (ClassCastException e) {
					log.warn("Set value for '" + VALUE_KEY_NUMBER_POINTS_Y + "', is not expected class (Integer).");
				}
			}
			
			if(valueMap.containsKey(VALUE_KEY_SCAN_ID)) {
				try {
					setScanId((Integer)valueMap.get(VALUE_KEY_SCAN_ID));
					eventValueMap.put(VALUE_KEY_SCAN_ID, getScanId());
				}
				catch (ClassCastException e) {
					log.warn("Set value for '" + VALUE_KEY_SCAN_ID + "', is not expected class (Integer).");
				}
			}
			
			if(valueMap.containsKey(VALUE_KEY_SAMPLE_IMAGE)) {
				try {
					setSampleImage((byte[])valueMap.get(VALUE_KEY_SAMPLE_IMAGE));
				}
				catch(ClassCastException e) {
					log.warn("Set value for '" + VALUE_KEY_SAMPLE_IMAGE + "', is not expected class (byte[])."); 
				}
			}
			
			if(valueMap.containsKey(VALUE_KEY_SAMPLE_IMAGE_SCALE)) {
				try {
					setSampleImageScale((Double)valueMap.get(VALUE_KEY_SAMPLE_IMAGE_SCALE));
				}
				catch(ClassCastException e) {
					log.warn("Set value for '" + VALUE_KEY_SAMPLE_IMAGE_SCALE + "', is not expected class (Double)."); 
				}
			}
			
			if(valueMap.containsKey(VALUE_KEY_SAMPLE_IMAGE_POSITION_X)) {
				try {
					setSampleImagePositionX((Double)valueMap.get(VALUE_KEY_SAMPLE_IMAGE_POSITION_X));
				}
				catch(ClassCastException e) {
					log.warn("Set value for '" + VALUE_KEY_SAMPLE_IMAGE_POSITION_X + "', is not expected class (Double)."); 
				}
			}
			
			if(valueMap.containsKey(VALUE_KEY_SAMPLE_IMAGE_POSITION_Y)) {
				try {
					setSampleImagePositionY((Double)valueMap.get(VALUE_KEY_SAMPLE_IMAGE_POSITION_Y));
				}
				catch(ClassCastException e) {
					log.warn("Set value for '" + VALUE_KEY_SAMPLE_IMAGE_POSITION_Y + "', is not expected class (Double)."); 
				}	
			}
			
			super.setValue(value);
			
			if(!eventValueMap.isEmpty()) {
				publishEvent(new DeviceEvent(DeviceEventType.VALUE_CHANGE, id, eventValueMap, status, alarm));
			}
			
			if(valueMap.containsKey(VALUE_KEY_START)) {
				start();
			}
			
			if(valueMap.containsKey(VALUE_KEY_STOP)) {
				stop();
			}
			
			if(valueMap.containsKey(VALUE_KEY_PAUSE)) {
				pause();
			}
		}
		catch (ClassCastException e) {
			log.warn("Set value argument is not expected class (Map<String,Object>).");
		}
	}
	
	public void start() {
		switch (getScanState()) {
			case STOPPED:
			case COMPLETE:
				if(isRunning()) {
					sendQuitScanCmd();	
				}
				if(getScanDetails() != null) {
					if(log.isInfoEnabled()) {
						log.info(getAcqCommand());
					}
					runAcqCommand();
					sendStartScanCmd();
				}
				break;
		}
	}

	public void stop() {
		switch (getScanState()) {
			case STARTING:
			case ACQUIRING:
			case STOPPING:
			case PAUSED:
				sendStopScanCmd();
				break;
		}
	}

	public void pause() {
		switch (getScanState()) {
			case STARTING:
			case ACQUIRING:
			case STOPPING:
				sendPauseScanCmd();
				break;
				
			case PAUSED:
				sendResumeScanCmd();
		}
	}

	protected void handleScanStateChange(Map<String, Object> valueMap) {
		switch (getScanState()) {
			case STARTING:
				if(isScanning()) {
					getScanDetails().put(SCAN_DETAILS_KEY_START_DATE, new Date());
					valueMap.put(VALUE_KEY_SCAN_DETAILS, getScanDetails());
					valueMap.put(VALUE_KEY_SCAN_ID, getScanId());
					updateCountTotal(valueMap);
					saveSampleImage();
				}
				break;
		
			case STOPPED:
			case COMPLETE:
				if(isScanning()) {
					getScanDetails().put(SCAN_DETAILS_KEY_END_DATE, new Date());
					valueMap.put(VALUE_KEY_SCAN_DETAILS, getScanDetails());
					valueMap.put(VALUE_KEY_SCAN_ID, getScanId());
					setScanDetails(null);
					setScanId(0);
				}
				break;
		}
		super.handleScanStateChange(valueMap);
	}
	
	public int getScanId() {
		return scanId;
	}
	protected void setScanId(int scanId) {
		this.scanId = scanId;
	}
	
	protected Map<String,Serializable> getScanDetails() {
		
		if(scanDetails != null) {
			return scanDetails;
		}
		
		Parameters parameters = new Parameters();
		parameters.put(SCAN_PARAM_DATA_FILE_BASE, getDataFileBase());
		parameters.put(SCAN_PARAM_DATA_FILE_VERSION, getDestFileVersion());
		parameters.put(SCAN_PARAM_START_POSITION_X, String.valueOf(getStartPositionX()));
		parameters.put(SCAN_PARAM_START_POSITION_Y, String.valueOf(getStartPositionY()));
		parameters.put(SCAN_PARAM_END_POSITION_X, String.valueOf(getEndPositionX()));
		parameters.put(SCAN_PARAM_END_POSITION_Y, String.valueOf(getEndPositionY()));
		parameters.put(SCAN_PARAM_STEP_SIZE_X, String.valueOf(getStepSizeX()));
		parameters.put(SCAN_PARAM_STEP_SIZE_Y, String.valueOf(getStepSizeY()));
		parameters.put(SCAN_PARAM_NUMBER_POINTS_X, String.valueOf(getNPointsX()));
		parameters.put(SCAN_PARAM_NUMBER_POINTS_Y, String.valueOf(getNPointsY()));
		parameters.put(SCAN_PARAM_SAMPLE_IMAGE_FILE, String.valueOf(getSampleImageName()));
		parameters.put(SCAN_PARAM_SAMPLE_IMAGE_SCALE, String.valueOf(getSampleImageScale()));
		parameters.put(SCAN_PARAM_SAMPLE_IMAGE_POSITION_X, String.valueOf(getSampleImagePositionX()));
		parameters.put(SCAN_PARAM_SAMPLE_IMAGE_POSITION_Y, String.valueOf(getSampleImagePositionY()));
				
		try {
			Resource destDirResource = resourceManager.createRelativeResource("scan-" + scanId);
			setDestDirName(destDirResource.getFile().getPath() + "/");
		}
		catch(IOException e) {
			log.warn("IOException while creating scan data directory.", e);
			return null;
		}
		
		scanDetails = new HashMap<String,Serializable>();
		scanDetails.put(SCAN_DETAILS_KEY_PARAMETERS, parameters);
		scanDetails.put(SCAN_DETAILS_KEY_DATA_URL, getDestDirName());
		return scanDetails;
	}
	
	protected void setScanDetails(Map<String,Serializable> scanDetails) {
		this.scanDetails = scanDetails;
	}
	
	protected boolean isScanning() {
		return (scanDetails != null);
	}
	
	protected void updateCountTotal(Map<String,Object> valueMap) {
		setCountTotal(getNPointsX() * getNPointsY());
		valueMap.put(VALUE_KEY_COUNT_TOTAL, getCountTotal());
		valueMap.put(VALUE_KEY_COUNT, 0);
	}
	
	protected String getDataFileBase() {
		String dataFileBase = String.format(getDestFileName(), getDestFileSeqNo());
		return dataFileBase.replaceAll("\\.dat\\Z", "");
	}
	
	public int getDestFileSeqNo() {
		return destFileSeqNo;
	}
	public void setDestFileSeqNo(int destFileSeqNo) {
		this.destFileSeqNo = destFileSeqNo;
	}

	public String getScanNameX() {
		return scanOptionsX.getScanName();
	}
	public void setScanNameX(String scanName) {
		scanOptionsX.setScanName(scanName);
	}
	
	public String getScanNameY() {
		return scanOptionsY.getScanName();
	}
	public void setScanNameY(String scanName) {
		scanOptionsY.setScanName(scanName);
	}
	
	public int getCountTotal() {
		return countTotal;
	}
	protected void setCountTotal(int countTotal) {
		this.countTotal = countTotal;
	}
	
	public ResourceManager getResourceManager() {
		return resourceManager;
	}
	public void setResourceManager(ResourceManager resourceManager) {
		this.resourceManager = resourceManager;
	}

	public byte[] getSampleImage() {
		return sampleImage;
	}
	public void setSampleImage(byte[] sampleImage) {
		this.sampleImage = sampleImage;
	}
	
	public double getSampleImageScale() {
		return sampleImageScale;
	}
	public void setSampleImageScale(double sampleImageScale) {
		if(sampleImageScale > 0.0) {
			this.sampleImageScale = sampleImageScale;
		}
	}
	
	public double getSampleImagePositionX() {
		return sampleImagePositionX;
	}
	public void setSampleImagePositionX(double sampleImagePositionX) {
		this.sampleImagePositionX = sampleImagePositionX;
	}
	
	public double getSampleImagePositionY() {
		return sampleImagePositionY;
	}
	public void setSampleImagePositionY(double sampleImagePositionY) {
		this.sampleImagePositionY = sampleImagePositionY;
	}
	
	public String getSampleImageName() {
		return sampleImageName;
	}
	public void setSampleImageName(String sampleImageName) {
		this.sampleImageName = sampleImageName;
	}
	
	public String getSampleImageFormat() {
		String[] split = sampleImageName.split("\\.");
		if(split.length > 1) {
			return split[split.length - 1];
		}
		else {
			return DEFAULT_SAMPLE_IMAGE_FORMAT;
		}
	}
	
	public double getStartPositionX() {
		return scanOptionsX.getStartValue();
	}
	public void setStartPositionX(double startPositionX) {
		scanOptionsX.setEndValue(startPositionX + ((getNPointsX() - 1) * getStepSizeX()));
		scanOptionsX.setStartValue(startPositionX);
	}
	
	protected double getEndPositionX() {
		return scanOptionsX.getEndValue();
	}
	protected void setEndPositionX(double endPositionX) {
		if(endPositionX > getStartPositionX()) {
			double delta = endPositionX - getStartPositionX();
			long nSteps = Math.round(delta / getStepSizeX());
			scanOptionsX.setEndValue(getStartPositionX() + (nSteps * getStepSizeX()));
		}
		else {
			scanOptionsX.setEndValue(getStartPositionX());
		}
	}
	
	public int getNPointsX() {
		double delta = getEndPositionX() - getStartPositionX();
		return (int)Math.round((delta / getStepSizeX()) + 1.0);
	}
	public void setNPointsX(int nPointsX) {
		if(nPointsX >= 1) {
			scanOptionsX.setEndValue(getStartPositionX() + ((nPointsX - 1) * getStepSizeX()));
		}
	}

	public double getStepSizeX() {
		return scanOptionsX.getStepSize();
	}
	public void setStepSizeX(double stepSizeX) {
		if(stepSizeX > 0.0) { 
			scanOptionsX.setEndValue(getStartPositionX() + ((getNPointsX() - 1) * stepSizeX));
			scanOptionsX.setStepSize(stepSizeX);
		}
	}

	public double getStartPositionY() {
		return scanOptionsY.getStartValue();
	}
	public void setStartPositionY(double startPositionY) {
		scanOptionsY.setEndValue(startPositionY + ((getNPointsY() - 1) * getStepSizeY()));
		scanOptionsY.setStartValue(startPositionY);
	}
	
	protected double getEndPositionY() {
		return scanOptionsY.getEndValue();
	}
	protected void setEndPositionY(double endPositionY) {
		if(endPositionY > getStartPositionY()) {
			double delta = endPositionY - getStartPositionY();
			long nSteps = Math.round(delta / getStepSizeY());
			scanOptionsY.setEndValue(getStartPositionY() + (nSteps * getStepSizeY()));
		}
		else {
			scanOptionsY.setEndValue(getStartPositionY());	
		}
	}
	
	public int getNPointsY() {
		double delta = getEndPositionY() - getStartPositionY();
		return (int)Math.round((delta / getStepSizeY()) + 1.0);
	}
	public void setNPointsY(int nPointsY) {
		if(nPointsY >= 1) {
			scanOptionsY.setEndValue(getStartPositionY() + ((nPointsY - 1) * getStepSizeY()));
		}
	}

	public double getStepSizeY() {
		return scanOptionsY.getStepSize();
	}
	public void setStepSizeY(double stepSizeY) {
		if(stepSizeY > 0.0) {
			scanOptionsY.setEndValue(getStartPositionY() + ((getNPointsY() - 1) * stepSizeY));
			scanOptionsY.setStepSize(stepSizeY);
		}
	}
	
	protected void saveSampleImage() {
		
		BufferedImage bufferedSampleImage;
		
		try {
			ByteArrayInputStream sampleImageInputStream = new ByteArrayInputStream(sampleImage);
			bufferedSampleImage = ImageIO.read(sampleImageInputStream);	
		}
		catch(IOException e) {
			log.warn("Sample Image not found, unable to read image.", e);
			return;
		}
		
		Graphics2D scanRegionGraphics = bufferedSampleImage.createGraphics();
		
		double width = bufferedSampleImage.getWidth(); 
		double height = bufferedSampleImage.getHeight();
		scanRegionGraphics.translate(width / 2.0, height / 2.0);
		scanRegionGraphics.scale(1.0, -1.0); // flip upside down!
		
		double sampleImageScale = getSampleImageScale();
		double scale = 1.0 / sampleImageScale;
		scanRegionGraphics.scale(scale, scale);
		
		double translateX = -1.0 * getSampleImagePositionX();
		double translateY = -1.0 * getSampleImagePositionY();
		scanRegionGraphics.translate(translateX, translateY);
		
		double halfStepX = getStepSizeX() / 2.0;
		double halfStepY = getStepSizeY() / 2.0;
		double startX = getStartPositionX() - halfStepX;
		double startY = getStartPositionY() - halfStepY;
		double sizeX = (getEndPositionX() - startX) + halfStepX;
		double sizeY = (getEndPositionY() - startY) + halfStepY;
		Rectangle2D scanRegion = new Rectangle2D.Double(startX, startY, sizeX, sizeY);
		
		Composite composite;
		composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.80f);
		scanRegionGraphics.setStroke(new BasicStroke((float)(2.0 * sampleImageScale)));
		scanRegionGraphics.setComposite(composite);
		scanRegionGraphics.setColor(Color.RED);
		scanRegionGraphics.draw(scanRegion);
		
		composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.0f);
		scanRegionGraphics.setComposite(composite);
		scanRegionGraphics.setColor(Color.RED);
		scanRegionGraphics.fill(scanRegion);
		
		try {	
			File file = new File(getDestDirName(), getSampleImageName()); 
			ImageIO.write(bufferedSampleImage, getSampleImageFormat(), file);
		}
		catch(IOException e) {
			log.warn("Sample Image found, unable to write to file.", e);
			return;
		}
	}
}
