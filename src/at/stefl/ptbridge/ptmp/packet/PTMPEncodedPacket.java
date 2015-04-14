package at.stefl.ptbridge.ptmp.packet;

import java.util.Arrays;

import at.stefl.ptbridge.ptmp.PTMPConfiguration;
import at.stefl.ptbridge.ptmp.PTMPDataReader;
import at.stefl.ptbridge.ptmp.PTMPDataWriter;
import at.stefl.ptbridge.ptmp.PTMPEncoding;


public class PTMPEncodedPacket extends PTMPPacket {
	
	private final byte[] value;
	private final PTMPEncoding encoding;
	
	public PTMPEncodedPacket(int type, byte[] value, PTMPEncoding encoding) {
		super(type);
		
		this.value = Arrays.copyOf(value, value.length);
		this.encoding = encoding;
	}
	
	public PTMPEncodedPacket(int type, byte[] value,
			PTMPConfiguration configuration) {
		this(type, value, configuration.getEncoding());
	}
	
	public byte[] getValue() {
		return Arrays.copyOf(value, value.length);
	}
	
	public PTMPEncoding getEncoding() {
		return encoding;
	}
	
	public void getValue(PTMPDataWriter out) {
		if (out.getEncoding() != encoding)
			throw new IllegalArgumentException("Illegal encoding!");
		
		out.write(value);
	}
	
	public byte[] getBytes() {
		return super.getBytes(encoding);
	}
	
	public void parseValue(PTMPDataReader in) {
		throw new UnsupportedOperationException();
	}
	
	protected boolean legalType(int type) {
		return true;
	}
	
}