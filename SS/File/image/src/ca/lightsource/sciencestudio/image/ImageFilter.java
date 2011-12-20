/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     ImageFilter class.
 *     
 */
package ca.lightsource.sciencestudio.image;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.continuation.Continuation;
import org.eclipse.jetty.continuation.ContinuationSupport;
import org.eclipse.jetty.http.HttpMethods;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.util.URIUtil;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.resource.Resource;


/**
 * This filter performs the following tasks: - responding to non-GET request directly (add HEAD later?) - responding to
 * the requests not for png (add spe or other format later?) - generating png for an spe image if it is not there
 *
 * Specially, a Continuation HashMap is used to maintain the png files that are being generated when requested. Only the
 * first request will trigger a conversion, and others all are suspended by the Continuation and later be resumed when
 * the conversion is finished.
 *
 * @author Dong Liu
 *
 */
public class ImageFilter implements Filter {

	final static String _DEFAULT_BASE = ".";
	final static int __DEFAULT_WAIT_MS = 10000;

	final static String RESOURCE_BASE_INIT_PARAM = "resourceBase";
	final static String WAIT_INIT_PARAM = "waitMs";
	final static String ImageFilter = "ImageFiler";

	private ServletContext _context;
	private ContextHandler _contextHandler;
	private ConcurrentHashMap<String, List<Continuation>> converting = new ConcurrentHashMap<String, List<Continuation>>();
	private Resource _resourceBase;
	private long _timeoutMs;

	public void init(FilterConfig filterConfig) throws ServletException {
		_context = filterConfig.getServletContext();
		ContextHandler.Context scontext = ContextHandler.getCurrentContext();
		if (scontext == null)
			_contextHandler = ((ContextHandler.Context) _context).getContextHandler();
		else
			_contextHandler = ContextHandler.getCurrentContext().getContextHandler();

		long wait = __DEFAULT_WAIT_MS;
		if (filterConfig.getInitParameter(WAIT_INIT_PARAM) != null)
			wait = Integer.parseInt(filterConfig.getInitParameter(WAIT_INIT_PARAM));
		_timeoutMs = wait;

		String baseParam = filterConfig.getInitParameter(RESOURCE_BASE_INIT_PARAM);
		if (baseParam != null) {
			setBaseResource(baseParam);
		} else {
			setBaseResource(_DEFAULT_BASE);
		}

	}

	private void setBaseResource(Resource base) {
		_resourceBase = base;
	}

	/**
	 * @param resourceBase
	 *            The base resource as a string.
	 */
	private void setBaseResource(String resourceBase) {
		try {
			setBaseResource(Resource.newResource(resourceBase));
		} catch (Exception e) {
			Log.warn(e.toString());
			Log.debug(e);
			throw new IllegalArgumentException(resourceBase);
		}
	}

	public Resource getResource(String pathInContext) {
		Resource r = null;
		try {
			if (_resourceBase != null)
				r = _resourceBase.addPath(pathInContext);
			else {
				URL u = _context.getResource(pathInContext);
				r = _contextHandler.newResource(u);
			}

			if (Log.isDebugEnabled())
				Log.debug("RESOURCE " + pathInContext + "=" + r);
		} catch (IOException e) {
			Log.ignore(e);
		}
		return r;
	}

	public Resource getResource(HttpServletRequest request) {

		return null;

	}

	public void destroy() {

	}

	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException,
			ServletException {

		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;

		if (!HttpMethods.GET.equals(request.getMethod())) {
			response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
		}

		String servletPath = request.getServletPath();
		String pathInfo = request.getPathInfo();
		String pathInContext = URIUtil.addPaths(servletPath, pathInfo);

		// TODO add authentication later if needed. Or do it as the way used to be in XRF

		if (pathInContext.endsWith(".png")) {
			// check if it is a fresh request and there is a Continuation for it
			if (request.getAttribute(ImageFilter) == null) {
				// fresh request
				if (converting.contains(pathInContext)) {
					// in the map
					request.setAttribute(ImageFilter, Boolean.FALSE);
					Continuation destContinuation = ContinuationSupport.getContinuation(request);
					destContinuation.setTimeout(_timeoutMs);
					destContinuation.suspend();
					// Log.info(request.hashCode() + " suspended");
					boolean add = converting.get(pathInContext).add(destContinuation);
					if (!add) {
						// something wrong give up
						Log.warn("cannot add continuation, give up");
						chain.doFilter(request, response);
					}
					return;
				} else {
					// not in the map
					Resource resource = getResource(pathInContext);

					if (resource == null) {
						response.sendError(HttpServletResponse.SC_NOT_FOUND);
					}

					if (!resource.exists()) {
						File dest = resource.getFile();
						Resource origResource = getResource(pathInContext.replaceAll("[.][a-z]{3,4}$", ".spe"));
						if (!origResource.exists()) {
							response.sendError(HttpServletResponse.SC_NOT_FOUND);
						} else {
							// TODO verify if the spe file is finished here?

							List continuationList = Collections.synchronizedList(new ArrayList<Continuation>());
							if (converting.putIfAbsent(pathInContext, continuationList) == null) {
								// do the conversion and return
								Converter.convert(origResource.getFile().getPath(), "png", dest);

								// resume all the continuations in the list
								Iterator<Continuation> continuations = continuationList.iterator();
								for (; continuations.hasNext();) {
									Continuation each = continuations.next();
									each.setAttribute(ImageFilter, Boolean.TRUE);
									each.resume();
									Log.info(request.hashCode() + " resume");
								}
								if (converting.remove(pathInContext) == null) {
									Log.warn(dest.toString() + " cannot be found in the map. ");
								}
								chain.doFilter(request, response);
							} else {
								request.setAttribute(ImageFilter, Boolean.FALSE);
								Continuation destContinuation = ContinuationSupport.getContinuation(request);
								destContinuation.setTimeout(_timeoutMs);
								destContinuation.suspend();
								// Log.info(request.hashCode() + " suspended");
								boolean add = converting.get(pathInContext).add(destContinuation);
								if (!add) {
									// something wrong give up
									Log.warn("cannot add continuation, give up");
									chain.doFilter(request, response);
								}
								return;
							}

						}
					} else {
						chain.doFilter(request, response);
					}
				}

			} else {
				// redispatched request
				Boolean finished = (Boolean) request.getAttribute(ImageFilter);

				if (finished.booleanValue()) {
					chain.doFilter(request, response);
				} else {
					// inform the client, it is timeout.
					Log.warn("A time out?");
					response.sendError(HttpServletResponse.SC_REQUEST_TIMEOUT, "Converting takes too long.");
				}

			}
		} else {
			chain.doFilter(request, response);
		}

	}

}
