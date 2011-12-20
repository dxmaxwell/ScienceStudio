/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *    ImageService class.
 *     
 */
package ca.lightsource.sciencestudio.image;


import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.NCSARequestLog;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.handler.RequestLogHandler;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.FilterMapping;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.RolloverFileOutputStream;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.thread.QueuedThreadPool;


/**
 * 	This service starts an embedded jetty server to serve the request for images
 *  in a resource base. A handler will first check if the resource exits or can be
 *  generated. If it does, the request will be passed and processed by a servlet.
 *  If it does not, then no resource (404) will be returned.
 *
 *  The converting map maintains the current images that are in the process of
 *  converting. Obviously, the requests of a converting image need to wait for the
 *  completion to continue.
 *
 * @author ”Dong Liu”
 *
 */
public class ImageService {

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		int max_thread = 100;
		int port = Integer.parseInt(System.getProperty("jetty.port", "8919"));
		String host = System.getProperty("jetty.host", "localhost");
		String resourceBase = System.getProperty("base", ".");
		String logBase = System.getProperty("log",".");

		QueuedThreadPool _pool = new QueuedThreadPool();
		_pool.setMaxThreads(max_thread);
		_pool.setMinThreads(10);
		_pool.setDaemon(true);

		Server _server = new Server();
		_server.setThreadPool(_pool);
		_server.setGracefulShutdown(500);

		Connector _connector = new SelectChannelConnector();
		_connector.setPort(port);
		_connector.setHost(host);
		_connector.setMaxIdleTime(600000);
		_server.setConnectors(new Connector[] { _connector });


		HandlerCollection handlers = new HandlerCollection();


		ContextHandlerCollection contexts = new ContextHandlerCollection();
		RequestLogHandler requestLogHandler = new RequestLogHandler();


		handlers.setHandlers(new Handler[] {contexts, requestLogHandler });
		_server.setHandler(handlers);

		NCSARequestLog requestLog = new NCSARequestLog(logBase + "/is-yyyy_mm_dd.log");
        requestLog.setExtended(false);
        requestLogHandler.setRequestLog(requestLog);



		ServletContextHandler root = new ServletContextHandler(contexts, "/", ServletContextHandler.SESSIONS);

		ServletHolder defaultServlet = new ServletHolder(new DefaultServlet());
        Map<String, String> defaultServletConfig = new HashMap<String, String>();
        defaultServletConfig.put("dirAllowed", "False");
        defaultServletConfig.put("welcomeServlets", "False");
        defaultServletConfig.put("gzip", "True");
        defaultServletConfig.put("resourceBase", resourceBase);
        defaultServletConfig.put("aliases", "True");
        defaultServletConfig.put("maxCacheSize", "256000000"); // 256M
        defaultServletConfig.put("maxCachedFileSize", "100000"); // 100K
        defaultServletConfig.put("maxCachedFiles", "1000");
        defaultServletConfig.put("cacheType", "both");
        defaultServletConfig.put("useFileMappedBuffer", "True");
        defaultServletConfig.put("cacheControl", "max-age=360000,private"); // 100h ~= 5d, not publicly cached


        defaultServlet.setInitParameters(defaultServletConfig);
        defaultServlet.setInitOrder(0);
        root.addServlet(defaultServlet, "/");



        FilterHolder imageFiler = new FilterHolder(new ImageFilter());
        imageFiler.setInitParameter(ImageFilter.RESOURCE_BASE_INIT_PARAM, resourceBase);
        imageFiler.setInitParameter(ImageFilter.WAIT_INIT_PARAM, "10000");
        root.addFilter(imageFiler, "/*", FilterMapping.DEFAULT);

        RolloverFileOutputStream logFile = new RolloverFileOutputStream(logBase+"yyyy_mm_dd.stderrout.log", false, 90, TimeZone.getDefault());
        PrintStream _log = new PrintStream(logFile);
        Log.info("Redirecting stderr stdout to " + logFile.getFilename());
        System.setErr(_log);
        System.setOut(_log);

		_server.start();
		_server.join();


	}

}
