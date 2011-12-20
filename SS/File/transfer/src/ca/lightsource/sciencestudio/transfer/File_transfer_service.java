/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     File_transfer_service class.
 *     
 */
package ca.lightsource.sciencestudio.transfer;

import java.io.PrintStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.security.HashRealmResolver;
import org.eclipse.jetty.http.security.Constraint;
import org.eclipse.jetty.security.ConstraintMapping;
import org.eclipse.jetty.security.ConstraintSecurityHandler;
import org.eclipse.jetty.security.HashLoginService;
import org.eclipse.jetty.security.LoginService;
import org.eclipse.jetty.security.authentication.DigestAuthenticator;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.NCSARequestLog;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.handler.RequestLogHandler;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.server.session.SessionHandler;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.FilterMapping;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.servlets.GzipFilter;
import org.eclipse.jetty.util.RolloverFileOutputStream;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.thread.QueuedThreadPool;

/**
 * This service starts an embedded jetty server to serve the request from
 * UWO_XRD_service.
 *
 * @author ”Dong Liu”
 *
 *
 */
public class File_transfer_service {

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        int max_thread = 200;
        int max_idle = 60000;
        int port = Integer.parseInt(System.getProperty("jetty.port", "8080"));
        String host = System.getProperty("jetty.host", "localhost");
        String securityConfig = System.getProperty("security", "./realm.properties");
        String clientRealm = System.getProperty("realm", "./realm.client");
        String serviceName = System.getProperty("name", "service");
        String imageType = System.getProperty("type", "spe,tif");
        String resourceBase = System.getProperty("base", ".");
        String log = System.getProperty("logStd", "yes");
        String logBase = System.getProperty("log", ".");
        String debug = System.getProperty("DEBUG");
        String magic = System.getProperty("magic", "4");
        int max_connection = Integer.parseInt(System.getProperty("client.mc", "20"));

        if (debug != null)
            System.setProperty("org.eclipse.jetty.util.log.DEBUG", "true");

        QueuedThreadPool _pool = new QueuedThreadPool();
        _pool.setMaxThreads(max_thread);
        _pool.setMinThreads(10);
        _pool.setDaemon(true);

        HttpClient _httpClient = new HttpClient();
        _httpClient.setThreadPool(_pool);
        _httpClient.setConnectorType(HttpClient.CONNECTOR_SELECT_CHANNEL);
        _httpClient.setMaxConnectionsPerAddress(max_connection);
        _httpClient.setTimeout(300000); // 5 minutes
        _httpClient.setMaxRetries(3);

        final ConcurrentLinkedQueue<Transfer> _transferred = new ConcurrentLinkedQueue<Transfer>();

        // a thread pool executor for decoding the gzip files
        ThreadPoolExecutor _decodeExecutor = new ThreadPoolExecutor(5, max_connection, 10, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());

        // a single thread pool executor for submitted transfer task
        ThreadPoolExecutor _transferExecutor = new ThreadPoolExecutor(1, 1, Integer.MAX_VALUE, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>()) {
            protected void beforeExecute(Thread t, Runnable r) {
                ((Transfer) r).setStarted(new Long(System.currentTimeMillis()));
                _transferred.add((Transfer) r);
                super.beforeExecute(t, r);
            }

        };

        Server _server = new Server();
        _server.setThreadPool(_pool);
        _server.addBean(_httpClient);
        _server.setGracefulShutdown(500);
        _server.setAttribute("host", host);
        _server.setAttribute("httpclient", _httpClient);
        _server.setAttribute("transferred", _transferred);
        _server.setAttribute("decodeexecutor", _decodeExecutor);
        _server.setAttribute("transferexecutor", _transferExecutor);
        _server.setAttribute("pool", _pool);

        SelectChannelConnector _connector = new SelectChannelConnector();
        _connector.setPort(port);
        _connector.setHost(host);
        _connector.setMaxIdleTime(max_idle);
        _connector.setLowResourceMaxIdleTime(max_idle / 6);
        _connector.setLowResourcesConnections(max_connection / 20);
        _server.setConnectors(new Connector[] { _connector });

        // server security start
        LoginService loginService = new HashLoginService("transfer", securityConfig);
        _server.addBean(loginService);

        Constraint constraint = new Constraint();
        constraint.setName(Constraint.__DIGEST_AUTH);
        constraint.setRoles(new String[]{"user","admin"});
        constraint.setAuthenticate(true);

        ConstraintMapping cm = new ConstraintMapping();
        cm.setConstraint(constraint);
        cm.setPathSpec("/*");

        Set<String> knownRoles = new HashSet<String>();
        knownRoles.add("user");
        knownRoles.add("admin");

        ConstraintSecurityHandler security = new ConstraintSecurityHandler();
        security.setConstraintMappings(Collections.singletonList(cm), knownRoles);
        security.setAuthenticator(new DigestAuthenticator());
        security.setLoginService(loginService);
        security.setStrict(true);
        // server security end

        // client security start
        HashRealmResolver realmResolver = new HashRealmResolver();
        ClientRealmUtil.addRealms(realmResolver, clientRealm);
        _httpClient.setRealmResolver(realmResolver);
        // client security end

