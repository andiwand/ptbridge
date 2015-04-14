package at.stefl.ptbridge.ptmp;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import at.stefl.ptbridge.ptmp.packet.PTMPAuthenticationChallengePacket;
import at.stefl.ptbridge.ptmp.packet.PTMPAuthenticationRequestPacket;
import at.stefl.ptbridge.ptmp.packet.PTMPAuthenticationResponsePacket;
import at.stefl.ptbridge.ptmp.packet.PTMPAuthenticationStatusPacket;
import at.stefl.ptbridge.ptmp.packet.PTMPDisconnectPacket;
import at.stefl.ptbridge.ptmp.packet.PTMPEncodedPacket;
import at.stefl.ptbridge.ptmp.packet.PTMPNegotiationPacket;
import at.stefl.ptbridge.ptmp.packet.PTMPPacket;
import at.stefl.ptbridge.ptmp.packet.PTMPPacketReader;
import at.stefl.ptbridge.ptmp.packet.PTMPPacketWriter;


public class PTMPConnection {
	
	private static final int DEFAULT_PACKET_QUEUE_SIZE = 100;
	
	private static final String DEFAULT_USERNAME = "";
	private static final String DEFAULT_PASSWORD = "";
	
	private final UUID uuid;
	
	private Socket socket;
	private PTMPPacketReader packetReader;
	private PTMPPacketWriter packetWriter;
	private PacketReceiver packetReceiver;
	private List<PTMPPacketListener> packetListeners = new ArrayList<PTMPPacketListener>();
	private int packetQueueSize = DEFAULT_PACKET_QUEUE_SIZE;
	
	private PTMPConfiguration configuration;
	
	private PTMPConfiguration preferredConfiguration = null;
	
	private PTMPState state = PTMPState.NOT_CONNECTED;
	private List<PTMPStateListener> stateListeners = new ArrayList<PTMPStateListener>();
	
	public PTMPConnection() {
		this(UUID.randomUUID());
	}
	
	public PTMPConnection(UUID uuid) {
		this.uuid = uuid;
	}
	
	public PTMPConnection(UUID uuid, PTMPConfiguration preferredConfiguration) {
		this(uuid);
		
		this.preferredConfiguration = preferredConfiguration;
	}
	
	public int getPacketQueueSize() {
		return packetQueueSize;
	}
	
	public PTMPConfiguration getPreferredConfiguration() {
		return preferredConfiguration;
	}
	
	public PTMPConfiguration getConfiguration() {
		return configuration;
	}
	
	public PTMPState getState() {
		synchronized (state) {
			return state;
		}
	}
	
	public void setPacketQueueSize(int packetQueueSize) {
		this.packetQueueSize = packetQueueSize;
	}
	
	public void setPreferredConfiguration(
			PTMPConfiguration preferredConfiguration) {
		this.preferredConfiguration = preferredConfiguration;
	}
	
	private void setState(PTMPState newState) {
		PTMPState oldState = state;
		state = newState;
		
		if (newState == oldState) return;
		
		synchronized (stateListeners) {
			for (PTMPStateListener stateListener : stateListeners) {
				stateListener.stateChanged(newState, oldState);
			}
		}
	}
	
	public void addPacketListener(PTMPPacketListener packetListener) {
		synchronized (packetListeners) {
			packetListeners.add(packetListener);
		}
	}
	
	public void addStateListener(PTMPStateListener stateListener) {
		synchronized (stateListeners) {
			stateListeners.add(stateListener);
		}
	}
	
	public void removePacketListener(PTMPPacketListener packetListener) {
		synchronized (packetListeners) {
			packetListeners.remove(packetListener);
		}
	}
	
	public void removeStateListener(PTMPStateListener stateListener) {
		synchronized (stateListeners) {
			stateListeners.remove(stateListener);
		}
	}
	
	public void connect(InetAddress address) throws IOException {
		connect(address, DEFAULT_USERNAME, DEFAULT_PASSWORD);
	}
	
	public void connect(InetAddress address, int port) throws IOException {
		connect(address, PTMPAssignments.PORT, DEFAULT_USERNAME,
				DEFAULT_PASSWORD);
	}
	
	public void connect(Socket socket) throws IOException {
		connect(socket, DEFAULT_USERNAME, DEFAULT_PASSWORD);
	}
	
	public void connect(InetAddress address, String password)
			throws IOException {
		connect(address, PTMPAssignments.PORT, DEFAULT_USERNAME, password);
	}
	
	public void connect(InetAddress address, String username, String password)
			throws IOException {
		connect(address, PTMPAssignments.PORT, username, password);
	}
	
	public void connect(InetAddress address, int port, String password)
			throws IOException {
		connect(address, port, DEFAULT_USERNAME, password);
	}
	
