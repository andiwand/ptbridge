package at.andiwand.packettracer.bridge.traverser.translator;

import at.andiwand.packetsocket.pdu.PDU;
import at.andiwand.packettracer.bridge.ptmp.multiuser.pdu.MultiuserPDU;


public abstract class PDUTranslator {
	
	public abstract PDU toNetwork(MultiuserPDU pdu);
	
	public abstract MultiuserPDU toMultiuser(PDU pdu);
	
}