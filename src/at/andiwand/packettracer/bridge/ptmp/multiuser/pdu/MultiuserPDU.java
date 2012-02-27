package at.andiwand.packettracer.bridge.ptmp.multiuser.pdu;

import at.andiwand.packettracer.bridge.ptmp.PTMPConfiguration;
import at.andiwand.packettracer.bridge.ptmp.PTMPDataReader;
import at.andiwand.packettracer.bridge.ptmp.PTMPDataWriter;
import at.andiwand.packettracer.bridge.ptmp.PTMPEncoding;


public abstract class MultiuserPDU {
	
	public abstract void getBytes(PTMPDataWriter writer);
	
	public byte[] getBytes(PTMPEncoding encoding) {
		PTMPDataWriter writer = new PTMPDataWriter(encoding);
		getBytes(writer);
		return writer.getData();
	}
	
	public byte[] getBytes(PTMPConfiguration configuration) {
		return getBytes(configuration.getEncoding());
	}
	
	public abstract void parse(PTMPDataReader reader);
	
	public void parse(byte[] value, PTMPEncoding encoding) {
		parse(new PTMPDataReader(value, encoding));
	}
	
}