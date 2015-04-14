package at.stefl.ptbridge.ptmp.multiuser.pdu;

import at.stefl.commons.network.Assignments;
import at.stefl.commons.network.ip.IPv4Address;
import at.stefl.commons.network.mac.MACAddress;
import at.stefl.ptbridge.ptmp.PTMPDataReader;
import at.stefl.ptbridge.ptmp.PTMPDataWriter;


public class MultiuserARPPacket extends MultiuserPDU {
	
	public static void writeHardwareAddress(Object address, short hardwareType,
			PTMPDataWriter writer) {
		switch (hardwareType) {
		case Assignments.ARP.HARDWARE_TYPE_ETHERNET:
			MACAddress macAddress = (MACAddress) address;
			writer.writeMACAddress(macAddress);
			break;
		default:
			throw new IllegalStateException("Unsupported hardware type!");
		}
	}
	
	public static void writeProtocolAddress(Object address, short protocolType,
			PTMPDataWriter writer) {
		switch (protocolType) {
		case Assignments.Ethernet.TYPE_IPV4:
			writer.writeIPv4Address((IPv4Address) address);
			break;
		default:
			throw new IllegalStateException("Unsupported protocol type!");
		}
	}
	
	public static Object readHardwareAddress(short hardwareType,
			PTMPDataReader reader) {
		switch (hardwareType) {
		case Assignments.ARP.HARDWARE_TYPE_ETHERNET:
			return reader.readMACAddress();
			
		default:
			throw new IllegalStateException("Unsupported hardware type!");
		}
	}
	
	public static Object readProtocolAddress(short protocolType,
			PTMPDataReader reader) {
		switch (protocolType) {
		case Assignments.Ethernet.TYPE_IPV4:
			return reader.readIPv4Address();
			
		default:
			throw new IllegalStateException("Unsupported protocol type!");
		}
	}
	
	private short hardwareType;
	private short protocolType;
	private short operation;
	private Object senderHardwareAddress;
	private Object targetHardwareAddress;
	private Object senderProtocolAddress;
	private Object targetProtocolAddress;
	
	public short getHardwareType() {
		return hardwareType;
	}
	
	public short getProtocolType() {
		return protocolType;
	}
	
	public short getOperation() {
		return operation;
	}
	
	public Object getSenderHardwareAddress() {
		return senderHardwareAddress;
	}
	
	public Object getTargetHardwareAddress() {
		return targetHardwareAddress;
	}
	
	public Object getSenderProtocolAddress() {
		return senderProtocolAddress;
	}
	
	public Object getTargetProtocolAddress() {
		return targetProtocolAddress;
	}
	
	public void getBytes(PTMPDataWriter writer) {
		writer.writeShort(hardwareType);
		writer.writeShort(protocolType);
		writer.writeByte(Assignments.ARP.getHardwareLength(hardwareType));
		writer.writeByte(Assignments.ARP.getProtocolLength(protocolType));
		writer.writeShort(operation);
		writeHardwareAddress(senderHardwareAddress, hardwareType, writer);
		writeProtocolAddress(senderProtocolAddress, protocolType, writer);
		writeHardwareAddress(targetHardwareAddress, hardwareType, writer);
		writeProtocolAddress(targetProtocolAddress, protocolType, writer);
	}
	
	public void setHardwareType(short hardwareType) {
		this.hardwareType = hardwareType;
	}
	
	public void setProtocolType(short protocolType) {
		this.protocolType = protocolType;
	}
	
	public void setOperation(short operation) {
		this.operation = operation;
	}
	
	public void setSenderHardwareAddress(Object senderHardwareAddress) {
		this.senderHardwareAddress = senderHardwareAddress;
	}
	
	public void setTargetHardwareAddress(Object targetHardwareAddress) {
		this.targetHardwareAddress = targetHardwareAddress;
	}
	
	public void setSenderProtocolAddress(Object senderProtocolAddress) {
		this.senderProtocolAddress = senderProtocolAddress;
	}
	
	public void setTargetProtocolAddress(Object targetProtocolAddress) {
		this.targetProtocolAddress = targetProtocolAddress;
	}
	
	public void parse(PTMPDataReader reader) {
		hardwareType = reader.readShort();
		protocolType = reader.readShort();
		
		if (reader.readByte() != Assignments.ARP
				.getHardwareLength(hardwareType))
			throw new IllegalStateException("Illegal hardware address length!");
		
		if (reader.readByte() != Assignments.ARP
				.getProtocolLength(protocolType))
			throw new IllegalStateException("Illegal protocol address length!");
		
		operation = reader.readShort();
		senderHardwareAddress = readHardwareAddress(hardwareType, reader);
		senderProtocolAddress = readProtocolAddress(protocolType, reader);
		targetHardwareAddress = readHardwareAddress(hardwareType, reader);
		targetProtocolAddress = readProtocolAddress(protocolType, reader);
	}
	
}