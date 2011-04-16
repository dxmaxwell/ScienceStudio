/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 * 		AbstractAcqLibCmdLineScanDevice class.
 *     
 */
package ca.sciencestudio.vespers.bcm.daq;

import java.lang.Runtime;
import java.lang.Process;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ca.sciencestudio.device.control.event.DeviceEvent;
import ca.sciencestudio.device.control.event.DeviceEventType;

/**
 * @author maxweld
 *
 */
public abstract class AbstractAcqLibCmdLineScanDevice extends AbstractScanDevice {
	
	public static final String VALUE_KEY_COUNT = "scanCount";
	public static final String VALUE_KEY_PROGRESS = "scanProgress";
	public static final String VALUE_KEY_SCAN_STATE = "scanState";
	public static final String VALUE_KEY_EXEC_FILE = "execFile";
	public static final String VALUE_KEY_CONFIG_FILE = "configFile";
	public static final String VALUE_KEY_DEST_DIR_NAME = "destDirName";
	public static final String VALUE_KEY_DEST_FILE_NAME = "destFileName";
	
	public static final String OPTION_DEST_DIRNAME = " -d \"%s\" ";
	public static final String OPTION_DEST_FILENAME = " -f \"%s\" -u 1 ";
	public static final String OPTION_INTERACTIVE = " -i ";
	public static final String OPTION_MACRO = " -m  \"%s\" ";
	public static final String OPTION_MACRO_FILE = " -M \"%s\" ";
	public static final String OPTION_SCAN = "%s %s %s %s";
	public static final String OPTION_SCAN_NAME = " -n \"%s\" ";
	public static final String OPTION_SCAN_START = " -s %g ";
	public static final String OPTION_SCAN_END = " -e %g ";
	public static final String OPTION_SCAN_STEP = " -p %g ";
	
	public static final String COMMAND_ACQUISITION = "\"%s\" \"%s\" %s %s";	
	public static final String[] COMMAND_SHELL = { "sh", "-l", "-c" };
	public static final String[] COMMAND_SWITCH_USER = { "su", "%s", "-l", "-c" }; 
	
	public static final String PATTERN_COUNT_FMT = "^Event %s:.*count (.*)$";
	public static final Pattern PATTERN_PROGRESS = Pattern.compile("^Progress: (.*)$");
	public static final Pattern PATTERN_SCAN_MODE = Pattern.compile("^Mode: (.*)$");
	public static final Pattern PATTERN_SCAN_CMD = Pattern.compile("^Cmd: (.*)$");
	public static final Pattern PATTERN_SCAN_WARN = Pattern.compile("^Warning: (.*)$");
	public static final Pattern PATTERN_SCAN_ERROR = Pattern.compile("^Error: (.*)$");
	
	public static final long SEND_SCAN_CMD_SLEEP_TIME = 100;
	
	public static final String DEFAULT_EXECUTABLE_FILE = "acquisitionCL";
	
	public static enum ScanMode {
		
		UNKNOWN, STANDBY, STARTUP, RUNUP, RUN, OFF;
				
		public static ScanMode findScanMode(String mode) {
			for(ScanMode scanMode : values()) {
				if(scanMode.name().equalsIgnoreCase(mode)) {
					return scanMode;
				}
			}
			return UNKNOWN;
		}
	}
	
	public static enum ScanCmd {
		UNKNOWN, START, STOP, PAUSE, RESUME, QUIT;
		
		public static ScanCmd findScanCmd(String cmd) {
			for(ScanCmd scanCmd : values()) {
				if(scanCmd.name().equalsIgnoreCase(cmd)) {
					return scanCmd;
				}
			}
			return UNKNOWN;
		}
	}
	
	
	private Process process = null;
	private ProcessBuilder processBuilder;
	
	private String executableFile = DEFAULT_EXECUTABLE_FILE;
	private String configurationFile = "";
	
	private String runAsUser = "";
	private boolean runAsUserOptionEnabled = false; 
	private String destFileName = "";
	private boolean destFileNameOptionEnabled = false;
	private String destDirName = "";
	private boolean destDirNameOptionEnabled = false;
	private String destFileVersion = "";
	
	private List<String> macroList = new ArrayList<String>();
	private List<String> macroFileList = new ArrayList<String>();
	private List<ScanOptions> scanOptionsList = new ArrayList<ScanOptions>();
	
