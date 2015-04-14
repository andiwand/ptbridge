package at.stefl.ptbridge.ptmp;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.UUID;


public class PTMPKeyUtil {
	
	private static final String KEY_CHARSET = "us-ascii";
	
	public static byte[] getBytes(UUID serverUuid, UUID clientUuid,
			Date serverTimestamp, Date clientTimestamp) {
		String key = "";
		
		key += "{" + serverUuid.toString() + "}";
		key += "{" + clientUuid.toString() + "}";
		key += "PTMP";
		key += PTMPAssignments.TIMESTAMP_FORMAT.format(serverTimestamp);
		key += PTMPAssignments.TIMESTAMP_FORMAT.format(clientTimestamp);
		
		try {
			return key.getBytes(KEY_CHARSET);
		} catch (UnsupportedEncodingException e) {
			throw new IllegalStateException("Unreachable section!");
		}
	}
	
	private PTMPKeyUtil() {}
	
}