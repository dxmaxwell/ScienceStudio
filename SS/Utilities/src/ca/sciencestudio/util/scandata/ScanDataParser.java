/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     ScanDataParser class.
 *     
 */
package ca.sciencestudio.util.scandata;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author medrand
 *
 */
public class ScanDataParser {
	private BufferedReader dataFileReader;
	private BufferedReader spectrumFileReader;
	private PrintWriter printWriter;
	private String dirPath;
	private String dataFilename;
	private String spectrumFilename;
	private Map<Integer, ScanEvent> scanEventMap;
	private String spectrumName;
	
	
	public ScanDataParser(String dirPath, String dataFilename,
			String spectrumFilename, String spectrumName) {
		
		this.dirPath = dirPath;
		this.dataFilename = dataFilename;
		this.spectrumFilename = spectrumFilename;
		this.spectrumName = spectrumName;
		this.scanEventMap = getScanEventMap();
	}
	
	public void parserData() throws Exception {
		String line = null;
		int counter = 1;
		
		Set<Integer> keys = scanEventMap.keySet();
		
		for(Integer key : keys) {
			
			ScanEvent se = scanEventMap.get(key);
			
			openDataFile();
			
			while((line = dataFileReader.readLine()) != null) {
				if(line.startsWith("#")) {
					continue;
				}
				line = line.replace(",", "");
				String[] tokens = line.split(" ");
				int eventId = Integer.parseInt(tokens[0]);
				
				if(eventId == se.getId() && se.isSpectrum()) {
					
					StringBuffer buf = new StringBuffer();
					int idx = 1;
					
					String file = dirPath + getOutputFilename(dataFilename, counter++);
					
					openOutputFile(file);
					
					printWriter.println("<scanPoint>");
					
					for(String name : se.getColumns()) {
						String newName = name.replace(":", "_");
						buf.append("\t<" + newName + ">");
						
						if(name.equals(spectrumName)) {
							buf.append(getSpectrum(Integer.parseInt(tokens[idx])));
						} else {
							buf.append(tokens[idx]);
						}
						
						buf.append("</" + newName + ">\n");
						 
						idx++;
					}
					printWriter.print(buf.toString());
					printWriter.println("</scanPoint>");
					
					closeOutputFile();
				}
			}
		}
	}
	
	private void openDataFile() throws Exception {
		dataFileReader = new BufferedReader(new FileReader(dirPath + dataFilename));
	}
	
	private void openSpectrumFile() throws Exception {
		spectrumFileReader = new BufferedReader(new FileReader(dirPath + spectrumFilename));
	}
	
	private void openOutputFile(String filename) throws Exception {
		printWriter = new PrintWriter(new FileWriter(filename));
	}
	
	private void closeOutputFile() throws Exception {
		printWriter.flush();
		printWriter.close();
	}
	
	private ScanEvent getScanEvent(String str) {
		ScanEvent event = null;
		
		if(!str.startsWith("#(")) {
			return event;
		}
		
		event = new ScanEvent();
		String[] tokens = str.split(" ");
		tokens[0] = tokens[0].replace("#", "");
		tokens[0] = tokens[0].replace("(", "");
		tokens[0] = tokens[0].replace(")", "");
		event.setId(Integer.parseInt(tokens[0]));
		
		List<String> columns = new ArrayList<String>();
		for(int i = 2; i < tokens.length; i++) {
			columns.add(tokens[i]);
		}
		if(columns.contains(spectrumName)) {
			event.setSpectrum(true);
		}
		event.setColumns(columns);
		return event;
	}
	
	private Map<Integer, ScanEvent> getScanEventMap() {
		Map<Integer, ScanEvent> scanEventMap = new HashMap<Integer, ScanEvent>();
		String line = null;
		
		try {
			openDataFile();
			
			while((line = dataFileReader.readLine()) != null) {
				ScanEvent event = getScanEvent(line);
				if(event != null && !scanEventMap.containsKey(event.getId())) {
					scanEventMap.put(event.getId(), event);
				}
			}
		} catch(Exception e) {
			System.err.println(e.getMessage());
			return null;
		}
		return scanEventMap;
	}
	
	private String getOutputFilename(String originalFilename, int count) {
		String filename = null;
		if(count < 10) {
			filename = "scanPoint_0" + count + ".xml"; 
		} else {
			filename = "scanPoint_" + count + ".xml";
		}
		return filename;
	}
	
	private String getSpectrum(int startPos) throws Exception {
		StringBuffer buf = new StringBuffer();
		int intChar = 0;
		
		openSpectrumFile();
		
		spectrumFileReader.skip(startPos);
		intChar = spectrumFileReader.read();
		
		while((intChar != ((int) '\n')) && (intChar != -1)) {
			buf.append((char) intChar);
			intChar = spectrumFileReader.read();
		}
		String data = buf.toString();
		data = data.replace(",", "");
		
		return data;
	}
	
	private class ScanEvent {
		private int id;
		private boolean spectrum;
		private List<String> columns;
		
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		public boolean isSpectrum() {
			return spectrum;
		}
		public void setSpectrum(boolean spectrum) {
			this.spectrum = spectrum;
		}
		public List<String> getColumns() {
			return columns;
		}
		public void setColumns(List<String> columns) {
			this.columns = columns;
		}
		
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ScanDataParser parser = new ScanDataParser("/Users/medrand/scanData/","scanData.001.dat",
				"scanData.001_spectra.dat", "IOC1607-004:mca1");
		try {
			parser.parserData();
		} catch(Exception e) {
			System.err.println(e.getMessage());
			System.exit(1);
		}
	}
}
