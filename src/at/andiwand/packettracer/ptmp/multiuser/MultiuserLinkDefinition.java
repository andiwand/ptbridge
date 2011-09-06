package at.andiwand.packettracer.ptmp.multiuser;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;


public class MultiuserLinkDefinition {
	
	public static final int TYPE_NONE				= 0;
	public static final int TYPE_STRAIGHT_THROUGH	= 1;
	public static final int TYPE_CROSS_OVER			= 2;
	public static final int TYPE_CONSOLE			= 3;
	public static final int TYPE_FIBER				= 4;
	public static final int TYPE_SERIAL				= 5;
	public static final int TYPE_PHONE				= 6;
	public static final int TYPE_COAXIAL			= 7;
	public static final Set<Integer> TYPE_SET = Collections
			.unmodifiableSet(new HashSet<Integer>(Arrays.asList(TYPE_NONE,
					TYPE_STRAIGHT_THROUGH, TYPE_CROSS_OVER, TYPE_CONSOLE,
					TYPE_FIBER, TYPE_SERIAL, TYPE_PHONE, TYPE_COAXIAL)));
	
	public static final int INTERFACE_CONSOLE					= 0;
	public static final int INTERFACE_COPPER_ETHERNET			= 2;
	public static final int INTERFACE_COPPER_FAST_ETHERNET		= 3;
	public static final int INTERFACE_COPPER_GIGABIT_ETHERNET	= 4;
	public static final int INTERFACE_FIBER_FAST_ETHERNET		= 5;
	public static final int INTERFACE_FIBER_GIGABIT_ETHERNET	= 6;
	public static final int INTERFACE_SERIAL_BIG				= 7;
	public static final int INTERFACE_SERIAL					= 8;
	public static final int INTERFACE_ANALOG_PHONE				= 18;
	public static final int INTERFACE_COAXIAL					= 21;
	public static final Set<Integer> INTERFACE_SET = Collections
			.unmodifiableSet(new HashSet<Integer>(Arrays
					.asList(INTERFACE_CONSOLE, INTERFACE_COPPER_ETHERNET,
							INTERFACE_COPPER_FAST_ETHERNET,
							INTERFACE_COPPER_GIGABIT_ETHERNET,
							INTERFACE_FIBER_FAST_ETHERNET,
							INTERFACE_FIBER_GIGABIT_ETHERNET,
							INTERFACE_SERIAL_BIG, INTERFACE_SERIAL,
							INTERFACE_ANALOG_PHONE, INTERFACE_COAXIAL)));
	
	
	public static boolean legalType(int type) {
		return TYPE_SET.contains(type);
	}
	public static boolean legalInterfaceType(int interfaceType) {
		return INTERFACE_SET.contains(interfaceType);
	}
	
	
	
	
	
	private final UUID uuid;
	private int type;
	
	private int interfaceType;
	private String interfaceName = "";
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
		uuid = UUID.nameUUIDFromBytes(interfaceName.getBytes());
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
	public int getType() {
		return type;
	}
	
	public int getInterfaceType() {
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
	
	
	public void setType(int type) {
		if (!legalType(type))
			throw new IllegalArgumentException("Illegal type");
		
		this.type = type;
	}
	
	public void setInterfaceType(int interfaceType) {
		if (!legalInterfaceType(interfaceType))
			throw new IllegalArgumentException("Illegal interface type");
		
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