package at.andiwand.library.network.pdu.formatter;

import java.net.Inet4Address;
import java.net.Inet6Address;

import at.andiwand.library.network.Assignments;
import at.andiwand.library.network.mac.MACAddress;
import at.andiwand.library.network.pdu.ARPPacket;
import at.andiwand.library.network.pdu.ProtocolDataUnit;
import at.andiwand.library.util.DataReader;
import at.andiwand.library.util.DataWriter;


public class ARPPacketFormatter extends ProtocolDataUnitFormatter {
	
	public static void writeHardwareAddress(Object address, short hardwareType,
			DataWriter dataWriter) {
		switch (hardwareType) {
		case Assignments.ARP.HARDWARE_TYPE_ETHERNET:
			MACAddress macAddress = (MACAddress) address;
			dataWriter.writeMACAddress(macAddress);
			break;
		
		default:
			throw new IllegalStateException(
					"The hardware type is not supported");
		}
	}
	public static void writeProtocolAddress(Object address, short protocolType,
			DataWriter dataWriter) {
		switch (protocolType) {
		case Assignments.Ethernet.TYPE_IPV4:
			Inet4Address inet4Address = (Inet4Address) address;
			dataWriter.writeIP4Addres(inet4Address);
			break;
		case Assignments.Ethernet.TYPE_IPV6:
			Inet6Address inet6Address = (Inet6Address) address;
			dataWriter.writeIP6Addres(inet6Address);
			break;
		
		default:
			throw new IllegalStateException(
					"The protocol type is not supported");
		}
	}
	
	public static Object readHardwareAddress(short hardwareType,
			DataReader dataReader) {
		switch (hardwareType) {
		case Assignments.ARP.HARDWARE_TYPE_ETHERNET:
			return dataReader.readMACAddress();
		
		default:
			throw new IllegalStateException(
					"The hardware type is not supported");
		}
	}
	public static Object readProtocolAddress(short protocolType,
			DataReader dataReader) {
		switch (protocolType) {
		case Assignments.Ethernet.TYPE_IPV4:
			return dataReader.readIP4Addres();
		case Assignments.Ethernet.TYPE_IPV6:
			return dataReader.readIP6Addres();
		
		default:
			throw new IllegalStateException(
					"The protocol type is not supported");
		}
	}
	
	
	public void format(ProtocolDataUnit protocolDataUnit, DataWriter dataWriter) {
		if (!(protocolDataUnit instanceof ARPPacket))
			throw new IllegalArgumentException("Illegal PDU type");
		ARPPacket packet = (ARPPacket) protocolDataUnit;
		
		dataWriter.writeShort(packet.getHardwareType());
		dataWriter.writeShort(packet.getProtocolType());
		dataWriter.writeByte(Assignments.ARP.getHardwareLength(packet
				.getHardwareType()));
		dataWriter.writeByte(Assignments.ARP.getProtocolLength(packet
				.getProtocolType()));
		dataWriter.writeShort(packet.getOperation());
		writeHardwareAddress(packet.getSenderHardwareAddress(), packet
				.getHardwareType(), dataWriter);
		writeProtocolAddress(packet.getSenderProtocolAddress(), packet
				.getProtocolType(), dataWriter);
		writeHardwareAddress(packet.getTargetHardwareAddress(), packet
				.getHardwareType(), dataWriter);
		writeProtocolAddress(packet.getTargetProtocolAddress(), packet
				.getProtocolType(), dataWriter);
	}
	
	public ARPPacket parse(DataReader dataReader) {
		ARPPacket packet = new ARPPacket();
		
		packet.setHardwareType(dataReader.readShort());
		packet.setProtocolType(dataReader.readShort());
		
		if (dataReader.readByte() != Assignments.ARP.getHardwareLength(packet
				.getHardwareType()))
			throw new IllegalStateException("Illegal hardware address length");
		
		if (dataReader.readByte() != Assignments.ARP.getProtocolLength(packet
				.getProtocolType()))
			throw new IllegalStateException("Illegal protocol address length");
		
		packet.setOperation(dataReader.readShort());
		packet.setSenderHardwareAddress(readHardwareAddress(packet
				.getHardwareType(), dataReader));
		packet.setSenderProtocolAddress(readProtocolAddress(packet
				.getProtocolType(), dataReader));
		packet.setTargetHardwareAddress(readHardwareAddress(packet
				.getHardwareType(), dataReader));
		packet.setTargetProtocolAddress(readProtocolAddress(packet
				.getProtocolType(), dataReader));
		
		return packet;
	}
	
}