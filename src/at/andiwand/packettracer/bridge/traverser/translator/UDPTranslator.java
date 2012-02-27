package at.andiwand.packettracer.bridge.traverser.translator;

import at.andiwand.packetsocket.pdu.DHCPPacket;
import at.andiwand.packetsocket.pdu.PDU;
import at.andiwand.packetsocket.pdu.UDPSegment;
import at.andiwand.packettracer.bridge.ptmp.multiuser.pdu.MultiuserDHCPPacket;
import at.andiwand.packettracer.bridge.ptmp.multiuser.pdu.MultiuserPDU;
import at.andiwand.packettracer.bridge.ptmp.multiuser.pdu.MultiuserUDPSegment;


public class UDPTranslator extends
		GenericPDUTranslator<UDPSegment, MultiuserUDPSegment> {
	
	private static final TranslationAssociator TRANSLATION_ASSOCIATOR = new TranslationAssociator();
	
	static {
		TRANSLATION_ASSOCIATOR.putTranslator(DHCPPacket.class,
				MultiuserDHCPPacket.class, DHCPTranslator.class);
	}
	
	@Override
	protected MultiuserUDPSegment translateGeneric(UDPSegment segment) {
		MultiuserUDPSegment result = new MultiuserUDPSegment();
		
		Class<?> payloadClass = segment.getPayload().getClass();
		PDUTranslator payloadTranslator = TRANSLATION_ASSOCIATOR
				.getTranslatorInstance(payloadClass);
		MultiuserPDU payload = payloadTranslator
				.translate(segment.getPayload());
		result.setPayload(payload);
		
		result.setSourcePort((short) segment.getSourcePort());
		result.setDestinationPort((short) segment.getDestinationPort());
		
		return null;
	}
	
	@Override
	protected UDPSegment translateGeneric(MultiuserUDPSegment segment) {
		UDPSegment result = new UDPSegment();
		
		result.setSourcePort(segment.getSourcePort());
		result.setDestinationPort(segment.getDestinationPort());
		
		Class<?> payloadClass = segment.getPayload().getClass();
		PDUTranslator payloadTranslator = TRANSLATION_ASSOCIATOR
				.getTranslatorInstance(payloadClass);
		PDU payload = payloadTranslator.translate(segment.getPayload());
		result.setPayload(payload);
		
		return null;
	}
	
}