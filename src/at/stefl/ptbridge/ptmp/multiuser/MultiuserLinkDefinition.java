package at.stefl.ptbridge.ptmp.multiuser;

import java.util.UUID;


public class MultiuserLinkDefinition {
	
	private final UUID uuid;
	private MultiuserLinkType type;
	
	private MultiuserInterfaceType interfaceType;
	private String interfaceName;
	private boolean interfaceUp;
	private boolean interfaceCrossing;
	
	private int bandwidth;
	private boolean fullDuplex;
	private boolean autoBandwidth;
	private boolean autoDuplex;
	
	private int clockRate;
	private boolean dce;
	
	private boolean deviceUp;
	
	public MultiuserLinkDefinition() {
		this(UUID.randomUUID());
	}
	
	public MultiuserLinkDefinition(UUID uuid) {
		this.uuid = uuid;
	}
	
	public MultiuserLinkDefinition(String interfaceName) {
		this(UUID.nameUUIDFromBytes(interfaceName.getBytes()));
		
		this.interfaceName = interfaceName;
	}
	
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (obj == this) return true;
		
		if (!(obj instanceof MultiuserLinkDefinition)) return false;
		MultiuserLinkDefinition link = (MultiuserLinkDefinition) obj;
		
		return uuid.equals(link.uuid);
	}
	
	public int hashCode() {
		return uuid.hashCode();
	}
	
	public UUID getUuid() {
		return uuid;
	}
	
	public MultiuserLinkType getType() {
		return type;
	}
	
	public MultiuserInterfaceType getInterfaceType() {
		return interfaceType;
	}
	
	public String getInterfaceName() {
		return interfaceName;
	}
	
	public boolean isInterfaceUp() {
		return interfaceUp;
	}
	
	public boolean isInterfaceCrossing() {
		return interfaceCrossing;
	}
	
	public int getBandwidth() {
		return bandwidth;
	}
	
	public boolean isFullDuplex() {
		return fullDuplex;
	}
	
	public boolean isAutoBandwidth() {
		return autoBandwidth;
	}
	
	public boolean isAutoDuplex() {
		return autoDuplex;
	}
	
	public int getClockRate() {
		return clockRate;
	}
	
	public boolean isDCE() {
		return dce;
	}
	
	public boolean isDeviceUp() {
		return deviceUp;
	}
	
	public void setType(MultiuserLinkType type) {
		this.type = type;
	}
	
	public void setInterfaceType(MultiuserInterfaceType interfaceType) {
		this.interfaceType = interfaceType;
	}
	
	public void setInterfaceName(String interfaceName) {
		this.interfaceName = interfaceName;
	}
	
	public void setInterfaceUp(boolean interfaceUp) {
		this.interfaceUp = interfaceUp;
	}
	
	public void setInterfaceCrossing(boolean interfaceCrossing) {
		this.interfaceCrossing = interfaceCrossing;
	}
	
	public void setBandwidth(int bandwidth) {
		this.bandwidth = bandwidth;
	}
	
	public void setFullDuplex(boolean fullDuplex) {
		this.fullDuplex = fullDuplex;
	}
	
	public void setAutoBandwidth(boolean autoBandwidth) {
		this.autoBandwidth = autoBandwidth;
	}
	
	public void setAutoDuplex(boolean autoDuplex) {
		this.autoDuplex = autoDuplex;
	}
	
	public void setClockRate(int clockRate) {
		this.clockRate = clockRate;
	}
	
	public void setDCE(boolean dce) {
		this.dce = dce;
	}
	
	public void setDeviceUp(boolean deviceUp) {
		this.deviceUp = deviceUp;
	}
	
}