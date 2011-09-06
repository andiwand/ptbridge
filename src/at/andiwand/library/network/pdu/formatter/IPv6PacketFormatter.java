package at.andiwand.library.network.pdu.formatter;

import at.andiwand.library.network.pdu.IPv6Packet;
import at.andiwand.library.network.pdu.ProtocolDataUnit;
import at.andiwand.library.util.DataReader;
import at.andiwand.library.util.DataWriter;


public class IPv6PacketFormatter extends ProtocolDataUnitFormatter {
	
	public void format(ProtocolDataUnit protocolDataUnit, DataWriter dataWriter) {
		if (!(protocolDataUnit instanceof IPv6Packet))
			throw new IllegalArgumentException("Illegal PDU type");
//		IPv6Packet packet = (IPv6Packet) protocolDataUnit;
		
		
	}
	public ProtocolDataUnit parse(DataReader dataReader) {
		IPv6Packet packet = new IPv6Packet();
		
		
		
		return packet;
	}
	
}