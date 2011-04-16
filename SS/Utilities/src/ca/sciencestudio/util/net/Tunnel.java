/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     Tunnel class.
 *     
 */
package ca.sciencestudio.util.net;

import java.net.InetAddress;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author maxweld
 *
 */
public abstract class Tunnel {
	
	protected Thread monitorTunnelThread = null;
	
	protected InetAddress bindAddress = null;
	protected int localPort = 0;
	
	protected InetAddress remoteAddress = null;
	protected int remotePort = 0;
	
	protected Log logger = LogFactory.getLog(getClass());
	
	public abstract long getLocalBytesForwarded();
	public abstract long getRemoteBytesForwarded();
	
	public abstract boolean isClosed();
	
	public synchronized void close() {
		if((monitorTunnelThread != null) && monitorTunnelThread.isAlive()) {
			logger.debug("Interrupt Monitor Thread!");
			monitorTunnelThread.interrupt();
		}
	}
	
	public InetAddress getBindAddress() {
		return bindAddress;
	}

	public int getLocalPort() {
		return localPort;
	}
	
	public InetAddress getRemoteAddress() {
		return remoteAddress;
	}

	public int getRemotePort() {
		return remotePort;
	}
	
	protected class MonitorTunnelThread extends Thread {
		
		private long timeout;
		
		private long localBytesForwarded;
		private long remoteBytesForwarded;
		
		public MonitorTunnelThread(long timeout) {
			this.timeout = timeout;
			localBytesForwarded = 0L;
			remoteBytesForwarded = 0L;
			setDaemon(true);
		}
		
		@Override
		public void run() {
			while(true) {
				try {
					logger.debug("Monitor Sleeping!");
					sleep(timeout);
					logger.debug("Monitor Sleeping Done!");
				}
				catch(InterruptedException e) {
					logger.debug("Monitor Thread Interrupted");
					break;
				}
				
				if(isClosed()) {
					break;
				}
				
				if(!isActive()) {
					close();
					break;
				}
			}
			
			logger.debug("Monitor Thread Done!");
		}
		
		protected boolean isActive() {
			long bytesForwarded;
			boolean localActivity = false;
			bytesForwarded = getLocalBytesForwarded();
			if(logger.isDebugEnabled()) {
				logger.debug("Local Bytes Forwarded: " + bytesForwarded + " (" + localBytesForwarded + ")");
			}
			if(bytesForwarded > localBytesForwarded) {
				localBytesForwarded = bytesForwarded;
				localActivity = true;
			}
			boolean remoteActivity = false;
			bytesForwarded = getRemoteBytesForwarded();
			if(logger.isDebugEnabled()) {
				logger.debug("Remote Bytes Forwarded: " + bytesForwarded + " (" + remoteBytesForwarded + ")");
			}
			if(bytesForwarded > remoteBytesForwarded) {
				remoteBytesForwarded = bytesForwarded;
				remoteActivity = true;
			}
			return (localActivity || remoteActivity);
		}
	}
	
	protected abstract class ForwardThread extends Thread {
		
		protected long nBytesForwarded = 0;

		public synchronized long getNBytesForwarded() {
			return nBytesForwarded;
		}
		
		protected synchronized void addNBytesForwarded(long nBytesForwarded) {
			this.nBytesForwarded += nBytesForwarded;
		}
	}
}
