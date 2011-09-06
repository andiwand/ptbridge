package at.andiwand.packettracer.ptmp.multiuser;

import at.andiwand.packettracer.ptmp.PTMPPacketListener;
import at.andiwand.packettracer.ptmp.packet.PTMPEncodedPacket;


public interface MultiuserPacketListener extends PTMPPacketListener {
	
	public void receivePacket(PTMPEncodedPacket packet);
	
}