	private int scanCount = 0;
	private double scanProgress = 0.0;
	private ScanMode scanMode = ScanMode.OFF;
	private ScanCmd scanCmd = ScanCmd.STOP;
	private String scanCountEvent = "";
	private String scanWarn = "";
	private String scanError = "";
	
	private Pattern scanCountPattern = Pattern.compile(String.format(PATTERN_COUNT_FMT, ""));
	
	protected AbstractAcqLibCmdLineScanDevice(String id) {
		super(id);
		new ErrorOutputReaderThread();
		new StandardOutputReaderThread();
		this.scanState = ScanDeviceState.STOPPED;
		processBuilder = new ProcessBuilder(getAcqCommandList());
		Runtime.getRuntime().addShutdownHook(new ProcessShutdownHook());
	}
	
	public Object getValue() {
		Map<String,Object> valueMap = new HashMap<String,Object>();
		valueMap.put(VALUE_KEY_COUNT, getCount());
		valueMap.put(VALUE_KEY_PROGRESS, getScanProgress());
		valueMap.put(VALUE_KEY_SCAN_STATE, getScanState().name());
		valueMap.put(VALUE_KEY_EXEC_FILE, getExecutableFile());
		valueMap.put(VALUE_KEY_CONFIG_FILE, getConfigurationFile());
		valueMap.put(VALUE_KEY_DEST_DIR_NAME, getDestDirName());
		valueMap.put(VALUE_KEY_DEST_FILE_NAME, getDestFileName());
		return valueMap;
	}
	
	@SuppressWarnings("unchecked")
	public void setValue(Object value) {
		try {
			Map<String,Object> valueMap = (Map<String,Object>)value;
			HashMap<String,Object> eventValueMap = new HashMap<String,Object>();
			
			if(valueMap.containsKey(VALUE_KEY_DEST_DIR_NAME)) {
				try {
					setDestDirName((String)valueMap.get(VALUE_KEY_DEST_DIR_NAME));
					eventValueMap.put(VALUE_KEY_DEST_DIR_NAME, getDestDirName());
				}
				catch(ClassCastException e) {
					log.warn("Set value for '" + VALUE_KEY_DEST_DIR_NAME + "', is not expected class (String).");
				}
			}
			
			if(valueMap.containsKey(VALUE_KEY_DEST_FILE_NAME)) {
				try {
					setDestFileName((String)valueMap.get(VALUE_KEY_DEST_FILE_NAME));
					eventValueMap.put(VALUE_KEY_DEST_FILE_NAME, getDestFileName());
				}
				catch(ClassCastException e) {
					log.warn("Set value for '" + VALUE_KEY_DEST_FILE_NAME + "', is not expected class (String).");
				}
			}
			
			if(valueMap.containsKey(VALUE_KEY_CONFIG_FILE)) {
				try {
					setConfigurationFile((String)VALUE_KEY_CONFIG_FILE);
					eventValueMap.put(VALUE_KEY_CONFIG_FILE, getConfigurationFile());
				}
				catch(ClassCastException e) {
					log.warn("Set value for '" + VALUE_KEY_CONFIG_FILE + "', is not expected class (String).");
				}
			}

			if(!eventValueMap.isEmpty()) {
				publishEvent(new DeviceEvent(DeviceEventType.VALUE_CHANGE, id, eventValueMap, status, alarm));
			}
		}
		catch(ClassCastException e) {
			log.warn("Set value argument is not expected class (Map<String,Object>).");
		}
	}
	
	
	public Process runAcqCommand() {
		if(!isRunning()) {
			try {
				processBuilder.command(getAcqCommandList());
				process = processBuilder.start();
			}
			catch (IOException e) {
				log.warn(String.format("Command could not be executed. (Executable: %s)", getExecutableFile()));
			}
		}
		return process;
	}

	public boolean isRunning() {
		if(process == null) {
			return false;
		}
		else {
			try {
				process.exitValue();
				return false;
			}
			catch(IllegalThreadStateException e) {
				return true;
			}
		}
	}
	
	public String getAcqCommand() {
		StringBuffer command = new StringBuffer();
		for(String acqCommand : getAcqCommandList()) {
			command.append(acqCommand + " ");
		}
		return command.toString().trim();
	}
	
