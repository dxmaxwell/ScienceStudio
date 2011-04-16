/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     PeekableBufferedReader class.
 *     
 */
package ca.sciencestudio.util.text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

/**
 * @author maxweld
 *
 */
public class PeekableBufferedReader extends BufferedReader {

	public static final int DEFAULT_BUFFER_SIZE = 8192; // Java default for BufferedReader //
	public static final int DEFAULT_READ_AHEAD_LIMIT = 4096;
	
	private int readAheadLimit;
	
	public PeekableBufferedReader(Reader in) {
		this(in, DEFAULT_READ_AHEAD_LIMIT, DEFAULT_BUFFER_SIZE);
	}

	public PeekableBufferedReader(Reader in, int readAheadLimit) {
		this(in, readAheadLimit, DEFAULT_BUFFER_SIZE);
	}
	
	public PeekableBufferedReader(Reader in, int readAheadLimit, int bufferSize) {
		super(in, bufferSize);
		if(readAheadLimit > 0) {
			this.readAheadLimit = readAheadLimit;
		}
		else {
			throw new IllegalArgumentException("Read ahead limit must be great than zero.");
		}
	}

	public String peekLine() throws IOException {
		super.mark(readAheadLimit);
		String line = readLine();
		super.reset();
		return line;
	}
	
	@Override
	public boolean markSupported() {
		return false;
	}
		
	@Override
	public void mark(int readAheadLimit) throws IOException {
		throw new IOException("Mark operation not supported by reader.");
	}

	@Override
	public void reset() throws IOException {
		throw new IOException("Reset operation not supported by reader.");
	}

	public int getReadAheadLimit() {
		return readAheadLimit;
	}
	public void setReadAheadLimit(int readAheadLimit) {
		this.readAheadLimit = readAheadLimit;
	}
}
