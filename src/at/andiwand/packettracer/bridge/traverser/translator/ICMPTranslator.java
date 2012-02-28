package at.andiwand.packettracer.bridge.traverser.translator;

import at.andiwand.packetsocket.pdu.ICMPPacket;
import at.andiwand.packettracer.bridge.ptmp.multiuser.pdu.MultiuserICMPPacket;


public class ICMPTranslator extends
		GenericPDUTranslator<ICMPPacket, MultiuserICMPPacket> {
	
	@Override
	protected MultiuserICMPPacket toMultiuserGeneric(ICMPPacket packet) {
		MultiuserICMPPacket result;
		
		if (packet instanceof ICMPPacket.Echo) {
			ICMPPacket.Echo echo = (ICMPPacket.Echo) packet;
			
			MultiuserICMPPacket.Echo resultEcho = new MultiuserICMPPacket.Echo();
			resultEcho.setIdentifier(echo.getIdentifier());
			resultEcho.setSequenceNumber(echo.getSequenceNumber());
			result = resultEcho;
		} else {
			throw new IllegalArgumentException("Unsupported ICMP packet!");
		}
		
		result.setType(packet.getType());
		result.setCode(packet.getCode());
		
		return result;
	}
	
	@Override
	protected ICMPPacket toNetworkGeneric(MultiuserICMPPacket packet) {
		ICMPPacket result;
		
		if (packet instanceof MultiuserICMPPacket.Echo) {
			MultiuserICMPPacket.Echo echo = (MultiuserICMPPacket.Echo) packet;
			
			ICMPPacket.Echo resultEcho = new ICMPPacket.Echo();
			resultEcho.setIdentifier(echo.getIdentifier());
			resultEcho.setSequenceNumber(echo.getSequenceNumber());
			resultEcho.setData(new byte[0]);
			result = resultEcho;
		} else {
			throw new IllegalArgumentException("Unsupported ICMP packet!");
		}
		
		result.setType(packet.getType());
		result.setCode(packet.getCode());
		
		return result;
	}
}