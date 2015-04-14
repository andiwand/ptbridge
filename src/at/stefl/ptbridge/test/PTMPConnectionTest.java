package at.stefl.ptbridge.test;

import java.net.InetAddress;

import at.stefl.ptbridge.ptmp.PTMPAuthentication;
import at.stefl.ptbridge.ptmp.PTMPCompression;
import at.stefl.ptbridge.ptmp.PTMPConfiguration;
import at.stefl.ptbridge.ptmp.PTMPConnection;
import at.stefl.ptbridge.ptmp.PTMPEncoding;
import at.stefl.ptbridge.ptmp.PTMPEncryption;


public class PTMPConnectionTest {
	
	public static void main(String[] args) throws Throwable {
		PTMPConnection ptmpConnection = new PTMPConnection();
		ptmpConnection.setPreferredConfiguration(new PTMPConfiguration(
				PTMPEncoding.TEXT, PTMPEncryption.NONE, PTMPCompression.NO,
				PTMPAuthentication.CLEAR_TEXT, 0));
		
		ptmpConnection.connect(InetAddress.getLocalHost(), "cisco");
		System.out.println("connected");
		
		ptmpConnection.close();
		System.out.println("closed");
	}
	
}