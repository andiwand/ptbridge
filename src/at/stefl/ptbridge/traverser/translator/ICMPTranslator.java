package at.stefl.ptbridge.traverser.translator;

import at.stefl.packetsocket.pdu.ICMPPacket;
import at.stefl.ptbridge.ptmp.multiuser.pdu.MultiuserICMPPacket;


public class ICMPTranslator extends
		GenericPDUTranslator<ICMPPacket, MultiuserICMPPacket> {
	
	@Override
	protected MultiuserICMPPacket toMultiuserGeneric(ICMPPacket packet) {
		MultiuserICMPPacket result = new MultiuserICMPPacket();
		
		result.setType(packet.getType());
		result.setCode(packet.getCode());
		
		MultiuserICMPPacket.ICMPPayload resultPayload;
		ICMPPacket.ICMPPayload payload = packet.getPayload();
		
		if (payload instanceof ICMPPacket.EchoPayload) {
			ICMPPacket.EchoPayload echoPayload = (ICMPPacket.EchoPayload) payload;
			
			MultiuserICMPPacket.EchoPayload resultEchoPayload = new MultiuserICMPPacket.EchoPayload();
			resultEchoPayload.setIdentifier(echoPayload.getIdentifier());
			resultEchoPayload
					.setSequenceNumber(echoPayload.getSequenceNumber());
			resultPayload = resultEchoPayload;
		} else {
			throw new IllegalArgumentException("Unsupported ICMP packet!");
		}
		
		result.setPayload(resultPayload);
		
		return result;
	}
	
	@Override
	protected ICMPPacket toNetworkGeneric(MultiuserICMPPacket packet) {
		ICMPPacket result = new ICMPPacket();
		
		result.setType(packet.getType());
		result.setCode(packet.getCode());
		
		ICMPPacket.ICMPPayload resultPayload;
		MultiuserICMPPacket.ICMPPayload payload = packet.getPayload();
		
		if (payload instanceof MultiuserICMPPacket.EchoPayload) {
			MultiuserICMPPacket.EchoPayload echoPayload = (MultiuserICMPPacket.EchoPayload) payload;
			
			ICMPPacket.EchoPayload resultEchoPayload = new ICMPPacket.EchoPayload();
			resultEchoPayload.setIdentifier(echoPayload.getIdentifier());
			resultEchoPayload
					.setSequenceNumber(echoPayload.getSequenceNumber());
			resultEchoPayload.setData(new byte[0]);
			resultPayload = resultEchoPayload;
		} else {
			throw new IllegalArgumentException("Unsupported ICMP packet!");
		}
		
		result.setPayload(resultPayload);
		
		return result;
	}
}