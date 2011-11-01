/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     ByteArrayHttpInputMessage class.
 *     
 */
package ca.sciencestudio.util.http;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;

/**
 * @author maxweld
 * 
 *
 */
public class ByteArrayHttpInputMessage implements HttpInputMessage {

	private HttpHeaders httpHeaders = new HttpHeaders();
	
	private ByteArrayInputStream inputStream;
	
	public ByteArrayHttpInputMessage(byte[] data) {
		this(data, new HttpHeaders());
	}
	
	public ByteArrayHttpInputMessage(byte[] data, HttpHeaders httpHeaders) {
		this.httpHeaders = httpHeaders;
		this.inputStream = new ByteArrayInputStream(data);
	}
	
	@Override
	public HttpHeaders getHeaders() {
		return httpHeaders;
	}

	@Override
	public InputStream getBody() throws IOException {
		return inputStream;
	}
}