	public List<String> getAcqCommandList() {
		List<String> command = new ArrayList<String>();
		if(isRunAsUserOptionEnabled()) {
			command.add(COMMAND_SWITCH_USER[0]);
			command.add(String.format(COMMAND_SWITCH_USER[1], getRunAsUser()));
			//command.add(COMMAND_SWITCH_USER[2]);
			command.add(COMMAND_SWITCH_USER[3]);
		}
		else {
			command.add(COMMAND_SHELL[0]);
			//command.add(COMMAND_SHELL[1]);
			command.add(COMMAND_SHELL[2]);
		}
		command.add(String.format(COMMAND_ACQUISITION, getExecutableFile(), getConfigurationFile(), getMacroOptions(), getAcqOptions()));			
		return command;
	}
	
	public String getMacroOptions() {
		StringBuffer macroOptions = new StringBuffer();
		for(String macro : macroList) {
			macroOptions.append(String.format(OPTION_MACRO, macro));
		}
		for(String macroFile : macroFileList) {
			macroOptions.append(String.format(OPTION_MACRO_FILE, macroFile));
		}
		return macroOptions.toString();
	}
	
	public String getAcqOptions() {
		StringBuffer options = new StringBuffer(OPTION_INTERACTIVE);
		options.append(getDestFileNameOption());
		options.append(getDestDirNameOption());
		for(ScanOptions scanOptions : scanOptionsList) {
			options.append(scanOptions.getOptions());
		}
		return options.toString();	
	}

	public void sendStartScanCmd() {
		if(isRunning()) {
			PrintStream ps = new PrintStream(process.getOutputStream());
			ps.println(ScanCmd.START.name());
			ps.flush();
			try {
				Thread.sleep(SEND_SCAN_CMD_SLEEP_TIME);
			}
			catch (InterruptedException e) {
				return;
			}
		}
	}

	public void sendStopScanCmd() {
		if(isRunning()) {
			PrintStream ps = new PrintStream(process.getOutputStream());
			ps.println(ScanCmd.STOP.name());
			ps.flush();
			try {
				Thread.sleep(SEND_SCAN_CMD_SLEEP_TIME);
			}
			catch (InterruptedException e) {
				return;
			}
		}
	}
	
	public void sendPauseScanCmd() {
		if(isRunning()) {
			PrintStream ps = new PrintStream(process.getOutputStream());
			ps.println(ScanCmd.PAUSE.name());
			ps.flush();
			try {
				Thread.sleep(SEND_SCAN_CMD_SLEEP_TIME);
			}
			catch (InterruptedException e) {
				return;
			}
		}
	}
	
	public void sendResumeScanCmd() {
		if(isRunning()) {
			PrintStream ps = new PrintStream(process.getOutputStream());
			ps.println(ScanCmd.RESUME.name());
			ps.flush();
			try {
				Thread.sleep(SEND_SCAN_CMD_SLEEP_TIME);
			}
			catch (InterruptedException e) {
				return;
			}
		}
	}
	
	public void sendQuitScanCmd() {
		if(isRunning()) {
			PrintStream ps = new PrintStream(process.getOutputStream());
			ps.println(ScanCmd.QUIT.name());
			ps.flush();
			try {
				Thread.sleep(SEND_SCAN_CMD_SLEEP_TIME);
			}
			catch (InterruptedException e) {
				return;
			}
		}
	}
	
	
	
	public String getExecutableFile() {
		return executableFile;
	}
	public void setExecutableFile(String executableFile) {
		this.executableFile = executableFile;
	}
	
	public String getConfigurationFile() {
		return configurationFile;
	}
	public void setConfigurationFile(String configurationFile) {
		this.configurationFile = configurationFile;
	}
	
	public String getRunAsUser() {
		return runAsUser;
	}
	public void setRunAsUser(String runAsUser) {
		this.runAsUser = runAsUser;
		setRunAsUserOptionEnabled(true);
	}
	
	public boolean isRunAsUserOptionEnabled() {
		return runAsUserOptionEnabled;
	}
	public void setRunAsUserOptionEnabled(boolean runAsUserOptionEnabled) {
		this.runAsUserOptionEnabled = runAsUserOptionEnabled;
	}
	
	public String getDestFileName() {
		return destFileName;
	}
	public void setDestFileName(String destFileName) {
		this.destFileName = destFileName;
		setDestFileNameOptionEnabled(true);
	}
	public boolean isDestFileNameOptionEnabled() {
		return destFileNameOptionEnabled;
	}
	public void setDestFileNameOptionEnabled(boolean destFileNameOptionEnabled) {
		this.destFileNameOptionEnabled = destFileNameOptionEnabled;
	}
	public String getDestFileNameOption() {
		if(isDestFileNameOptionEnabled()) {
			return String.format(OPTION_DEST_FILENAME, getDestFileName());
		}
		else {
			return "";
		}
	}
	
