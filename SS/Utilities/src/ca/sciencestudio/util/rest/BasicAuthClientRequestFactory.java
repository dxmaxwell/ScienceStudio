/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     BasicAuthClientRequestFactory class.
 *     
 */
package ca.sciencestudio.util.rest;

import java.io.IOException;
import java.net.URI;

import org.apache.commons.codec.binary.Base64;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;

/**
 * @author maxweld
 * 
 *
 */
public class BasicAuthClientRequestFactory implements ClientHttpRequestFactory {

	private static final String BASIC_AUTHENTICATION_HEADER_NAME = "Authorization";
	private static final String BASIC_AUTHENTICATION_HEADER_PREFIX = "Basic "; 
	private static final String USERNAME_PASSWORD_SEPARATOR = ":";
	
	private String username = "";
	
	private String password = "";
	
	private ClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
	
	@Override
	public ClientHttpRequest createRequest(URI uri, HttpMethod httpMethod) throws IOException {
		
		ClientHttpRequest request = requestFactory.createRequest(uri, httpMethod);
	
		if(username == null) {
			throw new IOException("Username is null. Check request factory configuration.");
		}
		
		if(password == null) {	
			throw new IOException("Password is null. Check request factory configuration.");
		}
		
		String up = username + USERNAME_PASSWORD_SEPARATOR + password;
		String upBase64 = Base64.encodeBase64String(up.getBytes());
		
		// NOTE: the behaviour of "encodeBase64String" has changed between 
		// commons-codec v1.4 and v1.5. (Check the documentation for details.)
		// When commons-codec v1.5 is used than the 'trim' will not be required.
		String authentication = BASIC_AUTHENTICATION_HEADER_PREFIX + upBase64.trim();
		request.getHeaders().set(BASIC_AUTHENTICATION_HEADER_NAME, authentication);
		
		return request;
	}

	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	public ClientHttpRequestFactory getRequestFactory() {
		return requestFactory;
	}
	public void setRequestFactory(ClientHttpRequestFactory requestFactory) {
		this.requestFactory = requestFactory;
	}
}
