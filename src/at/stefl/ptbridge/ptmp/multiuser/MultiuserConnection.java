package at.stefl.ptbridge.ptmp.multiuser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import at.stefl.ptbridge.ptmp.PTMPConnection;
import at.stefl.ptbridge.ptmp.PTMPPacketListener;
import at.stefl.ptbridge.ptmp.multiuser.packet.MultiuserInitialisationPacket;
import at.stefl.ptbridge.ptmp.multiuser.packet.MultiuserLinkDefinitionPacket;
import at.stefl.ptbridge.ptmp.multiuser.packet.MultiuserLinkDefinitionPacket.ChangeType;
import at.stefl.ptbridge.ptmp.multiuser.packet.MultiuserNetworkNamePacket;
import at.stefl.ptbridge.ptmp.multiuser.packet.MultiuserNetworkPacket;
import at.stefl.ptbridge.ptmp.multiuser.packet.MultiuserPacket;
import at.stefl.ptbridge.ptmp.multiuser.packet.MultiuserXYZPacket;
import at.stefl.ptbridge.ptmp.packet.PTMPEncodedPacket;
import at.stefl.ptbridge.ptmp.packet.PTMPPacket;


public class MultiuserConnection {
	
	private static interface ConnectionManager extends MultiuserPacketListener {}
	
	public static final String DEFAULT_USERNAME = "";
	public static final String DEFAULT_NETWORK_NAME = "Java Multiuser Connection";
	
	private class PacketListener implements PTMPPacketListener {
		public void receivePacket(PTMPEncodedPacket packet) {
			if ((packet.getType() < MultiuserPacket.TYPE_MIN)
					|| (packet.getType() > MultiuserPacket.TYPE_MAX)) return;
			
			handlePacket(packet);
		}
	}
	
	private class ClientConnectionManager implements ConnectionManager {
		public void receivePacket(PTMPEncodedPacket packet) {
			try {
				switch (packet.getType()) {
				case MultiuserPacket.TYPE_INITIALISATION_RESPONSE:
					// MultiuserInitialisationPacket initialisationResponse =
					// new MultiuserInitialisationPacket(
					// packet);
					
					MultiuserXYZPacket xyz = new MultiuserXYZPacket(0);
					sendImpl(xyz);
					
					MultiuserNetworkNamePacket networkName = new MultiuserNetworkNamePacket(
							MultiuserConnection.this.networkName);
					sendImpl(networkName);
					
					break;
				case MultiuserPacket.TYPE_XYZ:
					synchronized (linkDefinitionMap) {
						for (Map.Entry<Integer, MultiuserLinkDefinition> entry : linkDefinitionMap
								.entrySet()) {
							changeLink(ChangeType.NEW, entry.getKey(), entry
									.getValue());
						}
						
						connected = true;
					}
					
					synchronized (connectionMonitor) {
						connectionMonitor.notify();
					}
					
					break;
				}
			} catch (IOException e) {
				synchronized (connectionMonitor) {
					connectionMonitor.notify();
				}
				
				connectionError = e;
			}
		}
	}
	
	private PTMPConnection ptmpConnection;
	private PacketListener ptmpPacketListener;
	
	private ConnectionManager connectionManager;
	private Object connectionMonitor = new Object();
	private IOException connectionError;
	private boolean connected;
	
	private List<MultiuserPacketListener> packetListeners = new ArrayList<MultiuserPacketListener>();
	
	private final UUID uuid;
	private String networkName;
	private String remoteNetworkName;
	
	private Map<Integer, MultiuserLinkDefinition> linkDefinitionMap = new HashMap<Integer, MultiuserLinkDefinition>();
	private List<MultiuserLinkListener> linkListeners = new ArrayList<MultiuserLinkListener>();
	private List<MultiuserLinkDefinitionPacket> linkDefinitionPacketQueue = new LinkedList<MultiuserLinkDefinitionPacket>();
	private Map<Integer, MultiuserLinkDefinition> remoteLinkDefinitionMap = new HashMap<Integer, MultiuserLinkDefinition>();
	private Map<Integer, MultiuserLinkAdapter> linkAdapterMap = new HashMap<Integer, MultiuserLinkAdapter>();
	
	public MultiuserConnection() {
		this(DEFAULT_NETWORK_NAME);
	}
	
	public MultiuserConnection(String networkName) {
		this(UUID.randomUUID(), networkName);
	}
	
	public MultiuserConnection(UUID uuid, String networkName) {
		this.uuid = uuid;
		this.networkName = networkName;
	}
	
	public PTMPConnection getPtmpConnection() {
		return ptmpConnection;
	}
	
	public UUID getUuid() {
		return uuid;
	}
	
