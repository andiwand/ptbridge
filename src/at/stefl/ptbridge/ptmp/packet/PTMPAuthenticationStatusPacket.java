package at.stefl.ptbridge.ptmp.packet;

import at.stefl.ptbridge.ptmp.PTMPDataReader;
import at.stefl.ptbridge.ptmp.PTMPDataWriter;
import at.stefl.ptbridge.ptmp.PTMPEncoding;


public class PTMPAuthenticationStatusPacket extends PTMPPacket {
	
	public static final int TYPE = TYPE_AUTHENTICATION_STATUS;
	
	private boolean status;
	
	public PTMPAuthenticationStatusPacket(boolean status) {
		super(TYPE);
		
		this.status = status;
	}
	
	public PTMPAuthenticationStatusPacket(PTMPDataReader in) {
		super(in);
	}
	
	public PTMPAuthenticationStatusPacket(byte[] packet, PTMPEncoding encoding) {
		super(packet, encoding);
	}
	
	public PTMPAuthenticationStatusPacket(PTMPEncodedPacket packet) {
		super(packet);
	}
	
	public PTMPAuthenticationStatusPacket(PTMPAuthenticationStatusPacket packet) {
		super(packet);
		
		status = packet.status;
	}
	
	public boolean getStatus() {
		return status;
	}
	
	public void getValue(PTMPDataWriter out) {
		out.writeBoolean(status);
	}
	
	public void setStatus(boolean status) {
		this.status = status;
	}
	
	public void parseValue(PTMPDataReader in) {
		status = in.readBoolean();
	}
	
	protected boolean legalType(int type) {
		return type == TYPE;
	}
	
}