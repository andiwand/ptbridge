package at.andiwand.packettracer.bridge.test;

import java.net.InetAddress;

import at.andiwand.packetsocket.EthernetSocket;
import at.andiwand.packettracer.bridge.ptmp.PTMPAssignments;
import at.andiwand.packettracer.bridge.ptmp.PTMPAuthentication;
import at.andiwand.packettracer.bridge.ptmp.PTMPCompression;
import at.andiwand.packettracer.bridge.ptmp.PTMPConfiguration;
import at.andiwand.packettracer.bridge.ptmp.PTMPConnection;
import at.andiwand.packettracer.bridge.ptmp.PTMPEncoding;
import at.andiwand.packettracer.bridge.ptmp.PTMPEncryption;
import at.andiwand.packettracer.bridge.ptmp.PTMPState;
import at.andiwand.packettracer.bridge.ptmp.PTMPStateListener;
import at.andiwand.packettracer.bridge.ptmp.multiuser.MultiuserConnection;
import at.andiwand.packettracer.bridge.ptmp.multiuser.MultiuserInterfaceType;
import at.andiwand.packettracer.bridge.ptmp.multiuser.MultiuserLinkAdapter;
import at.andiwand.packettracer.bridge.ptmp.multiuser.MultiuserLinkDefinition;
import at.andiwand.packettracer.bridge.ptmp.multiuser.MultiuserLinkType;
import at.andiwand.packettracer.bridge.traverser.EthernetTraverser;


public class EthernetTraverserTest {
	
	public static void main(String[] args) throws Throwable {
		String interfaze = "eth0";
		InetAddress address = InetAddress.getByName("127.0.0.1");
		int port = PTMPAssignments.PORT;
		
		if (args.length > 0) {
			try {
				interfaze = args[0];
				String[] tmp = args[1].split(":");
				address = InetAddress.getByName(tmp[0]);
				if (tmp.length > 1) port = Integer.parseInt(tmp[1]);
			} catch (Throwable t) {
				System.err.println("usage: <program> [<interface> <pt_ip>[:<pt_port>]]");
				System.err.println();
				System.err.println("default: eth0 127.0.0.1:38000");
				System.exit(1);
			}
		}
		
		EthernetSocket ethernetSocket = new EthernetSocket(
				EthernetSocket.PROTOCOL_ALL);
		ethernetSocket.bind(interfaze);
		ethernetSocket.enablePromiscMode(interfaze);
		
		PTMPConnection ptmpConnection = new PTMPConnection();
		ptmpConnection.setPreferredConfiguration(new PTMPConfiguration(
				PTMPEncoding.TEXT, PTMPEncryption.NONE, PTMPCompression.NO,
				PTMPAuthentication.CLEAR_TEXT, 0));
		ptmpConnection.connect(address, port, "PacketTracer Bridge", "");
		MultiuserConnection multiuserConnection = new MultiuserConnection(
				"Ethernet");
		MultiuserLinkDefinition linkDefinition = new MultiuserLinkDefinition(
				"Ethernet Bridge Interface");
		linkDefinition.setType(MultiuserLinkType.STRAIGHT_THROUGH);
		linkDefinition.setBandwidth(10000);
		linkDefinition.setFullDuplex(true);
		linkDefinition.setAutoBandwidth(true);
		linkDefinition.setAutoDuplex(true);
		linkDefinition.setInterfaceType(MultiuserInterfaceType.COPPER_FAST_ETHERNET);
		linkDefinition.setInterfaceCrossing(false);
		linkDefinition.setInterfaceUp(true);
		linkDefinition.setDeviceUp(true);
		multiuserConnection.addMultiuserLink(0, linkDefinition);
		multiuserConnection.connect(ptmpConnection);
		MultiuserLinkAdapter linkAdapter = multiuserConnection.getLinkAdapter(0);
		
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