        HandlerCollection handlers = new HandlerCollection();

        ContextHandlerCollection contexts = new ContextHandlerCollection();

        RequestLogHandler requestLogHandler = new RequestLogHandler();

        handlers.setHandlers(new Handler[] { contexts, requestLogHandler });
        _server.setHandler(handlers);

        NCSARequestLog requestLog = new NCSARequestLog(logBase + "/yyyy_mm_dd.request.log");
        requestLog.setExtended(false);
        requestLogHandler.setRequestLog(requestLog);

        ServletContextHandler root = new ServletContextHandler(contexts, "/", new SessionHandler(), security, new ServletHandler(), null);

        ServletHolder scansServlet = new ServletHolder(new ScansServlet());
        Map<String, String> scansServletConfig = new HashMap<String, String>();
        scansServletConfig.put("resourceBase", resourceBase);
        scansServletConfig.put("cacheBase", logBase);
        scansServletConfig.put("type", imageType);
        scansServletConfig.put("magic", magic);
        scansServlet.setInitParameters(scansServletConfig);
        scansServlet.setInitOrder(1);
        root.addServlet(scansServlet, "/_scans/*");

        ServletHolder transferServlet = new ServletHolder(new TransferServlet());
//        Map<String, String> transferServletConfig = new HashMap<String, String>();
        scansServlet.setInitOrder(1);
        root.addServlet(transferServlet, "/_transfer/*");

        ServletHolder usageServlet = new ServletHolder(new UsageServlet());
        Map<String, String> usageServletConfig = new HashMap<String, String>();
        usageServletConfig.put("resourceBase", resourceBase);
        usageServletConfig.put("cacheControl", "max-age=3600,private"); // 1h, not publicly cached
        usageServletConfig.put("name", serviceName);
        usageServletConfig.put("host", host);
        usageServlet.setInitParameters(usageServletConfig);
        usageServlet.setInitOrder(1);

        root.addServlet(usageServlet, "/_usage");
        root.addServlet(usageServlet, "/_echo");

//        ServletHolder eventServlet = new ServletHolder(new EventServlet());
//        Map<String, String> eventServletConfig = new HashMap<String, String>();
//        eventServletConfig.put("resourceBase", resourceBase);
//        eventServlet.setInitParameters(eventServletConfig);
//        eventServlet.setInitOrder(1);
//
//        root.addServlet(eventServlet, "/_events/*");

        ServletHolder defaultServlet = new ServletHolder(new DefaultServlet());
        Map<String, String> defaultServletConfig = new HashMap<String, String>();
        defaultServletConfig.put("aliases", "false");
        defaultServletConfig.put("acceptRanges", "true");
        defaultServletConfig.put("dirAllowed", "false");
        defaultServletConfig.put("welcomeServlets", "false");
        defaultServletConfig.put("redirectWelcome", "false");
        defaultServletConfig.put("maxCacheSize", "256000000");
        defaultServletConfig.put("maxCachedFileSize", "10000000");
        defaultServletConfig.put("maxCachedFiles", "2048");
        defaultServletConfig.put("gzip", "false");
        defaultServletConfig.put("useFileMappedBuffer", "true");
        defaultServletConfig.put("resourceBase", resourceBase);
        defaultServlet.setInitParameters(defaultServletConfig);
        defaultServlet.setInitOrder(0);

        root.addServlet(defaultServlet, "/");

        ServletHolder cacheServlet = new ServletHolder(new DefaultServlet());
        Map<String, String> cacheServletConfig = new HashMap<String, String>();
        cacheServletConfig.put("aliases", "false");
        cacheServletConfig.put("acceptRanges", "false");
        cacheServletConfig.put("dirAllowed", "false");
        cacheServletConfig.put("welcomeServlets", "false");
        cacheServletConfig.put("redirectWelcome", "false");
        cacheServletConfig.put("maxCacheSize", "256000000");
        cacheServletConfig.put("maxCachedFileSize", "10000000");
        cacheServletConfig.put("maxCachedFiles", "2048");
        cacheServletConfig.put("gzip", "false");
        cacheServletConfig.put("useFileMappedBuffer", "true");
        cacheServletConfig.put("resourceBase", logBase);
        cacheServlet.setInitParameters(cacheServletConfig);
        cacheServlet.setInitOrder(0);
        
        root.addServlet(cacheServlet, "/.cache/*");
        
        FilterHolder gzipFilter = new FilterHolder(new GzipFilter());
        gzipFilter.setInitParameter("minGzipSize", "256");
        root.getServletHandler().addFilterWithMapping(gzipFilter, "/*", FilterMapping.ALL);

        if (log.startsWith("y") || log.startsWith("Y") || log.startsWith("t") || log.startsWith("T")) {
            RolloverFileOutputStream logFile = new RolloverFileOutputStream(logBase + "yyyy_mm_dd.stderrout.log", false, 90, TimeZone.getDefault());
            PrintStream _log = new PrintStream(logFile);

            Log.info("Redirecting stderr stdout to " + logFile.getFilename());
            System.setErr(_log);
            System.setOut(_log);
        }

        _server.start();
        _server.join();

    }

}
