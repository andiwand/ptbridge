package at.andiwand.packettracer.test;

import java.net.InetAddress;

import at.andiwand.packettracer.ptmp.PTMPConfiguration;
import at.andiwand.packettracer.ptmp.PTMPConnection;


public class TestPTMPConnection {
	
	public static void main(String[] args) throws Throwable {
		PTMPConnection ptmpConnection = new PTMPConnection();
		ptmpConnection.setPreferredConfiguration(new PTMPConfiguration(
				PTMPConfiguration.ENCODING_TEXT,
				PTMPConfiguration.ENCRYPTION_NONE,
				PTMPConfiguration.COMPRESSION_NO,
				PTMPConfiguration.AUTHENTICATION_CLEAR_TEXT));
		
		ptmpConnection.connect(InetAddress.getLocalHost(), "cisco");
		System.out.println("connected");
		
		ptmpConnection.close();
		System.out.println("closed");
	}
	
}