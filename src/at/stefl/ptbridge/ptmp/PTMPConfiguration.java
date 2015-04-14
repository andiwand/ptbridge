package at.stefl.ptbridge.ptmp;

import java.util.Arrays;


public class PTMPConfiguration {
	
	public static final PTMPEncoding DEFAULT_ENCODING = PTMPEncoding.TEXT;
	public static final PTMPEncryption DEFAULT_ENCRYPTION = PTMPEncryption.NONE;
	public static final PTMPCompression DEFAULT_COMPRESSION = PTMPCompression.NO;
	public static final PTMPAuthentication DEFAULT_AUTHENTICATION = PTMPAuthentication.CLEAR_TEXT;
	private static final int DEFAULT_KEEPALIVE = 0;
	
	public static final PTMPConfiguration DEFAULT = new PTMPConfiguration(
			DEFAULT_ENCODING, DEFAULT_ENCRYPTION, DEFAULT_COMPRESSION,
			DEFAULT_AUTHENTICATION, DEFAULT_KEEPALIVE);
	
	private final PTMPEncoding encoding;
	private final PTMPEncryption encryption;
	private final PTMPCompression compression;
	private final PTMPAuthentication authentication;
	private final int keepalive;
	
	public PTMPConfiguration(PTMPEncoding encoding, PTMPEncryption encryption,
			PTMPCompression compression, PTMPAuthentication authentication,
			int keepalive) {
		this.encoding = encoding;
		this.encryption = encryption;
		this.compression = compression;
		this.authentication = authentication;
		this.keepalive = keepalive;
	}
	
	public String toString() {
		return "encoding: " + encoding + "; " + "encryption: " + encryption
				+ "; " + "compression: " + compression + "; "
				+ "authentication: " + authentication + ";";
	}
	
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (obj == this) return true;
		
		if (!(obj instanceof PTMPConfiguration)) return false;
		PTMPConfiguration configuration = (PTMPConfiguration) obj;
		
		return (encoding == configuration.encoding)
				&& (encryption == configuration.encryption)
				&& (compression == configuration.compression)
				&& (authentication == configuration.authentication);
	}
	
	public int hashCode() {
		return Arrays.hashCode(new Object[] {encoding, encryption, compression,
				authentication});
	}
	
	public PTMPEncoding getEncoding() {
		return encoding;
	}
	
	public PTMPEncryption getEncryption() {
		return encryption;
	}
	
	public PTMPCompression getCompression() {
		return compression;
	}
	
	public PTMPAuthentication getAuthentication() {
		return authentication;
	}
	
	public int getKeepalive() {
		return keepalive;
	}
	
}