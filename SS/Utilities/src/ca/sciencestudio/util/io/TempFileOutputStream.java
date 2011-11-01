/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *      TempFileOutputStream class.	     
 */
package ca.sciencestudio.util.io;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author maxweld
 * 
 *
 */
public class TempFileOutputStream extends OutputStream {
	
	private File tempFile;
	
	private FileOutputStream fileOutputStream;
	
	public TempFileOutputStream(String prefix) throws IOException {
		this(prefix, "");
	}
	
	public TempFileOutputStream(String prefix, String suffix) throws IOException {
		tempFile = File.createTempFile(prefix, suffix);
		fileOutputStream = new FileOutputStream(tempFile);
	}

	public TempFileOutputStream(String prefix, String suffix, File directory) throws IOException {
		tempFile = File.createTempFile(prefix, suffix, directory);
		fileOutputStream = new FileOutputStream(tempFile);
	}

	@Override
	public void write(int b) throws IOException {
		fileOutputStream.write(b);
	}

	@Override
	public void write(byte[] b) throws IOException {
		fileOutputStream.write(b);
	}

	@Override
	public void write(byte[] b, int off, int len) throws IOException {
		fileOutputStream.write(b, off, len);
	}

	@Override
	public void flush() throws IOException {
		fileOutputStream.flush();
	}

	@Override
	public void close() throws IOException {
		fileOutputStream.close();
	}
	
	public File getTempFile() {
		return tempFile;
	}
}
