package at.stefl.ptbridge.ptmp.multiuser;

import at.stefl.ptbridge.ptmp.PTMPPacketListener;
import at.stefl.ptbridge.ptmp.packet.PTMPEncodedPacket;


public interface MultiuserPacketListener extends PTMPPacketListener {
	
	public void receivePacket(PTMPEncodedPacket packet);
	
}