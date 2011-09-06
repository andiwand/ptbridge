package at.andiwand.packettracer.ptmp.multiuser.pdu;

import at.andiwand.packettracer.ptmp.PTMPConfiguration;
import at.andiwand.packettracer.ptmp.PTMPDataReader;
import at.andiwand.packettracer.ptmp.PTMPDataWriter;


public abstract class MultiuserProtocolDataUnit {
	
	public abstract void getBytes(PTMPDataWriter writer);
	public byte[] getBytes(int encoding) {
		PTMPDataWriter writer = new PTMPDataWriter(encoding);
		getBytes(writer);
		return writer.getData();
	}
	public byte[] getBytes(PTMPConfiguration configuration) {
		return getBytes(configuration.encoding());
	}
	
	public abstract void parse(PTMPDataReader reader);
	public void parse(byte[] value, int encoding) {
		parse(new PTMPDataReader(value, encoding));
	}
	
}