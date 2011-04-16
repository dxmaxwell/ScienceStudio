/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     DaoSecurityContextRepository class.
 *     
 */
package ca.sciencestudio.security.spring.web.context;

import java.util.Date;
import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpRequestResponseHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.util.WebUtils;

import ca.sciencestudio.login.model.LoginSession;
import ca.sciencestudio.login.model.dao.LoginSessionDAO;

/**
 * @author maxweld
 *
 */
public class DaoSecurityContextRepository implements SecurityContextRepository {

	public static final String DEFAULT_LOGIN_SESSION_COOKIE_NAME = "JSCIENCEID";
	public static final String DEFAULT_LOGIN_SESSION_COOKIE_PATH = "/";
	public static final int DEFAULT_LOGIN_SESSION_COOKIE_MAX_AGE = -1;
	
	private String cookieName = DEFAULT_LOGIN_SESSION_COOKIE_NAME;
	private String cookiePath = DEFAULT_LOGIN_SESSION_COOKIE_PATH;
	private int cookieMaxAge = DEFAULT_LOGIN_SESSION_COOKIE_MAX_AGE;
	
	private LoginSessionDAO loginSessionDAO;
	
	protected Log logger = LogFactory.getLog(getClass());
	
	@Override
	public boolean containsContext(HttpServletRequest request) {
		if(logger.isDebugEnabled()) {
			logger.debug("Contains security context: " + request.getRequestURL());
		}
		
		DaoSecurityContextRepositoryRequestWrapper req = buildRequestWrapper(request);
		LoginSession loginSession = loginSessionDAO.getLoginSessionById(req.getLoginSessionId());
		return (loginSession != null);
	}

	@Override
	public SecurityContext loadContext(HttpRequestResponseHolder requestResponseHolder) {
		if(logger.isDebugEnabled()) {
			logger.debug("Load security context: " + requestResponseHolder.getRequest().getRequestURL());
		}
		
		DaoSecurityContextRepositoryRequestWrapper request = 
				buildRequestWrapper(requestResponseHolder.getRequest());
		
		DaoSecurityContextRepositoryResponseWrapper response =
				buildResponseWrapper(request, requestResponseHolder.getResponse());
		
		requestResponseHolder.setRequest(request);
		requestResponseHolder.setResponse(response);
		
		if(logger.isDebugEnabled()) {
			logger.debug("Load security context from LoginSession with id: " + request.getLoginSessionId());
		}
		
		LoginSession loginSession = loginSessionDAO.getLoginSessionById(request.getLoginSessionId());
		if(loginSession == null) {
			if(logger.isDebugEnabled()) {
				logger.debug("LoginSession not found with id: " + request.getLoginSessionId() + ", creating empty context.");
			}
			request.setNewSecurityContext(true);
			return SecurityContextHolder.createEmptyContext();
		}
		
		Object securityContext = loginSession.getSession();
		if(!(securityContext instanceof SecurityContext)) {
			if(logger.isDebugEnabled()) {
				logger.debug("LoginSession does not contain a SecurityContext object, creating empty context.");
			}
			request.setNewSecurityContext(true);
			return SecurityContextHolder.createEmptyContext();
		}
		
		return (SecurityContext) securityContext;
	}
	
