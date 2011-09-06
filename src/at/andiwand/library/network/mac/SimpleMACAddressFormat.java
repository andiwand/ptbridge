package at.andiwand.library.network.mac;

import java.text.FieldPosition;
import java.text.ParsePosition;


public class SimpleMACAddressFormat extends MACAddressFormat {
	
	private static final long serialVersionUID = -1900400344119211331L;
	
	public static final int DIGIT_COUNT = 12;
	public static final String DEFAULT_PATTERN = "xx:xx:xx:xx:xx:xx";
	
	
	
	private String pattern;
	
	
	
	public SimpleMACAddressFormat() {
		this(DEFAULT_PATTERN);
	}
	public SimpleMACAddressFormat(String pattern) {
		int digitCount = pattern.length() - pattern.toLowerCase().replaceAll("x", "").length();
		if (digitCount != DIGIT_COUNT) {
			throw new IllegalArgumentException("Illegal digit count!");
		}
		
		this.pattern = pattern;
	}
	
	
	
	public StringBuffer format(MACAddress address, StringBuffer toAppendTo, FieldPosition pos) {
		String hexValue = Long.toHexString(address.toLong());
		int hexIndex = 0;
		
		for (int i = 0; i < pattern.length(); i++) {
			char c = pattern.charAt(i);
			
			if (Character.toLowerCase(c) == 'x') {
				char hexChar;
				if (hexIndex + hexValue.length() < DIGIT_COUNT) hexChar = '0';
				else hexChar = hexValue.charAt(hexIndex + hexValue.length() - DIGIT_COUNT);
				
				if (Character.isUpperCase(c)) hexChar = Character.toUpperCase(hexChar);
				toAppendTo.append(hexChar);
				hexIndex++;
			} else {
				toAppendTo.append(c);
			}
		}
		
		return toAppendTo;
	}
	
	
	@Override
	public MACAddress parseObject(String source, ParsePosition pos) {
		String hexString = "";
		
		if (source.length() != pattern.length())
			throw new IllegalArgumentException("The length of the given string is illegal!");
		
		for (int i = 0; i < source.length(); i++) {
			char c = source.charAt(i);
			char patternChar = pattern.charAt(i);
			
			if (Character.toLowerCase(patternChar) == 'x')
				hexString += c;
			
			pos.setIndex(i);
		}
		
		return MACAddress.getByAddress(Long.parseLong(hexString, 16));
	}
	
}