	public void connect(InetAddress address, int port, String username,
			String password) throws IOException {
		if (getState() != PTMPState.NOT_CONNECTED)
			throw new IOException("Socket is already connected!");
		
		setState(PTMPState.CONNECTING);
		
		Socket socket = new Socket(address, port);
		connect(socket, username, password);
	}
	
	public void connect(Socket socket, String password) throws IOException {
		connect(socket, DEFAULT_USERNAME, password);
	}
	
	public void connect(Socket socket, String username, String password)
			throws IOException {
		if ((getState() != PTMPState.NOT_CONNECTED)
				&& getState() != PTMPState.CONNECTING)
			throw new IOException("Already connected!");
		
		this.socket = socket;
		packetReader = new PTMPPacketReader(socket.getInputStream());
		packetWriter = new PTMPPacketWriter(socket.getOutputStream());
		
		if (preferredConfiguration == null)
			preferredConfiguration = PTMPConfiguration.DEFAULT;
		
		PTMPEncodedPacket tmp;
		setState(PTMPState.NEGOTIATION);
		
		UUID clientUuid = uuid;
		Date clientTimestamp = new Date();
		PTMPNegotiationPacket negotiationRequest = new PTMPNegotiationPacket(
				PTMPPacket.TYPE_NEGOTIATION_REQUEST, clientUuid,
				preferredConfiguration, clientTimestamp);
		packetWriter.writePacket(negotiationRequest);
		
		tmp = packetReader.readPacket();
		PTMPNegotiationPacket negotiationResponse = new PTMPNegotiationPacket(
				tmp);
		UUID serverUuid = negotiationResponse.getApplicationId();
		Date serverTimestamp = negotiationResponse.getTimestamp();
		configuration = negotiationResponse.getConfiguration();
		
		packetReader.setConfiguration(configuration);
		packetWriter.setConfiguration(configuration);
		
		switch (configuration.getEncryption()) {
		case NONE:
			break;
		case XOR:
			byte[] key = PTMPKeyUtil.getBytes(serverUuid, clientUuid,
					serverTimestamp, clientTimestamp);
			packetReader.setDecryptionKey(key);
			packetWriter.setEncryptionKey(key);
		default:
			throw new IllegalStateException("Unsupported encryption!");
		}
		
		setState(PTMPState.AUTHETICATING);
		
		PTMPAuthenticationRequestPacket authenticationRequest = new PTMPAuthenticationRequestPacket(
				username);
		packetWriter.writePacket(authenticationRequest);
		
		tmp = packetReader.readPacket();
		PTMPAuthenticationChallengePacket authenticationChallenge = new PTMPAuthenticationChallengePacket(
				tmp);
		String challenge = authenticationChallenge.getChallengeText();
		
		String digest = configuration.getAuthentication().calculateDigest(
				password, challenge);
		PTMPAuthenticationResponsePacket authenticationResponse = new PTMPAuthenticationResponsePacket(
				username, digest);
		packetWriter.writePacket(authenticationResponse);
		
		tmp = packetReader.readPacket();
		if (tmp.getType() == PTMPPacket.TYPE_DISCONNECT)
			throw new IOException("Authentication failed!");
		PTMPAuthenticationStatusPacket authenticationStatus = new PTMPAuthenticationStatusPacket(
				tmp);
		if (!authenticationStatus.getStatus())
			throw new IOException("Authentication failed!");
		
		setState(PTMPState.ESTABLISHED);
		packetReceiver = new PacketReceiver();
	}
	
	public void send(PTMPPacket packet) throws IOException {
		if (state != PTMPState.ESTABLISHED)
			throw new IOException("The connection is not ready!");
		
		packetWriter.writePacket(packet);
	}
	
	public void close() throws IOException {
		try {
			if (getState() == PTMPState.NOT_CONNECTED) return;
			
			PTMPDisconnectPacket disconnect = new PTMPDisconnectPacket();
			packetWriter.writePacket(disconnect);
			
			setState(PTMPState.NOT_CONNECTED);
		} finally {
			if (packetReceiver != null) packetReceiver.interrupt();
			packetReceiver = null;
			
			if (socket != null) socket.close();
			socket = null;
			packetReader = null;
			packetWriter = null;
		}
	}
	
	private void handlePacket(PTMPEncodedPacket packet) {
		if (packet.getType() == PTMPPacket.TYPE_DISCONNECT) {
			try {
				close();
			} catch (IOException e) {}
			
			return;
		}
		
		synchronized (packetListeners) {
			for (PTMPPacketListener packetListener : packetListeners) {
				packetListener.receivePacket(packet);
			}
		}
	}
	
	private class PacketReceiver extends Thread {
		public PacketReceiver() {
			start();
		}
		
		public void run() {
			try {
				while (!isInterrupted()) {
					PTMPEncodedPacket packet = packetReader.readPacket();
					handlePacket(packet);
				}
			} catch (IOException e) {
				try {
					close();
				} catch (Throwable t) {}
			}
		}
	}
	
}