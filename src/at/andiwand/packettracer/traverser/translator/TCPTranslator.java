package at.andiwand.packettracer.traverser.translator;

import at.andiwand.library.network.pdu.ProtocolDataUnit;
import at.andiwand.library.network.pdu.TCPSegment;
import at.andiwand.packettracer.ptmp.multiuser.pdu.MultiuserProtocolDataUnit;
import at.andiwand.packettracer.ptmp.multiuser.pdu.MultiuserTCPSegment;


public class TCPTranslator extends ProtocolDataUnitTranslator {
	
	public MultiuserTCPSegment translate(ProtocolDataUnit protocolDataUnit) {
		return null;
	}
	
	public TCPSegment translate(MultiuserProtocolDataUnit protocolDataUnit) {
		return null;
	}
	
}