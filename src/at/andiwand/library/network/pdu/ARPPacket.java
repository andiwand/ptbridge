package at.andiwand.library.network.pdu;


public class ARPPacket extends ProtocolDataUnit {
	
	private short hardwareType;
	private short protocolType;
	private short operation;
	private Object senderHardwareAddress;
	private Object senderProtocolAddress;
	private Object targetHardwareAddress;
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
	public Object getSenderProtocolAddress() {
		return senderProtocolAddress;
	}
	public Object getTargetHardwareAddress() {
		return targetHardwareAddress;
	}
	public Object getTargetProtocolAddress() {
		return targetProtocolAddress;
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
	public void setSenderProtocolAddress(Object senderProtocolAddress) {
		this.senderProtocolAddress = senderProtocolAddress;
	}
	public void setTargetHardwareAddress(Object targetHardwareAddress) {
		this.targetHardwareAddress = targetHardwareAddress;
	}
	public void setTargetProtocolAddress(Object targetProtocolAddress) {
		this.targetProtocolAddress = targetProtocolAddress;
	}
	
}