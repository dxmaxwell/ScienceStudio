/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     SocketTunnel class.
 *     
 */
package ca.sciencestudio.util.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import ca.sciencestudio.util.net.Tunnel;

/**
 * @author maxweld
 *
 */
public class SocketTunnel extends Tunnel {

	private static final int SOCKET_FORWARD_BUFFER_SIZE = 4096;
	private static final int SERVER_SOCKET_BACKLOG = 0;
	
	private ServerSocket localServerSocket;
	
	private Socket localSocket = null;
	private Socket remoteSocket = null;
	
	private ForwardThread localForwardThread = null;
	private ForwardThread remoteForwardThread = null;
	
	public SocketTunnel(String remoteHost, int remotePort, long inactiveTimeout) throws IOException {
		this(0L, remoteHost, remotePort, inactiveTimeout);
	}
	
	public SocketTunnel(long acceptTimeout, String remoteHost, int remotePort, long inactiveTimeout) throws IOException {
		this(0, acceptTimeout, remoteHost, remotePort, inactiveTimeout);
	}

	public SocketTunnel(String acceptHost, long acceptTimeout, String remoteHost, int remotePort, long inactiveTimeout) throws IOException {
		this(0, acceptHost, acceptTimeout, remoteHost, remotePort, inactiveTimeout);
	}
	
	public SocketTunnel(int localPort, long acceptTimeout, String remoteHost, int remotePort, long inactiveTimeout) throws IOException {
		this(localPort, (InetAddress)null, acceptTimeout, InetAddress.getByName(remoteHost), remotePort, inactiveTimeout);
	}
	
	public SocketTunnel(int localPort, String acceptHost, long acceptTimeout, String remoteHost, int remotePort, long inactiveTimeout) throws IOException {
		this(localPort, InetAddress.getByName(acceptHost), acceptTimeout, InetAddress.getByName(remoteHost), remotePort, inactiveTimeout);
	}
	
	public SocketTunnel(int localPort, InetAddress acceptAddress, long acceptTimeout, InetAddress remoteAddress, int remotePort, long inactiveTimeout) throws IOException {
		this((InetAddress)null, localPort, acceptAddress, acceptTimeout, remoteAddress, remotePort, inactiveTimeout);
	}
	
	public SocketTunnel(InetAddress bindAddress, int localPort, InetAddress acceptAddress, long acceptTimeout, InetAddress remoteAddress, int remotePort, long inactiveTimeout) throws IOException {
		
		if(bindAddress == null) {
			this.localServerSocket = new ServerSocket(localPort, SERVER_SOCKET_BACKLOG);
		} else {
			this.localServerSocket = new ServerSocket(localPort, SERVER_SOCKET_BACKLOG, bindAddress);
		}
		
		this.bindAddress = localServerSocket.getInetAddress();
		this.localPort = localServerSocket.getLocalPort(); 

		this.remoteAddress = remoteAddress;
		this.remotePort = remotePort;

		Thread acceptThread = new ServerSocketAcceptThread(acceptAddress, inactiveTimeout);
		acceptThread.start();
		
		Thread timeoutThread = new ServerSocketTimeoutThread(acceptThread, acceptTimeout);
		timeoutThread.start();
	}
	
	@Override
	public long getLocalBytesForwarded() {
		if(localForwardThread != null) {
			return localForwardThread.getNBytesForwarded();
		}
		
		return 0L;
	}

	@Override
	public long getRemoteBytesForwarded() {
		if(remoteForwardThread != null) {
			return localForwardThread.getNBytesForwarded();
		}
		
		return 0L;
	}

	@Override
	public boolean isClosed() {
		
		if(localServerSocket.isClosed()) {
			
			boolean isLocalSocketClosed = true;
			if(localSocket != null) {
				isLocalSocketClosed = localSocket.isClosed();
			}
			
			boolean isRemoteSocketClosed = true;
			if(remoteSocket != null) {
				isRemoteSocketClosed = remoteSocket.isClosed();
			}
			
			return (isLocalSocketClosed && isRemoteSocketClosed);
		}
		else {
			return false;
		}
	}

	@Override
	public synchronized void close() {
		
		logger.debug("Close Tunnel!");
		
		if(localServerSocket.isClosed()) {
			logger.debug("Tunnel is open, close it now!");
			try {
				boolean needSleep = false;
				
				if((localSocket != null) && !localSocket.isInputShutdown()) {
					localSocket.shutdownInput();
					needSleep = true;
				}
			
				if((remoteSocket != null) && !remoteSocket.isInputShutdown()) {
					remoteSocket.shutdownInput();
					needSleep = true;
				}
			
				if(needSleep) {
					try {
						Thread.sleep(50L);
					}
					catch(InterruptedException e) {
						logger.warn("Interrupted while waiting for socket input to shutdown.", e);
					}
					needSleep = false;
				}
				
				if((localSocket != null) && !localSocket.isOutputShutdown()) {
					localSocket.shutdownOutput();
					needSleep = true;
				}
			
				if((remoteSocket != null) && !remoteSocket.isOutputShutdown()) {
					remoteSocket.shutdownOutput();
					needSleep = true;
				}
			
				if(needSleep) {
					try {
						Thread.sleep(50L);
					}
					catch(InterruptedException e) {
						logger.warn("Interrupted while waiting for socket output to shutdown.", e);
					}
					needSleep = false;
				}
			
				if((localSocket != null) && !localSocket.isClosed()) {
					localSocket.close();
				}
				
				if((remoteSocket != null) && !remoteSocket.isClosed()) {
					remoteSocket.close();
				}	
			}
			catch(IOException e) {
				logger.warn("Input/Output Exception while closing local and remote sockets.", e);
			}
		}
		else {
			try {
				localServerSocket.close();
			}
			catch(IOException e) {
				logger.warn("Input/Output Exception while closing local server socket.", e);
			}
		}
		
		super.close();
	}
	