	public String getDestDirName() {
		return destDirName;
	}
	public void setDestDirName(String destDirName) {
		this.destDirName = destDirName;
		setDestDirNameOptionEnabled(true);
	}
	public boolean isDestDirNameOptionEnabled() {
		return destDirNameOptionEnabled;
	}
	public void setDestDirNameOptionEnabled(boolean destDirNameOptionEnabled) {
		this.destDirNameOptionEnabled = destDirNameOptionEnabled;
	}
	public String getDestDirNameOption() {
		if(isDestDirNameOptionEnabled()) {
			return String.format(OPTION_DEST_DIRNAME, getDestDirName());
		}
		else {
			return "";
		}
	}

	public String getDestFileVersion() {
		return destFileVersion;
	}
	public void setDestFileVersion(String destFileVersion) {
		this.destFileVersion = destFileVersion;
	}

	public String getWorkingDirectory() {
		return processBuilder.directory().getAbsolutePath();
	}
	public void setWorkingDirectory(String workingDirectory) {
		if(workingDirectory == null || workingDirectory.length() == 0) {
			processBuilder.directory(null);
			return;
		}
		File file = new File(workingDirectory);
		if(file.isDirectory()) {
			processBuilder.directory(file);
		}
		else {
			log.warn("Given path is not a Directory. Cannot set working directory.");
		}
		return;
	}
	
	public String getScanCountEvent() {
		return scanCountEvent;
	}
	public void setScanCountEvent(String scanCountEvent) {
		if(scanCountEvent != null) {
			this.scanCountEvent = scanCountEvent;
			this.scanCountPattern = Pattern.compile(String.format(PATTERN_COUNT_FMT, scanCountEvent));
		}
	}
	
	public int getCount() {
		return scanCount;
	}
	protected boolean setScanCount(int scanCount) {
		if(this.scanCount == scanCount) {
			return false;
		}
		else {
			this.scanCount = scanCount;
			return true;
		}
	}
	
	public double getScanProgress() {
		return scanProgress;
	}
	protected boolean setScanProgress(double scanProgress) {
		if(this.scanProgress == scanProgress) {
			return false;
		}
		else {
			this.scanProgress = scanProgress;
			return true;
		}
	}
	
	protected String getScanMode() {
		return scanMode.name();
	}
	protected boolean setScanMode(String mode) {
		ScanMode scanMode = ScanMode.findScanMode(mode);
		if(this.scanMode == scanMode) {
			return false;
		}
		else {
			this.scanMode = scanMode;
			return true;
		}	
	}
	
	protected String getScanCmd() {
		return scanCmd.name();
	}
	protected boolean setScanCmd(String cmd) {
		ScanCmd scanCmd = ScanCmd.findScanCmd(cmd);
		if(this.scanCmd == scanCmd) {
			return false;
		}
		else {
			this.scanCmd = scanCmd;
			return true;
		}
	}
	
	protected boolean setScanState(ScanDeviceState scanState) {
		if(this.scanState == scanState) {
			return false;
		}
		else {
			this.scanState = scanState;
			return true;
		}
	}
	
	protected ScanDeviceState nextScanState() {
		
		switch (scanState) {
			case STOPPED:
			case COMPLETE:
				switch (scanCmd) {
					case START:
						switch (scanMode) {
							case RUNUP:
							case STARTUP:
								return ScanDeviceState.STARTING;
						}
						break;
				}
				break;	
			
				
			case STARTING:
				switch (scanCmd) {
					case START:
					case RESUME:
						switch (scanMode) {
							case RUN:
								return ScanDeviceState.ACQUIRING;
							case OFF:
								return ScanDeviceState.STOPPED;
						}
						break;
						
					case PAUSE:
						return ScanDeviceState.PAUSED;
					
					case STOP:
						return ScanDeviceState.STOPPING;
				}
				break;	
			
				
			case PAUSED:
				switch (scanCmd) {
					case RESUME:
						switch (scanMode) {
							case RUNUP:
							case STARTUP:
								return ScanDeviceState.STARTING;
							case RUN:
								return ScanDeviceState.ACQUIRING;
							case STANDBY:
								return ScanDeviceState.STOPPING;
							case OFF:
								return ScanDeviceState.STOPPED;
						}
						break;
						
					case STOP:
						switch (scanMode) {
							case STANDBY:
								return ScanDeviceState.STOPPING;
							case OFF:
								return ScanDeviceState.STOPPED;
						}
						break;
				}
				break;
				
			
			case ACQUIRING:
				switch (scanCmd) {
					case START:
					case RESUME:
					case STOP:
						switch(scanMode) {
							case STANDBY:
								return  ScanDeviceState.STOPPING;
							case OFF:
								return ScanDeviceState.STOPPED;
						}
						break;

					case PAUSE:
						return ScanDeviceState.PAUSED;
				}
				break;
				
			
			case STOPPING:
				switch (scanCmd) {
					case START:
					case RESUME:
						switch (scanMode) {
							case OFF:
								return ScanDeviceState.COMPLETE;
						}
						break;
				
					case STOP:
						switch (scanMode) {
							case OFF:
								return ScanDeviceState.STOPPED;
						}
						break;
				}
				break;
		}
		
		return scanState;
	}

