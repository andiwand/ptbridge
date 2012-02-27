package at.andiwand.packettracer.bridge.traverser.translator;

import at.andiwand.packetsocket.pdu.PDU;
import at.andiwand.packettracer.bridge.ptmp.multiuser.pdu.MultiuserPDU;


public abstract class PDUTranslator {
	
	public abstract PDU translate(MultiuserPDU pdu);
	
	public abstract MultiuserPDU translate(PDU pdu);
	
}