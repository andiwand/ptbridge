package at.andiwand.packettracer.bridge.traverser.translator;

import at.andiwand.packetsocket.pdu.PDU;
import at.andiwand.packettracer.bridge.ptmp.multiuser.pdu.MultiuserPDU;


public abstract class GenericPDUTranslator<T1 extends PDU, T2 extends MultiuserPDU> extends
		PDUTranslator {
	
	@Override
	@SuppressWarnings("unchecked")
	public T1 translate(MultiuserPDU pdu) {
		return translateGeneric((T2) pdu);
	}
	
	protected abstract T1 translateGeneric(T2 pdu);
	
	@Override
	@SuppressWarnings("unchecked")
	public T2 translate(PDU pdu) {
		return translateGeneric((T1) pdu);
	}
	
	protected abstract T2 translateGeneric(T1 pdu);
	
}