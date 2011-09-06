package at.andiwand.packettracer.ptmp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class PTMPTimestamp {
	
	private static final SimpleDateFormat TIMESTAMP_FORMAT = new SimpleDateFormat(
			"yyyyMMddHHmmss");
	
	
	
	private final Date timestamp;
	
	
	public PTMPTimestamp() {
		this(new Date());
	}
	public PTMPTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	public PTMPTimestamp(String timestamp) {
		try {
			this.timestamp = TIMESTAMP_FORMAT.parse(timestamp);
		} catch (ParseException e) {
			throw new IllegalArgumentException("Illegal format");
		}
	}
	
	
	public String toString() {
		return TIMESTAMP_FORMAT.format(timestamp);
	}
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (obj == this) return true;
		if (!(obj instanceof PTMPTimestamp)) return false;
		PTMPTimestamp timestamp = (PTMPTimestamp) obj;
		
		return this.timestamp.equals(timestamp.timestamp);
	}
	public int hashCode() {
		return timestamp.hashCode();
	}
	
	public Date getTimestamp() {
		return timestamp;
	}
	
}