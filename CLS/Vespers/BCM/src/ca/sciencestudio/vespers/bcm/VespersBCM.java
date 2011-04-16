/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 * 		VespersBCM main class.
 *     
 */
package ca.sciencestudio.vespers.bcm;

import java.io.PrintWriter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.Parser;
import org.apache.commons.cli.PosixParser;
import org.apache.commons.cli.Options;

import org.springframework.beans.BeansException;

import ca.sciencestudio.device.control.component.epics.EpicsDevice;
import ca.sciencestudio.device.control.factory.DeviceFactory;
import ca.sciencestudio.device.control.factory.spring.SpringDeviceFactoryImpl;

/**
 * @author maxweld
 *
 */
public class VespersBCM {
	
	private static final String COMMAND_LINE_SYNTAX = 
		"java -jar VespersBCM.jar [ OPTIONS ] path/to/config/file.xml";
	
	protected static final Log log = LogFactory.getLog(VespersBCM.class);
	
	protected static final Options options = new Options(); 
	
	static {
		EpicsDevice.PENDIO_MAX_RETRY = 4;
		EpicsDevice.PENDIO_TIMEOUT_SECONDS = 30.0;
		options.addOption("h", "help", false, "Display this Help Message");
		options.addOption("w", true, "Wait for <arg> Seconds for Device Initialization");
	}
	
	public static void main(String[] args) {
		
		log.info("Set Epics Device Timeout: " + EpicsDevice.PENDIO_TIMEOUT_SECONDS);
		
		CommandLine cmdLine;
		
		try {
			Parser parser = new PosixParser();
			cmdLine = parser.parse(options, args);
		}
		catch(ParseException e) {
			printUsageWithMessage(e.getMessage());
			return;
		}
		
		if(cmdLine.hasOption("h")) {
			printUsage();
			return;
		}
		
		if(cmdLine.getArgs().length < 1) {
			printUsageWithMessage("Configuration File MUST be Specified.");
			return;
		}
		
		if(args.length < 1) {
			printUsageWithMessage("Wrong number of arguments.  A configuration file must be specified.");
			return;
		}
		
		try {
			log.info("Attempting to load XML Application Context: " + cmdLine.getArgs()[0]);
			SpringDeviceFactoryImpl.loadXmlApplicationContext(cmdLine.getArgs()[0]);
		}
		catch(BeansException e) {
			log.error("Error occurred while loading XML Application Context. Aborting.");
			log.error(e.getMessage());
			return;
		}
		
		if(cmdLine.hasOption("w")) {
			try {
				long delay = Long.parseLong(cmdLine.getOptionValue("w"));
				log.info("Waiting for devices to initialize (" + delay + " seconds).");			
				Thread.sleep(delay * 1000L);
			}
			catch (NumberFormatException e) {
				log.warn("Wait time is not a valid number!");
			}
			catch (InterruptedException e) {
				log.warn("Deivce initialization interrupted!");
			}
		}
		
		try {
			DeviceFactory.publishDeviceValues();
		}
		catch(Exception e) {
			log.warn("Exception while publishing device values. Aborting.", e);
			System.exit(1);
		}

	}
	
	protected static void printUsageWithMessage(String message) {
		System.err.println(message);
		printUsage();
	}
	
	protected static void printUsage() {
		HelpFormatter formatter = new HelpFormatter();		
		PrintWriter helpPrintWriter = new PrintWriter(System.err, true);
		formatter.printHelp(helpPrintWriter, HelpFormatter.DEFAULT_WIDTH, COMMAND_LINE_SYNTAX,
				null, options, HelpFormatter.DEFAULT_LEFT_PAD, HelpFormatter.DEFAULT_DESC_PAD, null);
	}
}
