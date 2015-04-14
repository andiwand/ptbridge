package at.stefl.ptbridge.ptmp;

import java.text.SimpleDateFormat;

import at.stefl.commons.network.mac.MACAddressFormat;
import at.stefl.commons.network.mac.SimpleMACAddressFormat;


public class PTMPAssignments {
	
	public static final int PORT = 38000;
	
	public static final char STRING_TERMINATION = '\0';
	public static final String STRING_CHARSET = "utf-8";
	
	public static final int IP4_SIZE = 4;
	
	public static final int MAC_SIZE = 6;
	public static final MACAddressFormat MAC_ADDRESS_FORMAT = new SimpleMACAddressFormat(
			"XXXX.XXXX.XXXX");
	
	public static final int UUID_SIZE = 16;
	
	public static final SimpleDateFormat TIMESTAMP_FORMAT = new SimpleDateFormat(
			"yyyyMMddHHmmss");
	
	private PTMPAssignments() {}
	
}