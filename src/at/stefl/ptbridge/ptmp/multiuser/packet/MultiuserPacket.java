package at.stefl.ptbridge.ptmp.multiuser.packet;

import at.stefl.ptbridge.ptmp.PTMPDataReader;
import at.stefl.ptbridge.ptmp.PTMPEncoding;
import at.stefl.ptbridge.ptmp.packet.PTMPEncodedPacket;
import at.stefl.ptbridge.ptmp.packet.PTMPPacket;


public abstract class MultiuserPacket extends PTMPPacket {
	
	public static final int TYPE_MIN = TYPE_MULTIUSER_MESSAGES_MIN;
	public static final int TYPE_MAX = TYPE_MULTIUSER_MESSAGES_MAX;
	
	public static final int TYPE_INITIALISATION_REQUEST = 200;
	public static final int TYPE_INITIALISATION_RESPONSE = 201;
	public static final int TYPE_XYZ = 202;
	public static final int TYPE_LINK_DEFINITION = 203;
	public static final int TYPE_NETWORK_PACKET = 205;
	public static final int TYPE_NETWORK_NAME = 210;
	
	public static boolean legalMultiuserType(int type) {
		return (type >= TYPE_MIN) && (type <= TYPE_MAX);
	}
	
	public MultiuserPacket(int type) {
		super(type);
	}
	
	public MultiuserPacket(PTMPDataReader reader) {
		super(reader);
	}
	
	public MultiuserPacket(byte[] packet, PTMPEncoding encoding) {
		super(packet, encoding);
	}
	
	public MultiuserPacket(PTMPEncodedPacket packet) {
		super(packet);
	}
	
	public MultiuserPacket(PTMPPacket packet) {
		super(packet);
	}
	
}