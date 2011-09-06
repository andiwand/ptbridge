package at.andiwand.packettracer.ptmp.packet;

import java.util.UUID;

import at.andiwand.packettracer.ptmp.PTMPConfiguration;
import at.andiwand.packettracer.ptmp.PTMPDataReader;
import at.andiwand.packettracer.ptmp.PTMPDataWriter;
import at.andiwand.packettracer.ptmp.PTMPTimestamp;


public class PTMPNegotiationPacket extends PTMPPacket {
	
	public static final int TYPE_REQUEST = TYPE_NEGOTIATION_REQUEST;
	public static final int TYPE_RESPONSE = TYPE_NEGOTIATION_RESPONSE;
	
	private static final String PTMP_IDENTIFIER = "PTMP";
	private static final int PTMP_VERSION = 1;
	
	
	
	
	private UUID applicationId;
	private PTMPConfiguration configuration;
	private PTMPTimestamp timestamp;
	private int keepAlivePeriod;
	
	
	
	public PTMPNegotiationPacket(int type, UUID applicationId,
			PTMPConfiguration configuration, PTMPTimestamp timestamp,
			int keepAlivePeriod) {
		super(type);
		
		this.applicationId = applicationId;
		this.configuration = configuration;
		this.timestamp = timestamp;
		this.keepAlivePeriod = keepAlivePeriod;
	}
	public PTMPNegotiationPacket(int type, PTMPConfiguration configuration) {
		this(type, UUID.randomUUID(), configuration, new PTMPTimestamp(), 0);
	}
	public PTMPNegotiationPacket(PTMPDataReader reader) {
		super(reader);
	}
	public PTMPNegotiationPacket(byte[] packet, int encoding) {
		super(packet, encoding);
	}
	public PTMPNegotiationPacket(PTMPEncodedPacket packet) {
		super(packet);
	}
	public PTMPNegotiationPacket(PTMPNegotiationPacket packet) {
		super(packet);
		
		applicationId = packet.applicationId;
		configuration = packet.configuration;
		timestamp = packet.timestamp;
		keepAlivePeriod = packet.keepAlivePeriod;
	}
	
	
	
	public UUID getApplicationId() {
		return applicationId;
	}
	public PTMPConfiguration getConfiguration() {
		return configuration;
	}
	public PTMPTimestamp getTimestamp() {
		return timestamp;
	}
	public int getKeepAlivePeriod() {
		return keepAlivePeriod;
	}
	public void getValue(PTMPDataWriter writer) {
		writer.writeString(PTMP_IDENTIFIER);
		writer.writeInt(PTMP_VERSION);
		writer.writeUuid(applicationId);
		writer.writeConfiguration(configuration);
		writer.writeString(timestamp.toString());
		writer.writeInt(keepAlivePeriod);
		writer.writeString("");
	}
	
	public void setApplicationId(UUID applicationId) {
		this.applicationId = applicationId;
	}
	public void setConfiguration(PTMPConfiguration configuration) {
		this.configuration = configuration;
	}
	public void setTimestamp(PTMPTimestamp timestamp) {
		this.timestamp = timestamp;
	}
	public void setKeepAlivePeriod(int keepAlivePeriod) {
		this.keepAlivePeriod = keepAlivePeriod;
	}
	
	
	public void parseValue(PTMPDataReader reader) {
		if (!reader.readString().equals(PTMP_IDENTIFIER))
			throw new IllegalArgumentException("PTMP identifier not found");
		
		if (reader.readInt() != PTMP_VERSION)
			throw new IllegalArgumentException("Illegal PTMP version");
		
		applicationId = reader.readUuid();
		
		configuration = reader.readConfiguration();
		
		timestamp = new PTMPTimestamp(reader.readString());
		
		keepAlivePeriod = reader.readInt();
		
		reader.readString();
	}
	
	
	protected boolean legalType(int type) {
		return (type == TYPE_REQUEST) || (type == TYPE_RESPONSE);
	}
	
}