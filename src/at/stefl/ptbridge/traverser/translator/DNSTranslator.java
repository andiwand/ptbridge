package at.stefl.ptbridge.traverser.translator;

import at.stefl.packetsocket.pdu.DNSPacket;
import at.stefl.ptbridge.ptmp.multiuser.pdu.MultiuserDHCPPacket;


public class DNSTranslator extends
		GenericPDUTranslator<DNSPacket, MultiuserDHCPPacket> {
	
	@Override
	protected MultiuserDHCPPacket toMultiuserGeneric(DNSPacket packet) {
		return null;
	}
	
	@Override
	protected DNSPacket toNetworkGeneric(MultiuserDHCPPacket pdu) {
		return null;
	}
	
}