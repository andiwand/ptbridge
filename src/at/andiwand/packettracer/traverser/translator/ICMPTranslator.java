package at.andiwand.packettracer.traverser.translator;

import at.andiwand.library.network.pdu.ICMPPacket;
import at.andiwand.library.network.pdu.ProtocolDataUnit;
import at.andiwand.packettracer.ptmp.multiuser.pdu.MultiuserICMPPacket;
import at.andiwand.packettracer.ptmp.multiuser.pdu.MultiuserProtocolDataUnit;


public class ICMPTranslator extends ProtocolDataUnitTranslator {
	
	public MultiuserICMPPacket translate(ProtocolDataUnit protocolDataUnit) {
		if (!(protocolDataUnit instanceof ICMPPacket))
			throw new IllegalArgumentException("Illegal PDU class");
		ICMPPacket packet = (ICMPPacket) protocolDataUnit;
		
		MultiuserICMPPacket result = new MultiuserICMPPacket();
		
		result.setType(packet.getType());
		result.setCode(packet.getCode());
		
		if (packet instanceof ICMPPacket.Echo) {
			ICMPPacket.Echo echo = (ICMPPacket.Echo) packet;
			
			result.setIdentifier(echo.getIdentifier());
			result.setSequenceNumber(echo.getSequenceNumber());
		} else {
			throw new IllegalArgumentException("Illegal ICMP packet");
		}
		
		return result;
	}
	
	public ICMPPacket translate(MultiuserProtocolDataUnit protocolDataUnit) {
		if (!(protocolDataUnit instanceof MultiuserICMPPacket))
			throw new IllegalArgumentException("Illegal PDU class");
		MultiuserICMPPacket packet = (MultiuserICMPPacket) protocolDataUnit;
		
		ICMPPacket.Echo result = new ICMPPacket.Echo();
		
		result.setType(packet.getType());
		result.setCode(packet.getCode());
		result.setIdentifier(packet.getIdentifier());
		result.setSequenceNumber(packet.getSequenceNumber());
		result.setData(new byte[0]);
		
		return result;
	}
	
}