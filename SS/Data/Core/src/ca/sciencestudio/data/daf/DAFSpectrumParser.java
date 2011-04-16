/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     AcqDatSpectraParser class.
 *     
 */
package ca.sciencestudio.data.daf;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ca.sciencestudio.data.daf.DAFSpectrumRecord;
import ca.sciencestudio.data.support.DataFormatException;

/**
 * @author maxweld
 *
 */
public class DAFSpectrumParser {

	private static final String RANDOM_ACCESS_FILE_MODE = "r";
	private static final int PARSE_BYTE_BUFFER_LENGTH = 8192;
	
	private static final String SPECTRUM_VALUE_SEPARATOR = ",";
	private static final String SPECTRUM_RECORD_SEPARATOR = "\n";
	private static final Pattern SPECTRUM_VALUE_PATTERN = Pattern.compile("\\s*([^,]+)\\s*[,]?");
	
	private RandomAccessFile spectraFile;
	
	public DAFSpectrumParser(String spectraFilePath) throws DataFormatException, IOException {
		this(new RandomAccessFile(spectraFilePath, RANDOM_ACCESS_FILE_MODE));
	}
	
	public DAFSpectrumParser(File spectraFile) throws DataFormatException, IOException {
		this(new RandomAccessFile(spectraFile, RANDOM_ACCESS_FILE_MODE));
	}
	
	public DAFSpectrumParser(RandomAccessFile spectraFile) {
		this.spectraFile = spectraFile; 
	}
	
	public DAFSpectrumRecord parseRecord(long offset, int nValues) throws DataFormatException, IOException {
		
		spectraFile.seek(offset);
		
		int readLength = 0;
		boolean last = false;
		Matcher matcher = null;
		String matcherInput = "";
		StringBuilder stringBuffer = new StringBuilder();
		List<String> values = new ArrayList<String>(nValues);
		byte[] byteBuffer = new byte[PARSE_BYTE_BUFFER_LENGTH];
		
		while(values.size() < nValues) {
			
			if(last) {
				throw new DataFormatException("End of line before values (" + nValues + ") could be parsed.");
			}
			
			readLength = spectraFile.read(byteBuffer);
			
			if(readLength == -1) {
				throw new DataFormatException("End of file before values (" + nValues + ") could be parsed.");
			}
			
			stringBuffer.append(new String(byteBuffer, 0, readLength));
			
			if(readLength < byteBuffer.length) {
				matcherInput = stringBuffer.toString();
				last = true;
			}
			
			int recSepIdx = stringBuffer.indexOf(SPECTRUM_RECORD_SEPARATOR);
			if(recSepIdx >= 0) {
				matcherInput = stringBuffer.substring(0, recSepIdx);
				last = true;
			}
			
			if(!last) {
				int valSepIdx = stringBuffer.lastIndexOf(SPECTRUM_VALUE_SEPARATOR);
				if(valSepIdx >= 0) {
					matcherInput = stringBuffer.substring(0, valSepIdx);
					stringBuffer.delete(0, valSepIdx);
				}
				else {
					matcherInput = stringBuffer.toString();
					stringBuffer.delete(0, stringBuffer.length());
				}
			}
				
			matcher = SPECTRUM_VALUE_PATTERN.matcher(matcherInput);
			while(matcher.find() && (values.size() < nValues)) {
				values.add(matcher.group(1));
			}
		}
		
		return new DAFSpectrumRecord(offset, values);
	}
}
