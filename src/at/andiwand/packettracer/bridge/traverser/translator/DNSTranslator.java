package at.andiwand.packettracer.bridge.traverser.translator;

import at.andiwand.packetsocket.pdu.DNSPacket;
import at.andiwand.packettracer.bridge.ptmp.multiuser.pdu.MultiuserDHCPPacket;


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