package at.andiwand.packettracer.bridge.traverser.translator;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import at.andiwand.library.network.ip.IPv4Address;
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
	
	private final Map<Set<IPv4Address>, TranslationHelper> translationAssociatorMap = new HashMap<Set<IPv4Address>, TranslationHelper>();
	
	private TranslationHelper getTranslationHelper(IPv4Address address1,
			IPv4Address address2) {
		Set<IPv4Address> addressSet = new HashSet<IPv4Address>();
		addressSet.add(address1);
		addressSet.add(address2);
		
		TranslationHelper result = translationAssociatorMap.get(addressSet);
		if (result == null) {
			result = new TranslationHelper(TRANSLATION_ASSOCIATOR);
			translationAssociatorMap.put(addressSet, result);
		}
		
		return result;
	}
	
	private PDUTranslator getTranslator(IPv4Address address1,
			IPv4Address address2, Class<?> payloadClass) {
		return getTranslationHelper(address1, address2).getTranslator(
				payloadClass);
	}
	
	private MultiuserPDU toMultiuser(IPv4Packet packet) {
		PDUTranslator translator = getTranslator(packet.getSource(), packet
				.getDestination(), packet.getPayload().getClass());
		return translator.toMultiuser(packet.getPayload());
	}
	
	private PDU toNetwork(MultiuserIPv4Packet packet) {
		PDUTranslator translator = getTranslator(packet.getSource(), packet
				.getDestination(), packet.getPayload().getClass());
		return translator.toNetwork(packet.getPayload());
	}
	
	@Override
	protected MultiuserIPv4Packet toMultiuserGeneric(IPv4Packet packet) {
		MultiuserIPv4Packet result = new MultiuserIPv4Packet();
		
		MultiuserPDU payload = toMultiuser(packet);
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
		
		PDU payload = toNetwork(packet);
		result.setPayload(payload);
		
		return result;
	}
	
}