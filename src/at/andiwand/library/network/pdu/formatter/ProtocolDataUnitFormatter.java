package at.andiwand.library.network.pdu.formatter;

import at.andiwand.library.network.pdu.ProtocolDataUnit;
import at.andiwand.library.util.DataReader;
import at.andiwand.library.util.DataWriter;


public abstract class ProtocolDataUnitFormatter {
	
	public byte[] format(ProtocolDataUnit protocolDataUnit) {
		DataWriter dataWriter = new DataWriter();
		format(protocolDataUnit, dataWriter);
		return dataWriter.getData();
	}
	public abstract void format(ProtocolDataUnit protocolDataUnit,
			DataWriter dataWriter);
	
	public ProtocolDataUnit parse(byte[] buffer) {
		return parse(new DataReader(buffer));
	}
	public ProtocolDataUnit parse(byte[] buffer, int offset, int length) {
		return parse(new DataReader(buffer, offset, length));
	}
	public abstract ProtocolDataUnit parse(DataReader dataReader);
	
}