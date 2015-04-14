package at.stefl.ptbridge.traverser.translator;

import at.stefl.packetsocket.pdu.PDU;
import at.stefl.ptbridge.ptmp.multiuser.pdu.MultiuserPDU;


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