package at.andiwand.packettracer.traverser.translator;

import java.util.HashMap;
import java.util.Map;

import at.andiwand.library.network.pdu.ProtocolDataUnit;
import at.andiwand.packettracer.ptmp.multiuser.pdu.MultiuserProtocolDataUnit;


public class TranslationAssociator {
	
	private final Map<Class<?>, Class<? extends ProtocolDataUnitTranslator>> translatorMap = new HashMap<Class<?>, Class<? extends ProtocolDataUnitTranslator>>();
	
	
	public TranslationAssociator() {}
	public TranslationAssociator(TranslationAssociator translationAssociator) {
		translatorMap.putAll(translationAssociator.translatorMap);
	}
	
	
	public Class<? extends ProtocolDataUnitTranslator> getTranslatorClass(
			Class<?> clazz) {
		return translatorMap.get(clazz);
	}
	public ProtocolDataUnitTranslator getTranslatorInstance(Class<?> clazz) {
		Class<? extends ProtocolDataUnitTranslator> translatorClass = getTranslatorClass(clazz);
		
		if (translatorClass == null)
			return null;
		
		try {
			return translatorClass.newInstance();
		} catch (Exception e) {
			throw new IllegalStateException("Unreachable section");
		}
	}
	
	public void putTranslator(Class<? extends ProtocolDataUnit> clazzA,
			Class<? extends MultiuserProtocolDataUnit> clazzB,
			Class<? extends ProtocolDataUnitTranslator> translatorClass) {
		translatorMap.put(clazzA, translatorClass);
		translatorMap.put(clazzB, translatorClass);
	}
	public void removeTranslator(Class<? extends ProtocolDataUnit> clazzA,
			Class<? extends MultiuserProtocolDataUnit> clazzB) {
		translatorMap.remove(clazzA);
		translatorMap.remove(clazzB);
	}
	
}