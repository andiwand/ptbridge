package at.andiwand.packettracer.bridge.ptmp;

import at.andiwand.packettracer.bridge.ptmp.packet.PTMPEncodedPacket;


public interface PTMPPacketListener {
	
	public void receivePacket(PTMPEncodedPacket packet);
	
}