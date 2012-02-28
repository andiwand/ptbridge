package at.andiwand.packettracer.bridge.traverser.translator;

import at.andiwand.packetsocket.pdu.ICMPPacket;
import at.andiwand.packetsocket.pdu.IPv4Packet;
import at.andiwand.packetsocket.pdu.PDU;
import at.andiwand.packetsocket.pdu.TCPSegment;
import at.andiwand.packetsocket.pdu.UDPSegment;
import at.andiwand.packettracer.bridge.ptmp.multiuser.pdu.MultiuserICMPPacket;
import at.andiwand.packettracer.bridge.ptmp.multiuser.pdu.MultiuserIPv4Packet;
import at.andiwand.packettracer.bridge.ptmp.multiuser.pdu.MultiuserPDU;
import at.andiwand.packettracer.bridge.ptmp.multiuser.pdu.MultiuserTCPSegment;
import at.andiwand.packettracer.bridge.ptmp.multiuser.pdu.MultiuserUDPSegment;


public class IPv4Translator extends
		GenericPDUTranslator<IPv4Packet, MultiuserIPv4Packet> {
	
	private static final TranslationHelper TRANSLATION_ASSOCIATOR = new TranslationHelper() {
		{
			putTranslator(ICMPPacket.class, MultiuserICMPPacket.class,
					ICMPTranslator.class);
			putTranslator(TCPSegment.class, MultiuserTCPSegment.class,
					TCPTranslator.class);
			putTranslator(UDPSegment.class, MultiuserUDPSegment.class,
					UDPTranslator.class);
		}
	};
	
	@Override
	protected MultiuserIPv4Packet toMultiuserGeneric(IPv4Packet packet) {
		MultiuserIPv4Packet result = new MultiuserIPv4Packet();
		
		Class<?> payloadClass = packet.getPayload().getClass();
		PDUTranslator payloadTranslator = TRANSLATION_ASSOCIATOR
				.getTranslator(payloadClass);
		MultiuserPDU payload = payloadTranslator.toMultiuser(packet
				.getPayload());
		result.setPayload(payload);
		
		result.setVersion(packet.getVersion());
		result.setTypeOfService(packet.getTypeOfService());
		result.setIdentication((short) (packet.getIdentication() & 0x7fff));
		result.setFlags(packet.getFlags());
		result.setFragmentOffset(packet.getFragmentOffset());
		result.setTimeToLive(packet.getTimeToLive());
		result.setProtocol((short) (packet.getProtocol() & 0xff));
		result.setUnknown1(0);
		result.setUnknown2(0);
		result.setSource(packet.getSource());
		result.setDestination(packet.getDestination());
		
		return result;
	}
	
	@Override
	protected IPv4Packet toNetworkGeneric(MultiuserIPv4Packet packet) {
		IPv4Packet result = new IPv4Packet();
		
		result.setVersion(packet.getVersion());
		result.setTypeOfService(packet.getTypeOfService());
		result.setIdentication(packet.getIdentication());
		result.setFlags(packet.getFlags());
		result.setFragmentOffset(packet.getFragmentOffset());
		result.setTimeToLive(packet.getTimeToLive());
		result.setProtocol((byte) packet.getProtocol());
		result.setSource(packet.getSource());
		result.setDestination(packet.getDestination());
		
		Class<?> payloadClass = packet.getPayload().getClass();
		PDUTranslator payloadTranslator = TRANSLATION_ASSOCIATOR
				.getTranslator(payloadClass);
		PDU payload = payloadTranslator.toNetwork(packet.getPayload());
		result.setPayload(payload);
		
		return result;
	}
	
}