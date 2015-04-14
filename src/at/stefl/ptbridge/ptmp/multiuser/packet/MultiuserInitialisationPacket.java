package at.stefl.ptbridge.ptmp.multiuser.packet;

import java.util.UUID;

import at.stefl.ptbridge.ptmp.PTMPDataReader;
import at.stefl.ptbridge.ptmp.PTMPDataWriter;
import at.stefl.ptbridge.ptmp.PTMPEncoding;
import at.stefl.ptbridge.ptmp.packet.PTMPEncodedPacket;


public class MultiuserInitialisationPacket extends MultiuserPacket {
	
	public static final int TYPE_REQUEST = MultiuserPacket.TYPE_INITIALISATION_REQUEST;
	public static final int TYPE_RESPONSE = MultiuserPacket.TYPE_INITIALISATION_RESPONSE;
	
	private String username;
	private UUID uuid;
	
	public MultiuserInitialisationPacket(int type, String username, UUID uuid) {
		super(type);
		
		this.username = username;
		this.uuid = uuid;
	}
	
	public MultiuserInitialisationPacket(PTMPDataReader reader) {
		super(reader);
	}
	
	public MultiuserInitialisationPacket(byte[] packet, PTMPEncoding encoding) {
		super(packet, encoding);
	}
	
	public MultiuserInitialisationPacket(PTMPEncodedPacket packet) {
		super(packet);
	}
	
	public MultiuserInitialisationPacket(MultiuserInitialisationPacket packet) {
		super(packet);
		
		username = packet.username;
		uuid = packet.uuid;
	}
	
	public String getUsername() {
		return username;
	}
	
	public UUID getUuid() {
		return uuid;
	}

	@Override
	public void getValue(PTMPDataWriter writer) {
		writer.writeString(username);
		writer.writeUuid(uuid);
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

	@Override
	public void parseValue(PTMPDataReader reader) {
		username = reader.readString();
		uuid = reader.readUuid();
	}
	
	protected boolean legalType(int type) {
		return (type == TYPE_REQUEST) || (type == TYPE_RESPONSE);
	}
	
}