package at.stefl.ptbridge.ptmp.multiuser.pdu;

import at.stefl.ptbridge.ptmp.PTMPConfiguration;
import at.stefl.ptbridge.ptmp.PTMPDataReader;
import at.stefl.ptbridge.ptmp.PTMPDataWriter;
import at.stefl.ptbridge.ptmp.PTMPEncoding;


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