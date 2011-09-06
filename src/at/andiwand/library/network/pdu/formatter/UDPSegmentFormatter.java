package at.andiwand.library.network.pdu.formatter;

import at.andiwand.library.network.pdu.ProtocolDataUnit;
import at.andiwand.library.network.pdu.UDPSegment;
import at.andiwand.library.util.DataReader;
import at.andiwand.library.util.DataWriter;


public class UDPSegmentFormatter extends ProtocolDataUnitFormatter {
	
	public void format(ProtocolDataUnit protocolDataUnit, DataWriter dataWriter) {
		if (!(protocolDataUnit instanceof UDPSegment))
			throw new IllegalArgumentException("Illegal PDU class");
		UDPSegment segment = (UDPSegment) protocolDataUnit;
		
		DataWriter segmentDataWriter = new DataWriter();
		
		segmentDataWriter.writeShort(segment.getSourcePort());
		segmentDataWriter.writeShort(segment.getDestinationPort());
		segmentDataWriter.writeShort(0); // length
		segmentDataWriter.writeShort(0); // checksum
		
		
		
		byte[] bytes = segmentDataWriter.getData();
		int length = bytes.length;
		bytes[4] = (byte) (length >> 8);
		bytes[5] = (byte) (length >> 0);
		
		dataWriter.write(bytes);
	}
	public UDPSegment parse(DataReader dataReader) {
		UDPSegment segment = new UDPSegment();
		
		segment.setSourcePort(dataReader.readShort());
		segment.setDestinationPort(dataReader.readShort());
		dataReader.readShort(); // length
		dataReader.readShort(); // checksum
		
		
		
		return segment;
	}
	
}