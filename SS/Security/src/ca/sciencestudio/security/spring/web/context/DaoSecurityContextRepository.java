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
		
		return !buildRequestWrapper(request).isNewLoginSesion();
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
		
		if(request.isNewLoginSesion()) {
			return SecurityContextHolder.createEmptyContext();
		}
		
		Object securityContext = request.getLoginSession().getSessionData();
		if(!(securityContext instanceof SecurityContext)) {
			if(logger.isDebugEnabled()) {
				logger.debug("LoginSession does not contain a SecurityContext object, creating empty context.");
			}
			request.setLoginSession(null);
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
		
		LoginSession loginSession;
		
		if(req.isNewLoginSesion()) {
			String loginSessionUuid = buildLoginSessionUuid();
			loginSession = new LoginSession();
			loginSession.setSessionUuid(loginSessionUuid);
			loginSession.setSessionData(securityContext);
			loginSession.setTimestamp(new Date());
			loginSessionDAO.add(loginSession);
			
			req.setLoginSession(loginSession);
			req.setLoginSessionUuidFromParameter(false);
			resp.addCookie(buildCookie(loginSessionUuid));
			
			if(logger.isDebugEnabled()) {
				logger.debug("LoginSession cookie added to response with uuid: " + loginSessionUuid);
			}
		}
		else {
			loginSession = req.getLoginSession();
			loginSession.setSessionData(securityContext);
			loginSession.setTimestamp(new Date());
			loginSessionDAO.edit(loginSession);
			
			if(req.isLoginSessionUuidFromParameter()) {
				String loginSessionUuid = loginSession.getSessionUuid();
				resp.addCookie(buildCookie(loginSession.getSessionUuid()));
				req.setLoginSessionUuidFromParameter(false);
				
				if(logger.isDebugEnabled()) {
					logger.debug("LoginSession cookie added to response with uuid: " + loginSessionUuid);
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
		
		String loginSessionUuid = getLoginSessionUuidFromCookie(request);
		if(loginSessionUuid != null) {
			if(logger.isDebugEnabled()) {
				logger.debug("LoginSessionUuid found from cookie: " + loginSessionUuid);
			}
		}		
		else {
			loginSessionUuid = getLoginSessionUuidFromParamters(request);
			if(loginSessionUuid != null) {
				if(logger.isDebugEnabled()) {
					logger.debug("LoginSessionUuid found from parameters: " + loginSessionUuid);
				}
				req.setLoginSessionUuidFromParameter(true);
			}	
		}

		if(loginSessionUuid != null) {
			if(logger.isDebugEnabled()) {
				logger.debug("Load security context from LoginSession with uuid: " + loginSessionUuid);
			}
			
			LoginSession loginSession = loginSessionDAO.getByUuid(loginSessionUuid);
			if(loginSession != null) {
				req.setLoginSession(loginSession);
			}
			else if(logger.isDebugEnabled()) {
				logger.debug("LoginSession not found with uuid: " + loginSessionUuid + ", creating empty context.");
			}
		}
		
		return req;
	}
	
	protected String buildLoginSessionUuid() {
		return UUID.randomUUID().toString().toUpperCase();
	}
	
	protected Cookie buildCookie(String loginSessionUuid) {
		Cookie cookie = new Cookie(cookieName, loginSessionUuid);
		cookie.setMaxAge(cookieMaxAge);
		cookie.setPath(cookiePath);
		return cookie;
	}
	
	protected String getLoginSessionUuidFromCookie(HttpServletRequest request) {
		Cookie cookie = WebUtils.getCookie(request, cookieName);
		if(cookie == null) {
			return null;
		} 
		
		return cookie.getValue(); 
	}
	
	protected String getLoginSessionUuidFromParamters(HttpServletRequest request) {
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
