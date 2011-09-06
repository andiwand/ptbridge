package at.andiwand.packettracer.ptmp.packet;

import at.andiwand.packettracer.ptmp.PTMPConfiguration;
import at.andiwand.packettracer.ptmp.PTMPDataReader;
import at.andiwand.packettracer.ptmp.PTMPDataWriter;


public class PTMPEncodedPacket extends PTMPPacket {
	
	private final byte[] value;
	private final int encoding;
	
	
	public PTMPEncodedPacket(int type, byte[] value, int encoding) {
		super(type);
		
		this.value = value;
		this.encoding = encoding;
	}
	public PTMPEncodedPacket(int type, byte[] value,
			PTMPConfiguration configuration) {
		this(type, value, configuration.encoding());
	}
	
	
	public byte[] getValue() {
		return value;
	}
	public int encoding() {
		return encoding;
	}
	public void getValue(PTMPDataWriter writer) {
		if (writer.encoding() != encoding)
			throw new IllegalArgumentException("Illegal encoding");
		
		writer.write(value);
	}
	public byte[] getBytes() {
		return super.getBytes(encoding);
	}
	
	public void parseValue(PTMPDataReader reader) {
		throw new UnsupportedOperationException();
	}
	
	protected boolean legalType(int type) {
		return true;
	}
	
}