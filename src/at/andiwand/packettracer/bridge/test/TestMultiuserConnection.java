package at.andiwand.packettracer.bridge.test;

import java.io.IOException;
import java.net.InetAddress;

import at.andiwand.packettracer.bridge.ptmp.PTMPAuthentication;
import at.andiwand.packettracer.bridge.ptmp.PTMPCompression;
import at.andiwand.packettracer.bridge.ptmp.PTMPConfiguration;
import at.andiwand.packettracer.bridge.ptmp.PTMPConnection;
import at.andiwand.packettracer.bridge.ptmp.PTMPEncoding;
import at.andiwand.packettracer.bridge.ptmp.PTMPEncryption;
import at.andiwand.packettracer.bridge.ptmp.PTMPState;
import at.andiwand.packettracer.bridge.ptmp.PTMPStateListener;
import at.andiwand.packettracer.bridge.ptmp.multiuser.MultiuserConnection;
import at.andiwand.packettracer.bridge.ptmp.multiuser.MultiuserLinkDefinition;
import at.andiwand.packettracer.bridge.ptmp.multiuser.MultiuserLinkListener;


public class TestMultiuserConnection {
	
	public static void main(String[] args) throws Throwable {
		PTMPConnection ptmpConnection = new PTMPConnection();
		ptmpConnection.setPreferredConfiguration(new PTMPConfiguration(
				PTMPEncoding.TEXT, PTMPEncryption.NONE, PTMPCompression.NO,
				PTMPAuthentication.CLEAR_TEXT, 0));
		
		ptmpConnection.connect(InetAddress.getLocalHost(), "Bridge", "");
		System.out.println("ptmp connected");
		
		final MultiuserConnection multiuserConnection = new MultiuserConnection(
				"*network name*");
		multiuserConnection.addLinkListener(new MultiuserLinkListener() {
			public void linkAdded(int linkId, MultiuserLinkDefinition definition) {
				try {
					multiuserConnection.addMultiuserLink(linkId, definition);
				} catch (IOException e) {}
			}
			
			public void linkChanged(int linkId,
					MultiuserLinkDefinition definition) {
				try {
					multiuserConnection.changeMultiuserLink(linkId, definition);
				} catch (IOException e) {}
			}
			
			public void linkDetached(int linkId,
					MultiuserLinkDefinition definition) {
				try {
					multiuserConnection.removeMultiuserLink(linkId);
				} catch (IOException e) {}
			}
		});
		multiuserConnection.connect(ptmpConnection);
		System.out.println("multiuser connected");
		
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
		
		multiuserConnection.close();
		System.out.println("multiuser closed");
		
		ptmpConnection.close();
		System.out.println("ptmp closed");
	}
	
}