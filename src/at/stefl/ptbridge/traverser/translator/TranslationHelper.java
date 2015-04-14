package at.stefl.ptbridge.traverser.translator;

import java.util.HashMap;
import java.util.Map;

import at.stefl.packetsocket.pdu.PDU;
import at.stefl.ptbridge.ptmp.multiuser.pdu.MultiuserPDU;


public class TranslationHelper {
	
	private final Map<Class<?>, Class<? extends PDUTranslator>> translatorMap = new HashMap<Class<?>, Class<? extends PDUTranslator>>();
	private final Map<Class<?>, PDUTranslator> translatorInstanceMap = new HashMap<Class<?>, PDUTranslator>();
	
	public TranslationHelper() {}
	
	public TranslationHelper(TranslationHelper translationAssociator) {
		translatorMap.putAll(translationAssociator.translatorMap);
	}
	
	public Class<? extends PDUTranslator> getTranslatorClass(Class<?> clazz) {
		return translatorMap.get(clazz);
	}
	
	public PDUTranslator getTranslator(Class<?> clazz) {
		Class<? extends PDUTranslator> translatorClass = getTranslatorClass(clazz);
		if (translatorClass == null) return null;
		
		try {
			PDUTranslator result = translatorInstanceMap.get(translatorClass);
			
			if (result == null) {
				result = translatorClass.newInstance();
				translatorInstanceMap.put(translatorClass, result);
			}
			
			return result;
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