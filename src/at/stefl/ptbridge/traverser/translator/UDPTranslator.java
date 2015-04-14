package at.stefl.ptbridge.traverser.translator;

import at.stefl.packetsocket.pdu.DHCPPacket;
import at.stefl.packetsocket.pdu.DNSPacket;
import at.stefl.packetsocket.pdu.PDU;
import at.stefl.packetsocket.pdu.UDPSegment;
import at.stefl.ptbridge.ptmp.multiuser.pdu.MultiuserDHCPPacket;
import at.stefl.ptbridge.ptmp.multiuser.pdu.MultiuserPDU;
import at.stefl.ptbridge.ptmp.multiuser.pdu.MultiuserUDPSegment;


public class UDPTranslator extends
		GenericPDUTranslator<UDPSegment, MultiuserUDPSegment> {
	
	private static final TranslationHelper TRANSLATION_ASSOCIATOR = new TranslationHelper();
	
	static {
		TRANSLATION_ASSOCIATOR.putTranslator(DNSPacket.class,
				MultiuserDHCPPacket.class, DNSTranslator.class);
		TRANSLATION_ASSOCIATOR.putTranslator(DHCPPacket.class,
				MultiuserDHCPPacket.class, DHCPTranslator.class);
	}
	
	@Override
	protected MultiuserUDPSegment toMultiuserGeneric(UDPSegment segment) {
		MultiuserUDPSegment result = new MultiuserUDPSegment();
		
		Class<?> payloadClass = segment.getPayload().getClass();
		PDUTranslator payloadTranslator = TRANSLATION_ASSOCIATOR
				.getTranslator(payloadClass);
		MultiuserPDU payload = payloadTranslator.toMultiuser(segment
				.getPayload());
		result.setPayload(payload);
		
		result.setSourcePort((short) segment.getSourcePort());
		result.setDestinationPort((short) segment.getDestinationPort());
		
		return result;
	}
	
	@Override
	protected UDPSegment toNetworkGeneric(MultiuserUDPSegment segment) {
		UDPSegment result = new UDPSegment();
		
		result.setSourcePort(segment.getSourcePort());
		result.setDestinationPort(segment.getDestinationPort());
		
		Class<?> payloadClass = segment.getPayload().getClass();
		PDUTranslator payloadTranslator = TRANSLATION_ASSOCIATOR
				.getTranslator(payloadClass);
		PDU payload = payloadTranslator.toNetwork(segment.getPayload());
		result.setPayload(payload);
		
		return result;
	}
	
}