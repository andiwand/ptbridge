package at.andiwand.library.network.mac;

import java.text.FieldPosition;
import java.text.Format;
import java.text.ParseException;
import java.text.ParsePosition;


public abstract class MACAddressFormat extends Format {
	
	private static final long serialVersionUID = -6256557905009252815L;
	
	
	
	public abstract StringBuffer format(MACAddress address, StringBuffer toAppendTo, FieldPosition pos);
	
	@Override
	public final StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
		if (obj instanceof MACAddress) {
			return format((MACAddress) obj, toAppendTo, pos);
		} else {
			throw new IllegalArgumentException("Cannot format the given Object as MACAddress!");
		}
	}
	
	
	@Override
	public MACAddress parseObject(String source) throws ParseException {
		return (MACAddress) super.parseObject(source);
	}
	
	@Override
	public abstract MACAddress parseObject(String source, ParsePosition pos);
	
}