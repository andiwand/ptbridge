package at.stefl.ptbridge.main;

import java.net.InetAddress;

import at.stefl.packetsocket.EthernetSocket;
import at.stefl.ptbridge.ptmp.PTMPAuthentication;
import at.stefl.ptbridge.ptmp.PTMPCompression;
import at.stefl.ptbridge.ptmp.PTMPConfiguration;
import at.stefl.ptbridge.ptmp.PTMPConnection;
import at.stefl.ptbridge.ptmp.PTMPEncoding;
import at.stefl.ptbridge.ptmp.PTMPEncryption;
import at.stefl.ptbridge.ptmp.PTMPState;
import at.stefl.ptbridge.ptmp.PTMPStateListener;
import at.stefl.ptbridge.ptmp.multiuser.MultiuserConnection;
import at.stefl.ptbridge.ptmp.multiuser.MultiuserInterfaceType;
import at.stefl.ptbridge.ptmp.multiuser.MultiuserLinkAdapter;
import at.stefl.ptbridge.ptmp.multiuser.MultiuserLinkDefinition;
import at.stefl.ptbridge.ptmp.multiuser.MultiuserLinkType;
import at.stefl.ptbridge.traverser.EthernetTraverser;


public class Main {
	
	public static final String USAGE = "usage: ptbridge <interface> <pt_ip>:<pt_port> <password>";
	
	public static void main(String[] args) throws Throwable {
		String interfaze;
		InetAddress address;
		int port;
		String password;
		
		try {
			interfaze = args[0];
			String[] tmp = args[1].split(":");
			address = InetAddress.getByName(tmp[0]);
			port = Integer.parseInt(tmp[1]);
			password = args[2];
		} catch (Throwable t) {
			System.err.println(USAGE);
			System.exit(1);
			return;
		}
		
		EthernetSocket ethernetSocket = new EthernetSocket(
				EthernetSocket.PROTOCOL_ALL);
		ethernetSocket.bind(interfaze);
		ethernetSocket.enablePromiscMode(interfaze);
		
		PTMPConnection ptmpConnection = new PTMPConnection();
		ptmpConnection.setPreferredConfiguration(new PTMPConfiguration(
				PTMPEncoding.TEXT, PTMPEncryption.NONE, PTMPCompression.NO,
				PTMPAuthentication.CLEAR_TEXT, 0));
		ptmpConnection.connect(address, port, "PacketTracer Bridge", password);
		MultiuserConnection multiuserConnection = new MultiuserConnection(
				"Ethernet");
		MultiuserLinkDefinition linkDefinition = new MultiuserLinkDefinition(
				"Ethernet Bridge Interface");
		linkDefinition.setType(MultiuserLinkType.STRAIGHT_THROUGH);
		linkDefinition.setBandwidth(10000);
		linkDefinition.setFullDuplex(true);
		linkDefinition.setAutoBandwidth(true);
		linkDefinition.setAutoDuplex(true);
		linkDefinition
				.setInterfaceType(MultiuserInterfaceType.COPPER_FAST_ETHERNET);
		linkDefinition.setInterfaceCrossing(false);
		linkDefinition.setInterfaceUp(true);
		linkDefinition.setDeviceUp(true);
		multiuserConnection.addMultiuserLink(0, linkDefinition);
		multiuserConnection.connect(ptmpConnection);
		MultiuserLinkAdapter linkAdapter = multiuserConnection
				.getLinkAdapter(0);
		
		EthernetTraverser ethernetTraverser = new EthernetTraverser(
				ethernetSocket, linkAdapter);
		
		final Object monitor = new Object();
		
		ptmpConnection.addStateListener(new PTMPStateListener() {
			public void stateChanged(PTMPState newState, PTMPState oldState) {
				if (newState != PTMPState.NOT_CONNECTED) return;
				
				synchronized (monitor) {
					monitor.notify();
				}
			}
		});
		
		synchronized (monitor) {
			monitor.wait();
		}
		
		ethernetTraverser.close();
	}
	
}
