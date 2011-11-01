/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *      TempFileInputStream class.	     
 */
package ca.sciencestudio.util.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author maxweld
 *
 */
public class TempFileInputStream extends InputStream {

	private File tempFile;
	
	private FileInputStream fileInputStream;
	
	private boolean deleteOnClose;
	
	public TempFileInputStream(File tempFile, boolean deleteOnClose) throws FileNotFoundException {
		this.tempFile = tempFile;
		this.deleteOnClose = deleteOnClose;
		this.fileInputStream = new FileInputStream(tempFile);
	}

	public TempFileInputStream(TempFileOutputStream tempFileOutputStream, boolean deleteOnClose) throws FileNotFoundException {
		this(tempFileOutputStream.getTempFile(), deleteOnClose);
	}
	
	@Override
	public int read() throws IOException {
		return fileInputStream.read();
	}

	@Override
	public int read(byte[] b) throws IOException {
		return fileInputStream.read(b);
	}

	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		return fileInputStream.read(b, off, len);
	}

	@Override
	public long skip(long n) throws IOException {
		return fileInputStream.skip(n);
	}

	@Override
	public int available() throws IOException {
		return fileInputStream.available();
	}

	@Override
	public synchronized void mark(int readlimit) {
		fileInputStream.mark(readlimit);
	}

	@Override
	public synchronized void reset() throws IOException {
		fileInputStream.reset();
	}

	@Override
	public boolean markSupported() {
		return fileInputStream.markSupported();
	}

	@Override
	public void close() throws IOException {
		fileInputStream.close();
		if(deleteOnClose) {
			tempFile.delete();
			deleteOnClose = false;
		}
	}
}
