/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     TunnelFactory class.
 *          
 */
package ca.sciencestudio.util.net;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author maxweld
 *
 */
public class TunnelFactory {
	
	private static final int DEFAULT_REMOTE_PORT = 0;
	private static final int DEFAULT_MIN_LOCAL_PORT = 0;
	private static final int DEFAULT_MAX_LOCAL_PORT = 0;
	
	private static final long DEFAULT_ACCEPT_TIMEOUT = 30000;
	private static final long DEFAULT_INACTIVE_TIMEOUT = 15000;
	
	private static final boolean DEFAULT_IS_DATAGRAM = false;
	
	private static final long CREATE_TUNNEL_SLEEP = 25;
	
	private InetAddress bindAddress; 
	private InetAddress remoteAddress;
	
	private int remotePort;
	private int minLocalPort;
	private int maxLocalPort;
	
	private long acceptTimeout;
	private long inactiveTimeout;
	
	private boolean isDatagram;
	
	protected Log logger = LogFactory.getLog(getClass());
	
	public TunnelFactory() {
		this.bindAddress = null;
		try {
			this.remoteAddress = InetAddress.getLocalHost();
		}
		catch(UnknownHostException e) {
			logger.error("Unable to get inet address for localhost.", e);
		}
		this.remotePort = DEFAULT_REMOTE_PORT;
		this.minLocalPort = DEFAULT_MIN_LOCAL_PORT;
		this.maxLocalPort = DEFAULT_MAX_LOCAL_PORT;
		this.isDatagram = DEFAULT_IS_DATAGRAM;
		this.acceptTimeout = DEFAULT_ACCEPT_TIMEOUT;
		this.inactiveTimeout = DEFAULT_INACTIVE_TIMEOUT;
	}
	
	public TunnelFactory(String remoteHost, int remotePort) throws UnknownHostException {
		this(remoteHost, remotePort, DEFAULT_IS_DATAGRAM);
	}
	
	public TunnelFactory(String remoteHost, int remotePort, boolean isDatagram) throws UnknownHostException {
		this(DEFAULT_MIN_LOCAL_PORT, DEFAULT_MAX_LOCAL_PORT, remoteHost, remotePort, isDatagram);
	}
	
	public TunnelFactory(int localPort, String remoteHost, int remotePort) throws UnknownHostException {
		this(localPort, remoteHost, remotePort, DEFAULT_IS_DATAGRAM);
	}
	
	public TunnelFactory(int localPort, String remoteHost, int remotePort, boolean isDatagram) throws UnknownHostException {
		this(localPort, localPort, remoteHost, remotePort, isDatagram);
	}
	
	public TunnelFactory(int minLocalPort, int maxLocalPort, String remoteHost, int remotePort) throws UnknownHostException {
		this(minLocalPort, maxLocalPort, remoteHost, remotePort, DEFAULT_IS_DATAGRAM);
	}
	
	public TunnelFactory(int minLocalPort, int maxLocalPort, String remoteHost, int remotePort, boolean isDatagram) throws UnknownHostException {
		this((InetAddress)null, minLocalPort, maxLocalPort, InetAddress.getByName(remoteHost), remotePort, isDatagram);
	}
	
	public TunnelFactory(InetAddress bindAddress, int minLocalPort, int maxLocalPort, InetAddress remoteAddress, int remotePort) {
		this(bindAddress, minLocalPort, maxLocalPort, remoteAddress, remotePort, DEFAULT_IS_DATAGRAM);
	}
	
	public TunnelFactory(InetAddress bindAddress, int minLocalPort, int maxLocalPort, InetAddress remoteAddress, int remotePort, boolean isDatagram) {
		this.bindAddress = bindAddress;
		this.remoteAddress = remoteAddress;
		this.remotePort = remotePort;
		this.minLocalPort = minLocalPort;
		this.maxLocalPort = maxLocalPort;
		this.isDatagram = isDatagram;
		this.acceptTimeout = DEFAULT_ACCEPT_TIMEOUT;
		this.inactiveTimeout = DEFAULT_INACTIVE_TIMEOUT;
	}
	
	public Tunnel openTunnel() throws IOException {
		return openTunnel(null);
	}
	
	public Tunnel openTunnel(InetAddress acceptAddress) throws IOException {
		
		int localPortRange = maxLocalPort - minLocalPort;
		
		if(localPortRange < 0) {
			return openTunnel(0, acceptAddress);
		}
		else if(localPortRange == 0) {
			return openTunnel(minLocalPort, acceptAddress);
		}
		
		Random random = new Random();
		int localPortAttempts = localPortRange * 2;
		
		try {
			for(int attempts=0; attempts<localPortAttempts; attempts++) {
				try {
					return openTunnel(random.nextInt(localPortRange - 1) + minLocalPort, acceptAddress);
				}
				catch(IOException e) {
					Thread.sleep(CREATE_TUNNEL_SLEEP);
				}
			}
		}
		catch(InterruptedException e) {
			IOException ioException = new IOException("Interrupted while openning tunnel on random local port");
			logger.warn(ioException.getMessage(), e);
			throw ioException;
		}
		
		throw new IOException("Unable to open tunnel on random local port.");
	}

	protected Tunnel openTunnel(int localPort, InetAddress acceptAddress) throws IOException {
		if(logger.isDebugEnabled()) {
			logger.debug("Open Tunnel: datagram: " + isDatagram + ": local: " + bindAddress + ":" + localPort + ": remote: " + remoteAddress + ":" + remotePort + ": accept:" + acceptAddress);	
		}
		
		if(isDatagram) {
			return new DatagramSocketTunnel(bindAddress, localPort, acceptAddress, remoteAddress, remotePort, inactiveTimeout);
		}
		else {
			return new SocketTunnel(bindAddress, localPort, acceptAddress, acceptTimeout, remoteAddress, remotePort, inactiveTimeout);
		}
	}
	
	public void setRemoteHost(String remoteHost) throws UnknownHostException {
		setRemoteAddress(InetAddress.getByName(remoteHost));
	}

	public void setLocalPort(int localPort) {
		setMinLocalPort(localPort);
		setMaxLocalPort(localPort);
	}
	
	public InetAddress getBindAddress() {
		return bindAddress;
	}
	public void setBindAddress(InetAddress bindAddress) {
		this.bindAddress = bindAddress;
	}

	public InetAddress getRemoteAddress() {
		return remoteAddress;
	}
	public void setRemoteAddress(InetAddress remoteAddress) {
		this.remoteAddress = remoteAddress;
	}

	public int getRemotePort() {
		return remotePort;
	}
	public void setRemotePort(int remotePort) {
		this.remotePort = remotePort;
	}

	public int getMinLocalPort() {
		return minLocalPort;
	}
	public void setMinLocalPort(int minLocalPort) {
		this.minLocalPort = minLocalPort;
	}

	public int getMaxLocalPort() {
		return maxLocalPort;
	}
	public void setMaxLocalPort(int maxLocalPort) {
		this.maxLocalPort = maxLocalPort;
	}

	public long getInactiveTimeout() {
		return inactiveTimeout;
	}
	public void setInactiveTimeout(long inactiveTimeout) {
		this.inactiveTimeout = inactiveTimeout;
	}

	public boolean isDatagram() {
		return isDatagram;
	}
	public void setDatagram(boolean isDatagram) {
		this.isDatagram = isDatagram;
	}
}
