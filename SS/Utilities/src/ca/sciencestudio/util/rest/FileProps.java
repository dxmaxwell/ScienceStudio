/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *      FileProps class.	     
 */
package ca.sciencestudio.util.rest;

import java.io.File;

public class FileProps implements Comparable<FileProps> {

	public enum Type {
		FILE, DIRECTORY
	}
	
	private String path;
	private long length;
	private Type type;
	
	public FileProps() {
		this("", 0L, Type.FILE);
	}
	
	public FileProps(String path, long length, Type type) {
		this.type = type;
		this.path = path;
		this.length = length;
	}
	
	public FileProps(File file) {
		if(file.isDirectory()) {
			type = Type.DIRECTORY;
		} else if(file.isFile()) {
			type = Type.FILE;
		} else {
			throw new IllegalArgumentException("File is not a file or a directory.");
		}
		path = file.getPath();
		length = file.length();
	}
	
	public FileProps(File file, File relative) {
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

	@Override
	public int compareTo(FileProps o) {
		return path.compareToIgnoreCase(o.getPath());
	}
	
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}

	public long getLength() {
		return length;
	}
	public void setLength(long length) {
		this.length = length;
	}

	public Type getType() {
		return type;
	}
	public void setType(Type type) {
		this.type = type;
	}
}
