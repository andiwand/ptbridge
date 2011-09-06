package at.andiwand.packettracer.traverser.translator;

import java.net.Inet4Address;
import java.util.HashMap;
import java.util.Map;

import at.andiwand.library.network.pdu.ICMPPacket;
import at.andiwand.library.network.pdu.IPv4Packet;
import at.andiwand.library.network.pdu.ProtocolDataUnit;
import at.andiwand.library.network.pdu.TCPSegment;
import at.andiwand.library.network.pdu.UDPSegment;
import at.andiwand.library.util.BinaryTupel;
import at.andiwand.packettracer.ptmp.multiuser.pdu.MultiuserICMPPacket;
import at.andiwand.packettracer.ptmp.multiuser.pdu.MultiuserIPv4Packet;
import at.andiwand.packettracer.ptmp.multiuser.pdu.MultiuserProtocolDataUnit;
import at.andiwand.packettracer.ptmp.multiuser.pdu.MultiuserTCPSegment;
import at.andiwand.packettracer.ptmp.multiuser.pdu.MultiuserUDPSegment;


public class IPv4Translator extends ProtocolDataUnitTranslator {
	
	private static final TranslationAssociator TRANSLATION_ASSOCIATOR = new TranslationAssociator();
	
	static {
		TRANSLATION_ASSOCIATOR.putTranslator(ICMPPacket.class, MultiuserICMPPacket.class, ICMPTranslator.class);
		TRANSLATION_ASSOCIATOR.putTranslator(TCPSegment.class, MultiuserTCPSegment.class, TCPTranslator.class);
		TRANSLATION_ASSOCIATOR.putTranslator(UDPSegment.class, MultiuserUDPSegment.class, UDPTranslator.class);
	}
	
	
	
	private final Map<BinaryTupel<Inet4Address>, Map<Class<?>, ProtocolDataUnitTranslator>> translatorInstanceMap = new HashMap<BinaryTupel<Inet4Address>, Map<Class<?>,ProtocolDataUnitTranslator>>();
	
	
	public MultiuserIPv4Packet translate(ProtocolDataUnit protocolDataUnit) {
		if (!(protocolDataUnit instanceof IPv4Packet))
			throw new IllegalArgumentException("Illegal PDU class");
		IPv4Packet packet = (IPv4Packet) protocolDataUnit;
		
		MultiuserIPv4Packet result = new MultiuserIPv4Packet();
		
		Class<?> payloadClass = packet.getPayload().getClass();
		ProtocolDataUnitTranslator payloadTranslator = TRANSLATION_ASSOCIATOR
				.getTranslator(payloadClass);
		MultiuserProtocolDataUnit payload = payloadTranslator.translate(packet
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
	
	public IPv4Packet translate(MultiuserProtocolDataUnit protocolDataUnit) {
		if (!(protocolDataUnit instanceof MultiuserIPv4Packet))
			throw new IllegalArgumentException("Illegal PDU class");
		MultiuserIPv4Packet packet = (MultiuserIPv4Packet) protocolDataUnit;
		
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
		ProtocolDataUnitTranslator payloadTranslator = TRANSLATION_ASSOCIATOR
				.getTranslator(payloadClass);
		ProtocolDataUnit payload = payloadTranslator.translate(packet
				.getPayload());
		result.setPayload(payload);
		
		return result;
	}
	
}