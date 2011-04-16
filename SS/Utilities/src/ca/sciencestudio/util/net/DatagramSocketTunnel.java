/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     DatagramSocketTunnel class.
 *          
 */
package ca.sciencestudio.util.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import ca.sciencestudio.util.net.Tunnel;

/**
 * @author maxweld
 *
 */
public class DatagramSocketTunnel extends Tunnel {

	private static final int DATAGRAM_SOCKET_FORWARD_BUFFER_SIZE = 8196;
	
	private DatagramSocket localSocket;
	private DatagramSocket remoteSocket;
	
	private DatagramSocketForwardThread localForwardThread;
	
	public DatagramSocketTunnel(String remoteHost, int remotePort, long inactiveTimeout) throws IOException {	
		this(0, remoteHost, remotePort, inactiveTimeout);
	}
	
	public DatagramSocketTunnel(String acceptHost, String remoteHost, int remotePort, long inactiveTimeout) throws IOException {
		this(0, acceptHost, remoteHost, remotePort, inactiveTimeout);
	}
	
	public DatagramSocketTunnel(int localPort, String remoteHost, int remotePort, long inactiveTimeout) throws IOException {
		this((InetAddress)null, localPort, (InetAddress)null, InetAddress.getByName(remoteHost), remotePort, inactiveTimeout);
	}
	
	public DatagramSocketTunnel(int localPort, String acceptHost, String remoteHost, int remotePort, long inactiveTimeout) throws IOException {
		this((InetAddress)null, localPort, InetAddress.getByName(acceptHost), InetAddress.getByName(remoteHost), remotePort, inactiveTimeout);
	}
	
	public DatagramSocketTunnel(InetAddress bindAddress, int localPort, InetAddress acceptAddress, InetAddress remoteAddress, int remotePort, long inactiveTimeout) throws IOException {
		
		if(bindAddress == null) {
			this.localSocket = new DatagramSocket(localPort);
		} else {
			this.localSocket = new DatagramSocket(localPort, bindAddress);
		}
		
		this.bindAddress = localSocket.getInetAddress();
		this.localPort = localSocket.getLocalPort(); 

		this.remoteSocket = new DatagramSocket();
		
		this.remoteAddress = remoteAddress;
		this.remotePort = remotePort;

		this.localForwardThread = new DatagramSocketForwardThread(localSocket, remoteSocket, acceptAddress);
		this.localForwardThread.start();

		Thread monitorTunnelThread = new MonitorTunnelThread(inactiveTimeout);
		monitorTunnelThread.start();
	}
	
	@Override
	public long getLocalBytesForwarded() {
		return localForwardThread.getNBytesForwarded();
	}

	@Override
	public long getRemoteBytesForwarded() {
		return 0L;
	}
	
	@Override
	public boolean isClosed() {
		return (localSocket.isClosed() && remoteSocket.isClosed());
	}
	
	@Override
	public synchronized void close() {
		localSocket.close();
		remoteSocket.close();
		super.close();
	}
	
	protected class DatagramSocketForwardThread extends ForwardThread {
		
		private DatagramSocket inputSocket;
		private DatagramSocket outputSocket;
		private InetAddress acceptAddress;
		
		public DatagramSocketForwardThread(DatagramSocket inputSocket, DatagramSocket outputSocket, InetAddress acceptAddress) {
			this.inputSocket = inputSocket;
			this.outputSocket = outputSocket;
			this.acceptAddress = acceptAddress;
			setDaemon(true);
		}
		
		@Override
		public void run() {
			
			byte buffer[] = new byte[DATAGRAM_SOCKET_FORWARD_BUFFER_SIZE];
			DatagramPacket packet = new DatagramPacket(buffer, 0);
			
			try {
				while(true) {
					packet.setLength(DATAGRAM_SOCKET_FORWARD_BUFFER_SIZE);
					inputSocket.receive(packet);
					if((acceptAddress != null) && !acceptAddress.equals(packet.getAddress())) {
						continue;
					}	
					packet.setAddress(remoteAddress);
					packet.setPort(remotePort);
					outputSocket.send(packet);
					addNBytesForwarded(packet.getLength());
				}
			}
			catch(IOException e) {
				// Ignore Exception //
			}
			close();
			logger.debug("Datagram forward thread done!");
		}
	}
}
