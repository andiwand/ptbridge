package at.andiwand.packettracer.traverser.translator;

import java.util.HashMap;
import java.util.Map;

import at.andiwand.library.network.pdu.ARPPacket;
import at.andiwand.library.network.pdu.Ethernet2Frame;
import at.andiwand.library.network.pdu.IPv4Packet;
import at.andiwand.library.network.pdu.IPv6Packet;
import at.andiwand.library.network.pdu.ProtocolDataUnit;
import at.andiwand.packettracer.ptmp.multiuser.pdu.MultiuserARPPacket;
import at.andiwand.packettracer.ptmp.multiuser.pdu.MultiuserEthernet2Frame;
import at.andiwand.packettracer.ptmp.multiuser.pdu.MultiuserIPv4Packet;
import at.andiwand.packettracer.ptmp.multiuser.pdu.MultiuserIPv6Packet;
import at.andiwand.packettracer.ptmp.multiuser.pdu.MultiuserProtocolDataUnit;


public class Ethernet2Translator extends ProtocolDataUnitTranslator {
	
	private static final TranslationAssociator TRANSLATION_ASSOCIATOR = new TranslationAssociator();
	
	static {
		TRANSLATION_ASSOCIATOR.putTranslator(IPv4Packet.class, MultiuserIPv4Packet.class, IPv4Translator.class);
		TRANSLATION_ASSOCIATOR.putTranslator(ARPPacket.class, MultiuserARPPacket.class, ARPTranslator.class);
		TRANSLATION_ASSOCIATOR.putTranslator(IPv6Packet.class, MultiuserIPv6Packet.class, IPv6Translator.class);
	}
	
	
	
	private final Map<Class<?>, ProtocolDataUnitTranslator> translatorInstanceMap = new HashMap<Class<?>, ProtocolDataUnitTranslator>();
	
	
	private ProtocolDataUnitTranslator getTranslator(Class<?> payloadClass) {
		Class<?> translatorClass = TRANSLATION_ASSOCIATOR
				.getTranslatorClass(payloadClass);
		
		ProtocolDataUnitTranslator translator = translatorInstanceMap
				.get(translatorClass);
		if (translator == null) {
			translator = TRANSLATION_ASSOCIATOR
					.getTranslatorInstance(payloadClass);
			translatorInstanceMap.put(translatorClass, translator);
		}
		
		return translator;
	}
	
	public MultiuserEthernet2Frame translate(ProtocolDataUnit protocolDataUnit) {
		if (!(protocolDataUnit instanceof Ethernet2Frame))
			throw new IllegalArgumentException("Illegal PDU class");
		Ethernet2Frame frame = (Ethernet2Frame) protocolDataUnit;
		
		MultiuserEthernet2Frame result = new MultiuserEthernet2Frame();
		
		ProtocolDataUnitTranslator payloadTranslator = getTranslator(frame
				.getPayload().getClass());
		MultiuserProtocolDataUnit payload = payloadTranslator.translate(frame
				.getPayload());
		result.setPayload(payload);
		
		result.setSource(frame.getSource());
		result.setDestination(frame.getDestination());
		result.setUnknown1((short) 0);
		result.setType(frame.getType());
		
		return result;
	}
	public Ethernet2Frame translate(MultiuserProtocolDataUnit protocolDataUnit) {
		if (!(protocolDataUnit instanceof MultiuserEthernet2Frame))
			throw new IllegalArgumentException("Illegal PDU class");
		MultiuserEthernet2Frame frame = (MultiuserEthernet2Frame) protocolDataUnit;
		
		Ethernet2Frame result = new Ethernet2Frame();
		
		result.setDestination(frame.getDestination());
		result.setSource(frame.getSource());
		result.setType((short) frame.getType());
		
		ProtocolDataUnitTranslator payloadTranslator = getTranslator(frame
				.getPayload().getClass());
		ProtocolDataUnit payload = payloadTranslator.translate(frame
				.getPayload());
		result.setPayload(payload);
		
		return result;
	}
	
}