	private class ServerSocketAcceptThread extends Thread {
		
		private long timeout;
		private InetAddress address;
		
		public ServerSocketAcceptThread(InetAddress address, long timeout) {
			this.address = address;
			this.timeout = timeout;
			setDaemon(true);
		}
		
		@Override
		public void run() {
			try {
				logger.debug("Tunnel waiting to accept connection");
				localSocket = localServerSocket.accept();
				logger.debug("Tunnel connection from " + localSocket.getInetAddress());
				if((address != null) && !address.equals(localSocket.getInetAddress())) {
					logger.debug("Tunnel connection must be from " + address);
					close();
					return;
				}
			}
			catch(SocketException e) {
				logger.debug("Socket Closed!");
				// Thrown when server socket is closed while accepting connections, // 
				// this is normal if there is no connection before the timeout.     //
				close();
				return;
			}
			catch(IOException e) {
				logger.warn("Input/Output Exception while server socket accepting connections.", e);
				close();
				return;
			}
			
			try {
				if(!localServerSocket.isClosed()) {
					localServerSocket.close();
				}	
			}
			catch(IOException e) {
				logger.warn("Input/Output Exception while server socket closing.", e);
				close();
				return;
			}
			
			try {
				remoteSocket = new Socket(remoteAddress, remotePort);
			}
			catch(IOException e) {
				logger.warn("Input/Output Exception while remote socket connecting.", e);
				close();
				return;
			}
			
			try {
				InputStream localInputStream = localSocket.getInputStream();
				InputStream remoteInputStream = remoteSocket.getInputStream();
			
				OutputStream localOutputStream = localSocket.getOutputStream();
				OutputStream remoteOutputStream = remoteSocket.getOutputStream();
				
				localForwardThread = new SocketForwardThread(localInputStream, remoteOutputStream);
				remoteForwardThread = new SocketForwardThread(remoteInputStream, localOutputStream);
				monitorTunnelThread = new MonitorTunnelThread(timeout);
			}
			catch(IOException e) {
				logger.warn("Input/Output Exception while getting input and output streams.", e);
				close();
				return;
			}
			
			bindAddress = localSocket.getLocalAddress();
			localPort = localSocket.getLocalPort();
			
			localForwardThread.start();
			remoteForwardThread.start();
			
			logger.debug("Start Tunnel Monitor Thread!");
			monitorTunnelThread.start();
			
			logger.debug("Tunnel Accept Thread Done!");
		}
	}
	
	private class ServerSocketTimeoutThread extends Thread {
		
		private Thread acceptThread;
		private long timeout;
		
		public ServerSocketTimeoutThread(Thread acceptThread, long timeout) {
			this.acceptThread = acceptThread;
			this.timeout = timeout;
			setDaemon(true);
		}
		
		@Override
		public void run() {
			try {
				logger.debug("Timeout thread waiting for accept thread");
				acceptThread.join(timeout);
			}
			catch(InterruptedException e) {
				logger.warn("Interrupted while joining server socket accept thread.", e);
			}
			
			if(acceptThread.isAlive()) {
				close();
			}
			logger.debug("Timeout thread done!");
		}
	}
	
	protected class SocketForwardThread extends ForwardThread {
		
		private InputStream inputStream;
		private OutputStream outputStream;
		
		public SocketForwardThread(InputStream inputStream, OutputStream outputStream) {
			this.inputStream = inputStream;
			this.outputStream = outputStream;
			setDaemon(true);
		}
				
		@Override
		public void run() {
			
			int length = 0;
			byte buffer[] = new byte[SOCKET_FORWARD_BUFFER_SIZE];
				
			try {
				while(true) {
					length = inputStream.read(buffer);
					outputStream.write(buffer, 0, length);
					addNBytesForwarded(length);
				}
			}
			catch(IOException e) {
				// Break from continuous while loop. //
			}
			catch(IndexOutOfBoundsException e) {
				logger.debug("Index-out-of-Bounds. InputStream closed?");
				// When inputStream is closed, read returns (-1),   //
				// then this exception will be thrown during write. //
				// Break from continuous while loop.                //
			}
			close();
			logger.debug("Socket forward thread done!");
		}
	}
}
