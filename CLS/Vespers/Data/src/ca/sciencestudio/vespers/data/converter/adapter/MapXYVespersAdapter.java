/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     MapXYVespersAdapter interface.
 *     
 */
package ca.sciencestudio.vespers.data.converter.adapter;

import java.util.Date;

import ca.sciencestudio.data.support.ConverterException;

/**
 * 
 * @author maxweld
 *
 */
public interface MapXYVespersAdapter {

	public boolean openDestination() throws ConverterException;
	public void closeDestination() throws ConverterException;
	public void deleteDestination() throws ConverterException;
	
	// Scan //

	public void recScanStartTime(Date startTime) throws ConverterException;
	public void recScanEndTime(Date endTime) throws ConverterException;
	
	// Model //
	
	public void recProjectName(String projectName) throws ConverterException;
	public void recSessionName(String sessionName) throws ConverterException;
	public void recFacilityName(String facilityName) throws ConverterException;
	public void recLaboratoryName(String laboratoryName) throws ConverterException;
	public void recExperimentName(String experimentName) throws ConverterException;
	public void recInstrumentName(String instrumentName) throws ConverterException;
	public void recTechniqueName(String techniqueName) throws ConverterException;
	public void recScanName(String scanName) throws ConverterException;
	public void recSampleName(String sampleName) throws ConverterException;
	
	// Dimensions X //
	
	public void initSizeX(String name, String description) throws ConverterException;
	public void initStepX(String name, String description) throws ConverterException;
	public void initStartX(String name, String description) throws ConverterException;
	public void initEndX(String name, String description) throws ConverterException;
	
	public void recSizeX(int sizeX) throws ConverterException;      // point //
	public void recStepX(double stepX) throws ConverterException;   // millimeter //
	public void recStartX(double startX) throws ConverterException; // millimeter //
	public void recEndX(double endX) throws ConverterException;     // millimeter //
	
	// Dimensions Y //
	
	public void initSizeY(String name, String description) throws ConverterException;
	public void initStepY(String name, String description) throws ConverterException;
	public void initStartY(String name, String description) throws ConverterException;
	public void initEndY(String name, String description) throws ConverterException;
	
	public void recSizeY(int sizeY) throws ConverterException;      // point //
	public void recStepY(double stepY) throws ConverterException;   // millimeter //
	public void recStartY(double startY) throws ConverterException; // millimeter //
	public void recEndY(double endY) throws ConverterException;     // millimeter //
	
	// SED Detector //
	
	public void initSedMaxEnergy(String name, String description) throws ConverterException;
	public void initSedEnergyGapTime(String name, String description) throws ConverterException;
	public void initSedEnergyPeakingTime(String name, String description) throws ConverterException;
	public void initSedPresetRealTime(String name, String description) throws ConverterException;
	
	public void recSedMaxEnergy(double sedMaxEnergy) throws ConverterException;                 // e-volt //
	public void recSedEnergyGapTime(double sedEnergyGapTime) throws ConverterException;         // microsecond //
	public void recSedEnergyPeakingTime(double sedEnergyPeakingTime) throws ConverterException; // microsecond //
	public void recSedPresetRealTime(double sedPresetRealTime) throws ConverterException;       // second //
	
	// FED Detector //
	
	public void initFedMaxEnergy(String name, String description) throws ConverterException;
	public void initFedEnergyGapTime(String name, String description) throws ConverterException;
	public void initFedEnergyPeakingTime(String name, String description) throws ConverterException;
	public void initFedPresetRealTime(String name, String description) throws ConverterException;
	
	public void recFedMaxEnergy(double fedMaxEnergy) throws ConverterException;                 // e-volt //
	public void recFedEnergyGapTime(double fedEnergyGapTime) throws ConverterException;         // microsecond //
	public void recFedEnergyPeakingTime(double fedEnergyPeakingTime) throws ConverterException; // microsecond //
	public void recFedPresetRealTime(double fedPresetRealTime) throws ConverterException;       // second //

	//////////////////////////////////////////////////////////////////////////////////////
	
	public void openDataRecord(int dataRecordIndex) throws ConverterException;
	public void closeDataRecord(int dataRecordIndex) throws ConverterException;
	
	// Dimensions X //
	
	public void initPointX(String name, String description) throws ConverterException;
	public void initPositionX(String name, String description) throws ConverterException;
	
	public void recPointX(int pointX) throws ConverterException;          // point //
	public void recPositionX(double positionX) throws ConverterException; // millimeter //
	
	// Dimensions Y //
	
	public void initPointY(String name, String description) throws ConverterException;
	public void initPositionY(String name, String description) throws ConverterException;
	
	public void recPointY(int pointY) throws ConverterException;          // point //
	public void recPositionY(double positionY) throws ConverterException; // millimeter //
	
	// Beam Current //
	
	public void initSrCurrent(String name, String description) throws ConverterException;
	public void initMcsCurrent(int index, String name, String description) throws ConverterException;
	
	public void recSrCurrent(double srCurrent) throws ConverterException;              // milliampere //
	public void recMcsCurrent(int index, double mcsCurrent) throws ConverterException; // count //
	
	// SED Detector //
	
	public void initSedFastCount(String name, String description) throws ConverterException;
	public void initSedSlowCount(String name, String description) throws ConverterException;
	public void initSedDeadTimePct(String name, String description) throws ConverterException;
	public void initSedElapsedRealTime(String name, String description) throws ConverterException;
	public void initSedElapsedLiveTime(String name, String description) throws ConverterException;
	public void initSedSpectrum(int nChannels, String name, String description) throws ConverterException;
	
	public void recSedFastCount(long sedFastCount) throws ConverterException;               // count //
	public void recSedSlowCount(long sedSlowCount) throws ConverterException;               // count //
	public void recSedDeadTimePct(double sedDeadTimePct) throws ConverterException;         // percent //
	public void recSedElapsedRealTime(double sedElapsedRealTime) throws ConverterException; // second //
	public void recSedElapsedLiveTime(double sedElapsedLiveTime) throws ConverterException; // second //
	public void recSedSpectrum(long sedSpectrum[]) throws ConverterException;               // count //
	
	// FED Detector //
	
	public void initFedFastCount(int index, String name, String description) throws ConverterException;
	public void initFedSlowCount(int index, String name, String description) throws ConverterException;
	public void initFedDeadTimePct(int index, String name, String description) throws ConverterException;
	public void initFedElapsedRealTime(int index, String name, String description) throws ConverterException;
	public void initFedElapsedLiveTime(int index, String name, String description) throws ConverterException;
	public void initFedSpectrum(int index, int nChannels, String name, String description) throws ConverterException;
	public void initFedSumSpectrum(int nChannels, String name, String description) throws ConverterException;
	
	public void recFedFastCount(int index, long fedFastCount) throws ConverterException;
	public void recFedSlowCount(int index, long fedSlowCount) throws ConverterException;
	public void recFedDeadTimePct(int index, double fedDeadTimePct) throws ConverterException;         // percent //
	public void recFedElapsedRealTime(int index, double fedElapsedRealTime) throws ConverterException; // second //
	public void recFedElapsedLiveTime(int index, double fedElapsedLiveTime) throws ConverterException; // second //
	public void recFedSpectrum(int index, long fedSpectrum[]) throws ConverterException;               // count //
	public void recFedSumSpectrum(long fedSpectrum[]) throws ConverterException;                       // count //
}
