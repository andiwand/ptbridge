package at.andiwand.packettracer.bridge.traverser.translator;

import java.util.HashMap;
import java.util.Map;

import at.andiwand.packetsocket.pdu.PDU;
import at.andiwand.packettracer.bridge.ptmp.multiuser.pdu.MultiuserPDU;


public class TranslationAssociator {
	
	private final Map<Class<?>, Class<? extends PDUTranslator>> translatorMap = new HashMap<Class<?>, Class<? extends PDUTranslator>>();
	
	public TranslationAssociator() {}
	
	public TranslationAssociator(TranslationAssociator translationAssociator) {
		translatorMap.putAll(translationAssociator.translatorMap);
	}
	
	public Class<? extends PDUTranslator> getTranslatorClass(Class<?> clazz) {
		return translatorMap.get(clazz);
	}
	
	public PDUTranslator getTranslatorInstance(Class<?> clazz) {
		Class<? extends PDUTranslator> translatorClass = getTranslatorClass(clazz);
		
		if (translatorClass == null) return null;
		
		try {
			return translatorClass.newInstance();
		} catch (Exception e) {
			throw new IllegalStateException("Unreachable section");
		}
	}
	
	public void putTranslator(Class<? extends PDU> clazzA,
			Class<? extends MultiuserPDU> clazzB,
			Class<? extends PDUTranslator> translatorClass) {
		translatorMap.put(clazzA, translatorClass);
		translatorMap.put(clazzB, translatorClass);
	}
	
	public void removeTranslator(Class<? extends PDU> clazzA,
			Class<? extends MultiuserPDU> clazzB) {
		translatorMap.remove(clazzA);
		translatorMap.remove(clazzB);
	}
	
}