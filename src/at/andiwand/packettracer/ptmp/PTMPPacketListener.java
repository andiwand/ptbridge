package at.andiwand.packettracer.ptmp;

import at.andiwand.packettracer.ptmp.packet.PTMPEncodedPacket;


public interface PTMPPacketListener {
	
	public void receivePacket(PTMPEncodedPacket packet);
	
}