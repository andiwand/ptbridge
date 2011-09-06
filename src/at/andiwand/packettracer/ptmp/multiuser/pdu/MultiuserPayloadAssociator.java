package at.andiwand.packettracer.ptmp.multiuser.pdu;

import java.util.HashMap;
import java.util.Map;


public class MultiuserPayloadAssociator {
	
	private final Map<Class<? extends MultiuserProtocolDataUnit>, String> payloadNameMap = new HashMap<Class<? extends MultiuserProtocolDataUnit>, String>();
	private final Map<String, Class<? extends MultiuserProtocolDataUnit>> payloadClassMap = new HashMap<String, Class<? extends MultiuserProtocolDataUnit>>();
	
	
	
	public String getPayloadName(
			Class<? extends MultiuserProtocolDataUnit> payloadClass) {
		return payloadNameMap.get(payloadClass);
	}
	
	public Class<? extends MultiuserProtocolDataUnit> getPayloadClass(
			String payloadName) {
		return payloadClassMap.get(payloadName);
	}
	public MultiuserProtocolDataUnit getPayloadInstance(String payloadName) {
		Class<? extends MultiuserProtocolDataUnit> payloadClass = getPayloadClass(payloadName);
		
		if (payloadClass == null)
			return null;
		
		try {
			return payloadClass.newInstance();
		} catch (Exception e) {
			throw new IllegalStateException("Unreachable section");
		}
	}
	
	
	public void putEntry(String payloadName,
			Class<? extends MultiuserProtocolDataUnit> payloadClass) {
		payloadNameMap.put(payloadClass, payloadName);
		payloadClassMap.put(payloadName, payloadClass);
	}
	
	public void removeEntry(String payloadName) {
		Class<? extends MultiuserProtocolDataUnit> payloadClass = payloadClassMap.remove(payloadName);
		payloadNameMap.remove(payloadClass);
	}
	public void removeEntry(
			Class<? extends MultiuserProtocolDataUnit> payloadClass) {
		String payloadName = payloadNameMap.remove(payloadClass);
		payloadClassMap.remove(payloadName);
	}
	
	
	public void clear() {
		payloadNameMap.clear();
		payloadClassMap.clear();
	}
	
}