	@Override
	public void saveContext(SecurityContext securityContext, HttpServletRequest request, HttpServletResponse response) {
		 if(logger.isDebugEnabled()) {
			logger.debug("Save security context: " + request.getRequestURL());
		 }
		
		if(securityContext == null) {
			return;
		}
		
		if(securityContext.getAuthentication() instanceof AnonymousAuthenticationToken) {
			return;
		}
		
		DaoSecurityContextRepositoryRequestWrapper req;
		try {
			req = (DaoSecurityContextRepositoryRequestWrapper) request;
		}
		catch(ClassCastException e) {
			logger.warn("Save security context failed: request is not " + 
						DaoSecurityContextRepositoryRequestWrapper.class.getSimpleName());
			return;
		}

		DaoSecurityContextRepositoryResponseWrapper resp;
		try {
			resp = (DaoSecurityContextRepositoryResponseWrapper) response;
		}
		catch(ClassCastException e) {
			logger.warn("Save security context failed: response is not " + 
					DaoSecurityContextRepositoryResponseWrapper.class.getSimpleName());
			return;
		}
		
		String loginSessionId;
		LoginSession loginSession;
		
		if(req.isNewSecurityContext()) {
			loginSessionId = buildLoginSessionId();
			loginSession = new LoginSession();
			loginSession.setId(loginSessionId);
			loginSession.setTimestamp(new Date());
			loginSession.setSession(securityContext);
			loginSessionDAO.addLoginSession(loginSession);
			
			req.setNewSecurityContext(false);
			req.setLoginSessionId(loginSessionId);
			req.setLoginSessionIdFromParameter(false);
			resp.addCookie(buildCookie(loginSessionId));
			
			if(logger.isDebugEnabled()) {
				logger.debug("LoginSession cookie added to response with id: " + loginSessionId);
			}
		}
		else {
			loginSessionId = req.getLoginSessionId();
			loginSession = new LoginSession();
			loginSession.setId(loginSessionId);
			loginSession.setTimestamp(new Date());
			loginSession.setSession(securityContext);
			loginSessionDAO.editLoginSession(loginSession);
			
			if(req.isLoginSessionIdFromParameter()) {
				req.setLoginSessionIdFromParameter(false);
				resp.addCookie(buildCookie(loginSessionId));
				
				if(logger.isDebugEnabled()) {
					logger.debug("LoginSession cookie added to response with id: " + loginSessionId);
				}
			}
		}
	}
	
	protected DaoSecurityContextRepositoryResponseWrapper buildResponseWrapper(HttpServletRequest request, HttpServletResponse response) {
		return new DaoSecurityContextRepositoryResponseWrapper(request, response, this);
	}

	protected DaoSecurityContextRepositoryRequestWrapper buildRequestWrapper(HttpServletRequest request) {
		DaoSecurityContextRepositoryRequestWrapper req = 
				new DaoSecurityContextRepositoryRequestWrapper(request);
		
		String loginSessionId = getLoginSessionIdFromCookie(request);
		if(loginSessionId != null) {
			if(logger.isDebugEnabled()) {
				logger.debug("LoginSessionId found from cookie: " + loginSessionId);
			}
			req.setLoginSessionId(loginSessionId);
		}
		else {
			loginSessionId = getLoginSessionIdFromParamters(request);
			if(loginSessionId != null) {
				if(logger.isDebugEnabled()) {
					logger.debug("LoginSessionId found from parameters: " + loginSessionId);
				}
				req.setLoginSessionId(loginSessionId);
				req.setLoginSessionIdFromParameter(true);
			}	
		}

		return req;
	}
	
	protected String buildLoginSessionId() {
		return UUID.randomUUID().toString().toUpperCase();
	}
	
	protected Cookie buildCookie(String loginSessionId) {
		Cookie cookie = new Cookie(cookieName, loginSessionId);
		cookie.setMaxAge(cookieMaxAge);
		cookie.setPath(cookiePath);
		return cookie;
	}
	
	protected String getLoginSessionIdFromCookie(HttpServletRequest request) {
		Cookie cookie = WebUtils.getCookie(request, cookieName);
		if(cookie == null) {
			return null;
		} 
		
		return cookie.getValue(); 
	}
	
	protected String getLoginSessionIdFromParamters(HttpServletRequest request) {
		for(Object name : request.getParameterMap().keySet()) {
			if(cookieName.equalsIgnoreCase(name.toString())) {
				return request.getParameter(name.toString());
			}
		}
		
		return null;
	}
	
	public LoginSessionDAO getLoginSessionDAO() {
		return loginSessionDAO;
	}
	public void setLoginSessionDAO(LoginSessionDAO loginSessionDAO) {
		this.loginSessionDAO = loginSessionDAO;
	}

	public String getCookieName() {
		return cookieName;
	}
	public void setCookieName(String cookieName) {
		this.cookieName = cookieName;
	}

	public String getCookiePath() {
		return cookiePath;
	}
	public void setCookiePath(String cookiePath) {
		this.cookiePath = cookiePath;
	}

	public int getCookieMaxAge() {
		return cookieMaxAge;
	}
	public void setCookieMaxAge(int cookieMaxAge) {
		this.cookieMaxAge = cookieMaxAge;
	}
}