	protected void handleScanStateChange(Map<String,Object> valueMap) {
		log.info("Scan State change to: " + getScanState().name());
	}
	
	public String getScanWarn() {
		return scanWarn;
	}
	protected boolean setScanWarn(String scanWarn) {
		if(this.scanWarn.equals(scanWarn)) {
			return false;
		}
		else {
			this.scanWarn = scanWarn;
			return true;
		}
	}
	
	public String getScanError() {
		return scanError;
	}
	protected boolean setScanError(String scanError) {
		if(this.scanError.equals(scanError)) {
			return false;
		}
		else {
			this.scanError = scanError;
			return true;
		}
	}
	
	public void addMacro(String macro) {
		macroList.add(macro);
	}
	public void setMacro(String macro) {
		macroList.clear();
		addMacro(macro);
	}
	public void setMacroList(List<String> macroList) {
		this.macroList = macroList;
	}
	
	public void addMacroFile(String macroFile) {
		macroFileList.add(macroFile);
	}
	public void setMacroFile(String macroFile) {
		macroFileList.clear();
		addMacroFile(macroFile);
	}
	public void setMacroFileList(List<String> macroFileList) {
		this.macroFileList = macroFileList;
	}
	
	public ScanOptions addScanOptions(String scanName) {
		ScanOptions scanOptions = getScanOptions(scanName);
		if(scanOptions == null) {
			scanOptions = new ScanOptions(scanName);
			scanOptionsList.add(scanOptions);
		}
		return scanOptions;	
	}
	
	public void removeScanOptions(String scanName) {
		ScanOptions scanOptions = getScanOptions(scanName);
		if(scanOptions != null) { scanOptionsList.remove(scanOptions); }
	}
	
	public ScanOptions getScanOptions(String scanName) {
		for(ScanOptions scanOptions : scanOptionsList) {
			if(scanOptions.getScanName() != null && scanOptions.getScanName().equals(scanName)) {
				return scanOptions;
			}
		}
		return null;
	}
	
	protected class ScanOptions {
		
		private String scanName = "";
		private double startValue = 0.0;
		private boolean startOptionEnabled = false;
		private double endValue = 0.0;
		private boolean endOptionEnabled = false;
		private double stepSize = 1.0;
		private boolean stepOptionEnabled = false;
		
		String getOptions() {
			return String.format(OPTION_SCAN, getScanNameOption(), getStartOption(), getEndOption(), getStepOption());
		}

		public ScanOptions(String scanName) {
			setScanName(scanName);
		}

		public String getScanName() {
			return scanName;
		}
		public void setScanName(String scanName) {
			this.scanName = scanName; 
		}
		public String getScanNameOption() {
			return String.format(OPTION_SCAN_NAME, getScanName());
		}
		
		public double getStartValue() {
			return startValue;
		}
		public void setStartValue(double startValue) {
			this.startValue = startValue;
			setStartOptionEnabled(true);
		}
		public boolean isStartOptionEnabled() {
			return startOptionEnabled;
		}
		public void setStartOptionEnabled(boolean startOptionEnabled) {
			this.startOptionEnabled = startOptionEnabled;
		}
		public String getStartOption() {
			if(isStartOptionEnabled()) {
				return String.format(OPTION_SCAN_START, getStartValue());
			}
			else {
				return "";
			}
		}
		
