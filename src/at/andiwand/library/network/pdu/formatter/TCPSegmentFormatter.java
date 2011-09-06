package at.andiwand.library.network.pdu.formatter;

import at.andiwand.library.network.pdu.ProtocolDataUnit;
import at.andiwand.library.network.pdu.TCPSegment;
import at.andiwand.library.util.DataReader;
import at.andiwand.library.util.DataWriter;


public class TCPSegmentFormatter extends ProtocolDataUnitFormatter {
	
	public void format(ProtocolDataUnit protocolDataUnit, DataWriter dataWriter) {
		if (!(protocolDataUnit instanceof TCPSegment))
			throw new IllegalArgumentException("Illegal PDU type");
//		TCPSegment segment = (TCPSegment) protocolDataUnit;
		
		
	}
	public TCPSegment parse(DataReader dataReader) {
		TCPSegment packet = new TCPSegment();
		
		
		
		return packet;
	}
	
}