	public String getNetworkName() {
		return networkName;
	}
	
	public String getRemoteNetworkName() {
		return remoteNetworkName;
	}
	
	public int getLinkIdByDefinition(MultiuserLinkDefinition definition) {
		synchronized (linkDefinitionMap) {
			for (Map.Entry<Integer, MultiuserLinkDefinition> entry : linkDefinitionMap
					.entrySet()) {
				if (entry.getValue().equals(definition)) return entry.getKey();
			}
			
			return -1;
		}
	}
	
	public MultiuserLinkAdapter getLinkAdapter(int linkId) {
		return linkAdapterMap.get(linkId);
	}
	
	public MultiuserLinkAdapter getLinkAdapter(
			MultiuserLinkDefinition definition) {
		return linkAdapterMap.get(getLinkIdByDefinition(definition));
	}
	
	public void setNetworkName(String networkName) throws IOException {
		this.networkName = networkName;
		
		if (!connected) return;
		
		MultiuserNetworkNamePacket network = new MultiuserNetworkNamePacket(
				networkName);
		sendImpl(network);
	}
	
	public void addPacketListener(MultiuserPacketListener packetListener) {
		synchronized (packetListeners) {
			packetListeners.add(packetListener);
		}
	}
	
	public void addMultiuserLink(int linkId, MultiuserLinkDefinition definition)
			throws IOException {
		synchronized (linkDefinitionMap) {
			if (linkDefinitionMap.containsKey(linkId))
				removeMultiuserLink(linkId);
			
			if (linkDefinitionMap.containsValue(definition))
				removeMultiuserLink(definition);
			
			linkDefinitionMap.put(linkId, definition);
			linkAdapterMap.put(linkId, new MultiuserLinkAdapter(this, linkId));
			
			if (connected) {
				changeLink(ChangeType.NEW, linkId, definition);
			}
		}
	}
	
	public void addLinkListener(MultiuserLinkListener linkListener) {
		synchronized (linkListeners) {
			linkListeners.add(linkListener);
		}
	}
	
	public void removePacketListener(MultiuserPacketListener packetListener) {
		synchronized (packetListeners) {
			packetListeners.remove(packetListener);
		}
	}
	
	public void removeMultiuserLink(int linkId) throws IOException {
		synchronized (linkDefinitionMap) {
			MultiuserLinkDefinition link = linkDefinitionMap.remove(linkId);
			
			if (link == null) return;
			
			link.setType(MultiuserLinkType.NONE);
			link.setInterfaceName("");
			
			linkDefinitionMap.remove(linkId);
			MultiuserLinkAdapter linkAdapter = linkAdapterMap.get(linkId);
			if (linkAdapter != null) linkAdapter.close();
			linkAdapterMap.remove(linkId);
			
			if (connected) {
				ChangeType changeType;
				
				if (remoteLinkDefinitionMap.containsKey(linkId)) changeType = ChangeType.DETACH;
				else changeType = ChangeType.REMOVE;
				
				changeLink(changeType, linkId, link);
			}
		}
	}
	
	public void removeMultiuserLink(MultiuserLinkDefinition definition)
			throws IOException {
		removeMultiuserLink(getLinkIdByDefinition(definition));
	}
	
	public void removeLinkListener(MultiuserLinkListener linkListener) {
		synchronized (linkListeners) {
			linkListeners.remove(linkListener);
		}
	}
	
	public void connect(PTMPConnection ptmpConnection) throws IOException {
		connect(ptmpConnection, DEFAULT_USERNAME);
	}
	
	public synchronized void connect(PTMPConnection ptmpConnection,
			String username) throws IOException {
		if (connected) throw new IOException("Already connected!");
		
		this.ptmpConnection = ptmpConnection;
		ptmpPacketListener = new PacketListener();
		ptmpConnection.addPacketListener(ptmpPacketListener);
		
		connectionManager = new ClientConnectionManager();
		
		MultiuserInitialisationPacket initialisationRequest = new MultiuserInitialisationPacket(
				MultiuserInitialisationPacket.TYPE_REQUEST, username, uuid);
		sendImpl(initialisationRequest);
		
		try {
			synchronized (connectionMonitor) {
				connectionMonitor.wait();
			}
		} catch (InterruptedException e) {}
		
		connectionManager = null;
		
		if (!connected) {
			if (connectionError != null) throw connectionError;
			throw new IOException("Multiuser connection failed!");
		}
		
		for (MultiuserLinkAdapter linkAdapter : linkAdapterMap.values())
			linkAdapter.connect();
		
		for (MultiuserLinkDefinitionPacket linkPacket : linkDefinitionPacketQueue)
			fireLinkAdded(linkPacket.getLinkId(), linkPacket.getDefinition());
		
		linkDefinitionPacketQueue.clear();
	}
	
