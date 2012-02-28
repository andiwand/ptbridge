package at.andiwand.packettracer.bridge.test;

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
import at.andiwand.packettracer.bridge.ptmp.multiuser.MultiuserLinkMirrorAdapter;


public class MultiuserConnectionTest {
	
	public static void main(String[] args) throws Throwable {
		PTMPConnection ptmpConnection = new PTMPConnection();
		ptmpConnection.setPreferredConfiguration(new PTMPConfiguration(
				PTMPEncoding.TEXT, PTMPEncryption.NONE, PTMPCompression.NO,
				PTMPAuthentication.CLEAR_TEXT, 0));
		
		ptmpConnection.connect(InetAddress.getByName("127.0.0.1"),
				"PacketTracer Bridge", "");
		System.out.println("ptmp connected");
		
		final MultiuserConnection multiuserConnection = new MultiuserConnection(
				"*network name*");
		multiuserConnection.addLinkListener(new MultiuserLinkMirrorAdapter(
				multiuserConnection));
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