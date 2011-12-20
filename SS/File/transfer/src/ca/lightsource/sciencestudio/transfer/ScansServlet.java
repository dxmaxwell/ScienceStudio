/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     ScansServlet class.
 *     
 */
package ca.lightsource.sciencestudio.transfer;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.zip.GZIPInputStream;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.HttpExchange;
import org.eclipse.jetty.http.HttpFields;
import org.eclipse.jetty.http.HttpHeaderValues;
import org.eclipse.jetty.http.HttpHeaders;
import org.eclipse.jetty.http.HttpStatus;
import org.eclipse.jetty.io.Buffer;
import org.eclipse.jetty.server.Dispatcher;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.util.IO;
import org.eclipse.jetty.util.URIUtil;
import org.eclipse.jetty.util.ajax.JSON;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.thread.QueuedThreadPool;

/**
 *
 * Functionality interfaces
 * <p>
 * GET /_scans the list of scans available on the service
 * </p>
 * <p>
 * GET /_scans/{scanID} the list of images in the scan. The client can specify
 * the {@code type} of images in the parameter. The client can also get the
 * progress of a scan being transferred by including a {@code progress}
 * parameter.
 * </p>
 * <p>
 * GET /_scans/{scanID} gives the list of images in a short format. This short
 * format message is also cached in the .cache directory in service base. The
 * cache is expired by a request and a out-of-date timestamp.
 * </p>
 * <p>
 * GET /_scans/{scanID}/{imageName} the image
 * </p>
 * <p>
 * POST /_scans The CLS side service or others (for testing) inform the service
 * about the scan of scanID. The message might contain information about how the
 * images are named. Or it can indicate that all images with certain prefix
 * and/or suffix are expected to be transferred.
 * </p>
 * <p>
 * PUT /_scans/{scanID}/{imageName} add an image to an existed scan
 * </p>
 * <p>
 * TODO the md5 strings of the images will be checked against the fetched images
 * for data integrity. The md5 signatures can be fetched by a GET
 * /_scans/{scanID} request or the GET /scanID response. If the md5 strings do
 * not match, then another fetching is needed.
 * </p>
 *
 * TODO encoding all the url's
 *
 * @author Dong Liu
 *
 */
public class ScansServlet extends HttpServlet {

    private static final long serialVersionUID = 3450234358152487164L;
    private ServletContext _servletContext;
    private ContextHandler _contextHandler;
    private Server _server;
    private String host;
    private HttpClient _client;
    private ThreadPoolExecutor _decodeExecutor;
    private ThreadPoolExecutor _transferExecutor;
    private QueuedThreadPool _pool;

    private String _imageType = "spe, tif";

    private String _newline = System.getProperty("line.separator");


    private File _resourceBase;
    private File _cacheBase;
    private static int magic = 4;

    private static int buffer = 2 * 8192;

    public void init() throws UnavailableException {
        _servletContext = getServletContext();
        ContextHandler.Context scontext = ContextHandler.getCurrentContext();
        if (scontext == null)
            _contextHandler = ((ContextHandler.Context) _servletContext).getContextHandler();
        else
            _contextHandler = ContextHandler.getCurrentContext().getContextHandler();

        _server = _contextHandler.getServer();
        _client = (HttpClient) _server.getAttribute("httpclient");
        _decodeExecutor = (ThreadPoolExecutor) _server.getAttribute("decodeexecutor");
        _transferExecutor = (ThreadPoolExecutor) _server.getAttribute("transferexecutor");
        _pool = (QueuedThreadPool) _server.getAttribute("pool");
        host = (String) _server.getAttribute("host");


        String rb = getInitParameter("resourceBase");
        String cb = getInitParameter("cacheBase");

        if (rb != null) {
            try {
                _resourceBase = new File(rb);
                _cacheBase = new File(cb);
            } catch (Exception e) {
                throw new UnavailableException(e.toString());
            }
        }

        String imageType = getInitParameter("type");
        if (imageType != null) {
            _imageType = imageType;
        }

        String magicParameter = getInitParameter("magic");
        if (magicParameter != null)
            magic = Integer.parseInt(getInitParameter("magic"));

        if (Log.isDebugEnabled())
            Log.debug("resource base = " + _resourceBase.getAbsolutePath());

    }

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {

        String pathInContext = getPathInContext(request);
        int numberLayer = pathInContext.split(URIUtil.SLASH).length;
        if (numberLayer >= 4) {
            String forwardPath = pathInContext.substring(pathInContext.indexOf(URIUtil.SLASH, 1));
            request.getRequestDispatcher(forwardPath).forward(request, response);
            return;
        }

        // handle /_scans or /_scans/
        if (numberLayer == 2) {
            sendAllScans(request, response, _resourceBase);
            return;
        }

        // handle /_scans/scanName or /_scans/scanName/
        if (numberLayer == 3) {
            String path = pathInContext.substring(pathInContext.indexOf(URIUtil.SLASH, 1));
            File resource = new File(_resourceBase, path);
            if (request.getParameter("type") == null) {
                sendAllItems(request, response, resource);
                return;
            } else {
                String type = request.getParameter("type");
                sendAllItems(request, response, resource, type);
                return;
            }
        }

    }

    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {

        // Create a new scan with an id.
        // The id might be the same as that in the request message or be created based on that.
        Map requestMap = (Map) JSON.parse(request.getReader());

        if (requestMap == null) {
            if (Log.isDebugEnabled())
                Log.debug("Request does not contain json message");
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "need json message body");
            return;
        }

