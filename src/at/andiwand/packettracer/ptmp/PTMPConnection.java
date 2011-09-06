package at.andiwand.packettracer.ptmp;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import at.andiwand.packettracer.ptmp.packet.PTMPAuthenticationChallengePacket;
import at.andiwand.packettracer.ptmp.packet.PTMPAuthenticationRequestPacket;
import at.andiwand.packettracer.ptmp.packet.PTMPAuthenticationResponsePacket;
import at.andiwand.packettracer.ptmp.packet.PTMPAuthenticationStatusPacket;
import at.andiwand.packettracer.ptmp.packet.PTMPDisconnectPacket;
import at.andiwand.packettracer.ptmp.packet.PTMPEncodedPacket;
import at.andiwand.packettracer.ptmp.packet.PTMPNegotiationPacket;
import at.andiwand.packettracer.ptmp.packet.PTMPPacket;
import at.andiwand.packettracer.ptmp.packet.PTMPPacketReader;
import at.andiwand.packettracer.ptmp.packet.PTMPPacketWriter;


public class PTMPConnection {
	
	public static final int DEFAULT_PORT = 38000;
	
	public static final int DEFAULT_PACKET_QUEUE_SIZE = 100;
	
	public static final String DEFAULT_USERNAME = "";
	public static final String DEFAULT_PASSWORD = "";
	
	public static final PTMPConfiguration DEFAULT_CONFIGURATION = PTMPConfiguration.DEFAULT;
	public static final int DEFAULT_KEEPALIVE_PERIOD = 0;
	
	
	public static final PTMPConfiguration NO_PREFERRED_CONFIGURATION = null;
	public static final int NO_PREFERRED_KEEPALIVE_PERIOD = -1;
	
	
	
	
	private final UUID uuid;
	
	
	private Socket socket;
	private PTMPPacketReader packetReader;
	private PTMPPacketWriter packetWriter;
	private PacketReceiver packetReceiver;
	private List<PTMPPacketListener> packetListeners = new ArrayList<PTMPPacketListener>();
	private int packetQueueSize = DEFAULT_PACKET_QUEUE_SIZE;
	
	private PTMPConfiguration configuration;
	private int keepAlivePeriod;
	
	private PTMPConfiguration preferredConfiguration = NO_PREFERRED_CONFIGURATION;
	private int preferredKeepAlivePeriod = NO_PREFERRED_KEEPALIVE_PERIOD;
	
	
	private PTMPState state = PTMPState.NOT_CONNECTED;
	private List<PTMPStateListener> stateListeners = new ArrayList<PTMPStateListener>();
	
	
	
	public PTMPConnection() {
		this(UUID.randomUUID());
	}
	public PTMPConnection(UUID uuid) {
		this.uuid = uuid;
	}
	public PTMPConnection(UUID uuid, PTMPConfiguration preferredConfiguration,
			int preferredKeepAlivePeriod) {
		this(uuid);
		
		this.preferredConfiguration = preferredConfiguration;
		this.preferredKeepAlivePeriod = preferredKeepAlivePeriod;
	}
	
	
	
