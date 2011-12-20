/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     UsageServlet class.
 *     
 */
package ca.lightsource.sciencestudio.transfer;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.http.HttpHeaders;
import org.eclipse.jetty.server.Dispatcher;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.util.URIUtil;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.resource.Resource;

/**
 * GET /_all_scans : a list of all the scans in JSON
 *
 * @author Dong Liu
 *
 */
public class UsageServlet extends HttpServlet {

    private ServletContext _servletContext;
    private ContextHandler _contextHandler;

    private Resource _resourceBase;
    private String _cacheControl;

    private String _serviceName;
    private String _host;

    private String _newline = System.getProperty("line.separator");

    public void init() throws UnavailableException {

        _servletContext = getServletContext();
        ContextHandler.Context scontext = ContextHandler.getCurrentContext();
        if (scontext == null)
            _contextHandler = ((ContextHandler.Context) _servletContext).getContextHandler();
        else
            _contextHandler = ContextHandler.getCurrentContext().getContextHandler();

        String rb = getInitParameter("resourceBase");

        if (rb != null) {
            try {
                _resourceBase = _contextHandler.newResource(rb);
            } catch (Exception e) {
                Log.warn(Log.EXCEPTION, e);
                throw new UnavailableException(e.toString());
            }
        }

        String t = getInitParameter("cacheControl");
        if (t != null)
            _cacheControl = t;

        String name = getInitParameter("name");
        if (name != null)
            _serviceName = name;

        String host = getInitParameter("host");
        if (host != null)
            _host = host;

        if (Log.isDebugEnabled())
            Log.debug("resource base = " + _resourceBase);

    }

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        String servletPath = null;
        String pathInfo = null;
        Boolean included = request.getAttribute(Dispatcher.INCLUDE_REQUEST_URI) != null;
        if (included != null && included.booleanValue()) {
            servletPath = (String) request.getAttribute(Dispatcher.INCLUDE_SERVLET_PATH);
            pathInfo = (String) request.getAttribute(Dispatcher.INCLUDE_PATH_INFO);
            if (servletPath == null) {
                servletPath = request.getServletPath();
                pathInfo = request.getPathInfo();
            }
        } else {
            included = Boolean.FALSE;
            servletPath = request.getServletPath();
            pathInfo = request.getPathInfo();
        }

        String pathInContext = URIUtil.addPaths(servletPath, pathInfo);

        if (pathInContext.endsWith("_usage")) {
            response.setContentType("text/plain; charset=UTF-8");
            String usage = "This is the service for science studio XRD scans. You can try /_scans to get all the scans. \n" + "If you know a scanID, try /_scans/scanID for more information. " + _newline;
            byte[] data = usage.getBytes("UTF-8");
            response.setContentLength(data.length);
            if (_cacheControl != null)
                response.addHeader(HttpHeaders.CACHE_CONTROL, _cacheControl);
            response.getOutputStream().write(data);
            return;
        }

        if (pathInContext.endsWith("_echo")) {
            response.setContentType("application/json; charset=UTF-8");
            String echo = "{\"" + _serviceName + "_time\":" + System.currentTimeMillis() + "," + "\"host\": \"" + _host + "\"}" + _newline;
            byte[] data = echo.getBytes("UTF-8");
            response.setContentLength(data.length);
            if (_cacheControl != null)
                response.addHeader(HttpHeaders.CACHE_CONTROL, _cacheControl); //
            response.getOutputStream().write(data);
            return;

        }

    }
}
