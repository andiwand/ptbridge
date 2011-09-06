package at.andiwand.packettracer.traverser.translator;

import at.andiwand.library.network.pdu.ProtocolDataUnit;
import at.andiwand.packettracer.ptmp.multiuser.pdu.MultiuserProtocolDataUnit;


public abstract class ProtocolDataUnitTranslator {
	
	public abstract MultiuserProtocolDataUnit translate(ProtocolDataUnit protocolDataUnit);
	
	public abstract ProtocolDataUnit translate(MultiuserProtocolDataUnit protocolDataUnit);
	
}