	public void send(PTMPPacket packet) throws IOException {
		if (!connected) throw new IOException("The connection is not ready!");
		
		sendImpl(packet);
	}
	
	private void sendImpl(PTMPPacket packet) throws IOException {
		if (!MultiuserPacket.legalMultiuserType(packet.getType()))
			throw new IOException("Illegal packet type!");
		
		ptmpConnection.send(packet);
	}
	
	public void close() {
		ptmpConnection.removePacketListener(ptmpPacketListener);
		connectionError = null;
		remoteNetworkName = null;
		remoteLinkDefinitionMap.clear();
		
		for (MultiuserLinkAdapter linkAdapter : linkAdapterMap.values()) {
			linkAdapter.close();
		}
		
		connected = false;
	}
	
	private void handlePacket(PTMPEncodedPacket packet) {
		switch (packet.getType()) {
		case MultiuserPacket.TYPE_LINK_DEFINITION:
			MultiuserLinkDefinitionPacket linkPacket = new MultiuserLinkDefinitionPacket(
					packet);
			int linkId = linkPacket.getLinkId();
			MultiuserLinkDefinition definition = linkPacket.getDefinition();
			
			switch (linkPacket.getChangeType()) {
			case NEW:
			case OLD:
				if (connected) {
					if (remoteLinkDefinitionMap.containsKey(linkId)) fireLinkChanged(
							linkId, definition);
					else fireLinkAdded(linkId, definition);
				}
				
				remoteLinkDefinitionMap.put(linkPacket.getLinkId(), linkPacket
						.getDefinition());
				break;
			case DETACH:
				if (connected && remoteLinkDefinitionMap.containsKey(linkId))
					fireLinkDetached(linkId, definition);
				
				remoteLinkDefinitionMap.remove(linkId);
				break;
			case REMOVE:
				break;
			}
			
			if (!connected) linkDefinitionPacketQueue.add(linkPacket);
			
			return;
		case MultiuserPacket.TYPE_NETWORK_NAME:
			MultiuserNetworkNamePacket networkName = new MultiuserNetworkNamePacket(
					packet);
			remoteNetworkName = networkName.getNetworkName();
			return;
		case MultiuserPacket.TYPE_NETWORK_PACKET:
			System.out.println(new String(packet.getValue()).replaceAll("\0", " | "));
			//System.out.println(Arrays.toString(packet.getValue()));
			System.out.println();
			
			MultiuserNetworkPacket networkPacket = new MultiuserNetworkPacket(
					packet);
			MultiuserLinkAdapter linkAdapter = linkAdapterMap.get(networkPacket
					.getLinkId());
			
			if (linkAdapter != null)
				linkAdapter.queueNetworkPacket(networkPacket);
			
			return;
		}
		
		if (!connected) {
			if (connectionManager != null)
				connectionManager.receivePacket(packet);
			
			return;
		}
		
		synchronized (packetListeners) {
			for (MultiuserPacketListener packetListener : packetListeners) {
				packetListener.receivePacket(packet);
			}
		}
	}
	
	public void changeMultiuserLink(int linkId,
			MultiuserLinkDefinition definition) throws IOException {
		synchronized (linkDefinitionMap) {
			if (!linkDefinitionMap.get(linkId).equals(definition)) return;
			
			linkDefinitionMap.put(linkId, definition);
			
			if (connected) changeLink(ChangeType.NEW, linkId, definition);
		}
	}
	
	private void changeLink(ChangeType changeType, int linkId,
			MultiuserLinkDefinition link) throws IOException {
		MultiuserLinkDefinitionPacket linkPacket = new MultiuserLinkDefinitionPacket(
				changeType, linkId, link);
		sendImpl(linkPacket);
	}
	
	private void fireLinkAdded(int linkId, MultiuserLinkDefinition definition) {
		synchronized (linkListeners) {
			for (MultiuserLinkListener linkListener : linkListeners) {
				linkListener.linkAdded(linkId, definition);
			}
		}
	}
	
	private void fireLinkChanged(int linkId, MultiuserLinkDefinition definition) {
		synchronized (linkListeners) {
			for (MultiuserLinkListener linkListener : linkListeners) {
				linkListener.linkChanged(linkId, definition);
			}
		}
	}
	
	private void fireLinkDetached(int linkId, MultiuserLinkDefinition definition) {
		synchronized (linkListeners) {
			for (MultiuserLinkListener linkListener : linkListeners) {
				linkListener.linkDetached(linkId, definition);
			}
		}
	}
	
}