package at.stefl.ptbridge.traverser.translator;

import at.stefl.packetsocket.pdu.PDU;
import at.stefl.ptbridge.ptmp.multiuser.pdu.MultiuserPDU;


public abstract class PDUTranslator {
	
	public abstract PDU toNetwork(MultiuserPDU pdu);
	
	public abstract MultiuserPDU toMultiuser(PDU pdu);
	
}