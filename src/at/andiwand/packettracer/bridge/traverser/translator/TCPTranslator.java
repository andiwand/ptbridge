package at.andiwand.packettracer.bridge.traverser.translator;

import at.andiwand.packetsocket.pdu.TCPSegment;
import at.andiwand.packettracer.bridge.ptmp.multiuser.pdu.MultiuserTCPSegment;


public class TCPTranslator extends
		GenericPDUTranslator<TCPSegment, MultiuserTCPSegment> {
	
	@Override
	protected TCPSegment translateGeneric(MultiuserTCPSegment segment) {
		return null;
	}
	
	@Override
	protected MultiuserTCPSegment translateGeneric(TCPSegment segment) {
		return null;
	}
	
}