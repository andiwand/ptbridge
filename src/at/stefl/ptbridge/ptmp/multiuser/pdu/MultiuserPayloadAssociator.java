package at.stefl.ptbridge.ptmp.multiuser.pdu;

import java.util.HashMap;
import java.util.Map;


public class MultiuserPayloadAssociator {
	
	private final Map<Class<? extends MultiuserPDU>, String> payloadNameMap = new HashMap<Class<? extends MultiuserPDU>, String>();
	private final Map<String, Class<? extends MultiuserPDU>> payloadClassMap = new HashMap<String, Class<? extends MultiuserPDU>>();
	
	public String getPayloadName(Class<? extends MultiuserPDU> payloadClass) {
		return payloadNameMap.get(payloadClass);
	}
	
	public Class<? extends MultiuserPDU> getPayloadClass(String payloadName) {
		return payloadClassMap.get(payloadName);
	}
	
	public MultiuserPDU getPayloadInstance(String payloadName) {
		Class<? extends MultiuserPDU> payloadClass = getPayloadClass(payloadName);
		
		if (payloadClass == null) return null;
		
		try {
			return payloadClass.newInstance();
		} catch (Exception e) {
			throw new IllegalStateException("Cannot create instace!");
		}
	}
	
	public void putEntry(String payloadName,
			Class<? extends MultiuserPDU> payloadClass) {
		payloadNameMap.put(payloadClass, payloadName);
		payloadClassMap.put(payloadName, payloadClass);
	}
	
	public void removeEntry(String payloadName) {
		Class<? extends MultiuserPDU> payloadClass = payloadClassMap
				.remove(payloadName);
		payloadNameMap.remove(payloadClass);
	}
	
	public void removeEntry(Class<? extends MultiuserPDU> payloadClass) {
		String payloadName = payloadNameMap.remove(payloadClass);
		payloadClassMap.remove(payloadName);
	}
	
	public void clear() {
		payloadNameMap.clear();
		payloadClassMap.clear();
	}
	
}