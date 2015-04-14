package at.stefl.ptbridge.ptmp.multiuser.packet;

import at.stefl.ptbridge.ptmp.PTMPDataReader;
import at.stefl.ptbridge.ptmp.PTMPDataWriter;
import at.stefl.ptbridge.ptmp.PTMPEncoding;
import at.stefl.ptbridge.ptmp.packet.PTMPEncodedPacket;


public class MultiuserXYZPacket extends MultiuserPacket {
	
	public static final int TYPE = TYPE_XYZ;
	
	private int xyz;
	
	public MultiuserXYZPacket(int xyz) {
		super(TYPE);
		
		this.xyz = xyz;
	}
	
	public MultiuserXYZPacket(PTMPDataReader reader) {
		super(reader);
	}
	
	public MultiuserXYZPacket(byte[] packet, PTMPEncoding encoding) {
		super(packet, encoding);
	}
	
	public MultiuserXYZPacket(PTMPEncodedPacket packet) {
		super(packet);
	}
	
	public MultiuserXYZPacket(MultiuserXYZPacket packet) {
		super(packet);
		
		xyz = packet.xyz;
	}
	
	public int getXyz() {
		return xyz;
	}
	
	@Override
	public void getValue(PTMPDataWriter writer) {
		writer.writeInt(xyz);
	}
	
	public void setXyz(int xyz) {
		this.xyz = xyz;
	}
	
	@Override
	public void parseValue(PTMPDataReader reader) {
		xyz = reader.readInt();
	}
	
	@Override
	protected boolean legalType(int type) {
		return type == TYPE;
	}
	
}