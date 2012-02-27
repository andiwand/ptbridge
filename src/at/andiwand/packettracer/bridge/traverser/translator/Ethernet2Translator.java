package at.andiwand.packettracer.bridge.traverser.translator;

import java.util.HashMap;
import java.util.Map;

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
	
	private static final TranslationAssociator TRANSLATION_ASSOCIATOR = new TranslationAssociator();
	
	static {
		TRANSLATION_ASSOCIATOR.putTranslator(IPv4Packet.class,
				MultiuserIPv4Packet.class, IPv4Translator.class);
		TRANSLATION_ASSOCIATOR.putTranslator(ARPPacket.class,
				MultiuserARPPacket.class, ARPTranslator.class);
	}
	
	private final Map<Class<?>, PDUTranslator> translatorInstanceMap = new HashMap<Class<?>, PDUTranslator>();
	
	private PDUTranslator getTranslator(Class<?> payloadClass) {
		Class<?> translatorClass = TRANSLATION_ASSOCIATOR
				.getTranslatorClass(payloadClass);
		
		PDUTranslator translator = translatorInstanceMap.get(translatorClass);
		if (translator == null) {
			translator = TRANSLATION_ASSOCIATOR
					.getTranslatorInstance(payloadClass);
			translatorInstanceMap.put(translatorClass, translator);
		}
		
		return translator;
	}
	
	@Override
	protected MultiuserEthernet2Frame translateGeneric(Ethernet2Frame frame) {
		MultiuserEthernet2Frame result = new MultiuserEthernet2Frame();
		
		PDUTranslator payloadTranslator = getTranslator(frame.getPayload()
				.getClass());
		MultiuserPDU payload = payloadTranslator.translate(frame.getPayload());
		result.setPayload(payload);
		
		result.setSource(frame.getSource());
		result.setDestination(frame.getDestination());
		result.setUnknown1((short) 0);
		result.setType(frame.getType());
		
		return result;
	}
	
	@Override
	protected Ethernet2Frame translateGeneric(MultiuserEthernet2Frame frame) {
		Ethernet2Frame result = new Ethernet2Frame();
		
		result.setDestination(frame.getDestination());
		result.setSource(frame.getSource());
		result.setType((short) frame.getType());
		
		PDUTranslator payloadTranslator = getTranslator(frame.getPayload()
				.getClass());
		PDU payload = payloadTranslator.translate(frame.getPayload());
		result.setPayload(payload);
		
		return result;
	}
	
}