package at.andiwand.packettracer.bridge.traverser.translator;

import at.andiwand.packetsocket.pdu.ARPPacket;
import at.andiwand.packetsocket.pdu.Ethernet2Frame;
import at.andiwand.packetsocket.pdu.IPv4Packet;
import at.andiwand.packetsocket.pdu.PDU;
import at.andiwand.packettracer.bridge.ptmp.multiuser.pdu.MultiuserARPPacket;
import at.andiwand.packettracer.bridge.ptmp.multiuser.pdu.MultiuserEthernet2Frame;
import at.andiwand.packettracer.bridge.ptmp.multiuser.pdu.MultiuserIPv4Packet;
import at.andiwand.packettracer.bridge.ptmp.multiuser.pdu.MultiuserPDU;


public class Ethernet2Translator extends
		GenericPDUTranslator<Ethernet2Frame, MultiuserEthernet2Frame> {
	
	private static final TranslationHelper TRANSLATION_HELPER = new TranslationHelper() {
		{
			putTranslator(IPv4Packet.class, MultiuserIPv4Packet.class,
					IPv4Translator.class);
			putTranslator(ARPPacket.class, MultiuserARPPacket.class,
					ARPTranslator.class);
		}
	};
	
	@Override
	protected MultiuserEthernet2Frame toMultiuserGeneric(Ethernet2Frame frame) {
		MultiuserEthernet2Frame result = new MultiuserEthernet2Frame();
		
		PDUTranslator payloadTranslator = TRANSLATION_HELPER
				.getTranslator(frame.getPayload().getClass());
		MultiuserPDU payload = payloadTranslator.toMultiuser(frame.getPayload());
		result.setPayload(payload);
		
		result.setSource(frame.getSource());
		result.setDestination(frame.getDestination());
		result.setUnknown1((short) 0);
		result.setType(frame.getType());
		
		return result;
	}
	
	@Override
	protected Ethernet2Frame toNetworkGeneric(MultiuserEthernet2Frame frame) {
		Ethernet2Frame result = new Ethernet2Frame();
		
		result.setDestination(frame.getDestination());
		result.setSource(frame.getSource());
		result.setType((short) frame.getType());
		
		PDUTranslator payloadTranslator = TRANSLATION_HELPER
				.getTranslator(frame.getPayload().getClass());
		PDU payload = payloadTranslator.toNetwork(frame.getPayload());
		result.setPayload(payload);
		
		return result;
	}
	
}