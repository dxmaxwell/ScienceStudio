/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *      FileProperties class.	     
 */
package ca.sciencestudio.util.io;

import java.io.File;

/**
 * @author maxweld
 *
 */
public class FileProperties implements Comparable<FileProperties> {

	private static final String FILE_SIZE_FORMAT = "%.1f%s";
	private static final double FILE_SIZE_MULTIPLIER = 1000.0;
	private static final String[] FILE_SIZE_SUFFIXES = { "", "k", "M", "G", "T", "P", "E", "Z", "Y"};
	
	private String name;
	private String path;
	private String size;
	private long length;
	private boolean directory;
	
	public FileProperties(File file) {
		directory = file.isDirectory();
		if(file.isFile() || directory) {
			name = file.getName();
			path = file.getPath();
			length = file.length();	
			size = buildFileSize(length);
		} else {
			throw new IllegalArgumentException("File is not a file or a directory.");
		}
	}
	
	public FileProperties(File file, File relative) {
		this(file);
		String filePath = file.getAbsolutePath();
		String relativePath = relative.getAbsolutePath();
		if(filePath.startsWith(relativePath)) {
			if(filePath.length() == relativePath.length()) {
				path = File.separator;
			} else {
				path = filePath.substring(relativePath.length());
			}
		} else {
			throw new IllegalArgumentException("File is not below relative directory.");
		}
	}
	
	protected String buildFileSize(long length) {
		double size = length;
		String suffix = FILE_SIZE_SUFFIXES[0];
		for(int idx=0; idx<FILE_SIZE_SUFFIXES.length; idx++) {
			if(size <= FILE_SIZE_MULTIPLIER) {
				suffix = FILE_SIZE_SUFFIXES[idx];
				break;
			} 
			size /= FILE_SIZE_MULTIPLIER;
		}
		return String.format(FILE_SIZE_FORMAT, size, suffix); 
	}
	
	public int compareTo(FileProperties fileProperties) {
		return path.compareToIgnoreCase(fileProperties.getPath());
	}
	
	public String getName() {
		return name;
	}

	public String getPath() {
		return path;
	}

	public String getSize() {
		return size;
	}

	public long getLength() {
		return length;
	}

	public boolean isDirectory() {
		return directory;
	}
}
