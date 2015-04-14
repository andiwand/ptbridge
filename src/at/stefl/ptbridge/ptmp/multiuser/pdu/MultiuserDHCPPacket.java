package at.stefl.ptbridge.ptmp.multiuser.pdu;

import at.stefl.commons.network.ip.IPv4Address;
import at.stefl.commons.network.ip.SubnetMask;
import at.stefl.commons.network.mac.MACAddress;
import at.stefl.ptbridge.ptmp.PTMPDataReader;
import at.stefl.ptbridge.ptmp.PTMPDataWriter;


public class MultiuserDHCPPacket extends MultiuserPDU {
	
	private byte messageType;
	private byte unknown1;
	private byte unknown2;
	private byte unknown3;
	private byte unknown9;
	private String transactionId;
	private short secondsElapsed;
	private short flags;
	private int unknown4;
	private int unknown5;
	private int unknown6;
	private IPv4Address clientAddress;
	private IPv4Address yourAddress;
	private IPv4Address serverAddress;
	private IPv4Address gatewayAddress;
	private SubnetMask subnetMask;
	private MACAddress clientHardwareAddress;
	private String serverName;
	private String file;
	private IPv4Address tftpServerAddress;
	private int unknown7;
	private String unknown8;
	private IPv4Address dnsServerAddress;
	
	public byte getMessageType() {
		return messageType;
	}
	
	public byte getUnknown1() {
		return unknown1;
	}
	
	public byte getUnknown2() {
		return unknown2;
	}
	
	public byte getUnknown3() {
		return unknown3;
	}
	
	public byte getUnknown9() {
		return unknown9;
	}
	
	public String getTransactionId() {
		return transactionId;
	}
	
	public short getSecondsElapsed() {
		return secondsElapsed;
	}
	
	public short getFlags() {
		return flags;
	}
	
	public int getUnknown4() {
		return unknown4;
	}
	
	public int getUnknown5() {
		return unknown5;
	}
	
	public int getUnknown6() {
		return unknown6;
	}
	
	public IPv4Address getClientAddress() {
		return clientAddress;
	}
	
	public IPv4Address getYourAddress() {
		return yourAddress;
	}
	
	public IPv4Address getServerAddress() {
		return serverAddress;
	}
	
	public IPv4Address getGatewayAddress() {
		return gatewayAddress;
	}
	
	public SubnetMask getSubnetMask() {
		return subnetMask;
	}
	
	public MACAddress getClientHardwareAddress() {
		return clientHardwareAddress;
	}
	
	public String getServerName() {
		return serverName;
	}
	
	public String getFile() {
		return file;
	}
	
	public IPv4Address getTFTPServerAddress() {
		return tftpServerAddress;
	}
	
	public int getUnknown7() {
		return unknown7;
	}
	
	public String getUnknown8() {
		return unknown8;
	}
	
	public IPv4Address getDNSServerAddress() {
		return dnsServerAddress;
	}
	
	public void getBytes(PTMPDataWriter writer) {
		writer.writeByte(messageType);
		writer.writeByte(unknown1);
		writer.writeByte(unknown2);
		writer.writeByte(unknown3);
		writer.writeString(transactionId);
		writer.writeShort(secondsElapsed);
		writer.writeShort(flags);
		writer.writeInt(unknown4);
		writer.writeInt(unknown5);
		writer.writeInt(unknown6);
		writer.writeIPv4Address(clientAddress);
		writer.writeIPv4Address(yourAddress);
		writer.writeIPv4Address(serverAddress);
		writer.writeIPv4Address(gatewayAddress);
		writer.writeIPv4Address(subnetMask.toIPv4Address());
		writer.writeMACAddress(clientHardwareAddress);
		writer.writeString(serverName);
		writer.writeString(file);
		writer.writeIPv4Address(tftpServerAddress);
		writer.writeInt(unknown7);
		writer.writeString(unknown8);
		writer.writeIPv4Address(dnsServerAddress);
	}
	
	public void setMessageType(byte messageType) {
		this.messageType = messageType;
	}
	
	public void setUnknown1(byte unknown1) {
		this.unknown1 = unknown1;
	}
	
	public void setUnknown2(byte unknown2) {
		this.unknown2 = unknown2;
	}
	
	public void setUnknown3(byte unknown3) {
		this.unknown3 = unknown3;
	}
	
	public void setUnknown9(byte unknown9) {
		this.unknown9 = unknown9;
	}
	
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	
	public void setSecondsElapsed(short secondsElapsed) {
		this.secondsElapsed = secondsElapsed;
	}
	
	public void setFlags(short flags) {
		this.flags = flags;
	}
	
	public void setUnknown4(int unknown4) {
		this.unknown4 = unknown4;
	}
	
	public void setUnknown5(int unknown5) {
		this.unknown5 = unknown5;
	}
	
	public void setUnknown6(int unknown6) {
		this.unknown6 = unknown6;
	}
	
	public void setClientAddress(IPv4Address clientAddress) {
		this.clientAddress = clientAddress;
	}
	
	public void setYourAddress(IPv4Address yourAddress) {
		this.yourAddress = yourAddress;
	}
	
	public void setServerAddress(IPv4Address serverAddress) {
		this.serverAddress = serverAddress;
	}
	
	public void setGatewayAddress(IPv4Address gatewayAddress) {
		this.gatewayAddress = gatewayAddress;
	}
	
	public void setSubnetMask(SubnetMask subnetMask) {
		this.subnetMask = subnetMask;
	}
	
	public void setClientHardwareAddress(MACAddress clientHardwareAddress) {
		this.clientHardwareAddress = clientHardwareAddress;
	}
	
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}
	
	public void setFile(String file) {
		this.file = file;
	}
	
	public void setTFTPServerAddress(IPv4Address tftpServerAddress) {
		this.tftpServerAddress = tftpServerAddress;
	}
	
	public void setUnknown7(int unknown7) {
		this.unknown7 = unknown7;
	}
	
	public void setUnknown8(String unknown8) {
		this.unknown8 = unknown8;
	}
	
	public void setDNSServerAddress(IPv4Address dnsServerAddress) {
		this.dnsServerAddress = dnsServerAddress;
	}
	
	public void parse(PTMPDataReader reader) {
		messageType = reader.readByte();
		unknown1 = reader.readByte();
		unknown2 = reader.readByte();
		unknown3 = reader.readByte();
		unknown9 = reader.readByte();
		transactionId = reader.readString();
		secondsElapsed = reader.readShort();
		flags = reader.readShort();
		unknown4 = reader.readInt();
		unknown5 = reader.readInt();
		unknown6 = reader.readInt();
		clientAddress = reader.readIPv4Address();
		yourAddress = reader.readIPv4Address();
		serverAddress = reader.readIPv4Address();
		gatewayAddress = reader.readIPv4Address();
		subnetMask = new SubnetMask(reader.readIPv4Address());
		clientHardwareAddress = reader.readMACAddress();
		serverName = reader.readString();
		file = reader.readString();
		tftpServerAddress = reader.readIPv4Address();
		unknown7 = reader.readInt();
		unknown8 = reader.readString();
		dnsServerAddress = reader.readIPv4Address();
	}
	
}