		public double getEndValue() {
			return endValue;
		}
		public void setEndValue(double endValue) {
			this.endValue = endValue;
			setEndOptionEnabled(true);
		}
		public boolean isEndOptionEnabled() {
			return endOptionEnabled;
		}
		public void setEndOptionEnabled(boolean endOptionEnabled) {
			this.endOptionEnabled = endOptionEnabled;
		}
		public String getEndOption() {
			if(isEndOptionEnabled()) {
				return String.format(OPTION_SCAN_END, getEndValue());
			}
			else {
				return "";
			}
		}
		
		public double getStepSize() {
			return stepSize;
		}
		public void setStepSize(double stepSize) {
			this.stepSize = stepSize;
			setStepOptionEnabled(true);
		}
		public boolean isStepOptionEnabled() {
			return stepOptionEnabled;
		}
		public void setStepOptionEnabled(boolean stepOptionEnabled) {
			this.stepOptionEnabled = stepOptionEnabled;
		}
		public String getStepOption() {
			if(isStepOptionEnabled()) {
				return String.format(OPTION_SCAN_STEP, getStepSize());
			}
			else {
				return "";
			}
		}
	}
	
	private class ProcessShutdownHook extends Thread {
		
		public void run() {	
			try {
				if(isRunning()) {
					log.info("Quitting data acquisition command.");
					sendQuitScanCmd();
				}
			
				sleep(500);
				
				if(isRunning()) {
					log.warn("Destroying data acquisition process.");
					process.destroy();
					process.waitFor();
				}
			}
			catch (InterruptedException e) {
				log.warn("Interrupted while waiting for process to exit.");
				return;
			}
		}
	}
	
	private class StandardOutputReaderThread extends Thread {
		
		public StandardOutputReaderThread() {
			setDaemon(true);
			start();
		}
		
		public void run() {
			
			Matcher matcher;
			
			while(true) {
				if(process != null) {
					try {
						HashMap<String,Object> valueMap = new HashMap<String,Object>();
						BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
						while(reader.ready()) {
							String line = reader.readLine();
							
							//log.info("STDOUT> " + line);
							if((matcher = PATTERN_SCAN_MODE.matcher(line)).matches()) {
								if(setScanMode(matcher.group(1))) {
									if(setScanState(nextScanState())) {			
										valueMap.put(VALUE_KEY_SCAN_STATE, getScanState().name());
										handleScanStateChange(valueMap);
									}
								}
							}
							
							if((matcher = PATTERN_SCAN_CMD.matcher(line)).matches()) {
								if(setScanCmd(matcher.group(1))) {
									if(setScanState(nextScanState())) {
										valueMap.put(VALUE_KEY_SCAN_STATE, getScanState().name());
										handleScanStateChange(valueMap);
									}
								}
							}
							
							if((matcher = scanCountPattern.matcher(line)).matches()) {
								if(setScanCount(Integer.parseInt(matcher.group(1)))) {
									valueMap.put(VALUE_KEY_COUNT, getCount());
								}
							}
							
							if((matcher = PATTERN_PROGRESS.matcher(line)).matches()) {
								if(setScanProgress(Double.parseDouble(matcher.group(1)))) {
									valueMap.put(VALUE_KEY_PROGRESS, getScanProgress());
								}
							}
						}
						if(!valueMap.isEmpty()) {
							publishEvent(new DeviceEvent(DeviceEventType.VALUE_CHANGE, id, valueMap, status, alarm));
						}
					}
					catch(IOException e) {
						log.warn("Input exception while reading STDOUT.");
					}
				}
				try {
					sleep(500);
				}
				catch(InterruptedException e) {
					return;
				}
			}
		}
	}
	
	private class ErrorOutputReaderThread extends Thread {
		
		public ErrorOutputReaderThread() {
			setDaemon(true);
			start();
		}
		
		public void run() {
			
			Matcher matcher;
			
			while(true) {
				if(process != null) {
					try {
						BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));			
						while(reader.ready()) {
							String line = reader.readLine();
							
							log.info("STDERR> " + line);
							if((matcher = PATTERN_SCAN_WARN.matcher(line)).matches()) {
								setScanWarn(matcher.group(1));
								log.info(getScanWarn());
							}
							
							if((matcher = PATTERN_SCAN_ERROR.matcher(line)).matches()) {
								setScanError(matcher.group(1));
								log.info(getScanError());
							}
						}
					}
					catch(IOException e) {
						log.warn("Input exception while reading STDERR.");
					}
				}
				try {
					sleep(500);
				}
				catch(InterruptedException e) {
					return;
				}
			}
		}
	}
}