	public int getPacketQueueSize() {
		return packetQueueSize;
	}
	public PTMPConfiguration getConfiguration() {
		return configuration;
	}
	public int getKeepAlivePeriod() {
		return keepAlivePeriod;
	}
	public PTMPConfiguration getPreferredConfiguration() {
		return preferredConfiguration;
	}
	public int getPreferredKeepAlivePeriod() {
		return preferredKeepAlivePeriod;
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
	public void setPreferredKeepAlivePeriod(int preferredKeepAlivePeriod) {
		this.preferredKeepAlivePeriod = preferredKeepAlivePeriod;
	}
	private void setState(PTMPState newState) {
		synchronized (state) {
			PTMPState oldState = state;
			state = newState;
			
			if (newState != oldState) {
				synchronized (stateListeners) {
					for (PTMPStateListener stateListener : stateListeners) {
						stateListener.stateChanged(newState, oldState);
					}
				}
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
		connect(address, DEFAULT_PORT, DEFAULT_USERNAME, DEFAULT_PASSWORD);
	}
	public void connect(Socket socket) throws IOException {
		connect(socket, DEFAULT_USERNAME, DEFAULT_PASSWORD);
	}
	public void connect(InetAddress address, String password)
			throws IOException {
		connect(address, DEFAULT_PORT, DEFAULT_USERNAME, password);
	}
	public void connect(InetAddress address, String username, String password)
			throws IOException {
		connect(address, DEFAULT_PORT, username, password);
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
		
		connect(new Socket(address, port), username, password);
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
		
		if (preferredConfiguration == NO_PREFERRED_CONFIGURATION)
			preferredConfiguration = DEFAULT_CONFIGURATION;
		if (preferredKeepAlivePeriod == NO_PREFERRED_KEEPALIVE_PERIOD)
			preferredKeepAlivePeriod = DEFAULT_KEEPALIVE_PERIOD;
		
		
		PTMPEncodedPacket tmp;
		setState(PTMPState.NEGOTIATION);
		
		UUID clientUuid = uuid;
		PTMPTimestamp clientTimestamp = new PTMPTimestamp();
		PTMPNegotiationPacket negotiationRequest = new PTMPNegotiationPacket(
				PTMPNegotiationPacket.TYPE_REQUEST, clientUuid,
				preferredConfiguration, clientTimestamp,
				preferredKeepAlivePeriod);
		packetWriter.writePacket(negotiationRequest);
		
		tmp = packetReader.readPacket();
		PTMPNegotiationPacket negotiationResponse = new PTMPNegotiationPacket(
				tmp);
		UUID serverUuid = negotiationResponse.getApplicationId();
		PTMPTimestamp serverTimestamp = negotiationResponse
				.getTimestamp();
		configuration = negotiationResponse.getConfiguration();
		keepAlivePeriod = negotiationRequest.getKeepAlivePeriod();
		
		packetReader.setConfiguration(configuration);
		packetWriter.setConfiguration(configuration);
		
		if (configuration.encryption() == PTMPConfiguration.ENCRYPTION_XOR) {
			byte[] key = PTMPKey.createKey(serverUuid, clientUuid,
					serverTimestamp, clientTimestamp);
			
			packetReader.setDecryptionKey(key);
			packetWriter.setEncryptionKey(key);
		}
		
		setState(PTMPState.AUTHETICATING);
		
		PTMPAuthenticationRequestPacket authenticationRequest = new PTMPAuthenticationRequestPacket(
				username);
		packetWriter.writePacket(authenticationRequest);
		
		tmp = packetReader.readPacket();
//		PTMPAuthenticationChallengePacket authenticationChallenge = new PTMPAuthenticationChallengePacket(
//				tmp);
		new PTMPAuthenticationChallengePacket(tmp);
		
		String digest = configuration.getAuthenticationMethod()
				.calculateDigest(password);
		PTMPAuthenticationResponsePacket authenticationResponse = new PTMPAuthenticationResponsePacket(
				username, digest);
		packetWriter.writePacket(authenticationResponse);
		
		tmp = packetReader.readPacket();
		if (tmp.getType() == PTMPPacket.TYPE_DISCONNECT)
			throw new IOException("Authentication failed");
		PTMPAuthenticationStatusPacket authenticationStatus = new PTMPAuthenticationStatusPacket(
				tmp);
		
		if (!authenticationStatus.getStatus())
			throw new IOException("Authentication failed");
		
		setState(PTMPState.ESTABLISHED);
		
		
		packetReceiver = new PacketReceiver();
	}
	
	public void accept(Socket socket,
			PTMPAuthenticationManager authenticationManager) {
		// TODO implement
	}
	
	
	public void send(PTMPPacket packet) throws IOException {
		if (state != PTMPState.ESTABLISHED)
			throw new IOException("The connection is not ready!");
		
		packetWriter.writePacket(packet);
	}
	
	
	public void close() throws IOException {
		try {
			if (getState() != PTMPState.NOT_CONNECTED) {
				PTMPDisconnectPacket disconnect = new PTMPDisconnectPacket();
				packetWriter.writePacket(disconnect);
				
				setState(PTMPState.NOT_CONNECTED);
			}
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
				while(!isInterrupted()) {
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