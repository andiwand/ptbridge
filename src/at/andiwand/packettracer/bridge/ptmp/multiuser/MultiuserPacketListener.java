package at.andiwand.packettracer.bridge.ptmp.multiuser;

import at.andiwand.packettracer.bridge.ptmp.PTMPPacketListener;
import at.andiwand.packettracer.bridge.ptmp.packet.PTMPEncodedPacket;


public interface MultiuserPacketListener extends PTMPPacketListener {
	
	public void receivePacket(PTMPEncodedPacket packet);
	
}