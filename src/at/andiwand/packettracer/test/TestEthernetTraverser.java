package at.andiwand.packettracer.test;

import java.net.InetAddress;

import at.andiwand.packetsocket.EthernetSocket;
import at.andiwand.packettracer.ptmp.PTMPConfiguration;
import at.andiwand.packettracer.ptmp.PTMPConnection;
import at.andiwand.packettracer.ptmp.PTMPState;
import at.andiwand.packettracer.ptmp.PTMPStateListener;
import at.andiwand.packettracer.ptmp.multiuser.MultiuserConnection;
import at.andiwand.packettracer.ptmp.multiuser.MultiuserLinkAdapter;
import at.andiwand.packettracer.ptmp.multiuser.MultiuserLinkDefinition;
import at.andiwand.packettracer.traverser.EthernetTraverser;


public class TestEthernetTraverser {
	
	public static void main(String[] args) throws Throwable {
		EthernetSocket ethernetSocket = new EthernetSocket(
				EthernetSocket.PROTOCOL_ALL);
		ethernetSocket.bind("eth0");
		ethernetSocket.enablePromiscMode("eth0");
		
		PTMPConnection ptmpConnection = new PTMPConnection();
		ptmpConnection.setPreferredConfiguration(new PTMPConfiguration(
				PTMPConfiguration.ENCODING_TEXT,
				PTMPConfiguration.ENCRYPTION_NONE,
				PTMPConfiguration.COMPRESSION_NO,
				PTMPConfiguration.AUTHENTICATION_CLEAR_TEXT));
		ptmpConnection.connect(InetAddress.getLocalHost(), "Ethernet Bridge",
				"");
		MultiuserConnection multiuserConnection = new MultiuserConnection(
				"Ethernet Bridge");
		MultiuserLinkDefinition linkDefinition = new MultiuserLinkDefinition(
				"Ethernet Bridge");
		linkDefinition.setType(MultiuserLinkDefinition.TYPE_STRAIGHT_THROUGH);
		linkDefinition.setBandwidth(10000);
		linkDefinition.setFullDuplex(true);
		linkDefinition.setAutoBandwidth(true);
		linkDefinition.setAutoDuplex(true);
		linkDefinition.setInterfaceType(MultiuserLinkDefinition.INTERFACE_COPPER_FAST_ETHERNET);
		linkDefinition.setInterfaceCrossing(false);
		linkDefinition.setInterfaceUp(true);
		linkDefinition.setDeviceUp(true);
		multiuserConnection.addMultiuserLink(0, linkDefinition);
		multiuserConnection.connect(ptmpConnection);
		MultiuserLinkAdapter linkAdapter = multiuserConnection.getLinkAdapter(0);
		
		EthernetTraverser ethernetTraverser = new EthernetTraverser(ethernetSocket, linkAdapter);
		
		
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