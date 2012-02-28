package at.andiwand.packettracer.bridge.traverser.translator;

import at.andiwand.packetsocket.pdu.PDU;
import at.andiwand.packettracer.bridge.ptmp.multiuser.pdu.MultiuserPDU;


public abstract class GenericPDUTranslator<T1 extends PDU, T2 extends MultiuserPDU> extends
		PDUTranslator {
	
	@Override
	@SuppressWarnings("unchecked")
	public T1 toNetwork(MultiuserPDU pdu) {
		return toNetworkGeneric((T2) pdu);
	}
	
	protected abstract T1 toNetworkGeneric(T2 pdu);
	
	@Override
	@SuppressWarnings("unchecked")
	public T2 toMultiuser(PDU pdu) {
		return toMultiuserGeneric((T1) pdu);
	}
	
	protected abstract T2 toMultiuserGeneric(T1 pdu);
	
}