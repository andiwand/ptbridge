package at.andiwand.packettracer.traverser.translator;

import at.andiwand.library.network.pdu.IPv6Packet;
import at.andiwand.library.network.pdu.ProtocolDataUnit;
import at.andiwand.packettracer.ptmp.multiuser.pdu.MultiuserIPv6Packet;
import at.andiwand.packettracer.ptmp.multiuser.pdu.MultiuserProtocolDataUnit;


public class IPv6Translator extends ProtocolDataUnitTranslator {
	
	public MultiuserIPv6Packet translate(ProtocolDataUnit protocolDataUnit) {
		return null;
	}
	
	public IPv6Packet translate(MultiuserProtocolDataUnit protocolDataUnit) {
		return null;
	}
	
}