/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     ByteArrayHttpOutputMessage class.
 *     
 */
package ca.sciencestudio.util.tags;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpOutputMessage;

/**
 * @author maxweld
 *
 */
public class ByteArrayHttpOutputMessage implements HttpOutputMessage {

	private HttpHeaders httpHeaders = new HttpHeaders();
	
	private ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	
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
