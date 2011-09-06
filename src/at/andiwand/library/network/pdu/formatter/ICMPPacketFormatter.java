package at.andiwand.library.network.pdu.formatter;

import java.io.ByteArrayOutputStream;

import at.andiwand.library.network.Assignments;
import at.andiwand.library.network.InternetChecksum;
import at.andiwand.library.network.pdu.ICMPPacket;
import at.andiwand.library.network.pdu.ProtocolDataUnit;
import at.andiwand.library.util.DataReader;
import at.andiwand.library.util.DataWriter;


public class ICMPPacketFormatter extends ProtocolDataUnitFormatter {
	
	public void format(ProtocolDataUnit protocolDataUnit, DataWriter dataWriter) {
		if (!(protocolDataUnit instanceof ICMPPacket))
			throw new IllegalArgumentException("Illegal PDU class");
		ICMPPacket packet = (ICMPPacket) protocolDataUnit;
		
		DataWriter packetWriter = new DataWriter();
		
		packetWriter.writeByte(packet.getType());
		packetWriter.writeByte(packet.getCode());
		packetWriter.writeShort(0); // checksum
		
		switch (packet.getType()) {
		case Assignments.ICMP.TYPE_ECHO:
		case Assignments.ICMP.TYPE_ECHO_REPLY:
			ICMPPacket.Echo echoPacket = (ICMPPacket.Echo) packet;
			
			packetWriter.writeShort(echoPacket.getIdentifier());
			packetWriter.writeShort(echoPacket.getSequenceNumber());
			packetWriter.write(echoPacket.getData());
			
			break;
			
		default:
			throw new IllegalStateException("Unreachable section");
		}
		
		byte[] bytes = packetWriter.getData();
		short checksum = InternetChecksum.calculateChecksum(bytes, 0,
				bytes.length);
		bytes[2] = (byte) (checksum >> 8);
		bytes[3] = (byte) (checksum >> 0);
		
		dataWriter.write(bytes);
	}
	public ICMPPacket parse(DataReader dataReader) {
		ICMPPacket packet;
		
		byte type = dataReader.readByte();
		byte code = dataReader.readByte();
		dataReader.readShort(); // checksum
		
		switch (type) {
		case Assignments.ICMP.TYPE_ECHO:
		case Assignments.ICMP.TYPE_ECHO_REPLY:
			ICMPPacket.Echo echoPacket = new ICMPPacket.Echo();
			
			echoPacket.setIdentifier(dataReader.readUnsignedShort());
			echoPacket.setSequenceNumber(dataReader.readUnsignedShort());
			
			ByteArrayOutputStream dataOutputStream = new ByteArrayOutputStream();
			int read;
			while ((read = dataReader.read()) != -1) {
				dataOutputStream.write(read);
			}
			echoPacket.setData(dataOutputStream.toByteArray());
			
			packet = echoPacket;
			break;
		
		default:
			throw new IllegalStateException("Unreachable section");
		}
		
		packet.setType(type);
		packet.setCode(code);
		
		return packet;
	}
	
}