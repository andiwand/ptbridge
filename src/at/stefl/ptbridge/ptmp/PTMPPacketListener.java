package at.stefl.ptbridge.ptmp;

import at.stefl.ptbridge.ptmp.packet.PTMPEncodedPacket;


public interface PTMPPacketListener {
	
	public void receivePacket(PTMPEncodedPacket packet);
	
}