        if (!requestMap.containsKey("scan_url")) {
            if (Log.isDebugEnabled())
                Log.debug("Request does not contain scan_url");
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        final String scan_url = (String) requestMap.get("scan_url");
        String scan = null;

        try {
            // handle path with multiple hierarchies
            String[] splits = new URL(scan_url).getPath().split(URIUtil.SLASH);
            scan = splits[splits.length - 1];
        } catch (MalformedURLException e) {
            Log.warn(Log.EXCEPTION, e);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        final Long arrived = new Long(System.currentTimeMillis());
        File scanDir = null;
        final String patch = (String) requestMap.get("patch");
        try {
            scanDir = new File(_resourceBase, scan);
        } catch (NullPointerException e) {
            Log.warn(Log.EXCEPTION, e);
            if (Log.isDebugEnabled())
                Log.debug("no scan path in request");
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        String resourceURL = URIUtil.encodePath(new URL("http", request.getServerName(), request.getServerPort(), URIUtil.SLASH + "_scans" + URIUtil.SLASH + scan + URIUtil.SLASH).toString());

        if (scanDir.exists()) {
            if (Log.isDebugEnabled())
                Log.debug("The directory " + scanDir.toString() + " exists.");
            if (patch == null) {
                response.sendError(HttpServletResponse.SC_CONFLICT, scanDir.toString() + " exists, and patch is required");
                return;
            } else {
                response.setStatus(HttpStatus.OK_200);
            }

        } else {
            if (scanDir.mkdir()) {
                response.setStatus(HttpStatus.CREATED_201);
                response.setHeader(HttpHeaders.LOCATION, resourceURL);
            } else {
                if (Log.isDebugEnabled())
                    Log.debug("The directory " + scanDir.toString() + " cannot be created.");
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                return;
            }

        }

        if (requestMap.containsKey("image_suffix") && requestMap.containsKey("image_prefix")) {
            if (requestMap.containsKey("image_start") && requestMap.containsKey("image_end") && requestMap.containsKey("digit_number")){
                long image_start = ((Long) requestMap.get("image_start")).longValue();
                long image_end = ((Long) requestMap.get("image_end")).longValue();
                final int digit_number = ((Long) requestMap.get("digit_number")).intValue();
                if (image_end < image_start) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "image number wrong.");
                    return;
                }
                if (digit_number != 0 && ("" + image_end).length() > digit_number) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "image number format wrong.");
                    return;
                }
                Transfer task = new Transfer(arrived, requestMap, scanDir, _client, _decodeExecutor);
                _transferExecutor.execute(task);
            } else if (requestMap.containsKey("image_all")) {
                String all = (String) requestMap.get("image_all");
                if (getBoolean(all)) {
                    Transfer task = new Transfer(arrived, requestMap, scanDir, _client, _decodeExecutor);
                    _transferExecutor.execute(task);
                } else {
                    if (Log.isDebugEnabled())
                        Log.debug("image_all not true in the JSON.");
                    response.sendError(HttpStatus.BAD_REQUEST_400, "Cannot understand request.");
                }
            } else {
                if (Log.isDebugEnabled())
                    Log.debug("Missing image number related entries in the JSON.");
                response.sendError(HttpStatus.BAD_REQUEST_400, "Missing image number related information.");
            }
        } else {
            if (Log.isDebugEnabled())
                Log.debug("Missing image_prefix and/or image_suffix in the JSON.");
            response.sendError(HttpStatus.BAD_REQUEST_400, "Missing image_prefix and/or image_suffix information.");
        }

        response.setContentType("application/json; charset=UTF-8");

        Map responseMap = new HashMap();
        responseMap.put("original_url", scan_url);
        responseMap.put("scan_url", resourceURL);
        responseMap.put("transfer_id", arrived);
        String message = JSON.toString(responseMap) + _newline;
        byte[] data = message.getBytes("UTF-8");
        response.setContentLength(data.length);
        response.getOutputStream().write(data);
        return;
    }

    @Override
    protected void doPut(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        String pathInContext = getPathInContext(request);

        // handle /_scans/{scanName}/{imageName}

        int numberLayer = pathInContext.split("/").length;
        if (numberLayer < 4) {
            if (Log.isDebugEnabled())
                Log.debug("Request uri = " + pathInContext);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        String scan = pathInContext.split("/")[numberLayer - 2];
        String resource = pathInContext.split("/")[numberLayer - 1];

        File scanDir = null;
        File resourceFile = null;
        try {
            scanDir = new File(_resourceBase, scan);
            resourceFile = new File(scanDir, resource);
        } catch (NullPointerException e) {
            Log.warn(Log.EXCEPTION, e);
            if (Log.isDebugEnabled())
                Log.debug("no scan path in request");
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        if (!scanDir.exists()) {
            if (Log.isDebugEnabled())
                Log.debug("The directory " + scanDir.toString() + " does not exist.");

            response.setContentType("text/plain; charset=UTF-8");
            response.sendError(HttpServletResponse.SC_CONFLICT, scan + " not known.");
            return;
        }

        //TODO enable update here
        if (resourceFile.exists()) {
            if (Log.isDebugEnabled())
                Log.debug("The resource " + resourceFile.toString() + " already existed.");

            response.setContentType("text/plain; charset=UTF-8");
            response.sendError(HttpServletResponse.SC_CONFLICT, resource + " already existed.");
            return;
        }

        Map requestMap = (Map) JSON.parse(request.getReader());

        if (!requestMap.containsKey("resource_url") || !requestMap.containsKey("processing")) {
            if (Log.isDebugEnabled())
                Log.debug("Request does not contain scan_url or processing option.");
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        String resource_url = (String) requestMap.get("resource_url");
        boolean processing = getBoolean((String) requestMap.get("processing"));

        long size = 0;

        if (requestMap.containsKey("size")) {
            size = ((Long) requestMap.get("size")).longValue();
        }

        if (size != 0) {
            scheduleRetrieve(resource_url, scanDir, resourceFile, size);
        } else {
            // TODO a head first?
            scheduleRetrieve(resource_url, scanDir, resourceFile);
        }

        response.setStatus(HttpStatus.CREATED_201);
        response.setContentType("application/json; charset=UTF-8");

        Map responseMap = new HashMap();
        responseMap.put("resource_url", pathInContext);
        String message = JSON.toString(responseMap) + _newline;
        byte[] data = message.getBytes("UTF-8");
        response.setContentLength(data.length);
        response.getOutputStream().write(data);
        return;
    }

    private void scheduleRetrieve(String resourceUrl, File scanDir,
            final File resourceFile) throws FileNotFoundException {
        Log.info("****************************************");
        Log.info("start transfering" + resourceFile.toString());
        Log.info("****************************************");
        final long start = System.currentTimeMillis();

        final BufferedOutputStream target = new BufferedOutputStream(new FileOutputStream(resourceFile, true), buffer);

        HttpExchange exchange = new HttpExchange() {
            private volatile int _responseStatus;

            protected void onRetry() {
                Log.warn(this.toString() + " retries to " + this.getMethod() + " " + this.getURI());
            }

            protected void onResponseStatus(Buffer version, int status,
                    Buffer reason) throws IOException {
                _responseStatus = status;
                super.onResponseStatus(version, status, reason);
            }

            protected void onResponseContent(Buffer content) {

                if (_responseStatus == HttpStatus.OK_200) {
                    try {
                        content.writeTo(target);
                    } catch (IOException e) {
                        Log.warn(e);
                    }
                }
            }

            protected void onResponseComplete() throws IOException {
                target.close();
                Log.info("****************************************");
                Log.info(resourceFile.toString() + " finished in " + (System.currentTimeMillis() - start) + " milliseconds. ");
                Log.info("****************************************");
                super.onResponseComplete();
            }

        };
        exchange.setMethod("GET");
        exchange.setURL(resourceUrl);
        try {
            _client.send(exchange);
        } catch (IOException e) {
            Log.warn(Log.EXCEPTION, e);
        }
    }

    private void scheduleRetrieve(String resourceUrl, final File scanDir,
            final File resourceFile, long filesize, final int magic)
            throws FileNotFoundException {

        Log.info("****************************************");
        Log.info("start transfering" + resourceFile.toString());
        Log.info("****************************************");
        final long start = System.currentTimeMillis();

        // one random access file has problem of IO exceptions. try to use four regular files and then join them.

        final FileOutputStream target = new FileOutputStream(resourceFile, true);
        final AtomicInteger count = new AtomicInteger(magic);

        for (int i = 0; i < magic; i++) {

            final File part = new File(scanDir, resourceFile.getName() + ".part" + i + ".gz");
            final BufferedOutputStream fileStream = new BufferedOutputStream(new FileOutputStream(part), buffer);
            final ConcurrentHashMap<String, Integer> statusMap = new ConcurrentHashMap<String, Integer>();
            final ConcurrentHashMap<String, String> encodingMap = new ConcurrentHashMap<String, String>();
            final ConcurrentHashMap<String, String> rangeMap = new ConcurrentHashMap<String, String>();

            HttpExchange exchange = new HttpExchange() {

                private final HttpFields _responseFields = new HttpFields();
                private volatile int _responseStatus;

                private int _attempt = 0;

                protected void onConnectionFailed(Throwable x) {
                    super.onConnectionFailed(x);
                    if (_attempt < _client.maxRetries()) {
                        try {
                            this.reset();
                            _client.send(this);
                        } catch (IOException e) {
                            this.onException(x);
                        } finally {
                            _attempt = _attempt + 1;
                            Log.warn("resend " + _attempt + " times, " + this.toString());
                        }
                    }
                }

                protected void onRetry() throws IOException {
                    Log.warn(this.toString() + " retries to " + this.getMethod() + " " + this.getURI());
                    super.onRetry();
                }

                protected void onResponseHeader(Buffer name, Buffer value)
                        throws IOException {
                    _responseFields.add(name, value);
                    super.onResponseHeader(name, value);
                }

                protected void onResponseStatus(Buffer version, int status,
                        Buffer reason) throws IOException {
                    _responseStatus = status;
                    super.onResponseStatus(version, status, reason);
                }

                protected void onResponseContent(Buffer content)
                        throws IOException {
                    if (_responseStatus == HttpStatus.PARTIAL_CONTENT_206) {
                        content.writeTo(fileStream);
                    }
                }

                protected void onResponseComplete() throws IOException {
                    if (_responseStatus == HttpStatus.PARTIAL_CONTENT_206) {
                        fileStream.flush();
                        fileStream.close();
                    }
                    statusMap.putIfAbsent(part.getName(), new Integer(_responseStatus));
                    if (_responseFields.containsKey(HttpHeaders.CONTENT_RANGE_BUFFER)) {
                        rangeMap.putIfAbsent(part.getName(), _responseFields.getStringField(HttpHeaders.CONTENT_RANGE_BUFFER));
                    }
                    if (_responseFields.containsKey(HttpHeaders.CONTENT_ENCODING_BUFFER)) {
                        encodingMap.putIfAbsent(part.getName(), _responseFields.getStringField(HttpHeaders.CONTENT_ENCODING_BUFFER));
                    }
                    if (Log.isDebugEnabled()) {
                        Log.debug("The status is " + _responseStatus + " range is " + _responseFields.getStringField(HttpHeaders.CONTENT_RANGE_BUFFER) + " encoding is " + _responseFields.getStringField(HttpHeaders.CONTENT_ENCODING_BUFFER));
                    }

                    if (count.decrementAndGet() == 0) { // all exchanges done
                        Log.info("****************************************");
                        Log.info(resourceFile.toString() + " parts requests finished in " + (System.currentTimeMillis() - start) + " milliseconds. ");
                        Log.info("****************************************");

                        Enumeration<Integer> status = statusMap.elements();
                        boolean fail = false;
                        while (status.hasMoreElements()) {
                            if (status.nextElement().intValue() != 206) {
                                fail = true;
                                break;
                            }
                        }

                        Enumeration<String> encodings = encodingMap.elements();
                        boolean gzipEncoding = true;
                        while (encodings.hasMoreElements()) {
                            String encoding = encodings.nextElement();
                            if (encoding == null || encoding.indexOf(HttpHeaderValues.GZIP) == -1) {
                                gzipEncoding = false;
                                break;
                            }
                        }
                        if (!gzipEncoding) {
                            Log.warn("Not all the parts are in Gzip encoding.");
                        }

                        if (gzipEncoding && !fail) {
                            for (int i = 0; i < magic; i++) {
                                File part = new File(scanDir, resourceFile.getName() + ".part" + i + ".gz");
                                FileInputStream partStream = null;
                                GZIPInputStream gis = null;
                                try {
                                    partStream = new FileInputStream(part);
                                    gis = new GZIPInputStream(partStream);
                                    IO.copy(gis, target);
                                } finally {
                                    gis.close();
                                    partStream.close();
                                }
                            }

                            for (int i = 0; i < magic; i++) {
                                File part = new File(scanDir, resourceFile.getName() + ".part" + i);
                                part.delete();
                            }

                        }

                        target.close();

                        if (fail) {
                            resourceFile.delete();
                            Log.info("****************************************");
                            Log.info(resourceFile.toString() + " failed in " + (System.currentTimeMillis() - start) + " milliseconds. ");
                            Log.info("****************************************");

                        } else {

                            Log.info("****************************************");
                            Log.info(resourceFile.toString() + " finished in " + (System.currentTimeMillis() - start) + " milliseconds. ");
                            Log.info("****************************************");
                        }
                    }
                    super.onResponseComplete();
                }

            };
            exchange.setMethod("GET");
            exchange.setURL(resourceUrl);
            exchange.addRequestHeader(HttpHeaders.RANGE, rangesSpecifier(filesize, i, magic));
            exchange.addRequestHeader(HttpHeaders.ACCEPT_ENCODING_BUFFER, HttpHeaderValues.GZIP_BUFFER);
            try {
                _client.send(exchange);
            } catch (IOException e) {
                Log.warn(Log.EXCEPTION, e);
            }
        }

    }

    private String rangesSpecifier(long filesize, int sequence, int number) {
        long rangeSize = filesize / number;
        if (sequence == (number - 1)) {
            return "bytes=" + (rangeSize * sequence) + "-";
        } else {
            return "bytes=" + (rangeSize * sequence) + "-" + (rangeSize * (sequence + 1) - 1);
        }
    }

    private void scheduleRetrieve(String resourceUrl, File scanDir,
            File resourceFile, long filesize) throws FileNotFoundException {
        scheduleRetrieve(resourceUrl, scanDir, resourceFile, filesize, magic);
    }

    private Boolean getBoolean(String string) {
        return (string.startsWith("t") || string.startsWith("T") || string.startsWith("y") || string.startsWith("Y") || string.startsWith("1"));
    }

    private String getPathInContext(HttpServletRequest request) {
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

        return URIUtil.addPaths(servletPath, pathInfo);
    }

    protected void sendAllScans(HttpServletRequest request,
            HttpServletResponse response, File resource) throws IOException {
        byte[] data = null;
        String dir = getListScan(resource);
        if (dir == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "No resource");
            return;
        }

        data = dir.getBytes("UTF-8");
        response.setContentType("application/json; charset=UTF-8");
        response.setContentLength(data.length);
        response.getOutputStream().write(data);
    }

    private String getListScan(File resource) throws IOException {

        String[] ls = resource.list(new ScanFilter());
        if (ls == null)
            return null;
        Arrays.sort(ls);

        StringBuilder buf = new StringBuilder(4096);
//        buf.append("{\"num_scans\": " + ls.length + ", \"resource\": \""+ host + ":" + resource.getAbsolutePath() + "\" , \"scans\": [");
        buf.append("{\"num_scans\": " + ls.length + ", \"resource\": \"" + resource.getAbsolutePath() + "\" , \"scans\": [");
        for (int i = 0; i < ls.length; i++) {
            if (i != 0)
                buf.append(",");
            buf.append("{\"id\": \"" + ls[i] + "\", \"lastModified\": " + (long) (new File(resource, ls[i]).lastModified()) / 1000 + "}");
        }
        buf.append("]}" + _newline);
        return buf.toString();
    }


    protected void sendAllItems(HttpServletRequest request,
            HttpServletResponse response, File resource, String type)
            throws IOException, ServletException {
        byte[] data = null;
        // for details request, send the list directly
        if (request.getParameter("details") != null) {
            String dir = getListImage(resource, type);
            if (dir == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "No resouce");
                return;
            }

            data = dir.getBytes("UTF-8");
            response.setContentType("application/json; charset=UTF-8");
            response.setContentLength(data.length);
            response.getOutputStream().write(data);
            return;
        }
        File cacheDir = new File(_cacheBase, ".cache");
        if (!cacheDir.exists() || !cacheDir.isDirectory()) {
            if (Log.isDebugEnabled())
                Log.debug("create the cache dir.");
            if (!cacheDir.mkdir()) {
                Log.warn("failed to create the cache dir.");
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                return;
            }
        }

        File jsonCache = new File(cacheDir, resource.getName() + ".json");
        if (jsonCache.exists() && jsonCache.lastModified() >= resource.lastModified() && jsonCache.length() > 0) {
            // forward to to the file
            String forwardPath = URIUtil.SLASH + cacheDir.getName() + URIUtil.SLASH + jsonCache.getName();
            request.getRequestDispatcher(forwardPath).forward(request, response);
            return;
        }

        if (!jsonCache.exists() && !jsonCache.createNewFile()) {
            Log.warn("cannot create " + jsonCache.getName());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }

        FileWriter writer = new FileWriter(jsonCache, false); // write the new cache

        String[] ls = resource.list(new ImageFilter(type));
        if (ls == null) {
            writer.close();
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "No resouce");
            return;
        }

        Arrays.sort(ls);


        // If the first is not valid for the template then forward to details response
        if (!ScanNameUtil.isValid(ls[0])) {
            String forwardPath = getPathInContext(request) + "?details" ;
            request.getRequestDispatcher(forwardPath).forward(request, response);
            return;
        }

        StringBuilder buf = new StringBuilder(4096);
        ArrayList<ImageSet> setList = new ArrayList<ImageSet>();

        try {
            setList = compressSet(ls);
        } catch (NumberFormatException e) {
            Log.warn("The scan has an image named not in the predefined format.");
            String forwardPath = getPathInContext(request) + "?details" ;
            request.getRequestDispatcher(forwardPath).forward(request, response);
            return;
        }

        buf.append("{\"num_sets\": " + setList.size() + ", \"sets\": [");

        Iterator<ImageSet> i = setList.iterator();
        while (i.hasNext()) {
            ImageSet set = i.next();
            buf.append("{\"name\":\"" + set.getName() + "\",\"start\":" + set.getStart().intValue() + ",\"end\":" + set.getEnd().intValue() + ",\"type\":\"" + set.getType() + "\"");
            if (!set.missingEmpty()) {
                buf.append(",\"missing\":[");
                ArrayList<Integer> missings = set.getMissing();
                Iterator<Integer> m = missings.iterator();
                while (m.hasNext()) {
                    buf.append(m.next().intValue());
                    if (m.hasNext())
                        buf.append(",");
                }
                buf.append("]");
            }
            buf.append("}");
            if (i.hasNext())
                buf.append(",");
        }

        buf.append("]}" + _newline);
        String json = buf.toString();
        writer.write(json);
        writer.close();

        data = json.getBytes("UTF-8");
        response.setContentType("application/json; charset=UTF-8");
        response.setContentLength(data.length);
        response.getOutputStream().write(data);

    }

    protected void sendAllItems(HttpServletRequest request,
            HttpServletResponse response, File resource) throws IOException,
            ServletException {
        sendAllItems(request, response, resource, _imageType);
    }

    private String getListImage(File resource, String type) throws IOException {
        String[] ls = resource.list(new ImageFilter(type));
        if (ls == null)
            return null;
        Arrays.sort(ls);

        StringBuilder buf = new StringBuilder(4096);
        buf.append("{\"num_images\": " + ls.length + ", \"images\": [");
        for (int i = 0; i < ls.length; i++) {
            if (i != 0)
                buf.append(",");
            buf.append("{\"id\": \"" + ls[i] + "\"}");
        }
        buf.append("]}" + _newline);
        return buf.toString();
    }

    // Make the parser more robust by ScanNameUtil
    private ArrayList<ImageSet> compressSet(String[] ls) throws NumberFormatException {
        int length = ls.length;
        String[][] tokens = new String[length][];
        for (int i = 0; i < length; i++) {
            tokens[i] = ScanNameUtil.parse(ls[i]);
        }

        ArrayList<ImageSet> list = new ArrayList<ImageSet>();

        int startIndex = 0;
        for (int i = 0; i < length; i++) {
            if ((i == length - 1) || !tokens[i + 1][0].equals(tokens[i][0])) { // a new name found or that is all
                ImageSet set = new ImageSet();
                set.setName(tokens[i][0]);
                set.setType(tokens[i][2]);
                int setLength = i - startIndex + 1;
                int[] numbers = new int[setLength];
                for (int j = 0; j < setLength; j++) {
                    numbers[j] = Integer.parseInt(tokens[startIndex + j][1]);
                }
                Arrays.sort(numbers);
                set.setStart(new Integer(numbers[0]));
                set.setEnd(new Integer(numbers[setLength - 1]));
                if ((numbers[setLength - 1] - numbers[0] + 1) != setLength) { // some are missing
                    for (int n = 0; n < setLength - 1; n++) {
                        int diff = numbers[n + 1] - numbers[n];
                        if (diff > 1) {
                            set.addMissing(numbers[n] + 1, numbers[n + 1] - 1);
                        }
                    }
                }
                list.add(set);
                startIndex = i + 1;
            }

        }

        return list;

    }

    /**
     * Filter image according to the type
     *
     */
    private class ImageFilter implements FilenameFilter {
        private String type;

        public ImageFilter(String type) {
            super();
            this.type = type;
        }

        public boolean accept(File dir, String name) {
            if (new File(dir, name).isFile()) {
                return type.contains(name.substring(name.lastIndexOf(".") + 1).toLowerCase());
            } else {
                return false;
            }

        }

    }

    /**
     * Filter image according to the type
     *
     */
    private class ScanFilter implements FilenameFilter {

        public boolean accept(File dir, String name) {
            if (new File(dir, name).isDirectory()) {
//                return (!name.startsWith(".") && (name.startsWith("scan")));
                return (!name.startsWith("."));
            } else {
                return false;
            }

        }

    }

    private class ImageSet {

        private String name;
        private Integer start;
        private Integer end;
        private ArrayList<Integer> missing;
        private String type;
        //TODO private Integer numDigit; // the number of digits in the file name %d

        public ImageSet() {
            this.name = null;
            this.start = null;
            this.end = null;
            this.type = null;
            this.missing = new ArrayList<Integer>();
        }

        public String getName() {
            return name;
        }

        public Integer getStart() {
            return start;
        }

        public Integer getEnd() {
            return end;
        }

        public ArrayList<Integer> getMissing() {
            return missing;
        }

        public String getType() {
            return type;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setStart(Integer start) {
            this.start = start;
        }

        public void setEnd(Integer end) {
            this.end = end;
        }

        public boolean addMissing(Integer number) {
            return this.missing.add(number);
        }

        public boolean missingEmpty() {
            return this.missing.isEmpty();
        }

        public void setType(String type) {
            this.type = type;
        }

        public void addMissing(int start, int end) {
            for (int i = start; i <= end; i++) {
                this.addMissing(new Integer(i));
            }
        }

    }

}
