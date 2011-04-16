/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     TunnelManager class.
 *          
 */
package ca.sciencestudio.util.net;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Collections;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author maxweld
 *
 */
public class TunnelManager {

	private static final long DEFAULT_TUNNEL_SERIAL_ID = 1000L;

	private static final String TUNNEL_SERIAL_ID_FORMAT = "tunnel-%d"; 
	
	private TunnelFactory tunnelFactory;
	private Map<String, Tunnel> tunnelMap;
	private long tunnelSerialId;
	
	protected Log logger = LogFactory.getLog(getClass());
	
	public TunnelManager() {
		this(new TunnelFactory());
	}
	
	public TunnelManager(TunnelFactory tunnelFactory) {
		this.tunnelMap = Collections.synchronizedMap(new HashMap<String,Tunnel>());
		this.tunnelSerialId = DEFAULT_TUNNEL_SERIAL_ID;
		this.tunnelFactory = tunnelFactory;
	}
	
	public String openTunnel() throws IOException {
		return openTunnel(null);
	}
		
	public String openTunnel(InetAddress acceptAddress) throws IOException {
		String tunnelId;
		do {
			tunnelId = String.format(TUNNEL_SERIAL_ID_FORMAT, tunnelSerialId++);
		} while(contains(tunnelId));
		
		open(tunnelId);
		return tunnelId;
	}
		
	public Tunnel open(String tunnelId) throws IOException {
		return open(tunnelId, true);
	}
	
	public Tunnel open(String tunnelId, InetAddress acceptAddress) throws IOException {
		return open(tunnelId, acceptAddress, true);
	}
	
	public Tunnel open(String tunnelId, boolean close) throws IOException {
		return open(tunnelFactory, tunnelId, close);
	}
	
	public Tunnel open(String tunnelId, InetAddress acceptAddress, boolean close) throws IOException {
		return open(tunnelFactory, tunnelId, acceptAddress, close);
	}
	
	public Tunnel open(TunnelFactory tunnelFactory, String tunnelId) throws IOException {
		return open(tunnelFactory, tunnelId, true);
	}
	
	public Tunnel open(TunnelFactory tunnelFactory, String tunnelId, InetAddress acceptAddress) throws IOException {
		return open(tunnelFactory, tunnelId, acceptAddress, true);
	}
	
	public Tunnel open(TunnelFactory tunnelFactory, String tunnelId, boolean close) throws IOException {
		return open(tunnelFactory, tunnelId, null, close);
	}
	
	public Tunnel open(TunnelFactory tunnelFactory, String tunnelId, InetAddress acceptAddress, boolean close) throws IOException {
		
		if(close) {
			close(tunnelId);
		}
		
		if(logger.isDebugEnabled()) {
			logger.debug("Open tunnel with id: " + tunnelId);
		}
		
		Tunnel tunnel = tunnelFactory.openTunnel(acceptAddress);
		tunnelMap.put(tunnelId, tunnel);
		return tunnel;
	}
	
	public Tunnel get(String tunnelId) {
		return tunnelMap.get(tunnelId);
	}
	
	public boolean contains(String tunnelId) {
		return tunnelMap.containsKey(tunnelId);
	}
	
	public void close(String tunnelId) {
		Tunnel tunnel = get(tunnelId);
		if(tunnel != null) {
			if(logger.isDebugEnabled()) {
				logger.debug("Close tunnel with id: " + tunnelId);
			}
			tunnel.close();
		}
	}
	
	public void closeAll() {
		for(String tunnelId : tunnelMap.keySet()) {
			close(tunnelId);
		}
	}
	
	public void remove(String tunnelId) {
		Tunnel tunnel = tunnelMap.remove(tunnelId);
		if((tunnel != null) && logger.isDebugEnabled()) {
			logger.debug("Remove tunnel with id: " + tunnelId);
		}
	}
	
	public void removeAll() {
		// Careful to avoid 'ConcurrentModificationException' //
		Set<String> tunnelIds = new HashSet<String>(tunnelMap.keySet());
		for(String tunnelId : tunnelIds) {
			remove(tunnelId);
		}
	}
	
	public void closeAndRemove(String tunnelId) {
		close(tunnelId);
		remove(tunnelId);
	}
	
	public void closeAndRemoveAll() {
		closeAll();
		removeAll();
	}

	public TunnelFactory getTunnelFactory() {
		return tunnelFactory;
	}
	public void setTunnelFactory(TunnelFactory tunnelFactory) {
		this.tunnelFactory = tunnelFactory;
	}

	public Map<String, Tunnel> getTunnelMap() {
		return tunnelMap;
	}
	public void setTunnelMap(Map<String, Tunnel> tunnelMap) {
		this.tunnelMap = tunnelMap;
	}
}
