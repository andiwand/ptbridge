package at.stefl.ptbridge.ptmp.packet;

import at.stefl.ptbridge.ptmp.PTMPDataReader;
import at.stefl.ptbridge.ptmp.PTMPDataWriter;
import at.stefl.ptbridge.ptmp.PTMPEncoding;


public class PTMPDisconnectPacket extends PTMPPacket {
	
	public static final int TYPE = TYPE_DISCONNECT;
	
	public static final String NO_MESSAGE = "";
	
	private String message;
	
	public PTMPDisconnectPacket() {
		this(NO_MESSAGE);
	}
	
	public PTMPDisconnectPacket(String message) {
		super(TYPE);
	}
	
	public PTMPDisconnectPacket(PTMPDataReader in) {
		super(in);
	}
	
	public PTMPDisconnectPacket(byte[] packet, PTMPEncoding encoding) {
		super(packet, encoding);
	}
	
	public PTMPDisconnectPacket(PTMPEncodedPacket packet) {
		super(packet);
	}
	
	public PTMPDisconnectPacket(PTMPDisconnectPacket packet) {
		super(packet);
		
		message = packet.message;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void getValue(PTMPDataWriter out) {
		out.writeString(message);
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public void parseValue(PTMPDataReader in) {
		message = in.readString();
	}
	
	protected boolean legalType(int type) {
		return type == TYPE;
	}
	
}