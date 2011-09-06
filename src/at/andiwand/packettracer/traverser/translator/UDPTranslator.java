package at.andiwand.packettracer.traverser.translator;

import at.andiwand.library.network.pdu.DHCPPacket;
import at.andiwand.library.network.pdu.ProtocolDataUnit;
import at.andiwand.library.network.pdu.UDPSegment;
import at.andiwand.packettracer.ptmp.multiuser.pdu.MultiuserDHCPPacket;
import at.andiwand.packettracer.ptmp.multiuser.pdu.MultiuserProtocolDataUnit;
import at.andiwand.packettracer.ptmp.multiuser.pdu.MultiuserUDPSegment;


public class UDPTranslator extends ProtocolDataUnitTranslator {
	
	private static final TranslationAssociator TRANSLATION_ASSOCIATOR = new TranslationAssociator();
	
	static {
		TRANSLATION_ASSOCIATOR.putTranslator(DHCPPacket.class, MultiuserDHCPPacket.class, DHCPTranslator.class);
	}
	
	
	public MultiuserUDPSegment translate(ProtocolDataUnit protocolDataUnit) {
		if (!(protocolDataUnit instanceof UDPSegment))
			throw new IllegalArgumentException("Illegal PDU class");
		UDPSegment packet = (UDPSegment) protocolDataUnit;
		
		MultiuserUDPSegment result = new MultiuserUDPSegment();
		
		Class<?> payloadClass = packet.getPayload().getClass();
		ProtocolDataUnitTranslator> payloadTranslator = TRANSLATION_ASSOCIATOR
				.getTranslator(payloadClass);
		MultiuserProtocolDataUnit payload = payloadTranslator.translate(packet
				.getPayload());
		result.setPayload(payload);
		
		result.setSourcePort((short) packet.getSourcePort());
		result.setDestinationPort((short) packet.getDestinationPort());
		
		return null;
	}
	
	public UDPSegment translate(MultiuserProtocolDataUnit protocolDataUnit) {
		if (!(protocolDataUnit instanceof MultiuserUDPSegment))
			throw new IllegalArgumentException("Illegal PDU class");
		MultiuserUDPSegment packet = (MultiuserUDPSegment) protocolDataUnit;
		
		UDPSegment result = new UDPSegment();
		
		result.setSourcePort(packet.getSourcePort());
		result.setDestinationPort(packet.getDestinationPort());
		
		Class<?> payloadClass = packet.getPayload().getClass();
		ProtocolDataUnitTranslator payloadTranslator = TRANSLATION_ASSOCIATOR
				.getTranslator(payloadClass);
		ProtocolDataUnit payload = payloadTranslator.translate(packet
				.getPayload());
		result.setPayload(payload);
		
		return null;
	}
	
}