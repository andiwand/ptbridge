package at.andiwand.packettracer.ptmp.multiuser.pdu;

import java.net.Inet4Address;

import at.andiwand.library.network.Assignments;
import at.andiwand.library.network.mac.MACAddress;
import at.andiwand.packettracer.ptmp.PTMPDataReader;
import at.andiwand.packettracer.ptmp.PTMPDataWriter;


public class MultiuserARPPacket extends MultiuserProtocolDataUnit {
	
	private short hardwareType;
	private short protocolType;
	private short operation;
	private MACAddress senderHardwareAddress;
	private MACAddress targetHardwareAddress;
	private Inet4Address senderProtocolAddress;
	private Inet4Address targetProtocolAddress;
	
	
	
	public short getHardwareType() {
		return hardwareType;
	}
	public short getProtocolType() {
		return protocolType;
	}
	public short getOperation() {
		return operation;
	}
	public MACAddress getSenderHardwareAddress() {
		return senderHardwareAddress;
	}
	public MACAddress getTargetHardwareAddress() {
		return targetHardwareAddress;
	}
	public Inet4Address getSenderProtocolAddress() {
		return senderProtocolAddress;
	}
	public Inet4Address getTargetProtocolAddress() {
		return targetProtocolAddress;
	}
	public void getBytes(PTMPDataWriter writer) {
		writer.writeShort(hardwareType);
		writer.writeShort(protocolType);
		writer.writeByte(Assignments.ARP.getHardwareLength(hardwareType));
		writer.writeByte(Assignments.ARP.getProtocolLength(protocolType));
		writer.writeShort(operation);
		writer.writeMACAddress(senderHardwareAddress);
		writer.writeMACAddress(targetHardwareAddress);
		writer.writeIP4Addres(senderProtocolAddress);
		writer.writeIP4Addres(targetProtocolAddress);
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
	public void setSenderHardwareAddress(MACAddress senderHardwareAddress) {
		this.senderHardwareAddress = senderHardwareAddress;
	}
	public void setTargetHardwareAddress(MACAddress targetHardwareAddress) {
		this.targetHardwareAddress = targetHardwareAddress;
	}
	public void setSenderProtocolAddress(Inet4Address senderProtocolAddress) {
		this.senderProtocolAddress = senderProtocolAddress;
	}
	public void setTargetProtocolAddress(Inet4Address targetProtocolAddress) {
		this.targetProtocolAddress = targetProtocolAddress;
	}
	
	
	public void parse(PTMPDataReader reader) {
		hardwareType = reader.readShort();
		protocolType = reader.readShort();
		reader.readByte(); // hardware length
		reader.readByte(); // protocol length
		operation = reader.readShort();
		senderHardwareAddress = reader.readMACAddress();
		targetHardwareAddress = reader.readMACAddress();
		senderProtocolAddress = reader.readIP4Addres();
		targetProtocolAddress = reader.readIP4Addres();
	}
	
}