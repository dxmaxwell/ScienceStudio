/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     ByteArrayHttpOutputMessage class.
 *     
 */
package ca.sciencestudio.util.http;

import java.io.IOException;
import java.io.OutputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpOutputMessage;

/**
 * @author maxweld
 *
 */
public class ByteArrayHttpOutputMessage implements HttpOutputMessage {

	private HttpHeaders httpHeaders;	
	private ByteArrayOutputStream outputStream;

	public ByteArrayHttpOutputMessage() {
		this(new HttpHeaders());
	}
	
	public ByteArrayHttpOutputMessage(HttpHeaders httpHeaders) {
		this.httpHeaders = httpHeaders;
		this.outputStream = new ByteArrayOutputStream();
	}
	
	public byte[] toByteArray() {
		return outputStream.toByteArray();
	}
	
	@Override
	public HttpHeaders getHeaders() {
		return httpHeaders;
	}

	@Override
	public OutputStream getBody() throws IOException {
		return new BufferedOutputStream(outputStream);
	}
}
