package at.andiwand.packettracer.bridge.ptmp.packet;

import java.util.Arrays;

import at.andiwand.packettracer.bridge.ptmp.PTMPConfiguration;
import at.andiwand.packettracer.bridge.ptmp.PTMPDataReader;
import at.andiwand.packettracer.bridge.ptmp.PTMPDataWriter;
import at.andiwand.packettracer.bridge.ptmp.PTMPEncoding;


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
	
	public void getValue(PTMPDataWriter writer) {
		if (writer.getEncoding() != encoding)
			throw new IllegalArgumentException("Illegal encoding!");
		
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